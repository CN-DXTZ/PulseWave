#include "pulse.h"

void Pulse_Start()
{
	IE2 = 0x01;				// ��������2�ж�
	Uart2_SendChar(0x8a);	// ����������������
}
void Pulse_Stop()
{
	Uart2_SendChar(0x88);	// �ر�������������
	IE2 = 0x00;				// �رմ���2�ж�
}