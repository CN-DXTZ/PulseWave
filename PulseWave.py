# 脉搏波
# python3.6
import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
import SerialData
import DynamicWave
import SignalProcess


def DataLoad(file):
    x = []
    fr = open(file)
    for line in fr.readlines():
        strLine = line[1:-2].strip().split(',')
        x.append([float(number) for number in strLine])
    return np.mat(x)


if __name__ == '__main__':
    np.set_printoptions(suppress=True)  # 设置不以科学记数法表示
    # SerialData.ReadData(SerialData.SetSerial(), 6000)
    x = DataLoad('data.txt')
    # 静态图
    # plt.subplot(211)
    # plt.plot(x[:, 0])
    # plt.subplot(212)
    # plt.plot(x[6001:12000, 0])
    # plt.show()

    # 动态图
    # DynamicWave.display(np.array(x[:, 0].flat))

    # 平滑
    # SignalProcess.smooth(x[1000:3000, 0].flat)

    # 傅里叶变换
    # SignalProcess.ft(x[:3000, 0])
