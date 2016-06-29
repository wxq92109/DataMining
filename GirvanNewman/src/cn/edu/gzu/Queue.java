package cn.edu.gzu;

//ע�⣬�˶�����ѭ�����У������Ǵӿ⺯���ﾭ���������࣬��Ҫ����GN�㷨�ĳ���myGN.java�Լ�GN_use
class Queue //������
{
int maxSize; //���г��ȣ��ɹ��캯����ʼ��
int[] queArray; // ����
int front; //��ͷ
int rear; //��β
int nItems; //Ԫ�صĸ���
//--------------------------------------------------------------
public Queue(int s) // ���캯��
{
maxSize = s;
queArray = new int[maxSize];
front = 0;
rear = -1;
nItems = 0;
}
//--------------------------------------------------------------
public void insert(int j) // ������
{
if(rear == maxSize-1) // ����ѭ��
rear = -1;
queArray[++rear] = j; // ��βָ���1,��ֵj�����β
nItems++;
}
//--------------------------------------------------------------
public int remove() // ȡ�ö��еĶ�ͷԪ�ء�
{
int temp = queArray[front++]; // ȡֵ���޸Ķ�ͷָ��
if(front == maxSize) // ����ѭ��
front = 0;
nItems--;
return temp;
}
//--------------------------------------------------------------
public int peekFront() // ȡ�ö��еĶ�ͷԪ�ء��������� remove()��ͬ��remove()Ҫ�޸Ķ�ͷԪ��ָ�롣
{
return queArray[front];
}
//--------------------------------------------------------------
public boolean isEmpty() // �ж����Ƿ�Ϊ�ա���Ϊ�շ���һ����ֵ�����򷵻�һ����ֵ��
{
return (nItems==0);
}
//--------------------------------------------------------------
public boolean isFull() // �ж����Ƿ�����������������һ����ֵ�����򷵻�һ����ֵ��
{
return (nItems==maxSize);
}
//--------------------------------------------------------------
public int size() // ���ض��еĳ���
{
return nItems;
}
}

