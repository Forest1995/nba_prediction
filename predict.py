import redis
import json
import pandas as pd
import numpy as np
from keras.engine.saving import load_model

cli = redis.Redis()

game_detail_data = cli.hgetall("gameDetail")
game_detail_json = []
encoding = 'utf-8'
for k in game_detail_data:
    di_v = {}
    di_h = {}
    j = json.loads(game_detail_data[k])
    vls = j["g"]["vls"]
    hls = j["g"]["hls"]
    di_v.update(vls["tstsg"])
    di_v.update({"date": j["g"]["gdtutc"], "name": vls["ta"], "home": 0})
    game_detail_json.append(di_v)
    di_h.update(hls["tstsg"])
    di_h.update({"date": j["g"]["gdtutc"], "name": hls["ta"], "home": 1})
    game_detail_json.append(di_h)
game_detail_df = pd.DataFrame(game_detail_json)
game_detail_df = game_detail_df.fillna(value=0.0)


def predict(home=None, away=None):
    home_data = game_detail_df[(game_detail_df['name'] == home) & (game_detail_df['home'] == 1)].sort_values(by='date',
                                                                                                             ascending=False)[
                :5].mean()
    away_data = game_detail_df[(game_detail_df['name'] == away) & (game_detail_df['home'] == 0)].sort_values(by='date',
                                                                                                             ascending=False)[
                :5].mean()
    home_data = home_data.drop(['home'])
    away_data = away_data.drop(['home'])
    new_x = np.array(home_data - away_data)
    model = load_model('nba-model.hdf5')
    return model.predict_classes(new_x[np.newaxis, :], verbose=0)[0][0]


names = ["ATL", "BKN", "BOS", "CHA", "CHI", "CLE", "DAL", "DEN", "DET", "GSW", "HOU", "IND", "LAC", "LAL", "MEM", "MIA",
         "MIL", "MIN", "NOP", "NYK", "OKC", "ORL", "PHI", "PHX", "POR", "SAC", "SAS", "TOR", "UTA", "WAS"]
teams = set()
for i in range(0, 30):
    for j in range(0, 30):
        if i != j:
            teams.add(tuple([names[i], names[j]]))
print(len(teams))
result = set()
for t in teams:
    p = predict(t[0], t[1])
    if p == 1:
        print("%s(win) vs %s (loss)" % (t[0], t[1]))
    else:
        print("%s(loss) vs %s (win)" % (t[0], t[1]))
    result.add(tuple([t[0], t[1], p]))

result_df = pd.DataFrame(result)
print(result_df)
result_df.to_excel('test.xlsx', sheet_name='sheet1', index=False)

# Todo put the prediction result into mongoDB so that the front end can directly access
