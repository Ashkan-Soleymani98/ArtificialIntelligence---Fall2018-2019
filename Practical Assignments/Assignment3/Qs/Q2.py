import numpy as np

fileOpen = open("../DataSet/Cancer/train.txt")
data = []
for line in fileOpen:
    if '?' not in line:
        data.append([int(i) for i in line.split(',')])

x_train = np.array([a[1:-1] for a in data])
y_train = np.array([1 if a[-1] == 2 else 0 for a in data])

fileOpen = open("../DataSet/Cancer/test.txt")
data = []
for line in fileOpen:
    if '?' not in line:
        data.append([int(i) for i in line.split(',')])

x_test = np.array([a[1:-1] for a in data])
y_test = np.array([1 if a[-1] == 2 else 0 for a in data])

train_size = x_train.shape[0]
test_size = x_test.shape[0]
x_train = np.append(np.ones((train_size, 1)), x_train, axis=1)
x_test = np.append(np.ones((test_size, 1)), x_test, axis=1)

Ws = np.random.normal(0, 1, x_train.shape[1])
epochNumbers = 10000
learningRate = 0.01
for epoch in range(epochNumbers):
    prediction = 1 / (1 + np.exp(-np.dot(x_train, Ws)))
    gradient = np.dot(x_train.T, (prediction - y_train)) / x_train.shape[0]
    prediction = [1 if i >= 0.5 else 0 for i in prediction]
    Ws -= gradient * learningRate

counter = 0
categoryNumbers = 2
confusionMatrix = [[0 for i in range(categoryNumbers)] for j in range(categoryNumbers)]
for i in range(len(prediction)):
    confusionMatrix[y_train[i]][prediction[i]] += 1
    if prediction[i] == y_train[i]:
        counter += 1

precision = counter / len(prediction)

print("Confusion Matrix On Train Data for Logistic Regression is = ")
for i in confusionMatrix:
    print(*i)
print("Precision On Train Data for Logistic Regression is = ")
print(precision)
print()

prediction = 1 / (1 + np.exp(-np.matmul(x_test, Ws)))
prediction = [1 if i >= 0.5 else 0 for i in prediction]

counter = 0
categoryNumbers = 2
confusionMatrix = [[0 for i in range(categoryNumbers)] for j in range(categoryNumbers)]
for i in range(len(prediction)):
    confusionMatrix[y_test[i]][prediction[i]] += 1
    if prediction[i] == y_test[i]:
        counter += 1

precision = counter / len(prediction)

print("Confusion Matrix On Test Data for Logistic Regression (logloss) is = ")
for i in confusionMatrix:
    print(*i)
print("Precision On Test Data for Logistic Regression (logloss) is = ")
print(precision)
