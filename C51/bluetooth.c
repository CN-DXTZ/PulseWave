#include "bluetooth.h"

extern u8 send[];	// �洢һ��Ҫ���͵��������ݰ�


void BluetoothSend()
{
	u8 i=0;
	for(;i<43;i++)
	{
		Uart1_SendChar( send[i] );
	}
}