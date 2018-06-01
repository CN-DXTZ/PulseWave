# 从串口数据读取
import serial
import serial.tools.list_ports
import struct
import MySQL
import time
import multiprocessing
import DynamicWave


# 设置串口
def SetSerial(idx):
    plist = list(serial.tools.list_ports.comports())
    port = plist[idx][0]
    print(port)
    ser = serial.Serial(port, baudrate=38400)
    ser.write(b'\x88')
    return ser


def readData(idx, tableName):
    ser = SetSerial(idx)
    cur = MySQL.MysqlInit()

    sql = "drop table if exists %s;" % tableName
    sql += "create table %s (time int, value int);" % tableName
    cur.execute(sql)
    # 发送数据以启动
    ser.write(b'\x8a')
    # 剔除初始不稳定值
    temp = 0
    while True:
        dataHex = ser.read(7)  # 取7个字节
        dataDec = list(dataHex[1:4])  # 转换为十进制
        val = ((dataDec[0] << 16) + (dataDec[1] << 8) + dataDec[2])  # 还原真实数据
        if (val != 0 and temp == 0):
            temp = val
        if (val > 5 * temp):
            break

    print(tableName, " start store")
    # 循环n次取7个字节并存储
    t = 0
    while True:
        sql = "insert into %s values " % (tableName)
        for i in range(5):
            t = t + 1
            dataHex = ser.read(7)  # 取7个字节
            dataDec = list(dataHex[1:4])  # 转换为十进制
            val = ((dataDec[0] << 16) + (dataDec[1] << 8) + dataDec[2])  # 还原真实数据
            sql += "(%d,%d)," % (t, -val)
        sql = sql[:-1]
        cur.execute(sql)


def mulSwitch(it):
    if (it['flag']):
        time.sleep(3)
        DynamicWave.Display(it['table'])
    else:
        readData(it['idx'], it['table'])


def multipro(tableName):
    iters = []
    for i in range(2):
        for j in range(2):
            iters.append({'flag': i, 'idx': j, 'table': (tableName + '_0' + str(j + 1))})
    pool = multiprocessing.Pool()
    pool.map(mulSwitch, iters)


if __name__ == '__main__':
    multipro("dengbin_20180601")
