#include "main.h"
				
u8 state=0;		// 0-停止；1-开始；2-采集三轴加速度；3-蓝牙发送数组；
// 涉及数据包中包含脉搏波个数的值：
// 数组大小 40
// 串口中断 9
// gy_521 28-39
// 蓝牙 <40
u8 send[40];	// 存储一个要发送的蓝牙数据包

void main()
{
	Init();
	
	while(1)
	{
		if(state==2)		//采集三轴加速度
		{
			MPU6050_Store();
			state=3;
		}
		else if(state==3)	//蓝牙发送数组
		{
			BluetoothSend();
			state=1;
		}
	};
}

void Init()
{
	Uart_Init();  	//串口初始化
	MPU6050_Init();	//MPU6050初始化
	state=0;		//状态清零-停止
	send[0]=0xFF;	//数据头
}


//*********************************************************
//                      辅助函数
//*********************************************************
//延迟n毫秒
void Delaynms(unsigned int n)	
{						
	u8 i, j;

	while(n--)
	{
		i = 11;
		j = 188;
		do
		{
			while (--j);
		} while (--i);	
	}		
}