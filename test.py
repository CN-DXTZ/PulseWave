import numpy as np
import tensorflow as tf
import scipy.io as scio

# a = tf.Variable([[2], [1], [3], [1]])
# b = tf.Variable([[2], [3], [3], [2]])
# c = (a == b)
# # d = tf.reduce_sum(tf.to_int32(c))
#
# with tf.Session() as sess:
#     c1=sess.run(c)
#     print(c1)
#     # print(d1)
from sklearn import svm
from sklearn.model_selection import cross_val_score
from sklearn.datasets import load_iris
from sklearn.ensemble import AdaBoostClassifier

iris = load_iris()  # 还是那个数据集
a=iris.data
b=iris.target
print(a.shape)
print(b.shape)
clf = AdaBoostClassifier(svm.SVC(), n_estimators=200, algorithm='SAMME')  # 迭代100次
scores = cross_val_score(clf, iris.data, iris.target)  # 分类器的精确度
print(scores.mean())
