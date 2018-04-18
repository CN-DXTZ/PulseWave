import scipy.io as scio
import numpy as np
from sklearn import svm
import random

x = np.array(scio.loadmat("D:/cache/Matlab/项目/data.mat")["data"])
m, n = x.shape
mTrain = int(m * 0.8)
iter = 20
acc = 0
for i in range(iter):
    # 打乱数据
    x = x[random.sample(range(m), m), :]
    xTrain = x[:mTrain, :-1]
    yTrain = x[:mTrain, -1]
    xTest = x[mTrain:, :-1]
    yTest = x[mTrain:, -1]

    # 调用SVC()
    clf = svm.SVC()
    # fit()训练
    clf.fit(xTrain, yTrain)
    # predict()预测
    yPred = clf.predict(xTest)
    # 准确率：
    acc = acc + np.sum(yPred == (yTest))
print(acc / iter / len(yTest))
