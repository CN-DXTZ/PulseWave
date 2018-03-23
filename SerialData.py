# 从串口数据读取
# python3.6
import serial
import struct


# 设置串口
def SetSerial():
    ser = serial.Serial()
    ser.baudrate = 38400  # 波特率:38400
    ser.port = 'COM6'  # 端口:COM3
    # print(ser) # 串口信息
    ser.open()  # 打开串口
    # print(ser.is_open)  # 检验串口是否打开
    return ser


# 读取脉搏并存储
def ReadData(ser, num=100):
    ser.write(b'\x8a')  # 发送数据以启动
    file = open('data.txt', 'w')
    # 循环n次取7个字节并存储
    for i in range(num):
        dataB = ser.read(7)  # 取7个字节
        dataOri = list(struct.unpack('BBBBBBB', bytes(dataB)))  # 转换为十进制
        print(dataOri)
        data = [(dataOri[1] << 16) + (dataOri[2] << 8) + dataOri[3]] + dataOri[4:]  # 还原真实数据
        file.write(str(data) + '\n')
        print(data)
        # print()
    file.close()
    ser.write(b'\x88')  # 发送数据以停止


if __name__ == '__main__':
    ReadData(SetSerial())
