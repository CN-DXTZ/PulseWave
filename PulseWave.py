# 脉搏波
# python3.6
import numpy as np
import matplotlib.pyplot as plt
import SerialData


def DataLoad():
    x = []
    fr = open('data.txt')
    for line in fr.readlines():
        strLine = line[1:-2].strip().split(',')
        x.append([float(number) for number in strLine])
    return np.mat(x)


if __name__ == '__main__':
    np.set_printoptions(suppress=True)  # 设置不以科学记数法表示
    # SerialData.ReadData(SerialData.SetSerial(), 2000)
    x = DataLoad()
    print(x)
    plt.figure()
    plt.plot(x[:, 0])
    plt.show()
