package cn.edu.gzu;

//import java.io.IOException;
import java.util.Vector;

//��GN_use��Ҫ���myGN.java�ã�GN�㷨��ʵ����ҪGN_use�еķ�����GN_use�еķ���ȫ������static�ģ�Ϊ���Ǽ�ʱ�ͷ��ڴ档
//������ŵ�Ԫ�ض��ǵ�ţ��Ǵ�1��ʼ�ģ�����ӳ�䵽������ʱ��Ҫ��һ
public class GN_use {

//	--------------------------------------------------------------	
	//����һ��
	//����������������ӳ�����s�����·��static void BFS(Queue queue, Vector<v_side> vec_A_copy, int[] r_sign, dian B[], int s, int max, int num)
	static void BFS(Queue queue, Vector<v_side> vec_A, int[] r_sign, dian B[], int s, int max, int num){//
		//s�ǹ�����������ĳ�����,max��һ�����ŵ�ĸ���,num��һ�����ŵļ��ź��롣ע��s���±������s���±�Ϊs-1.����queue��Ľڵ�Ŷ��Ǵ�1��ʼ�ģ����Ǵ�0��ʼ��ע��
		//int position=0;
		for(int k=0; k<max; k++){
			queue.queArray[k]=-1;//ÿ�ζ�Ҫ�������queue������ݣ��´ε���ʱҪ��
		}
		queue.front=0;//����queue����ص���ҲҪ��ʼ����
		queue.rear=-1;
		queue.nItems=0;

		int i,j,signal=0;//ͨ��signal��ֵ���жϽڵ��ǲ���Ҷ�ڵ㣬ֵΪ0ʱ��ʾ��Ҷ�ڵ�,
		B[s-1].w=1;//Դ���ȨֵΪ1��
		queue.insert(s);       //Դ������У�ע��ÿ���ڵ�ֻ�ܽ�һ�ζ���

		while(!(queue.isEmpty())){
			i=queue.remove();   //�ڵ�����У�

			for(j=0; j<max; j++){

				if(j==s-1 || B[j].belong!=num || i-1==j )    //������Դ��s���߲�����������ŵĵ���ת����һ��ѭ��
					continue;	

				//position = GN_use.position_find(i-1, j);	
				if(GN_use.link_if(i-1, j, r_sign, vec_A)==true){//A_copy[position]==1if(GN_use.link_if(i-1, j, r_sign, vec_A_copy)==true)
					if(B[j].d==0){     //�ڵ�j+1û��ָ������ֵʱ�����
						B[j].d=B[i-1].d+1;
						B[j].w=B[i-1].w;
						signal=1;//�е㱻�������Ҫִ��signal++���Ը�������˵����i+1����Ҷ�ӽڵ㡣
						queue.insert(j+1);//���ʹ��Ľڵ�����У�ע��ÿ���ڵ�ֻ�ܽ�һ�ζ���
					}
					if(B[j].d!=0 && B[j].d==B[i-1].d+1){  //�ڵ�j+1ָ���˾���ֵ��������dj=di+1����ʱ�����
						B[j].w+=B[i-1].w;
						signal=1;//�е㱻�������Ҫִ��signal++���Ը�������˵����i+1����Ҷ�ӽڵ㡣
						//ע������ڵ�j+1����������У���Ϊǰ������ڵ��Ѿ����������
					}
					if(B[j].d!=0 && B[j].d<B[i-1].d+1)   //�ڵ�j+1ָ���˾���ֵ������dj<di+1ʱ,������һ��ѭ����
						continue;
				}
			}

			if(signal==0)  //signalֵΪ0˵����i��Ҷ�ڵ�
				B[i-1].leaves=1;
			else
				signal=0;//��signal��0���´�ѭ�������еĵ�Ҫ��				
		}

		//�����ǰ����е�Ҷ�ڵ��ƶ������е����
		for(i = 0; i < queue.nItems; i++){
			int queue_num = queue.queArray[i]-1;//�õ���ǰ�ڵ㣬�±��Ǵ�1��ʼ�ģ�ע�⣬��Ҫ��һ
			if(B[queue_num].leaves == 1){
				for(j = i; j < queue.nItems-1; j++){
					queue.queArray[i] = queue.queArray[i+1];
				}
				queue.queArray[queue.nItems-1] = queue_num+1;//�������Ҷ�ӽڵ���뵽�������
			}
		}

	}
	//ע�⣬�ù�����������㷨����������󼸸�Ԫ�ؿ϶���Ҷ�ڵ�
//	--------------------------------------------------------------
//	��������
	//��ĳ��Դ��s������Ը��ߵĽ��������ӷ���BFS(Queue queue, int A_copy[][], dian B[], int s, int max, int num)֮��
	//static void BC(Queue queue,Vector<v_side> vec_A_copy, Vector<v_side> vec_A, int[] r_sign, dian B[])
	static void BC(Queue queue, Vector<v_side> vec_A, int[] r_sign, dian B[]){//
		//����A1[][]�������Ȩֵ��
		int rear=queue.rear;//�Ѷ��еĶ�β�±긳��rear��
		int temp=rear;
		int i=queue.queArray[temp];
		//Ѱ��Ҷ�ڵ�ͷ�Ҷ�ڵ�,�����ж�
		while(B[i-1].leaves==1 && temp>=1)
			i=queue.queArray[--temp];
		//temp���Ƿ�Ҷ�ڵ��Ҷ�ڵ�ķֽ磬����temp+1�ǵ�һ��Ҷ�ڵ���±ꡣ

		int j,t1,t2;//position=0,position2=0;
		double h1,h2;
		//�����Ǵ����Ҷ�ڵ����ڵĽڵ�����
		for(i=rear; i>temp; i--)//Ҷ�ڵ�
			for(j=temp; j>-1; j--){//��Ҷ�ڵ�
				t2=queue.queArray[j];
				t1=queue.queArray[i];

				if(B[t2-1].d < B[t1-1].d-1)//�ж��Ƿ�ΪҶ�ڵ���ھӽڵ㣬���ǵĻ�������ѭ��,������һ��ѭ����
					continue;

				//position = GN_use.position_find(t1-1, t2-1);			
				if(GN_use.link_if(t1-1, t2-1, r_sign, vec_A) == true){//if(GN_use.link_if(t1-1, t2-1, r_sign, vec_A_copy) == true)
					h1=B[t1-1].w;//�Ȱ�Ȩֵת��Ϊdouble�ͺ������h2��h1
					h2=B[t2-1].w;	
					int index = GN_use.find_vec_index(t1-1, t2-1, r_sign, vec_A);
					if(index != -1){
						vec_A.elementAt(index).value = h2/h1;
					}
					//A1[position]=h2/h1;
				}
			}

		//���Ŵ�����������ڵ�����
		int h3,t3;
		double counter_temp=0;//���v(t1,t2)������λ���·��ı�Ȩֵ���ۼӡ�
		for(i=temp; i>-1; i--)
			for(j=i-1; j>-1; j--){
				t2=queue.queArray[j];
				t1=queue.queArray[i];

				if(B[t2-1].d != B[t1-1].d-1 || GN_use.link_if(t1-1, t2-1, r_sign, vec_A) != true ){//if(B[t2-1].d != B[t1-1].d-1 || GN_use.link_if(t1-1, t2-1, r_sign, vec_A_copy) != true 
					continue;
				}

				else{
					//�����������v(t1,t2)������λ���·��ı�
					h1=B[t1-1].w;//�Ȱ�Ȩֵ����h2��h1
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
							counter_temp += vec_A.elementAt(index).value;//counter_temp�ۼ�	
						}
						//counter_temp+=A1[position2];//counter_temp�ۼ�
					}
					//Ȩֵ�Ĵ���	
					int index = GN_use.find_vec_index(t1-1, t2-1, r_sign, vec_A);
					if(index != -1){
						vec_A.elementAt(index).value = (counter_temp+1.0)*(h2/h1);//Ȩֵ���Ҫ��1 ������h2/h1;	
					}
					counter_temp=0;//counter_temp��������,�´�ѭ����Ҫ�õ�
				}
			}

	} 
//	--------------------------------------------------------------
//	��������
	//�����еĽ���������
	static void BC_count(Vector<v_side> vec_A){//Vector<v_side> vec_A1, Vector<v_side> vec_A2
		//Ҫ��A1[][]�����ֵ�ӵ�����A2[][]��ȥ����������A1[][]��ʼ��
		for(int i = 0; i < vec_A.size(); i++){//for(int i = 0; i < vec_A1.size(); i++)
			vec_A.elementAt(i).value2 += vec_A.elementAt(i).value;//vec_A2.elementAt(i).value += vec_A1.elementAt(i).value;
			vec_A.elementAt(i).value = 0;//vec_A1.elementAt(i).value = 0;
		}
	}
//	--------------------------------------------------------------
//	�����ģ�
	//��E[][]�����е�Ԫ��(eij)��ֵ��
	static void  eij_counter(double E[], int i, int j, Vector<v_side> vec_A, int[] r_sign, dian B[], int v_all){
		//E[][]���Ǵ��(eij)�ľ���i��j	��ʾ�������ŵļ��ź���,v_all�������������бߵ��ܺͣ�
		//A[][]�Ǵ������������ڽӾ���B[]�Ǵ�ŵ���Ϣ
		int k1,k2;
		int v_ij_counter=0;
		double eij=0;
		if(i==j){//��һ�������ڵ�eii
			for(k1=0; k1<B.length; k1++)
				if(B[k1].belong==i)
					for(k2=k1+1; k2<B.length; k2++)
						if(B[k2].belong==j)
							if(GN_use.link_if(k1, k2, r_sign, vec_A)==true){//A[ position_find(k1,k2) ]==1
								v_ij_counter++;
							}			
		}
		else{//����������i��j֮���eij
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
//	�����壺
	//������û��ĳ�����ŷ���Ϊ�����µ����š����ص����µ���������������������Ƿ��ص���������Ŀ���෴�������������п��԰����������ȡ�����Եõ�ԭ������������
	//	static int community_divide(dian B[], Queue queue1, Vector<v_side> vec_A_copy, Vector<v_side> vec_A,int[] r_sign, int community_counter, boolean original_community)
	static int community_divide(dian B[], Queue queue1,Vector<v_side> vec_A,
								  int[] r_sign, int community_counter, boolean original_community){
		//����queue1��ŵ��������Ⱥ������,����A_copy[][]��ŵ���ȥ��      �������ı�   ����ڽӾ���
		//community_counter�����ż���������ʾ����������Ŀǰ�Ѿ�����Ϊ��������
		//���Ҫ�ѷ������community_counterֵ���أ����Ƿ����µ����Ų���
		int community_num=queue1.peekFront();//�Ѷ����ײ������ź���ȡ������
		int length=B.length;
		int[] a=new int[length];//a[]��b[]���鶼��������һ�����ŷ���Ϊ��������ʱ����Ÿ��Գ�Ա
		int[] b=new int[length];
		int[] c=new int[length];//c[]������������ź���Ϊcommunity_num�ĳ�Ա������
		//int position;

		int c_temp=0;//c_temp��ʾ��ǰ���ź���Ϊcommunity_num���ŵĳ�Ա����
		int i,j=0;
		for(i=0; i<length; i++){
			a[i]=-1;//a[i]��b[i]ͳһ��ֵΪ-1
			b[i]=-1;
			if(B[i].belong==community_num){
				c[j]=i;//�Ȱ�������ŵ����е���������c[]��
				j++;				
			}
		}
		c_temp=j;
		a[0]=c[0];//�Ȱ����ź���Ϊcommunity_num�ļ��ŵ�һ��Ԫ�ظ�a[];
		int first=a[0];
		int second,a_temp=1, b_temp1=0,b_temp=0;//������������a_temp��b_temp�ֱ��¼���Ѻ��������ŵ�Ԫ�ظ���

		for(i=1; i<c_temp; i++){//�Ѻ�a[0]�����ӵĵ�鲢������a[]�������ӵĵ�鲢������b[]
			second=c[i];

			if( GN_use.link_if(first, second, r_sign, vec_A)==true){//if( GN_use.link_if(first, second, r_sign, vec_A_copy)==true)
				a[a_temp++]=second;
			}
			else{
				b[b_temp1++]=second;
			}			
		}		
		b_temp=b_temp1;//�ѵ�ǰb[]��Ԫ�ظ����ȱ��浽b_temp��
		for(i=1; i<a_temp; i++){//��ɨ��b[]�е�Ԫ�أ���a[]�κ�һ��Ԫ���������ӵĵ��ٲ��ص�����a[]�С�
			first=a[i];
			for(j=0; j<b_temp1; j++){
				second=b[j];
				//if(second!=-1 && GN_use.link_if(first, second, r_sign, vec_A_copy)==true)
				if(second!=-1 && GN_use.link_if(first, second, r_sign, vec_A)==true){
					a[a_temp++]=second;//�鲢������a[]��ȥ����Ӧ��b[j]��ֵΪ-1��a_temp��b_temp��Ӧ�ظ��ġ�
					b[j]=-1;//�������鲢������a[]��ȥ����ô����b[]��ҲҪ��Ӧɾ������㣬��ֵΪ-1����ɾ������㡣
					b_temp--;
				}
			}
		}

		if(original_community==false){//��ԭʼ���ŵķ���
			if(b_temp==0 || a_temp==0){//���û�в����µ����ţ�����ԭֵ
				return community_counter;
			}
			boolean comminity_strong=true;
			//�����ǲ����԰�����GN�㷨�е�ǿ���Ŷ������ж������ѵ����Žṹ�Ƿ����
			if(b_temp>0 && a_temp>0 ){//���ѳ����ļ������ֻ��һ���㣬�㲻�㣿
				comminity_strong=GN_use.community_strong(B, vec_A, r_sign, a, b, a_temp, b_temp1);
			}

			if(comminity_strong==false){
				community_counter=0-community_counter;//����Ƿ��ѳ��������Ų������򷵻�community_counter���෴��
				return community_counter;
			}
			else if(b_temp>0 && b_temp<c_temp){//�ж��Ƿ�������µ�����
				community_counter++;//community_counterҪ��һ���������µļ��ź�
				for(i=0; i<a_temp; i++)
					B[a[i]].belong=community_counter;//������a[]��Ԫ�ع鲢�����ź�Ϊcommunity_counter���¼�����ȥ��
				//������b[]��Ԫ�������ԭ���Ǹ����ź�Ϊcommunity_num�ļ����
				queue1.remove();//�ȰѼ��ź�community_num�����С�
				queue1.insert(community_num);//�ٰ�community_num��community_counter�������ź�����У�����Ҫ�õ�
				queue1.insert(community_counter);
			} 		
		}  

		else if(original_community==true){//ԭʼ���ŵķ���
			if(b_temp>0 && b_temp<c_temp){//�ж��Ƿ�������µ�����
				community_counter++;//community_counterҪ��һ���������µļ��ź�
				for(i=0; i<a_temp; i++)
					B[a[i]].belong=community_counter;//������a[]��Ԫ�ع鲢�����ź�Ϊcommunity_counter���¼�����ȥ��
				//������b[]��Ԫ�������ԭ���Ǹ����ź�Ϊcommunity_num�ļ����
				queue1.remove();//�ȰѼ��ź�community_num�����С�
				queue1.insert(community_num);//�ٰ�community_num��community_counter�������ź�����У�����Ҫ�õ�
				queue1.insert(community_counter);
			}
			else{//�����һ������ԭʼ����û�в������ѣ�Ҫ�Ѷ���ͷ�еļ��ź��ó����ŵ�����β��ȥ��
				int queue_front=queue1.remove();
				queue1.insert(queue_front);
			}
		}

		return 	community_counter;//�ѷ������community_counterֵ���أ�����main���������µ����Ų���,Ϊ�������෴������ʱ��˵�����ѵĲ�����
	}

//	--------------------------------------------------------------	
//	��������
	//��������� B[]������Ԫ�ص���Ŀ��ʼ�����Ա���һ��Դ����������·����������
	static void chushihua(dian B[],int max)
	{
		for(int B_i_reset=0; B_i_reset<max; B_i_reset++ ) {//ֻ��B[i].belong��ó�ʼ������Ϊ�����������źű������š�
			B[B_i_reset].w=0;
			B[B_i_reset].d=0;						
			B[B_i_reset].leaves=0;
		}
	}

//	--------------------------------------------------------------
//	�����ߣ�
	//�����ǲ����԰�����GN�㷨�е�ǿ���Ŷ������ж������ѵ����Žṹ�Ƿ����
	//A[][]�Ǳ����������c[]��ĳ�����ŵĵ㼯�ϣ�community_num�Ǽ��źţ�c_temp��������ŵĵ���Ŀ��
	static boolean  community_strong(dian B[], Vector<v_side> vec_A, int[] r_sign, int c[], int d[], int c_temp,int d_temp){

		if(c_temp==1 || d_temp==1)//����������ֻ��һ���㣬˵��������ǹ����ĵ㡣�����жϣ�����ֱ�Ӱ�����������ˡ�
			return false;

		boolean if_strong=true;
		int i,j,c_j,d_j, cd_kout=0, c_kin=0,d_kin=0;
		//�����ǲ����԰�����GN�㷨�е�ǿ���Ŷ������ж�������		
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
	/*�ҵ���������ĳ��λ�á�*/
	static int  position_find(int x1, int x2){
		int x = ((x1 > x2) ? x1:x2);
		int y = ((x1 < x2) ? x1:x2);
		int position = (x-1)*x/2+y;
		return position;
	}

	//---------------------------------------------------------------------------
	/*�ҵ������ǣ������Խ��ߣ���ĳ��λ�á�*/
	static int position_find_E(int x1,int x2){
		int x = ((x1 >= x2) ? x1:x2);
		int y = ((x1 <= x2) ? x1:x2);
		int position = (x+1)*x/2+y;
		return position;
	}
	//---------------------------------------------------------------------------
	/*��Vector�е�������������*/
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
					vec_relationship1.add(v_sd);//�����������뵽vec_relationship��
					if(last_i != i){
						last_i = i;//���´�ʱ�кţ�
						r_sign[i] = vec_relationship1.size()-1;//���´��к���vec_B�е�һ�γ��ֵ�λ�á�
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
	/*��ָ�����������Ƿ��б����ӡ�*/
	static boolean link_if(int i, int j,int[] r_sign, Vector<v_side> vec_relationship){
		boolean link_if = false;
		int x =(i < j)? i:j;//x�ǽ�Сֵ,Ҳ���к�
		int y =(i > j)? i:j;//y���кţ��ǽϴ�ֵ��

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
	//����vector�е�����,Ѱ�ҵıߵĲ����ڣ��򷵻�����ֵ��-1
	static int find_vec_index(int i, int j, int[] r_sign, Vector<v_side> vec_relationship){
		int x =(i < j)? i:j;//x�ǽ�Сֵ,Ҳ���к�
		int y =(i > j)? i:j;//y���кţ��ǽϴ�ֵ��
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