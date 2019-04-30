#include "bluetooth.h"

extern u8 send[];	// 存储一个要发送的蓝牙数据包


void BluetoothSend()
{
	u8 i=0;
	for(;i<40;i++)
	{
		Uart1_SendChar( send[i] );
	}
}