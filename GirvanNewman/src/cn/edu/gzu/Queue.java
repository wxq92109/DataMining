package cn.edu.gzu;

//注意，此队列是循环队列，是我们从库函数里经过改造后的类，主要用于GN算法的程序myGN.java以及GN_use
class Queue //队列类
{
int maxSize; //队列长度，由构造函数初始化
int[] queArray; // 队列
int front; //队头
int rear; //队尾
int nItems; //元素的个数
//--------------------------------------------------------------
public Queue(int s) // 构造函数
{
maxSize = s;
queArray = new int[maxSize];
front = 0;
rear = -1;
nItems = 0;
}
//--------------------------------------------------------------
public void insert(int j) // 进队列
{
if(rear == maxSize-1) // 处理循环
rear = -1;
queArray[++rear] = j; // 队尾指针加1,把值j加入队尾
nItems++;
}
//--------------------------------------------------------------
public int remove() // 取得队列的队头元素。
{
int temp = queArray[front++]; // 取值和修改队头指针
if(front == maxSize) // 处理循环
front = 0;
nItems--;
return temp;
}
//--------------------------------------------------------------
public int peekFront() // 取得队列的队头元素。该运算与 remove()不同，remove()要修改队头元素指针。
{
return queArray[front];
}
//--------------------------------------------------------------
public boolean isEmpty() // 判队列是否为空。若为空返回一个真值，否则返回一个假值。
{
return (nItems==0);
}
//--------------------------------------------------------------
public boolean isFull() // 判队列是否已满。若已满返回一个真值，否则返回一个假值。
{
return (nItems==maxSize);
}
//--------------------------------------------------------------
public int size() // 返回队列的长度
{
return nItems;
}
}

