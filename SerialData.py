# 从串口数据读取
# python3.6
import serial
import struct
import MySQL
import time


# 设置串口
def SetSerial():
    ser = serial.Serial()
    ser.baudrate = 38400  # 波特率:38400
    ser.port = 'COM3'  # 端口
    # print(ser) # 串口信息
    ser.open()  # 打开串口
    # print(ser.is_open)  # 检验串口是否打开
    return ser


def ReadDataSQL():
    ser = SetSerial()
    cur = MySQL.MysqlInit()
    try:
        # 清空数据表
        sql = "delete from wavedata;"
        cur.execute(sql)
        # 发送数据以启动
        ser.write(b'\x8a')
        a = ser.read(2100)
        # 循环n次取7个字节并存储
        while True:
            t = time.time()
            dataB = ser.read(7)  # 取7个字节
            dataO = list(struct.unpack('BBBBBBB', bytes(dataB)))  # 转换为十进制
            val = -((dataO[1] << 16) + (dataO[2] << 8) + dataO[3])  # 还原真实数据
            data = [val] + dataO[4:]
            # print(data)
            sql = "insert into wavedata (time,value) value (%f,%d)" % (t, val)
            cur.execute(sql)
    finally:
        ser.write(b'\x88')  # 发送数据以停止


if __name__ == '__main__':
    # ReadDataText()
    ReadDataSQL()
