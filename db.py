from mongoengine import *
import pandas as pd

connect('whatsup',
        host='mongodb+srv://yuesen:kAI3rheE1eFAg32y@nbapredictiondb-s3lb4.mongodb.net/test?retryWrites=true&w=majority')


class Prediction(Document):
    HomeTeam = StringField(max_length=80, required=True)
    AwayTeam = StringField(max_length=30, required=True)
    win = IntField()


Prediction.drop_collection()
simplePrediction = Prediction("BOS", 'LOS', 1)
simplePrediction.save()
print(Prediction.objects.get(HomeTeam='BOS'))
df = pd.read_excel("test.xlsx")
print(df)

for row in df.iterrows():
    print(row[1][0], row[1][1], row[1][2])
    # print(type(row[1][0]), type(row[1][2]))
    simplePrediction = Prediction(row[1][0], row[1][1], row[1][2])
    simplePrediction.save()
