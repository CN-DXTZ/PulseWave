/***********************************************************************
引脚定义：串行口1：接收———RxD/P3.0		发送————TxD/P3.1； 
          串行口2：接收———RxD2/P1.2		发送————TxD2/P1.3；
功能描述：串行口1：和电脑/蓝牙通信
          串行口2：和脉搏波传感器通信
***********************************************************************/
#include "uart.h"


//**********变量声明************
extern u8 state;	//外部状态变量




//*********************************************************
//                      串口初始化
//*********************************************************
// 串口1初始化
void Uart1_Init()		//38400bps@11.0592MHz
{
	PCON &= 0x7F;		//波特率不倍速
	SCON = 0x50;		//8位数据,可变波特率
	AUXR |= 0x40;		//定时器1时钟为Fosc,即1T
	AUXR &= 0xFE;		//串口1选择定时器1为波特率发生器
	TMOD &= 0x0F;		//清除定时器1模式位
	TMOD |= 0x20;		//设定定时器1为8位自动重装方式
	
	TH1 = TL1 = -(FOSC/32/BAUD1); //设定定时器初值和重装值
	
	ES = 1;			//允许串口1中断
	ET1 = 0;		//禁止定时器1中断
	TR1 = 1;		//启动定时器1
}
// 串口2初始化
void Uart2_Init()		//38400bps@11.0592MHz
{
	AUXR &= 0xF7;		//波特率不倍速
	S2CON = 0x50;		//8位数据,可变波特率
	AUXR |= 0x04;		//独立波特率发生器时钟为Fosc,即1T
	BRT = -(FOSC/32/BAUD2);			//设定独立波特率发生器重装值
	AUXR |= 0x10;		//启动独立波特率发生器
	IE2 = 0x00;         //关串口2中断（0x01开）
}
// 全部串口通信初始化
void Uart_Init()		//38400bps@11.0592MHz
{
	Uart1_Init();
	Uart2_Init();
	ES=1;			//打开接收中断
	EA=1;			//打开总中断
	
	Uart1_SendString("Uart Initial\r\n");
}



//*********************************************************
//                      串口中断
//*********************************************************
// 串行口1中断：根据接收到的数字改变state的状态
void Uart1_Interrupt() interrupt 4 using 1
{
	u8 receive=SBUF; //存储接收到的数据
	
    if (RI)//接收中断标志
    {
        RI = 0;				//清除RI接收中断标志
//		Uart1_SendChar(receive);//发送接收到的数
		
		switch(receive)
		{
			case 0x31: // 停止
			{
				state=0;
				Pulse_Stop();
				Uart1_SendString("stop\r\n");			
				break;
			}
			case 0x32: // 开始
			{
				state=1;
				Pulse_Start();
				Uart1_SendString("start\r\n");
				break;
			}
		}
    }
}

//数据格式：七个字节为一组数据，采样率200Hz
//第0字节：数据头 0xff
//第1字节：脉搏波原始数据16~23位
//第2字节：脉搏波原始数据8~15位
//第3字节：脉搏波原始数据0~7位
//第4字节：心率
//第5字节：血氧饱和度
//第6字节：收缩压
//
// 串行口2中断：根据接收到的数字读取脉搏波传感器数据
extern u8 send[];	// 存储一个要发送的蓝牙数据包
u8 Pulse_state=3;	//记录读取的脉搏波数据在每组的第几位
u8 Pulse_num=0;		//记录脉搏波数据存储的数量
void Uart2_Interrupt() interrupt 8 using 2
{
	u8 receive=S2BUF;		//存储接收到的数
    if (S2CON&S2RI)	//接收中断标志
    {
        S2CON &= ~S2RI;			//清除接收中断标志
		
		if(Pulse_state==3 && receive==0xff)	//数据头
		{
			Pulse_state=0;
		}
		else if(Pulse_state<3)//原始数据
		{
			send[Pulse_num]=receive;
			Pulse_state++;
			Pulse_num++;
						
			// 10个脉搏波读取完
			if(Pulse_num==30) 
			{
				Pulse_num=1;
				state=2;
			}
		}
    }
}



//*********************************************************
//                      串口1发送数据
//*********************************************************
//发送字符
void Uart1_SendChar(u8 dat)
{
	SBUF=dat;
	while(!TI);			//等待数据发送完成
	TI=0;				//清除TI发送中断标志位
}

//发送字符串
void Uart1_SendString(u8 *s)
{	
    while (*s)              //判断是否到字符串重点
    {
        Uart1_SendChar(*s++);     //发送当前字符并移动指针
    }
}

//发送整型
void Uart1_SendInt(int date)
{	
	char s[12]="";
	int2str(date,s);
	Uart1_SendString(s);
}



//*********************************************************
//                      串口2发送数据
//*********************************************************
//发送字符
void Uart2_SendChar(u8 dat)
{
	S2BUF = dat;		          // 将字符送入串口2的发送缓冲寄存器
	while(!(S2CON&S2TI));	      // 判断发送是否结束：S2CON.bit2 = 0：发送中 1：发送结束
	S2CON &= ~S2TI;			      // 手动清零标志位，令S2CON.bit2 = 0
}


//发送字符串
void Uart2_SendString(u8 *s)
{
    while (*s)              //判断是否到字符串重点
    {
        Uart2_SendChar(*s++);     //发送当前字符并移动指针
    }
}

//发送整型
void Uart2_SendInt(int date)
{	
	char s[12]="";
	int2str(date,s);
	Uart2_SendString(s);
}



//*********************************************************
//                      辅助函数
//*********************************************************
//整型转字符串
void int2str(int date, char s[12])
{
	u8 num = 0, i = 0, flag = 0;
	char s0[12] = "";
	if (date < 0)
		flag = date = -date;
	do
	{
		s0[num++] = date % 10 + 0x30;
	} while (date /= 10);
	if (flag)
		s0[num] = '-';
	else
		num--;
	for (i = 0; i <= 10; i++)
		s[i] = s0[10 - i] ? s0[10 - i] : ' ';
}