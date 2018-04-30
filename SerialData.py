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
    # print(ser)  # 串口信息
    ser.write(b'\x88')
    return ser


def ReadDataToSQL(index):
    ser = SetSerial()
    cur = MySQL.MysqlInit()
    time.sleep(1)
    try:
        sql = "drop table if exists data%d" % index
        cur.execute(sql)
        sql = "create table data%d (time int, value int)" % index
        cur.execute(sql)
        # 发送数据以启动
        ser.write(b'\x8a')
        # 剔除初始不稳定值
        temp = 0
        while True:
            dataB = ser.read(7)  # 取7个字节
            dataO = list(struct.unpack('BBBBBBB', bytes(dataB)))  # 转换为十进制
            val = ((dataO[1] << 16) + (dataO[2] << 8) + dataO[3])  # 还原真实数据
            if (val != 0 and temp == 0):
                temp = val
            if (val > 5 * temp):
                break
        multiprocessing.Process(target=DynamicWave.Display, args=(index,)).start()
        # 循环n次取7个字节并存储
        t = 0
        while True:
            t = t + 1
            dataB = ser.read(7)  # 取7个字节
            dataO = list(struct.unpack('BBBBBBB', bytes(dataB)))  # 转换为十进制
            val = ((dataO[1] << 16) + (dataO[2] << 8) + dataO[3])  # 还原真实数据
            # data = [val] + dataO[4:]
            # print(val)
            sql = "insert into data%d (time,value) value (%d,%d)" % (index, t, -val)
            cur.execute(sql)
    finally:
        ser.write(b'\x88')  # 发送数据以停止


if __name__ == '__main__':
    ReadDataToSQL(11)
