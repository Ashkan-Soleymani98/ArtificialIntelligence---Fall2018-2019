# Naive Bayes
import scipy.io as sio
import numpy as np

train = sio.loadmat("../DataSet/Mnist/mnist_train")
test = sio.loadmat("../DataSet/Mnist/mnist_test")

x_train = train['X']
y_train = train['Y']

length = len(x_train)

proportion = 0.8
x_validation = x_train[int(proportion * length):]
y_validation = y_train[0][int(proportion * length):]

x_train = x_train[0:int(proportion * length)]
y_train = y_train[0][0:int(proportion * length)]

print(x_train)
print(y_train)
x_test = test['X']
y_test = test['Y']

digitNumbers = 10
maxPossiblePixelValue = 17
pixelNumbers = 64

bestT = 0
maxPrecision = 0

for t in range(0, 6):
    yFrequencies = [t * pixelNumbers * maxPossiblePixelValue for i in range(digitNumbers)]

    xFrequencies = [[[t for i in range(maxPossiblePixelValue)] for j in range(pixelNumbers)] for k in range(digitNumbers)]

    # print(dataRowsNumber)
    for i in range(len(y_train)):
        yFrequencies[int(y_train[i])] += 1
        for j in range(pixelNumbers):
            xFrequencies[int(y_train[i])][j][int(x_train[i][j])] += 1

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


    y_validation_predicated = [estimate(x) for x in x_validation]

    confusionMatrix = [[0 for i in range(digitNumbers)] for j in range(digitNumbers)]
    count = 0
    for i in range(len(y_validation)):
        confusionMatrix[y_validation[i]][y_validation_predicated[i]] += 1
        if y_validation[i] == y_validation_predicated[i]:
            count += 1

    precision = count / len(y_validation)

    print("Smooth Parameter = " + str(t))
    print("Confusion Matrix on Validation Data is: ")
    for i in confusionMatrix:
        print(*i)
    print("Precision on Validation Data is: ")
    print(precision)

    if precision > maxPrecision:
        bestT = t
        maxPrecision = precision

    print()


yFrequencies = [t * pixelNumbers * maxPossiblePixelValue for i in range(digitNumbers)]

xFrequencies = [[[t for i in range(maxPossiblePixelValue)] for j in range(pixelNumbers)] for k in range(digitNumbers)]

# print(dataRowsNumber)
for i in range(len(y_train)):
    yFrequencies[int(y_train[i])] += 1
    for j in range(pixelNumbers):
        xFrequencies[int(y_train[i])][j][int(x_train[i][j])] += 1

# print(xFrequencies)
# print(yFrequencies)

allFrequencies = sum(yFrequencies)
yProbabilities = [i / allFrequencies for i in yFrequencies]
xProbabilities = [[[i / sum(xFrequencies[k][j]) for i in xFrequencies[k][j]] for j in range(pixelNumbers)] for k in range(digitNumbers)]

# print(yProbabilities)
# print(xProbabilities)

y_test_predicated = [estimate(x) for x in x_test]

confusionMatrix = [[0 for i in range(digitNumbers)] for j in range(digitNumbers)]
count = 0
for i in range(len(y_test[0])):
    confusionMatrix[y_test[0][i]][y_test_predicated[i]] += 1
    if y_test[0][i] == y_test_predicated[i]:
        count += 1

precision = count / len(y_test[0])

print("Best Laplace Smoothing Parameter is = " + str(bestT))
print("Confusion Matrix On Test Data for best Smoothing Parameter = " + str(bestT) + " is: ")
for i in confusionMatrix:
    print(*i)
print("Precision On Test Data for best Smoothing Parameter = " + str(bestT) + " is: ")
print(precision)






