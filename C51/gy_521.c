#include "gy_521.h"

//**********����51��Ƭ���˿�************
sbit    SCL=P1^5;		//IICʱ�����Ŷ���
sbit    SDA=P1^4;		//IIC�������Ŷ���


//************************************************************************
//									I2C
//************************************************************************
//I2C��ʼ�ź�
void I2C_Start()
{
    SDA = 1;                    //����������
    SCL = 1;                    //����ʱ����
    Delay5us();                 //��ʱ
    SDA = 0;                    //�����½���
    Delay5us();                 //��ʱ
    SCL = 0;                    //����ʱ����
}

//I2Cֹͣ�ź�
void I2C_Stop()
{
    SDA = 0;                    //����������
    SCL = 1;                    //����ʱ����
    Delay5us();                 //��ʱ
    SDA = 1;                    //����������
    Delay5us();                 //��ʱ
}

//I2C����Ӧ���ź�
//��ڲ���:ack (0:ACK 1:NAK)
void I2C_SendACK(bit ack)
{
    SDA = ack;                  //дӦ���ź�
    SCL = 1;                    //����ʱ����
    Delay5us();                 //��ʱ
    SCL = 0;                    //����ʱ����
    Delay5us();                 //��ʱ
}

//I2C����Ӧ���ź�
bit I2C_RecvACK()
{
    SCL = 1;                    //����ʱ����
    Delay5us();                 //��ʱ
    CY = SDA;                   //��Ӧ���ź�
    SCL = 0;                    //����ʱ����
    Delay5us();                 //��ʱ
    return CY;
}

//��I2C���߷���һ���ֽ�����
void I2C_SendByte(u8 dat)
{
    u8 i;
    for (i=0; i<8; i++)         //8λ������
    {
        dat <<= 1;              //�Ƴ����ݵ����λ
        SDA = CY;               //�����ݿ�
        SCL = 1;                //����ʱ����
        Delay5us();             //��ʱ
        SCL = 0;                //����ʱ����
        Delay5us();             //��ʱ
    }
    I2C_RecvACK();
}

//��I2C���߽���һ���ֽ�����
u8 I2C_RecvByte()
{
    u8 i;
    u8 dat = 0;
    SDA = 1;                    //ʹ���ڲ�����,׼����ȡ����,
    for (i=0; i<8; i++)         //8λ������
    {
        dat <<= 1;
        SCL = 1;                //����ʱ����
        Delay5us();             //��ʱ
        dat |= SDA;             //������               
        SCL = 0;                //����ʱ����
        Delay5us();             //��ʱ
    }
    return dat;
}

//��I2C�豸д��һ���ֽ�����
void Single_WriteI2C(u8 REG_Address,u8 REG_data)
{
    I2C_Start();                  //��ʼ�ź�
    I2C_SendByte(SlaveAddress);   //�����豸��ַ+д�ź�
    I2C_SendByte(REG_Address);    //�ڲ��Ĵ�����ַ��
    I2C_SendByte(REG_data);       //�ڲ��Ĵ������ݣ�
    I2C_Stop();                   //����ֹͣ�ź�
}

//��I2C�豸��ȡһ���ֽ�����
u8 Single_ReadI2C(u8 REG_Address)
{
	u8 REG_data;
	I2C_Start();                   //��ʼ�ź�
	I2C_SendByte(SlaveAddress);    //�����豸��ַ+д�ź�
	I2C_SendByte(REG_Address);     //���ʹ洢��Ԫ��ַ����0��ʼ	
	I2C_Start();                   //��ʼ�ź�
	I2C_SendByte(SlaveAddress+1);  //�����豸��ַ+���ź�
	REG_data=I2C_RecvByte();       //�����Ĵ�������
	I2C_SendACK(1);                //����Ӧ���ź�
	I2C_Stop();                    //ֹͣ�ź�
	return REG_data;
}


//************************************************************************
//								MPU6050
//************************************************************************
//��ʼ��MPU6050
void MPU6050_Init()
{
	Single_WriteI2C(PWR_MGMT_1, 0x00);	//�������״̬
	Single_WriteI2C(SMPLRT_DIV, 0x07);
	Single_WriteI2C(CONFIG, 0x06);
	Single_WriteI2C(GYRO_CONFIG, 0x18);
	Single_WriteI2C(ACCEL_CONFIG, 0x01);
	
	Uart1_SendString("Triaxial Initial\r\n");
}

//�ϳ�����
//int MPU6050_GetData(u8 REG_Address)
//{
//	u8 H,L;
//	H=Single_ReadI2C(REG_Address);
//	L=Single_ReadI2C(REG_Address+1);
//	return ((H<<8)+L);   //�ϳ�����
//}

//��ʾ����
//void MPU6050_Display()
//{
//	Uart1_SendString("Triaxial: ");
//	Uart1_SendInt(MPU6050_GetData(ACCEL_XOUT_H));		//��ʾX����ٶ�
//	Uart1_SendInt(MPU6050_GetData(ACCEL_YOUT_H));		//��ʾY����ٶ�
//	Uart1_SendInt(MPU6050_GetData(ACCEL_ZOUT_H));		//��ʾZ����ٶ�
//	Uart1_SendInt(MPU6050_GetData(GYRO_XOUT_H));		//��ʾX����ٶ�
//	Uart1_SendInt(MPU6050_GetData(GYRO_YOUT_H));		//��ʾY����ٶ�
//	Uart1_SendInt(MPU6050_GetData(GYRO_ZOUT_H));		//��ʾZ����ٶ�
//	Uart1_SendString("\r\n");
//}

extern u8 send[];	// �洢һ��Ҫ���͵��������ݰ�
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
//                      ��������
//*********************************************************
//�ӳ�5΢��
void Delay5us()
{
	unsigned char i;

	_nop_();
	_nop_();
	_nop_();
	i = 10;
	while (--i);
}