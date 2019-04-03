#include "main.h"

				
u8 state=0;//0-所有传感器已关闭；1-所有传感器已打开；2-仅Triaxial已打开；3-仅Pulse已打开

void main()
{
	Init();
	
	while(1)
	{
		if(state==1)		//两个传感器都工作
		{
			MPU6050_Display();
		}
		else if(state==2)	//仅Triaxial工作
		{
			MPU6050_Display();
		}
		else if(state==3)	//仅Pulse工作
		{
			
		}
		
	};
}

void Init()
{
	Uart_Init();  	//串口初始化
	MPU6050_Init();	//MPU6050初始化
	state=0;		//状态清零
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