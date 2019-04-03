#include "pulse.h"

void Pulse_Start()
{
	IE2 = 0x01;				// 开启串口2中断
	Uart2_SendChar(0x8a);	// 开启脉搏波传感器
}
void Pulse_Stop()
{
	Uart2_SendChar(0x88);	// 关闭脉搏波传感器
	IE2 = 0x00;				// 关闭串口2中断
}