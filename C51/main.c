#include "main.h"

				
u8 state=0;//0-���д������ѹرգ�1-���д������Ѵ򿪣�2-��Triaxial�Ѵ򿪣�3-��Pulse�Ѵ�

void main()
{
	Init();
	
	while(1)
	{
		if(state==1)		//����������������
		{
			MPU6050_Display();
		}
		else if(state==2)	//��Triaxial����
		{
			MPU6050_Display();
		}
		else if(state==3)	//��Pulse����
		{
			
		}
		
	};
}

void Init()
{
	Uart_Init();  	//���ڳ�ʼ��
	MPU6050_Init();	//MPU6050��ʼ��
	state=0;		//״̬����
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