# Naive Bayes
import scipy.io as sio
import numpy as np

train = sio.loadmat("../DataSet/Mnist/mnist_train")
test = sio.loadmat("../DataSet/Mnist/mnist_test")

x_train = train['X']
y_train = train['Y']

x_test = test['X']
y_test = test['Y']

digitNumbers = 10
yFrequencies = [0 for i in range(digitNumbers)]

maxPossiblePixelValue = 17
pixelNumbers = 64
xFrequencies = [[[0 for i in range(maxPossiblePixelValue)] for j in range(pixelNumbers)] for k in range(digitNumbers)]


# print(dataRowsNumber)
for i in range(len(y_train[0])):
    yFrequencies[int(y_train[0][i])] += 1
    for j in range(pixelNumbers):
        xFrequencies[int(y_train[0][i])][j][int(x_train[i][j])] += 1

# print(xFrequencies)
# print(yFrequencies)

allFrequencies = sum(yFrequencies)
yProbabilities = [i / allFrequencies for i in yFrequencies]
xProbabilities = [[[i / sum(xFrequencies[k][j]) for i in xFrequencies[k][j]] for j in range(pixelNumbers)] for k in range(digitNumbers)]

# print(yProbabilities)
# print(xProbabilities)


def estimate(x):
    maxMult = 0
    maxDigit = 0
    for i in range(digitNumbers):
        mult = 1
        for j in range(pixelNumbers):
            mult *= xProbabilities[i][j][int(x[j])]
        if mult > maxMult:
            maxMult = mult
            maxDigit = i
    return maxDigit


y_test_predicated = [estimate(x) for x in x_test]

confusionMatrix = [[0 for i in range(digitNumbers)] for j in range(digitNumbers)]
count = 0
for i in range(len(y_test[0])):
    confusionMatrix[y_test[0][i]][y_test_predicated[i]] += 1
    if y_test[0][i] == y_test_predicated[i]:
        count += 1

precision = count / len(y_test[0])

print("Confusion Matrix is: ")
for i in confusionMatrix:
    print(*i)
print("Precision is: ")
print(precision)




