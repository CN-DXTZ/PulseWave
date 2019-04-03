# 从串口数据读取
import serial
import serial.tools.list_ports
import struct
import MySQL
import time
import multiprocessing
import DynamicWave


# 设置串口
def SetSerial():
    plist = list(serial.tools.list_ports.comports())
    ser = serial.Serial(plist[0][0], baudrate=38400)
    print(ser)  # 串口信息
    ser.write(b'\x88')
    return ser


def ReadDataToSQL(tableName):
    ser = SetSerial()
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
    multiprocessing.Process(target=DynamicWave.Display, args=(tableName,)).start()

    # 循环n次取7个字节并存储
    t = 0
    while True:
        t = t + 1
        dataHex = ser.read(7)  # 取7个字节
        dataDec = list(dataHex[1:4])  # 转换为十进制
        val = ((dataDec[0] << 16) + (dataDec[1] << 8) + dataDec[2])  # 还原真实数据
        # data = [val] + dataO[4:]
        # print(val)
        sql = "insert into %s (time,value) value (%d,%d)" % (tableName, t, -val)
        cur.execute(sql)


if __name__ == '__main__':
    ReadDataToSQL("dengbin_20180601_01")
