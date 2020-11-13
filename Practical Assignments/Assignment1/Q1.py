class MinHeap:
    def __init__(self):
        self.list = [None]

    @classmethod
    def parent(cls, i):
        return i // 2

    @classmethod
    def rightChildren(cls, i):
        return 2 * i + 1

    @classmethod
    def leftChildren(cls, i):
        return 2 * i

    def insert(self, node):
        self.list.append(node)
        self.bubbleUp(len(self.list) - 1)

    def bubbleUp(self, index):
        while index > 1:
            if not self.list[index].f < self.list[MinHeap.parent(index)].f:
                return
            self.list[index], self.list[MinHeap.parent(index)] = self.list[MinHeap.parent(index)], self.list[index]
            index = MinHeap.parent(index)

    def bubbleDown(self, index):
        while MinHeap.leftChildren(index) <= self.size():
            newInd = index
            if self.list[MinHeap.leftChildren(index)].f < self.list[index].f:
                newInd = MinHeap.leftChildren(index)
            if MinHeap.rightChildren(index) <= self.size() and self.list[MinHeap.rightChildren(index)].f < self.list[newInd].f:
                newInd = MinHeap.rightChildren(index)
            if index == newInd:
                break
            self.list[index], self.list[newInd] = self.list[newInd], self.list[index]
            index = newInd

    def dequeue(self):
        if self.size() == 0:
            raise Exception("Heap is empty")
        mini = self.list[1]
        self.list[1] = self.list[-1]
        del (self.list[-1])
        self.bubbleDown(1)
        return mini

    def size(self):
        return len(self.list) - 1


def manhattanDistance(a, b):
    return abs(a[0] - b[0]) + abs(a[1] - b[1])


n = int(input())

start = list(map(int, input().split()))
end = list(map(int, input().split()))

start = [i - 1 for i in start]
end = [i - 1 for i in end]

# print(n)
# print(start)
# print(end)

abilities = list(map(int, input().split()))

m = abilities[0]
M = abilities[1]
h = abilities[2]
t = abilities[3]
d = abilities[4]

plate = [[0 for i in range(n)] for i in range(n)]

for i in range(n):
    inline = input().split()
    for count, j in enumerate(inline):
        if j != 'e':
            if j[0] == 'm' and int(j[1]) > m:
                plate[i][count] = 1
            elif j[0] == 'M' and int(j[1]) > M:
                plate[i][count] = 1
            elif j[0] == 'h' and int(j[1]) > h:
                plate[i][count] = 1
            elif j[0] == 't' and int(j[1]) > t:
                plate[i][count] = 1
            elif j[0] == 'd' and int(j[1]) > d:
                plate[i][count] = 1


print(plate)


def possibleMove(x, y, move, amount):
    if move == 'x':
        if 0 <= x + amount < n and 0 <= y < n and plate[x + amount][y] == 0:
            return True
        else:
            return False
    elif move == 'y':
        if 0 <= x < n and 0 <= y + amount < n and plate[x][y + amount] == 0:
            return True
        else:
            return False
    return False


class bfsNode:
    def __init__(self, x, y, depth):
        self.x = x
        self.y = y
        self.depth = depth


# def bfs_heuristic(state, contourLevel):#, contourType): # contourType = 0 -> x + y , 1 -> x - y
#     limitContour = state.x + state.y
#     contourLevel = contourLevel + limitContour
#     queue = list()
#     queue.append(state)
#     marked = list()
#     while len(queue) > 0:
#         state = queue.pop(0)
#         isMarked = False
#         for i in marked:
#             if i.x == state.x and i.y == state.y:
#                 isMarked = True
#         if isMarked or plate[state.x][state.y] == 1:
#             continue
#         marked.append(state)
#         if state.x + state.y == contourLevel:
#             return state.depth
#         nodes = list()
#         if possibleMove(state.x, state.y, 'x', +1) and state.x + state.y + 1 >= limitContour:
#             nodes.append(bfsNode(state.x + 1, state.y, state.depth + 1))
#         if possibleMove(state.x, state.y, 'x', -1) and state.x + state.y - 1 >= limitContour:
#             nodes.append(bfsNode(state.x - 1, state.y, state.depth + 1))
#         if possibleMove(state.x, state.y, 'y', +1) and state.x + state.y + 1 >= limitContour:
#             nodes.append(bfsNode(state.x, state.y + 1, state.depth + 1))
#         if possibleMove(state.x, state.y, 'y', -1) and state.x + state.y - 1 >= limitContour:
#             nodes.append(bfsNode(state.x, state.y - 1, state.depth + 1))
#         queue.extend(nodes)
#     return -1


def bfs_heuristic(state, interContourAmount, direct, type): # Type = 1 -> x + y , -1 -> x - y
    queue = list()
    queue.append(state)
    marked = list()
    lowerBoundContour = state.x + type * state.y if direct == '+' else state.x + type * state.y - interContourAmount
    upperBoundContour = state.x + type * state.y + interContourAmount if direct == '+' else state.x + type * state.y
    while len(queue) > 0:
        state = queue.pop(0)
        isMarked = False
        for i in marked:
            if i.x == state.x and i.y == state.y:
                isMarked = True
        if isMarked or plate[state.x][state.y] == 1:
            continue
        marked.append(state)
        if state.x + type * state.y == (upperBoundContour if direct == '+' else lowerBoundContour):
            return state.depth
        nodes = list()
        if possibleMove(state.x, state.y, 'x', +1) and upperBoundContour >= state.x + 1 + type * state.y >= lowerBoundContour:
            nodes.append(bfsNode(state.x + 1, state.y, state.depth + 1))
        if possibleMove(state.x, state.y, 'x', -1) and upperBoundContour >= state.x - 1 + type * state.y >= lowerBoundContour:
            nodes.append(bfsNode(state.x - 1, state.y, state.depth + 1))
        if possibleMove(state.x, state.y, 'y', +1) and upperBoundContour >= state.x + type * (state.y + 1) >= lowerBoundContour:
            nodes.append(bfsNode(state.x, state.y + 1, state.depth + 1))
        if possibleMove(state.x, state.y, 'y', -1) and upperBoundContour >= state.x + type * (state.y - 1) >= lowerBoundContour:
            nodes.append(bfsNode(state.x, state.y - 1, state.depth + 1))
        queue.extend(nodes)
    return float('inf')


def heuristic(a, b):
    k1 = k2 = k3 = k4 = 0
    if sum(a) > sum(b):
        k2 = sum(a) - sum(b)
    else:
        k1 = sum(b) - sum(a)
    if a[0] - a[1] >= b[0] - b[1]:
        k4 = (a[0] - a[1]) - (b[0] - b[1])
    else:
        k3 = (b[0] - b[1]) - (a[0] - a[1])

    # print(a)
    # print(k1, k2, k3, k4)

    tempHs1 = [[bfs_heuristic(bfsNode(i, j, 0), k1, '+', 1) for j in range(n)] for i in range(n)]
    tempHs2 = [[bfs_heuristic(bfsNode(i, j, 0), k2, '-', 1) for j in range(n)] for i in range(n)]
    tempHs3 = [[bfs_heuristic(bfsNode(i, j, 0), k3, '+', -1) for j in range(n)] for i in range(n)]
    tempHs4 = [[bfs_heuristic(bfsNode(i, j, 0), k4, '-', -1) for j in range(n)] for i in range(n)]


    # print(bfs_heuristic2(bfsNode(0, 3, 0), 2, '-', -1))
    # print(*tempHs1)
    # print(*tempHs2)
    # print(*tempHs3)
    # print(*tempHs4)

    patternDB1 = [[float('inf') for j in range(n)] for i in range(n)]
    patternDB2 = [[float('inf') for j in range(n)] for i in range(n)]
    patternDB3 = [[float('inf') for j in range(n)] for i in range(n)]
    patternDB4 = [[float('inf') for j in range(n)] for i in range(n)]

    for i in range(n):
        for j in range(n):
            array1 = list()
            array2 = list()
            for k in range(i + j + 1):
                # print(i, j, k, i + j - k)
                if 0 <= k < n and 0 <= i + j - k < n:
                    array1.append(tempHs1[k][i + j - k] + manhattanDistance([i, j], [k, i + j - k]))
                    array2.append(tempHs2[k][i + j - k] + manhattanDistance([i, j], [k, i + j - k]))
            patternDB1[i][j] = min(array1)
            patternDB2[i][j] = min(array2)
            if plate[i][j] == 1:
                patternDB1[i][j] = float('inf')
                patternDB2[i][j] = float('inf')

    # print(patternDB1)
    # print(patternDB2)

    for i in range(n):
        for j in range(n):
            array3 = list()
            array4 = list()
            for k in range(max(0, i - j) , n - (j - i) + 1):
                if 0 <= k < n and 0 <= j - i + k < n:
                    array3.append(tempHs3[k][j - i + k] + manhattanDistance([i, j], [k, j - i + k]))
                    array4.append(tempHs4[k][j - i + k] + manhattanDistance([i, j], [k, j - i + k]))
            patternDB3[i][j] = min(array3)
            patternDB4[i][j] = min(array4)
            if plate[i][j] == 1:
                patternDB3[i][j] = float('inf')
                patternDB4[i][j] = float('inf')

    # print(patternDB3)
    # print(patternDB4)

    return max(patternDB1[a[0]][a[1]], patternDB2[a[0]][a[1]], patternDB3[a[0]][a[1]], patternDB4[a[0]][a[1]])


patternDB = [[heuristic([i, j], end) for j in range(n)] for i in range(n)]
print(patternDB)


class AStarNode:
    def __init__(self, x, y, g, h, prev):
        self.x = x
        self.y = y
        self.g = g
        self.h = h
        self.f = g + h
        self.prev = prev

# minHeap = MinHeap()
# test = [3, 4, 13, 22, 14, 56, 18, 90, -2, 91, 19, 3, 5, 78, 2]
# for i in test:
#     minHeap.insert(AStarNode(0, 0, i, 0, None))
#
# while minHeap.size() > 0:
#     print(minHeap.enqeue().f)
#


def AStarAlgo(state):
    markEval = [[False for i in range(n)] for i in range(n)]
    addedEval = [[False for i in range(n)] for i in range(n)]
    queue = MinHeap()
    queue.insert(state)
    moves = 0
    while queue.size() > 0:
        print(str(moves + 1) + ".", end="")
        for i in range(queue.size()):
            print("(" + str(queue.list[i + 1].x + 1) + ', ' + str(queue.list[i + 1].y + 1) + '): f = '
                  + str(queue.list[i + 1].g) + " + " + str(queue.list[i + 1].h) + ' = ' + str(queue.list[i + 1].f))
        moves += 1
        state = queue.dequeue()
        print("*(" + str(state.x + 1) + ", " + str(state.y + 1) + ") expands")
        if markEval[state.x][state.y]:
            continue
        markEval[state.x][state.y] = True
        if state.h == 0:
            return state
        x, y = state.x, state.y
        if possibleMove(state.x, state.y, 'x', +1) and not addedEval[state.x + 1][state.y]:
            queue.insert(AStarNode(x + 1, y, state.g + 1, patternDB[x + 1][y], state))
            addedEval[state.x + 1][state.y] = True
        if possibleMove(state.x, state.y, 'x', -1) and not addedEval[state.x - 1][state.y]:
            queue.insert(AStarNode(x - 1, y, state.g + 1, patternDB[x - 1][y], state))
            addedEval[state.x - 1][state.y] = True
        if possibleMove(state.x, state.y, 'y', +1) and not addedEval[state.x][state.y + 1]:
            queue.insert(AStarNode(x, y + 1, state.g + 1, patternDB[x][y + 1], state))
            addedEval[state.x][state.y + 1] = True
        if possibleMove(state.x, state.y, 'y', -1) and not addedEval[state.x][state.y - 1]:
            queue.insert(AStarNode(x, y - 1, state.g + 1, patternDB[x][y - 1], state))
            addedEval[state.x][state.y - 1] = True
    return None


# print(AStarAlgo(AStarNode(start[0], start[1], 0, patternDB[start[0]][start[1]], None)))

route = list()
node = AStarAlgo(AStarNode(start[0], start[1], 0, patternDB[start[0]][start[1]], None))

while node is not None:
    route.append('(' + str(node.x + 1) + ', ' + str(node.y + 1) + ')')
    node = node.prev

route.reverse()

print("Route: ", end="")
print(*route)







