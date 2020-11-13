import math
import numpy as np
import pandas as pd

proportion = 0.8

dataFrame = pd.read_csv("../DataSet/Weather/weather_train.csv", sep=',')
x_train = dataFrame.iloc[:int(proportion * dataFrame.shape[0]), :6].values
y_train = dataFrame.iloc[:int(proportion * dataFrame.shape[0]), 6:].values

x_validation = dataFrame.iloc[int(proportion * dataFrame.shape[0]):, :6].values
y_validation = dataFrame.iloc[int(proportion * dataFrame.shape[0]):, 6:].values

dataFrame = pd.read_csv("../DataSet/Weather/weather_test.csv", sep=',')
x_test = dataFrame.iloc[:, :6].values

train_size = x_train.shape[0]
validation_size = x_validation.shape[0]
test_size = x_test.shape[0]

x_train = np.append(np.ones((train_size, 1)), x_train, axis=1)
x_validation = np.append(np.ones((validation_size, 1)), x_validation, axis=1)
x_test = np.append(np.ones((test_size, 1)), x_test, axis=1)

y_test = dataFrame.iloc[:, 6:].values

n = x_train.shape[1]
regularizationTerms = [0.001, 0.01, 0.1, 0, 1, 10, 100, 1000]

bestError = float('Inf')
bestWeights = 0
bestLambda = 0
for lambd in regularizationTerms:
    w = (1/n) * np.matmul(x_train.transpose(), x_train)
    w += lambd * np.identity(n)
    w = np.linalg.inv(w)
    w = np.matmul(w, (1/n) * x_train.transpose())
    w = np.matmul(w, y_train)

    prediction = np.matmul(x_validation, w)

    errors = [(prediction[i][0] - y_validation[i][0]) ** 2 for i in range(len(y_validation))]
    error = math.sqrt(sum(errors) / test_size)

    print("Weights for lambda = " + str(lambd) + " are :")
    print(w)
    print("Error for lambda = " + str(lambd) + " is :")
    print(error)
    print()

    if error < bestError:
        bestError = error
        bestWeights = w
        bestLambda = lambd


prediction = np.matmul(x_test, bestWeights)

errors = [(prediction[i][0] - y_test[i][0]) ** 2 for i in range(len(y_test))]
error = math.sqrt(sum(errors) / test_size)

print("Best Lambda is : " + str(bestLambda))
print("Weights are :")
print(w)
print("Error is :")
print(error)









