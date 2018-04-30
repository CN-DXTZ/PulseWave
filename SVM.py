import scipy.io as scio
import numpy as np
from sklearn import svm
import random
import sklearn.preprocessing

np.set_printoptions(suppress=True)
x = np.array(scio.loadmat("D:/cache/Matlab/项目/data.mat")["data"])
# x = np.array(scio.loadmat("D:/cache/Matlab/项目/xSpilit.mat")["xSpilit"])  # (m, 200)
# y = np.array(scio.loadmat("D:/cache/Matlab/项目/yTrain.mat")["yTrain"])  # (m, 1)
# x = np.column_stack((x, y))

m, n = x.shape
mTrain = int(m * 0.8)
iter_num = 100
accTrain, accTest = 0, 0
for i in range(iter_num):
    # 打乱数据
    x = x[random.sample(range(m), m), :]
    xScale = sklearn.preprocessing.scale(x[:, :-1], axis=0)
    xScale = xScale[:, [1, 2, 3, 8, 9, 12, 14]]
    xTrain = xScale[:mTrain, :]
    xTest = xScale[mTrain:, :]

    yTrain = x[:mTrain, -1]
    yTest = x[mTrain:, -1]

    # 调用SVC()
    # clf = svm.SVC(C=1, class_weight='balanced', kernel='rbf', gamma=0.03, decision_function_shape='ovr')
    clf = svm.SVC(C=10, class_weight='balanced', kernel='linear', decision_function_shape='ovo')
    # fit()训练
    clf.fit(xTrain, yTrain)

    # 训练集准确率
    yPredTrain = clf.predict(xTrain)
    accTrain = accTrain + np.sum(yPredTrain == (yTrain)) / len(yTrain)

    # 测试集准确率
    yPredTest = clf.predict(xTest)
    accTest = accTest + np.sum(yPredTest == (yTest)) / len(yTest)

print(clf.coef_[0])
print("训练集准确率——", accTrain / iter_num, "测试集准确率——", accTest / iter_num)
