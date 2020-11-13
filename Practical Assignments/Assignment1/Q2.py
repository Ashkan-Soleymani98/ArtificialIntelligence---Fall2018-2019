import random
import copy
import time
import datetime
import numpy as np

n = int(input())
m = int(input())

statesNum = 1024
start = list(map(int, input().split()))

start = [i for i in start]

plate = [[0 for i in range(n + 2)] for i in range(n + 2)]

for j in range(0, n + 2):
    plate[0][j] = 3
    plate[n + 1][j] = 3
    plate[j][0] = 3
    plate[j][n + 1] = 3

for i in range(1, n + 1):
    inline = input().split()
    for j, block in enumerate(inline):
        if block == 'a':
            plate[i][j + 1] = 1
        elif block == 's':
            plate[i][j + 1] = 2
        elif block == 'w':
            plate[i][j + 1] = 3

# print(*plate)

# indexing: up -> 4, down -> 3, right -> 2, left -> 1, in -> 0
# 0 -> e, 1 -> a, 2 -> s, 3 -> w
# amount: 0 -> not move, 1 -> down, 2 -> up, -1 -> left, -2 -> right, 3 -> random

powersFour = [1, 4, 16, 64, 256]


def moveMap(state):
    # print(state)
    # print(powersFour)
    return sum([state[i] * powersFour[i] for i in range(len(state))])

# print(moveMap([0, 0, 0, 0, 0]))
# print(moveMap([1, 0, 2, 3, 0]))


def run(plan):
    gameMap = copy.deepcopy(plate)
    fitness = 0
    ammunition = 0
    location = copy.deepcopy(start)
    i = 0
    while i < m:
        x, y = location[0], location[1]
        if gameMap[x][y] == 1:
            ammunition += 1
            gameMap[x][y] = 0
        elif gameMap[x][y] == 2:
            if ammunition < 1:
                return fitness
            ammunition -= 1
            gameMap[x][y] = 0
        move = moveMap([gameMap[x][y], gameMap[x - 1][y], gameMap[x + 1][y], gameMap[x][y - 1], gameMap[x][y + 1]])
        move = plan[move]
        if move == 3:
            move = random.choice([-2, -1, 0, 1, 2])
        if move == 1 and gameMap[x][y - 1] != 3:
            location[1] -= 1
        elif move == 2 and gameMap[x][y + 1] != 3:
            location[1] += 1
        elif move == -1 and gameMap[x - 1][y] != 3:
            location[0] -= 1
        elif move == -2 and gameMap[x + 1][y] != 3:
            location[0] += 1
        fitness += 1
        i += 1
        # print(ammunition)
        # print(gameMap == plate)
    return fitness + ammunition


# print(run(plan))


def selection(plans, scores, num):
    scores, plans = zip(*sorted(zip(scores, plans)))
    scores = list(scores)
    plans = list(plans)
    return plans[-1:-num - 1:-1], scores[-1:-num - 1:-1]


def crossover(plans, numParts):
    parts = [[] for i in range(numParts)]
    retPlans = list()
    partSize = len(plans[0]) // numParts
    for i in range(numParts):
        for j in plans:
            a = i * partSize
            b = (i + 1) * partSize
            parts[i].append(j[a:b])
        parts[i] = np.random.permutation(parts[i])
    for i in range(len(plans)):
        tempList = list()
        for j in range(numParts):
            tempList.extend(parts[j][i])
        retPlans.append(tempList)
    return retPlans

def decision(probability):
    return random.random() < probability

def mutation(plans, num, prob):
    retPlans = list()
    for i in range(num):
        p = copy.deepcopy(random.choice(plans))
        for j in range(statesNum):
            if decision(prob):
                p[j] = random.choice([-2, -1, 0, 1, 2])
        retPlans.append(p)
    return retPlans


def geneticAlgo():
    global selectSize
    startingTime = time.time()
    plans = [[random.choice([-2, -1, 0, 1, 2]) for i in range(statesNum)] for i in range(seedSize)]
    scores = [run(i) for i in plans]
    seedInnerSize = selectSize // 2
    crossoverSize = 4
    mutationSize = selectSize // 3
    updateIteration = 15
    iterationNum = 0
    prob = 0.3
    while time.time() - startingTime < geneticPeriod:
        plans, scores = selection(plans, scores, selectSize)
        newPlans = crossover(plans, crossoverSize)
        newScores = [run(i) for i in newPlans]
        plans, scores = selection(plans + newPlans, scores + newScores, selectSize)
        newPlans = mutation(plans, mutationSize, prob)
        newScores = [run(i) for i in newPlans]
        plans, scores = selection(plans + newPlans, scores + newScores, selectSize)
        newPlans = [[random.choice([-2, -1, 0, 1, 2]) for i in range(statesNum)] for i in range(seedInnerSize)]
        newScores = [run(i) for i in newPlans]
        plans, scores = selection(plans + newPlans, scores + newScores, selectSize)
        prob = 0.3 + (0.01) if iterationNum % updateIteration != 0 else 0.7
        if iterationNum > updateIteration:
            selectSize += 100
            seedInnerSize += selectSize // 3
            mutationSize += selectSize // 8
            crossoverSize = min(64, crossoverSize * 2)
            iterationNum = 0
        iterationNum += 1
        print(scores[0:20])
    return plans[0], scores[0]




geneticPeriod = 600
seedSize = 3000
selectSize = seedSize // 5

p, s = geneticAlgo()

print(p)
print(s)





