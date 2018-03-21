# 脉搏波
# python3.6
import numpy as np
import matplotlib.pyplot as plt
import SerialData
import DynamicWave


def DataLoad():
    x = []
    fr = open('副本5.txt')
    for line in fr.readlines():
        strLine = line[1:-2].strip().split(',')
        x.append([float(number) for number in strLine])
    return np.mat(x)


if __name__ == '__main__':
    np.set_printoptions(suppress=True)  # 设置不以科学记数法表示
    # SerialData.ReadData(SerialData.SetSerial(), 2000)
    x = DataLoad()
    # 静态图
    # plt.figure()
    # plt.plot(x[:, 0])
    # plt.show()

    # 动态图
    DynamicWave.display(np.array(x[:, 0].flat))
