# 脉搏波
# python3.6
import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
import SerialData
import DynamicWave
import SignalProcess
import MySQL


def DataLoad(start=0,end=-1):
    cur = MySQL.MysqlInit()
    sql = 'select * from wavedata'
    cur.execute(sql)
    x = []
    for i in cur.fetchall():
        x.append(i[1])
    return x


if __name__ == '__main__':
    np.set_printoptions(suppress=True)  # 设置不以科学记数法表示
    # x=DataLoad()
    # # 静态图
    # # plt.subplot(2    11)
    # plt.plot(x[:])
    # # plt.subplot(212)
    # # plt.plot(x[500:1000, 0])
    # plt.show()

    # 动态检测
    SerialData.ReadDataToSQL()
    DynamicWave.Display()

    # 平滑
    # SignalProcess.smooth(x[1000:2000, 0].flat)
    #
    # 傅里叶变换
    # SignalProcess.ft(x[1000:2000, 0])
