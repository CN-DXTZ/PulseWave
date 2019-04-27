#include "main.h"
				
u8 state=0;		// 0-ֹͣ��1-��ʼ��2-�ɼ�������ٶȣ�3-�����������飻
u8 send[43];	// �洢һ��Ҫ���͵��������ݰ�

void main()
{
	Init();
	
	while(1)
	{
		if(state==2)		//�ɼ�������ٶ�
		{
			MPU6050_Store();
			state=3;
		}
		else if(state==3)	//������������
		{
			BluetoothSend();
			state=1;
		}
	};
}

void Init()
{
	Uart_Init();  	//���ڳ�ʼ��
	MPU6050_Init();	//MPU6050��ʼ��
	state=0;		//״̬����-ֹͣ
	send[0]=0xFF;	//����ͷ
}


//*********************************************************
//                      ��������
//*********************************************************
//�ӳ�n����
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