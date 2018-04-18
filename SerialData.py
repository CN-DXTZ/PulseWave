# 从串口数据读取
import serial
import struct
import MySQL
import time


# 设置串口
def SetSerial():
    ser = serial.Serial()
    ser.baudrate = 38400  # 波特率:38400
    ser.port = 'COM4'  # 端口
    # print(ser) # 串口信息
    ser.open()  # 打开串口
    # print(ser.is_open)  # 检验串口是否打开
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
            # print(val)
        # print("-----------------------------------")
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
    # SetSerial()
    ReadDataToSQL(11)
