#include "gy_521.h"

//**********定义51单片机端口************
sbit    SCL=P1^5;		//IIC时钟引脚定义
sbit    SDA=P1^4;		//IIC数据引脚定义


//************************************************************************
//									I2C
//************************************************************************
//I2C起始信号
void I2C_Start()
{
    SDA = 1;                    //拉高数据线
    SCL = 1;                    //拉高时钟线
    Delay5us();                 //延时
    SDA = 0;                    //产生下降沿
    Delay5us();                 //延时
    SCL = 0;                    //拉低时钟线
}

//I2C停止信号
void I2C_Stop()
{
    SDA = 0;                    //拉低数据线
    SCL = 1;                    //拉高时钟线
    Delay5us();                 //延时
    SDA = 1;                    //产生上升沿
    Delay5us();                 //延时
}

//I2C发送应答信号
//入口参数:ack (0:ACK 1:NAK)
void I2C_SendACK(bit ack)
{
    SDA = ack;                  //写应答信号
    SCL = 1;                    //拉高时钟线
    Delay5us();                 //延时
    SCL = 0;                    //拉低时钟线
    Delay5us();                 //延时
}

//I2C接收应答信号
bit I2C_RecvACK()
{
    SCL = 1;                    //拉高时钟线
    Delay5us();                 //延时
    CY = SDA;                   //读应答信号
    SCL = 0;                    //拉低时钟线
    Delay5us();                 //延时
    return CY;
}

//向I2C总线发送一个字节数据
void I2C_SendByte(u8 dat)
{
    u8 i;
    for (i=0; i<8; i++)         //8位计数器
    {
        dat <<= 1;              //移出数据的最高位
        SDA = CY;               //送数据口
        SCL = 1;                //拉高时钟线
        Delay5us();             //延时
        SCL = 0;                //拉低时钟线
        Delay5us();             //延时
    }
    I2C_RecvACK();
}

//从I2C总线接收一个字节数据
u8 I2C_RecvByte()
{
    u8 i;
    u8 dat = 0;
    SDA = 1;                    //使能内部上拉,准备读取数据,
    for (i=0; i<8; i++)         //8位计数器
    {
        dat <<= 1;
        SCL = 1;                //拉高时钟线
        Delay5us();             //延时
        dat |= SDA;             //读数据               
        SCL = 0;                //拉低时钟线
        Delay5us();             //延时
    }
    return dat;
}

//向I2C设备写入一个字节数据
void Single_WriteI2C(u8 REG_Address,u8 REG_data)
{
    I2C_Start();                  //起始信号
    I2C_SendByte(SlaveAddress);   //发送设备地址+写信号
    I2C_SendByte(REG_Address);    //内部寄存器地址，
    I2C_SendByte(REG_data);       //内部寄存器数据，
    I2C_Stop();                   //发送停止信号
}

//从I2C设备读取一个字节数据
u8 Single_ReadI2C(u8 REG_Address)
{
	u8 REG_data;
	I2C_Start();                   //起始信号
	I2C_SendByte(SlaveAddress);    //发送设备地址+写信号
	I2C_SendByte(REG_Address);     //发送存储单元地址，从0开始	
	I2C_Start();                   //起始信号
	I2C_SendByte(SlaveAddress+1);  //发送设备地址+读信号
	REG_data=I2C_RecvByte();       //读出寄存器数据
	I2C_SendACK(1);                //接收应答信号
	I2C_Stop();                    //停止信号
	return REG_data;
}


//************************************************************************
//								MPU6050
//************************************************************************
//初始化MPU6050
void MPU6050_Init()
{
	Single_WriteI2C(PWR_MGMT_1, 0x00);	//解除休眠状态
	Single_WriteI2C(SMPLRT_DIV, 0x07);
	Single_WriteI2C(CONFIG, 0x06);
	Single_WriteI2C(GYRO_CONFIG, 0x18);
	Single_WriteI2C(ACCEL_CONFIG, 0x01);
	
	Uart1_SendString("Triaxial Initial\r\n");
}

//合成数据
//int MPU6050_GetData(u8 REG_Address)
//{
//	u8 H,L;
//	H=Single_ReadI2C(REG_Address);
//	L=Single_ReadI2C(REG_Address+1);
//	return ((H<<8)+L);   //合成数据
//}

//显示数据
//void MPU6050_Display()
//{
//	Uart1_SendString("Triaxial: ");
//	Uart1_SendInt(MPU6050_GetData(ACCEL_XOUT_H));		//显示X轴加速度
//	Uart1_SendInt(MPU6050_GetData(ACCEL_YOUT_H));		//显示Y轴加速度
//	Uart1_SendInt(MPU6050_GetData(ACCEL_ZOUT_H));		//显示Z轴加速度
//	Uart1_SendInt(MPU6050_GetData(GYRO_XOUT_H));		//显示X轴角速度
//	Uart1_SendInt(MPU6050_GetData(GYRO_YOUT_H));		//显示Y轴角速度
//	Uart1_SendInt(MPU6050_GetData(GYRO_ZOUT_H));		//显示Z轴角速度
//	Uart1_SendString("\r\n");
//}

extern u8 send[];	// 存储一个要发送的蓝牙数据包
void MPU6050_Store()
{
	send[28]=Single_ReadI2C(ACCEL_XOUT_H);
	send[29]=Single_ReadI2C(ACCEL_XOUT_L);
	send[30]=Single_ReadI2C(ACCEL_YOUT_H);
	send[31]=Single_ReadI2C(ACCEL_YOUT_L);
	send[32]=Single_ReadI2C(ACCEL_ZOUT_H);
	send[33]=Single_ReadI2C(ACCEL_ZOUT_L);
	send[34]=Single_ReadI2C(GYRO_XOUT_H);
	send[35]=Single_ReadI2C(GYRO_XOUT_L);
	send[36]=Single_ReadI2C(GYRO_YOUT_H);
	send[37]=Single_ReadI2C(GYRO_YOUT_L);
	send[38]=Single_ReadI2C(GYRO_ZOUT_H);
	send[39]=Single_ReadI2C(GYRO_ZOUT_L);
}



//*********************************************************
//                      辅助函数
//*********************************************************
//延迟5微秒
void Delay5us()
{
	unsigned char i;

	_nop_();
	_nop_();
	_nop_();
	i = 10;
	while (--i);
}