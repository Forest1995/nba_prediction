import redis
import requests
import json

r = redis.Redis(host='localhost', port=6379, db=0)

# 爬虫地址

def generateKey(num):
    if 1 <= num <= 9:
        result = '0000' + str(x)
    elif 10 <= num <= 99:
        result = '000' + str(x)
    elif 100 <= num <= 999:
        result = '00' + str(x)
    else:
        result = '0' + str(x)
    return result

for x in range(1002, 1190):
    strs = generateKey(x)
    url = 'http://data.nba.com/data/10s/v2015/json/mobile_teams/nba/2018/scores/gamedetail/00218' + strs +'_gamedetail.json'
    response = requests.get(url=url, verify=False)
    text = response.text

    key = '00218' + strs +'_gamedetail'
    di = {}
    data = json.loads(text)

    vls = data["g"]["vls"]
    hls = data["g"]["hls"]
    vs = int(vls["s"])
    hs = int(hls["s"])
    # win or lose
    w = 1.0 if hs > vs else 0.0
    di.update({"win": w})
    # stats diff
    vsts = vls["tstsg"]
    hsts = hls["tstsg"]

    for k in hsts:
        di.update({k: int(hsts[k] - int(vsts[k]))})

    # team name
    vn = vls["ta"]
    hn = hls["ta"]
    di.update({"home": hn, "away": vn})

    # game date
    date = data["g"]["gdtutc"]
    di.update({"date": date})

    print(di)

    r.hset("gameDetailDiff", key, str(di))
    r.hset("gameDetail", key, text)



