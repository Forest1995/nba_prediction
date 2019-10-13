import numpy as np
import pandas as pd
import redis
import ast
from keras.models import Sequential
from keras.layers.core import Dense

import os

# os.environ["TF_CPP_MIN_LOG_LEVEL"]='1' # 这是默认的显示等级，显示所有信息
os.environ["TF_CPP_MIN_LOG_LEVEL"] = '2'  # 只显示 warning 和 Error
# os.environ["TF_CPP_MIN_LOG_LEVEL"]='3' # 只显示 Error


cli = redis.Redis()
data = cli.hgetall("gameDetailDiff")
encoding = 'utf-8'
df = pd.DataFrame([ast.literal_eval(data[k].decode(encoding)) for k in data])
df = df.fillna(value=0.0)  # 用 0 填补空白数据
print(df)

dataX = df.drop(["win", "date", "home", "away"], axis=1)
dataY = df["win"]
train_x = np.array(dataX)[::2]  # train set
print(train_x)
train_y = np.array(dataY)[::2]
test_x = np.array(dataX)[1::2]  # test set
print(test_x)
test_y = np.array(dataY)[1::2]

model = Sequential()
model.add(Dense(60, input_dim=train_x.shape[1], activation='relu'))
model.add(Dense(30, activation='relu'))
model.add(Dense(1, activation='sigmoid'))
model.compile(loss='binary_crossentropy', optimizer='adam', metrics=['accuracy'])

model.summary()

model.fit(train_x, train_y, batch_size=16, epochs=10)
model.evaluate(test_x,test_y)
model.save("nba-model.hdf5")




