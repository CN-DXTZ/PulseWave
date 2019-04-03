#ifndef _MAIN_H
#define _MAIN_H
#include "main.h"
#endif


#define FOSC 11059200L		//����
#define BAUD 38400			//������

#define S2RI 0x01	     // ����2�����ж������־λ
#define S2TI 0x02	     // ����2�����ж������־λ


//*********************************************************
//                      ��������
//*********************************************************
void Uart1_Init();					// ����1��ʼ��
void Uart2_Init();					// ����2��ʼ��
void Uart_Init();					// ����ͨ�ų�ʼ��

void int2str(int date, char s[12]);	//����ת�ַ���

void Uart1_SendChar(unsigned char date);	//����1��������
void Uart1_SendString(unsigned char *s);	//����1�����ַ���
void Uart1_SendInt(int date);				//����1��������

void Uart2_SendChar(unsigned char date);	//����2��������
void Uart2_SendString(unsigned char *s);	//����2�����ַ���
void Uart2_SendInt(int date);				//����2��������