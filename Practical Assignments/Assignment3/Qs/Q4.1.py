import math
import numpy as np
import pandas as pd


dataFrame = pd.read_csv("../DataSet/Weather/weather_train.csv", sep=',')
x_train = dataFrame.iloc[:, :6].values
y_train = dataFrame.iloc[:, 6:].values

dataFrame = pd.read_csv("../DataSet/Weather/weather_test.csv", sep=',')
x_test = dataFrame.iloc[:, :6].values
y_test = dataFrame.iloc[:, 6:].values

train_size = x_train.shape[0]
test_size = x_test.shape[0]

x_train = np.append(np.ones((train_size, 1)), x_train, axis=1)
x_test = np.append(np.ones((test_size, 1)), x_test, axis=1)

w = np.linalg.inv(np.matmul(x_train.transpose(), x_train))
w = np.matmul(w, x_train.transpose())
w = np.matmul(w, y_train)


prediction = np.matmul(x_test, w)

errors = [(prediction[i][0] - y_test[i][0]) ** 2 for i in range(len(y_test))]
error = math.sqrt(sum(errors) / test_size)

print("Weights are :")
print(w)
print("Error is :")
print(error)







