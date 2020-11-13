import sklearn as sk
from sklearn import tree

fileOpen = open("../DataSet/Cancer/train.txt")
data = []
for line in fileOpen:
    if '?' not in line:
        data.append([int(i) for i in line.split(',')])

x_train = [a[1:-1] for a in data]
y_train = [a[-1] for a in data]

fileOpen = open("../DataSet/Cancer/test.txt")
data = []
for line in fileOpen:
    if '?' not in line:
        data.append([int(i) for i in line.split(',')])

x_test = [a[1:-1] for a in data]
y_test = [a[-1] for a in data]

dt = tree.DecisionTreeClassifier()
dt.fit(x_train, y_train)

categoryNumbers = 5
confusionMatrix = [[0 for i in range(categoryNumbers)] for j in range(categoryNumbers)]
count = 0
for i in range(len(x_test)):
    predicted = dt.predict([x_test[i]])[0]
    confusionMatrix[y_test[i]][predicted] += 1
    if y_test[i] == predicted:
        count += 1

precision = count / len(x_test)

print("Confusion Matrix On Test Data for Decision Tree is = ")
for i in confusionMatrix:
    print(*i)
print("Precision On Test Data for Decision Tree is = ")
print(precision)



