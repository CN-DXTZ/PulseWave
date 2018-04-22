import tensorflow as tf
import scipy.io as scio
import numpy as np

# 数据准备
x = np.array(scio.loadmat("D:/cache/Matlab/项目/xSpilit.mat")["xSpilit"])  # (m, 200)
y = np.array(scio.loadmat("D:/cache/Matlab/项目/yTrain.mat")["yTrain"])  # (m, 1)


def weight_variable(shape):
    initial = tf.truncated_normal(shape, stddev=0.1)
    return tf.Variable(initial)


def bias_variable(shape):
    initial = tf.constant(0.1, shape=shape)
    return tf.Variable(initial)


# 定义输入
data_x = tf.placeholder(tf.float32, [None, 200])
data_y = tf.placeholder(tf.int32, [None, 1])
# 格式化输入
input_x = tf.reshape(data_x, [-1, 200, 1, 1])  # (m,200,1,1)
CLASS = 3
one_hot = tf.one_hot(data_y, CLASS, 1, 0)
input_y = tf.reshape(one_hot, [-1, CLASS])  # (m,3)

# 定义网络
'''
layer1:
1.conv1: [in=(m,200,1,1)]--[w=(7,1,1,96),s=(1,2,1,1),SAME]-->[out=(m,100,1,96)]
2.pool1: [in=(m,100,1,96)]--[max,k=(1,3,1,1),s=(1,2,1,1)),SAME]-->[out=(m,50,1,96)]
'''
with tf.variable_scope('conv1') as scope:
    kernel = tf.get_variable("weights", [7, 1, 1, 96])
    conv = tf.nn.conv2d(input_x, kernel, strides=[1, 2, 1, 1], padding='SAME')
    biases = tf.get_variable("biases", [96])
    conv1 = tf.nn.relu(tf.nn.bias_add(conv, biases), name=scope.name)
pool1 = tf.nn.max_pool(conv1, ksize=[1, 3, 1, 1], strides=[1, 2, 1, 1], padding='SAME', name='pool1')
# norm1 = tf.nn.lrn(pool1, 4, bias=1.0, alpha=0.001 / 9.0, beta=0.75, name='norm1')

'''
layer2:
1.conv2: [in=(m,50,1,96)]--[w=(5,1,96,256),s=(1,2,1,1),SAME]-->[out=(m,25,1,256)]
2.pool2: [in=(m,25,1,256)]--[max,k=(1,3,1,1),s=(1,2,1,1)),SAME]-->[out=(m,13,1,256)]
'''
with tf.variable_scope('conv2') as scope:
    kernel = tf.get_variable("weights", [5, 1, 96, 256])
    conv = tf.nn.conv2d(pool1, kernel, strides=[1, 2, 1, 1], padding='SAME')
    biases = tf.get_variable("biases", [256])
    conv2 = tf.nn.relu(tf.nn.bias_add(conv, biases), name=scope.name)
pool2 = tf.nn.max_pool(conv2, ksize=[1, 3, 1, 1], strides=[1, 2, 1, 1], padding='SAME', name='pool2')

'''
layer3:
conv3: [in=(m,13,1,256)]--[w=(3,1,256,384),s=(1,1,1,1),SAME]-->[out=(m,13,1,384)]
'''
with tf.variable_scope('conv3') as scope:
    kernel = tf.get_variable("weights", [3, 1, 256, 384])
    conv = tf.nn.conv2d(pool2, kernel, strides=[1, 1, 1, 1], padding='SAME')
    biases = tf.get_variable("biases", [384])
    conv3 = tf.nn.relu(tf.nn.bias_add(conv, biases), name=scope.name)

'''
layer4:
conv4: [in=(m,13,1,384)]--[w=(3,1,384,384),s=(1,1,1,1),SAME]-->[out=(m,13,1,384)]
'''
with tf.variable_scope('conv4') as scope:
    kernel = tf.get_variable("weights", [3, 1, 384, 384])
    conv = tf.nn.conv2d(conv3, kernel, strides=[1, 1, 1, 1], padding='SAME')
    biases = tf.get_variable("biases", [384])
    conv4 = tf.nn.relu(tf.nn.bias_add(conv, biases), name=scope.name)

'''
layer5:
1.conv5: [in=(m,13,1,384)]--[w=(3,1,384,256),s=(1,1,1,1),SAME]-->[out=(m,13,1,256)]
2.pool5: [in=(m,13,1,256)]--[max,k=(1,3,1,1),s=(1,2,1,1)),VALID]-->[out=(m,6,1,256)]
'''
with tf.variable_scope('conv5') as scope:
    kernel = tf.get_variable("weights", [3, 1, 384, 256])
    conv = tf.nn.conv2d(conv4, kernel, strides=[1, 1, 1, 1], padding='SAME')
    biases = tf.get_variable("biases", [256])
    conv5 = tf.nn.relu(tf.nn.bias_add(conv, biases), name=scope.name)
pool5 = tf.nn.max_pool(conv5, ksize=[1, 3, 1, 1], strides=[1, 2, 1, 1], padding='VALID', name='pool5')

'''
layer6:
fullcon1: [in=(m,6,1,256)]--[flatten=(m,1536)]-->[out=(m,1024)]
'''
with tf.variable_scope('fullcon1') as scope:
    flatten = tf.contrib.layers.flatten(pool5)
    fullcon1 = tf.layers.dense(flatten, 1024, activation=tf.nn.relu)

'''
layer7:
fullcon2: [in=(m,1024)]-->[out=(m,1024)]
'''
with tf.variable_scope('fullcon2') as scope:
    fullcon2 = tf.layers.dense(flatten, 1024, activation=tf.nn.relu)

'''
layer8:
fullcon3: [in=(m,6,1,256)]--[flatten=(m,1536)]-->[out=(m,1024)]
'''
with tf.variable_scope('fullcon3') as scope:
    output = tf.layers.dense(flatten, 3)

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

# with tf.Session() as sess:
#     idx = np.array(range(x.shape[0]))
#     p = int(x.shape[0] * 0.8)
#     acc = 0
#     train_num = 3
#
#     for i in range(train_num):
#         init = tf.group(tf.global_variables_initializer(), tf.local_variables_initializer())
#         sess.run(init)
#         np.random.shuffle(idx)
#         xTrain = x[idx[:p], :]
#         yTrain = y[idx[:p], :]
#         xTest = x[idx[p:], :]
#         yTest = y[idx[p:], :]
#         pre, accTrain, accTest = 0, 0, 0
#         iter_num = 100
#         for step in range(iter_num):
#             _, l, accTrain = sess.run([train, loss, accuracy], feed_dict={data_x: xTrain, data_y: yTrain})
#             pre, accTest = sess.run([pred, accuracy], feed_dict={data_x: xTest, data_y: yTest})
#             if step % 50 == 0:
#                 print("第", step, "代： 训练集准确率——", accTrain, "测试集准确率——", accTest)
#
#         print("第", iter_num, "代： 训练集准确率——", accTrain, "测试集准确率——", accTest)
#         print("测试集实际值:\n", yTest.reshape(-1))
#         print("测试集预测值:\n", pre)
#         print("--------------------------------------------------------------------------")
#
#         acc = acc + accTest
#
#     print("平均测试集准确率——", acc / train_num)

with tf.Session() as sess:
    idx = np.array(range(x.shape[0]))
    p = int(x.shape[0] * 0.8)
    acc = 0
    train_num = 3
    for i in range(train_num):
        init = tf.group(tf.global_variables_initializer(), tf.local_variables_initializer())
        sess.run(init)
        np.random.shuffle(idx)
        xTrain = x[idx[:p], :]
        yTrain = y[idx[:p], :]
        xTest = x[idx[p:], :]
        yTest = y[idx[p:], :]
        pre, accTrain, accTest, accTest0, step = 0, 0, 0, 0, 0
        while True:
            step = step + 1
            _, l, accTrain = sess.run([train, loss, accuracy], feed_dict={data_x: xTrain, data_y: yTrain})
            pre, accTest = sess.run([pred, accuracy], feed_dict={data_x: xTest, data_y: yTest})
            if step % 100 == 1:
                if (accTest > accTest0):
                    accTest0 = accTest
                    print("第", step, "代： 训练集准确率——", accTrain, "测试集准确率——", accTest)
                else:
                    break

        print("测试集实际值:\n", yTest.reshape(-1))
        print("测试集预测值:\n", pre)
        print("--------------------------------------------------------------------------")

        acc = acc + accTest

    print("平均测试集准确率——", acc / train_num)
