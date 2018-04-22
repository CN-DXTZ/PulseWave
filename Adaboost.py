import scipy.io as scio
import numpy as np
from sklearn.model_selection import cross_val_score
from sklearn.ensemble import AdaBoostClassifier
import random
from sklearn import svm

x = np.array(scio.loadmat("D:/cache/Matlab/项目/data.mat")["data"])

# x1 = list(x[1:43.:])
# x2 = list(x[237:, :])
# x = np.array(x1 + x2)
m, n = x.shape
mTrain = int(m * 0.8)
iter_num = 10
accTrain, accTest = 0, 0
for i in range(iter_num):
    # 打乱数据
    x = x[random.sample(range(m), m), :]
    xTrain = x[:mTrain, :-1]
    yTrain = x[:mTrain, -1].reshape(-1)
    xTest = x[mTrain:, :-1]
    yTest = x[mTrain:, -1].reshape(-1)

    clf = AdaBoostClassifier(n_estimators=100)
    # fit()训练
    clf.fit(xTrain, yTrain)

    # 训练集准确率
    yPredTrain = clf.predict(xTrain)
    accTrain = accTrain + np.sum(yPredTrain == (yTrain)) / len(yTrain)

    # 测试集准确率
    yPredTest = clf.predict(xTest)
    accTest = accTest + np.sum(yPredTest == (yTest)) / len(yTest)

print("训练集准确率——", accTrain / iter_num, "测试集准确率——", accTest / iter_num)
