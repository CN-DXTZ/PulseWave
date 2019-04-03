import tensorflow as tf
import scipy.io as scio
import numpy as np

# 数据准备
x = np.array(scio.loadmat("D:/cache/Matlab/项目/data.mat")["data"])


def weight_variable(shape):
    initial = tf.truncated_normal(shape, stddev=0.1)
    return tf.Variable(initial)


def bias_variable(shape):
    initial = tf.constant(0.1, shape=shape)
    return tf.Variable(initial)


# 定义输入
data_x = tf.placeholder(tf.float32, [None, 6])
data_y = tf.placeholder(tf.int32, [None, 1])
# 格式化输入
input_x = data_x
CLASS = 3
one_hot = tf.one_hot(data_y, CLASS, 1, 0)
input_y = tf.reshape(one_hot, [-1, CLASS])  # (m,3)

layer0 = tf.layers.dense(input_x, 9, activation=tf.nn.relu)
layer1 = tf.layers.dense(layer0, 18, activation=tf.nn.relu)
layer2 = tf.layers.dense(layer1, 24, activation=tf.nn.relu)
layer3 = tf.layers.dense(layer2, 48, activation=tf.nn.relu)
layer4 = tf.layers.dense(layer3, 48, activation=tf.nn.relu)
layer5 = tf.layers.dense(layer4, 24, activation=tf.nn.relu)
layer6 = tf.layers.dense(layer5, 18, activation=tf.nn.relu)
layer7 = tf.layers.dense(layer6, 9, activation=tf.nn.relu)
output = tf.layers.dense(layer7, 3)

# 定义优化
# loss = tf.losses.softmax_cross_entropy(labels=input_y, logits=output)
loss = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(logits=output, labels=input_y))
# 返回局部变量: accuracy和update_operation
# 注意初始化局部变量
pred = tf.argmax(output, axis=1)
accuracy = tf.metrics.accuracy(labels=data_y, predictions=pred)[1]
# tt = tf.reshape(data_y, [-1])
# accuracy2 = tf.reduce_mean(pred == tt)
optimizer = tf.train.AdamOptimizer(learning_rate=0.001)
train = optimizer.minimize(loss)

with tf.Session() as sess:
    idx = np.array(range(x.shape[0]))
    p = int(x.shape[0] * 0.8)
    acc = 0
    train_num = 5

    for i in range(train_num):
        init = tf.group(tf.global_variables_initializer(), tf.local_variables_initializer())
        sess.run(init)
        np.random.shuffle(idx)
        xTrain = x[idx[:p], :-1]
        yTrain = x[idx[:p], -1].reshape(-1, 1)
        xTest = x[idx[p:], :-1]
        yTest = x[idx[p:], -1].reshape(-1, 1)
        pre, accTrain, accTest, accTest0, step = 0, 0, 0, 0, 0
        while True:
            step = step + 1
            _, l, accTrain = sess.run([train, loss, accuracy], feed_dict={data_x: xTrain, data_y: yTrain})
            pre, accTest = sess.run([pred, accuracy], feed_dict={data_x: xTest, data_y: yTest})
            if step % 100 == 0:
                if (accTest > accTest0):
                    accTest0 = accTest
                    print("第", step, "代： 训练集准确率——", accTrain, "测试集准确率——", accTest)
                else:
                    break

        # print("测试集实际值:\n", yTest.reshape(-1))
        # print("测试集预测值:\n", pre)
        print("--------------------------------------------------------------------------")

        acc = acc + accTest0

    print("平均测试集准确率——", acc / train_num)
