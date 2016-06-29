package cn.edu.gzu;

//import java.io.IOException;
import java.util.Vector;

//类GN_use主要配合myGN.java用，GN算法的实现需要GN_use中的方法，GN_use中的方法全都采用static的，为的是及时释放内存。
//队列里放的元素都是点号，是从1开始的，所以映射到数组中时都要减一
public class GN_use {

//	--------------------------------------------------------------	
	//方法一：
	//广度优先搜索法，从出发点s找最短路径static void BFS(Queue queue, Vector<v_side> vec_A_copy, int[] r_sign, dian B[], int s, int max, int num)
	static void BFS(Queue queue, Vector<v_side> vec_A, int[] r_sign, dian B[], int s, int max, int num){//
		//s是广度优先搜索的出发点,max是一个集团点的个数,num是一个集团的集团号码。注意s和下标的区别，s的下标为s-1.队列queue里的节点号都是从1开始的，不是从0开始，注意
		//int position=0;
		for(int k=0; k<max; k++){
			queue.queArray[k]=-1;//每次都要清除队列queue里的内容，下次调用时要用
		}
		queue.front=0;//队列queue里相关的项也要初始化。
		queue.rear=-1;
		queue.nItems=0;

		int i,j,signal=0;//通过signal的值来判断节点是不是叶节点，值为0时表示是叶节点,
		B[s-1].w=1;//源点的权值为1。
		queue.insert(s);       //源点进队列，注意每个节点只能进一次队列

		while(!(queue.isEmpty())){
			i=queue.remove();   //节点出队列，

			for(j=0; j<max; j++){

				if(j==s-1 || B[j].belong!=num || i-1==j )    //遇到是源点s或者不属于这个集团的点则转入下一次循环
					continue;	

				//position = GN_use.position_find(i-1, j);	
				if(GN_use.link_if(i-1, j, r_sign, vec_A)==true){//A_copy[position]==1if(GN_use.link_if(i-1, j, r_sign, vec_A_copy)==true)
					if(B[j].d==0){     //节点j+1没有指定距离值时的情况
						B[j].d=B[i-1].d+1;
						B[j].w=B[i-1].w;
						signal=1;//有点被处理过，要执行signal++，以告诉下面说明点i+1不是叶子节点。
						queue.insert(j+1);//访问过的节点进队列，注意每个节点只能进一次队列
					}
					if(B[j].d!=0 && B[j].d==B[i-1].d+1){  //节点j+1指定了距离值，但还有dj=di+1成立时的情况
						B[j].w+=B[i-1].w;
						signal=1;//有点被处理过，要执行signal++，以告诉下面说明点i+1不是叶子节点。
						//注意这里节点j+1不用再入队列，因为前面这个节点已经入过队列了
					}
					if(B[j].d!=0 && B[j].d<B[i-1].d+1)   //节点j+1指定了距离值，但是dj<di+1时,进入下一次循环。
						continue;
				}
			}

			if(signal==0)  //signal值为0说明点i是叶节点
				B[i-1].leaves=1;
			else
				signal=0;//把signal置0，下次循环出队列的点要用				
		}

		//下面是把所有的叶节点移动到队列的最后。
		for(i = 0; i < queue.nItems; i++){
			int queue_num = queue.queArray[i]-1;//得到当前节点，下标是从1开始的，注意，故要减一
			if(B[queue_num].leaves == 1){
				for(j = i; j < queue.nItems-1; j++){
					queue.queArray[i] = queue.queArray[i+1];
				}
				queue.queArray[queue.nItems-1] = queue_num+1;//最后把这个叶子节点插入到队列最后
			}
		}

	}
	//注意，用广度优先搜索算法后队列里的最后几个元素肯定是叶节点
//	--------------------------------------------------------------
//	方法二：
	//以某个源点s出发求对各边的介数，紧接方法BFS(Queue queue, int A_copy[][], dian B[], int s, int max, int num)之后
	//static void BC(Queue queue,Vector<v_side> vec_A_copy, Vector<v_side> vec_A, int[] r_sign, dian B[])
	static void BC(Queue queue, Vector<v_side> vec_A, int[] r_sign, dian B[]){//
		//数组A1[][]用来存放权值，
		int rear=queue.rear;//把队列的队尾下标赋给rear。
		int temp=rear;
		int i=queue.queArray[temp];
		//寻找叶节点和非叶节点,仅是判断
		while(B[i-1].leaves==1 && temp>=1)
			i=queue.queArray[--temp];
		//temp就是非叶节点和叶节点的分界，其中temp+1是第一个叶节点的下标。

		int j,t1,t2;//position=0,position2=0;
		double h1,h2;
		//下面是处理和叶节点相邻的节点的情况
		for(i=rear; i>temp; i--)//叶节点
			for(j=temp; j>-1; j--){//非叶节点
				t2=queue.queArray[j];
				t1=queue.queArray[i];

				if(B[t2-1].d < B[t1-1].d-1)//判断是否为叶节点的邻居节点，不是的话就跳出循环,进入下一次循环。
					continue;

				//position = GN_use.position_find(t1-1, t2-1);			
				if(GN_use.link_if(t1-1, t2-1, r_sign, vec_A) == true){//if(GN_use.link_if(t1-1, t2-1, r_sign, vec_A_copy) == true)
					h1=B[t1-1].w;//先把权值转化为double型后而赋给h2和h1
					h2=B[t2-1].w;	
					int index = GN_use.find_vec_index(t1-1, t2-1, r_sign, vec_A);
					if(index != -1){
						vec_A.elementAt(index).value = h2/h1;
					}
					//A1[position]=h2/h1;
				}
			}

		//接着处理的是其他节点的情况
		int h3,t3;
		double counter_temp=0;//与边v(t1,t2)相邻且位于下方的边权值的累加。
		for(i=temp; i>-1; i--)
			for(j=i-1; j>-1; j--){
				t2=queue.queArray[j];
				t1=queue.queArray[i];

				if(B[t2-1].d != B[t1-1].d-1 || GN_use.link_if(t1-1, t2-1, r_sign, vec_A) != true ){//if(B[t2-1].d != B[t1-1].d-1 || GN_use.link_if(t1-1, t2-1, r_sign, vec_A_copy) != true 
					continue;
				}

				else{
					//下面是找与边v(t1,t2)相邻且位于下方的边
					h1=B[t1-1].w;//先把权值赋给h2和h1
					h2=B[t2-1].w;
					for(h3=i+1; h3<=rear; h3++){
						t3=queue.queArray[h3];	

						if(t1==t3)
							continue;

						if(B[t1-1].d != B[t3-1].d-1 || GN_use.link_if(t1-1, t3-1, r_sign, vec_A)!=true ){//if(B[t1-1].d != B[t3-1].d-1 || GN_use.link_if(t1-1, t3-1, r_sign, vec_A_copy)!=true )
							continue;
						}		
						int index = GN_use.find_vec_index(t1-1, t3-1, r_sign, vec_A);
						if(index != -1){
							counter_temp += vec_A.elementAt(index).value;//counter_temp累加	
						}
						//counter_temp+=A1[position2];//counter_temp累加
					}
					//权值的处理	
					int index = GN_use.find_vec_index(t1-1, t2-1, r_sign, vec_A);
					if(index != -1){
						vec_A.elementAt(index).value = (counter_temp+1.0)*(h2/h1);//权值最后要加1 ，乘以h2/h1;	
					}
					counter_temp=0;//counter_temp重新置零,下次循环还要用到
				}
			}

	} 
//	--------------------------------------------------------------
//	方法三：
	//把所有的介数加起来
	static void BC_count(Vector<v_side> vec_A){//Vector<v_side> vec_A1, Vector<v_side> vec_A2
		//要把A1[][]数组的值加到数组A2[][]中去，最后把数组A1[][]初始化
		for(int i = 0; i < vec_A.size(); i++){//for(int i = 0; i < vec_A1.size(); i++)
			vec_A.elementAt(i).value2 += vec_A.elementAt(i).value;//vec_A2.elementAt(i).value += vec_A1.elementAt(i).value;
			vec_A.elementAt(i).value = 0;//vec_A1.elementAt(i).value = 0;
		}
	}
//	--------------------------------------------------------------
//	方法四：
	//求E[][]数组中的元素(eij)的值；
	static void  eij_counter(double E[], int i, int j, Vector<v_side> vec_A, int[] r_sign, dian B[], int v_all){
		//E[][]就是存放(eij)的矩阵，i和j	表示两个集团的集团号码,v_all是整个网络所有边的总和，
		//A[][]是存放整个网络的邻接矩阵，B[]是存放点信息
		int k1,k2;
		int v_ij_counter=0;
		double eij=0;
		if(i==j){//求一个社团内的eii
			for(k1=0; k1<B.length; k1++)
				if(B[k1].belong==i)
					for(k2=k1+1; k2<B.length; k2++)
						if(B[k2].belong==j)
							if(GN_use.link_if(k1, k2, r_sign, vec_A)==true){//A[ position_find(k1,k2) ]==1
								v_ij_counter++;
							}			
		}
		else{//求两个社团i和j之间的eij
			for(k1=0; k1<B.length; k1++)
				for(k2=0; k2<B.length; k2++)
					if(B[k1].belong==i && B[k2].belong==j)
						if(GN_use.link_if(k1, k2, r_sign, vec_A)==true){//A[ position_find(k1,k2) ]==1
							v_ij_counter++;
						}

		}

		eij=(v_ij_counter * 1.0)/v_all;
		int position = GN_use.position_find_E(i,j);
		E[position] = eij;
	}
//	--------------------------------------------------------------
//	方法五：
	//分析有没有某个社团分裂为两个新的社团。返回的是新的社团数，如果不紧凑则是返回的是社团数目的相反数，在主函数中可以把这个社团数取反可以得到原来的社团数。
	//	static int community_divide(dian B[], Queue queue1, Vector<v_side> vec_A_copy, Vector<v_side> vec_A,int[] r_sign, int community_counter, boolean original_community)
	static int community_divide(dian B[], Queue queue1,Vector<v_side> vec_A,
								  int[] r_sign, int community_counter, boolean original_community){
		//队列queue1存放的是社团先后处理情况,数组A_copy[][]存放的是去掉      介数最大的边   后的邻接矩阵，
		//community_counter是社团计数器，表示整个大网络目前已经分裂为几个社团
		//最后要把分析后的community_counter值返回，看是否有新的社团产生
		int community_num=queue1.peekFront();//把队列首部的社团号码取出来。
		int length=B.length;
		int[] a=new int[length];//a[]和b[]数组都是用来在一个社团分裂为两个社团时，存放各自成员
		int[] b=new int[length];
		int[] c=new int[length];//c[]是用来存放社团号码为community_num的成员的数组
		//int position;

		int c_temp=0;//c_temp表示当前社团号码为community_num社团的成员个数
		int i,j=0;
		for(i=0; i<length; i++){
			a[i]=-1;//a[i]和b[i]统一赋值为-1
			b[i]=-1;
			if(B[i].belong==community_num){
				c[j]=i;//先把这个集团的所有点存放在数组c[]里
				j++;				
			}
		}
		c_temp=j;
		a[0]=c[0];//先把社团号码为community_num的集团第一个元素给a[];
		int first=a[0];
		int second,a_temp=1, b_temp1=0,b_temp=0;//定义两个变量a_temp和b_temp分别记录分裂后两个集团的元素个数

		for(i=1; i<c_temp; i++){//把和a[0]有连接的点归并到数组a[]，无连接的点归并到数组b[]
			second=c[i];

			if( GN_use.link_if(first, second, r_sign, vec_A)==true){//if( GN_use.link_if(first, second, r_sign, vec_A_copy)==true)
				a[a_temp++]=second;
			}
			else{
				b[b_temp1++]=second;
			}			
		}		
		b_temp=b_temp1;//把当前b[]中元素个数先保存到b_temp中
		for(i=1; i<a_temp; i++){//再扫描b[]中的元素，和a[]任何一个元素中有连接的点再补回到数组a[]中。
			first=a[i];
			for(j=0; j<b_temp1; j++){
				second=b[j];
				//if(second!=-1 && GN_use.link_if(first, second, r_sign, vec_A_copy)==true)
				if(second!=-1 && GN_use.link_if(first, second, r_sign, vec_A)==true){
					a[a_temp++]=second;//归并到数组a[]中去，相应的b[j]赋值为-1；a_temp和b_temp相应地更改。
					b[j]=-1;//把这个点归并到数组a[]中去，那么数组b[]中也要相应删除这个点，赋值为-1就是删除这个点。
					b_temp--;
				}
			}
		}

		if(original_community==false){//非原始社团的分裂
			if(b_temp==0 || a_temp==0){//如果没有产生新的社团，返回原值
				return community_counter;
			}
			boolean comminity_strong=true;
			//下面是采用自包含的GN算法中的强社团定义来判断所分裂的社团结构是否紧凑
			if(b_temp>0 && a_temp>0 ){//分裂出来的集团如果只有一个点，算不算？
				comminity_strong=GN_use.community_strong(B, vec_A, r_sign, a, b, a_temp, b_temp1);
			}

			if(comminity_strong==false){
				community_counter=0-community_counter;//如果是分裂出来的社团不紧凑则返回community_counter的相反数
				return community_counter;
			}
			else if(b_temp>0 && b_temp<c_temp){//判断是否产生了新的社团
				community_counter++;//community_counter要加一，产生了新的集团号
				for(i=0; i<a_temp; i++)
					B[a[i]].belong=community_counter;//把数组a[]的元素归并到集团号为community_counter的新集团里去。
				//而数组b[]的元素则放在原来那个集团号为community_num的集团里。
				queue1.remove();//先把集团号community_num出队列。
				queue1.insert(community_num);//再把community_num和community_counter两个集团号入队列，后面要用到
				queue1.insert(community_counter);
			} 		
		}  

		else if(original_community==true){//原始社团的分裂
			if(b_temp>0 && b_temp<c_temp){//判断是否产生了新的社团
				community_counter++;//community_counter要加一，产生了新的集团号
				for(i=0; i<a_temp; i++)
					B[a[i]].belong=community_counter;//把数组a[]的元素归并到集团号为community_counter的新集团里去。
				//而数组b[]的元素则放在原来那个集团号为community_num的集团里。
				queue1.remove();//先把集团号community_num出队列。
				queue1.insert(community_num);//再把community_num和community_counter两个集团号入队列，后面要用到
				queue1.insert(community_counter);
			}
			else{//如果这一步分析原始社团没有产生分裂，要把队列头中的集团号拿出来放到队列尾部去。
				int queue_front=queue1.remove();
				queue1.insert(queue_front);
			}
		}

		return 	community_counter;//把分析后的community_counter值返回，告诉main方法否有新的社团产生,为负数（相反数）的时候说明分裂的不紧凑
	}

//	--------------------------------------------------------------	
//	方法六：
	//下面把数组 B[]里所有元素的项目初始化，以便下一个源点出发求最短路径，介数等
	static void chushihua(dian B[],int max)
	{
		for(int B_i_reset=0; B_i_reset<max; B_i_reset++ ) {//只有B[i].belong项不用初始化，因为点隶属的社团号必须留着。
			B[B_i_reset].w=0;
			B[B_i_reset].d=0;						
			B[B_i_reset].leaves=0;
		}
	}

//	--------------------------------------------------------------
//	方法七：
	//下面是采用自包含的GN算法中的强社团定义来判断所分裂的社团结构是否紧凑
	//A[][]是边连接情况，c[]是某个社团的点集合，community_num是集团号，c_temp是这个集团的点数目。
	static boolean  community_strong(dian B[], Vector<v_side> vec_A, int[] r_sign, int c[], int d[], int c_temp,int d_temp){

		if(c_temp==1 || d_temp==1)//如果这个集团只有一个点，说明这个点是孤立的点。不用判断，不能直接把他分离出来了。
			return false;

		boolean if_strong=true;
		int i,j,c_j,d_j, cd_kout=0, c_kin=0,d_kin=0;
		//下面是采用自包含的GN算法中的强社团定义来判断所分裂		
		for(c_j=0;c_j<c_temp; c_j++){
			int c_family1=c[c_j];
			if(c_family1!=-1){
				for(i=0; i<d_temp; i++)
					if(d[i]!=-1){
						if(GN_use.find_vec_index(d[i], c_family1, r_sign, vec_A)!=-1){//if(GN_use.link_if(d[i], c_family1, r_sign, vec_A)==true)
							cd_kout++;
						}
					}
				for(j=c_j+1; j<c_temp; j++){
					int c_family2=c[j];

					if(GN_use.find_vec_index(c_family2, c_family1, r_sign, vec_A)!=-1){//if(GN_use.link_if(c_family2, c_family1, r_sign, vec_A)==true)
						c_kin++;
					}		
				}
			}		

		}

		for(d_j=0;d_j<d_temp; d_j++)
			if(d[d_j]!=-1){
				for(j=d_j+1;j<d_temp;j++)
					if(d[j]!=-1){

						if(GN_use.find_vec_index(d[j], d[d_j], r_sign, vec_A)!=-1){//if(GN_use.link_if(d[j], d[d_j], r_sign, vec_A)==true)
							d_kin++;
						}

					}

			}

		if(c_kin<cd_kout || d_kin<cd_kout){
			if_strong=false;
		}

		return if_strong;			
	}
	//---------------------------------------------------------------------------
	/*找到下三角中某个位置。*/
	static int  position_find(int x1, int x2){
		int x = ((x1 > x2) ? x1:x2);
		int y = ((x1 < x2) ? x1:x2);
		int position = (x-1)*x/2+y;
		return position;
	}

	//---------------------------------------------------------------------------
	/*找到下三角（包含对角线）中某个位置。*/
	static int position_find_E(int x1,int x2){
		int x = ((x1 >= x2) ? x1:x2);
		int y = ((x1 <= x2) ? x1:x2);
		int position = (x+1)*x/2+y;
		return position;
	}
	//---------------------------------------------------------------------------
	/*对Vector中的内容重新排序。*/
	static void v_order( Vector<v_side> vec_relationship,int[] r_sign){
		Vector<v_side> vec_relationship1 = new Vector<v_side>();
		int last_i = -1;
		for(int i = 0; i < r_sign.length; i++){
			for(int j = 0; j < vec_relationship.size(); j++){
				if( vec_relationship.elementAt(j).x == i){
					int x = vec_relationship.elementAt(j).x;
					int y = vec_relationship.elementAt(j).y;
					double value = vec_relationship.elementAt(j).value;
					v_side v_sd = new v_side(x,y,value);
					vec_relationship1.add(v_sd);//把这个对象加入到vec_relationship，
					if(last_i != i){
						last_i = i;//记下此时行号，
						r_sign[i] = vec_relationship1.size()-1;//记下此行号在vec_B中第一次出现的位置。
					}
				}
			}
		}
		for(int i = 0; i < vec_relationship.size(); i++){
			vec_relationship.elementAt(i).x = vec_relationship1.elementAt(i).x;
			vec_relationship.elementAt(i).y = vec_relationship1.elementAt(i).y;
			vec_relationship.elementAt(i).value = vec_relationship1.elementAt(i).value;
			vec_relationship.elementAt(i).value2 = vec_relationship1.elementAt(i).value2;
		}
	}
	//---------------------------------------------------------------------------
	/*找指定的两个点是否有边链接。*/
	static boolean link_if(int i, int j,int[] r_sign, Vector<v_side> vec_relationship){
		boolean link_if = false;
		int x =(i < j)? i:j;//x是较小值,也是行号
		int y =(i > j)? i:j;//y是列号，是较大值。

		int row_sign = r_sign[x];
		if( row_sign != -1){
			for(int r_s = r_sign[x]; r_s < vec_relationship.size(); r_s++){
				if(vec_relationship.elementAt(r_s).x != x){
					break;
				}
				if(vec_relationship.elementAt(r_s).y == y && vec_relationship.elementAt(r_s).remove_if != true){
					link_if = true;
					break;
				}
			}
		}

		return link_if;
	}
	//---------------------------------------------------------------------------
	//返回vector中的索引,寻找的边的不存在，则返回索引值是-1
	static int find_vec_index(int i, int j, int[] r_sign, Vector<v_side> vec_relationship){
		int x =(i < j)? i:j;//x是较小值,也是行号
		int y =(i > j)? i:j;//y是列号，是较大值。
		int index = -1;
		if(r_sign[x] == -1){
			return -1;
		}
		for(int r_s = r_sign[x]; r_s< vec_relationship.size(); r_s++){
			if(vec_relationship.elementAt(r_s).x != x){
				break;
			}
			if(vec_relationship.elementAt(r_s).y == y){
				index = r_s;
				break;
			}
		}
		return index;
	}
	//---------------------------------------------------------------------------


}