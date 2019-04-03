#ifndef _MAIN_H
#define _MAIN_H
#include "main.h"
#endif


#define FOSC 11059200L		//晶振
#define BAUD 38400			//波特率

#define S2RI 0x01	     // 串口2接收中断请求标志位
#define S2TI 0x02	     // 串口2发送中断请求标志位


//*********************************************************
//                      函数声明
//*********************************************************
void Uart1_Init();					// 串口1初始化
void Uart2_Init();					// 串口2初始化
void Uart_Init();					// 串口通信初始化

void int2str(int date, char s[12]);	//整型转字符串

void Uart1_SendChar(unsigned char date);	//串口1发送数据
void Uart1_SendString(unsigned char *s);	//串口1发送字符串
void Uart1_SendInt(int date);				//串口1发送整型

void Uart2_SendChar(unsigned char date);	//串口2发送数据
void Uart2_SendString(unsigned char *s);	//串口2发送字符串
void Uart2_SendInt(int date);				//串口2发送整型