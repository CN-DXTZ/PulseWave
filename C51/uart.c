/***********************************************************************
���Ŷ��壺���п�1�����ա�����RxD/P3.0		���͡�������TxD/P3.1�� 
          ���п�2�����ա�����RxD2/P1.2		���͡�������TxD2/P1.3��
�������������п�1���͵���ͨ��
          ���п�2����������������ͨ��
***********************************************************************/
#include "uart.h"


//**********��������************
extern u8 state;	//�ⲿ״̬����
u8 Pulse_state=0;	//��¼������������ÿ��ĵڼ�λ



//*********************************************************
//                      ���ڳ�ʼ��
//*********************************************************
// ����1��ʼ��
void Uart1_Init()		//38400bps@11.0592MHz
{
	PCON &= 0x7F;		//�����ʲ�����
	SCON = 0x50;		//8λ����,�ɱ䲨����
	AUXR |= 0x40;		//��ʱ��1ʱ��ΪFosc,��1T
	AUXR &= 0xFE;		//����1ѡ��ʱ��1Ϊ�����ʷ�����
	TMOD &= 0x0F;		//�����ʱ��1ģʽλ
	TMOD |= 0x20;		//�趨��ʱ��1Ϊ8λ�Զ���װ��ʽ
	
	TH1 = TL1 = -(FOSC/32/BAUD); //�趨��ʱ����ֵ����װֵ
	
	ES = 1;			//������1�ж�
	ET1 = 0;		//��ֹ��ʱ��1�ж�
	TR1 = 1;		//������ʱ��1
	
	Uart1_SendString("����ͨ��1�ѳ�ʼ��\r\n");
}
// ����2��ʼ��
void Uart2_Init()		//38400bps@11.0592MHz
{
	AUXR &= 0xF7;		//�����ʲ�����
	S2CON = 0x50;		//8λ����,�ɱ䲨����
	AUXR |= 0x04;		//���������ʷ�����ʱ��ΪFosc,��1T
	BRT = -(FOSC/32/BAUD);;			//�趨���������ʷ�������װֵ
	AUXR |= 0x10;		//�������������ʷ�����
	IE2 = 0x00;         //�ش���2�жϣ�0x01����
	
	Uart1_SendString("����ͨ��2�ѳ�ʼ��\r\n");
}
// ȫ������ͨ�ų�ʼ��
void Uart_Init()		//38400bps@11.0592MHz
{
	Uart1_Init();
	Uart2_Init();
	ES=1;			//�򿪽����ж�
	EA=1;			//�����ж�
	
	Uart1_SendString("����ͨ����ȫ����ʼ��\r\n");
}



//*********************************************************
//                      �����ж�
//*********************************************************
// ���п�1�жϣ����ݽ��յ������ָı�state��״̬
void Uart1_Interrupt() interrupt 4 using 1
{
	u8 receive=SBUF; //�洢���յ�������
	
    if (RI)//�����жϱ�־
    {
        RI = 0;				//���RI�����жϱ�־
		Uart1_SendChar(receive);//���ͽ��յ�����
		
		switch(receive)
		{
			case 0x31: //'1'
			{
				state=0;
				Pulse_Stop();
				Uart1_SendString("--Close All Sensor\r\n");			
				break;
			}
			case 0x32: //'2'
			{
				state=1;
				Pulse_Start();
				Uart1_SendString("--Open All Sensor\r\n");
				break;
			}
			case 0x33: //'3'
			{
				state=2;
				Uart1_SendString("--Only Open Triaxial\r\n");
				break;
			}
			case 0x34: //'4'
			{
				state=0;
				Uart1_SendString("--Only Open Triaxial\r\n");
				break;
			}				
			case 0x35: //'5'
			{
				state=3;
				Pulse_Start();
				Uart1_SendString("--Only Open Pulse\r\n");
				break;
			}			
			case 0x36: //'6'
			{
				state=0;
				Pulse_Stop();
				Uart1_SendString("--Only Close Pulse\r\n");
				break;
			}				
		}
    }
}

//���ݸ�ʽ���߸��ֽ�Ϊһ�����ݣ�������200Hz
//��0�ֽڣ�����ͷ 0xff
//��1�ֽڣ�������ԭʼ����16~23λ
//��2�ֽڣ�������ԭʼ����8~15λ
//��3�ֽڣ�������ԭʼ����0~7λ
//��4�ֽڣ�����
//��5�ֽڣ�Ѫ�����Ͷ�
//��6�ֽڣ�����ѹ
//u8 Pulse_state=0;	//��¼��ǰΪÿ�����ݵĵڼ�λ
//
// ���п�2�жϣ����ݽ��յ������ֶ�ȡ����������������
void Uart2_Interrupt() interrupt 8 using 2
{
	u8 receive=S2BUF;		//�洢���յ�����
    if (S2CON&S2RI)	//�����жϱ�־
    {
        S2CON &= ~S2RI;			//��������жϱ�־
		
		if(receive==0xff)	//����ͷ
		{
			Uart1_SendChar(receive);
			Pulse_state=1;
		}
		else if(Pulse_state<=3)//ԭʼ����
		{
			Uart1_SendChar(receive);
			Pulse_state++;
		}
    }
}



//*********************************************************
//                      ����1��������
//*********************************************************
//�����ַ�
void Uart1_SendChar(u8 dat)
{
	SBUF=dat;
	while(!TI);			//�ȴ����ݷ������
	TI=0;				//���TI�����жϱ�־λ
}

//�����ַ���
void Uart1_SendString(u8 *s)
{	
    while (*s)              //�ж��Ƿ��ַ����ص�
    {
        Uart1_SendChar(*s++);     //���͵�ǰ�ַ����ƶ�ָ��
    }
}

//��������
void Uart1_SendInt(int date)
{	
	char s[12]="";
	int2str(date,s);
	Uart1_SendString(s);
}



//*********************************************************
//                      ����2��������
//*********************************************************
//�����ַ�
void Uart2_SendChar(u8 dat)
{
	S2BUF = dat;		          // ���ַ����봮��2�ķ��ͻ���Ĵ���
	while(!(S2CON&S2TI));	      // �жϷ����Ƿ������S2CON.bit2 = 0�������� 1�����ͽ���
	S2CON &= ~S2TI;			      // �ֶ������־λ����S2CON.bit2 = 0
}


//�����ַ���
void Uart2_SendString(u8 *s)
{
    while (*s)              //�ж��Ƿ��ַ����ص�
    {
        Uart2_SendChar(*s++);     //���͵�ǰ�ַ����ƶ�ָ��
    }
}

//��������
void Uart2_SendInt(int date)
{	
	char s[12]="";
	int2str(date,s);
	Uart2_SendString(s);
}



//*********************************************************
//                      ��������
//*********************************************************
//����ת�ַ���
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