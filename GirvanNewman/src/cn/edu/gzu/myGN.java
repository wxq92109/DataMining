package cn.edu.gzu;

import java.util.Vector;


class dian{//�����е�Ľṹ
	int belong;//�������ĸ�����
	int d;//��Դ�ڵ�s�ľ���
	int w;//Ȩֵ�������·��������
	int leaves;//����Ƿ���Ҷ�ڵ㣬ֵΪ1ʱ��Ҷ�ڵ� 
//	--------------------------------------------------------------
	dian( int belong, int d, int w, int leaves){//���췽����ʼ��
		this.belong =belong;
		this.d=d;
		this.w=w;
		this.leaves=leaves;
	}
}



public class myGN {
	int original_community_alones;//��������ԭʼ���籾�������ɼ���������������ɣ���ô���ͷ��ع������ŵ���Ŀ��ע��original_community_alones���±��Ǵ�1��ʼ��
	int result_temp_i;//��������һ������µ�����result_temp[][]���±�
	int Q_i;	//Qֵ�������µ�����result_temp[][]���������±�
//	--------------------------------------------------------------	   
	myGN(int t1){//���췽��
		original_community_alones=t1;
		result_temp_i=t1;
		Q_i=t1;

	}

//	--------------------------------------------------------------	
//	V_remove_belong[][]�����������ÿ��ȥ���������ıߵ��������������v(i,j)���ڷ���Ϊ��������ʱȥ���ģ���V_remove_belong[i-1][j-1]ֵΪ2�����Ƿ���Ϊ��������ʱȥ���ģ���V_remove_belong[i-1][j-1]ֵΪ3
//	����A[][]�������б�����������ڽӾ���
//	result_temp[][]����ÿ���±�0��max-1���Ǵ��ÿ���������ļ��źţ����Ǽ��źű�����ת��Ϊdouble���ٴ�š�	
//	���ÿ��Q ֵ�µķ��������ÿ�ж�Ӧһ�������ÿ�����һ��Ԫ��result_temp[i][max]��Ŷ�Ӧ��Qֵ��
//	��Qֵ���ʱ��Q_i�������result_temp[][]�ж�Ӧ���к�i���Ա�����������ʱ�������ҵ���
//	��ԭʼ���籾������ɼ������������Ź��ɵģ�original_community_alones�Ǽ�¼ԭʼ����Ĺ���������Ŀ
//	result_temp_i���ڴ洢����Qֵ�·��ѵõ�������������������Ҫ�õ����±������

	void GN_deal( Vector<v_side> vec_A, int[] r_sign, Vector< point_belong> vec_result_temp, Vector< s_remove_belong > vec_V_remove_belong){//Vector<v_side> vec_A1,, int length_array
		int i,j;//position;
		int max=r_sign.length;//�����е������
		int v_all = vec_A.size();//��¼�ߵ�����

		dian[] B=new dian[max];		 
		for(i=0; i<max; i++){
			dian g=new dian(0,0,0,0);//���췽����ʼ�����˴�����һ����Ϊdian�Ķ��������������Ϊ����B[]
			B[i]=g;//
		}

		Queue queue=new Queue(max);//������У�������Ź�����������ҵ������·���������еĳ���Ϊmax
		Queue queue1=new Queue(max);//�˶���������ŵ��������Ⱥ������⣬���ŵķ��ѹ���һ�Ŷ�������Ҳ�ǲ��ù�����������������м��źŰ������Ⱥ�˳����д���
		for(i=0; i<max; i++){
			queue1.queArray[i]=-1;
			queue.queArray[i]=-1;//�Ѷ���queue��queue1������ݶ���-1
		}

		queue1.insert(0);//�ȰѼ��ź�0�������queue1�
		
		int community_counter=0, community_counter_temp=0;//����Ϊ�������ţ����������մ���ڱ���community_counter�У�community_tempֻ�����������м䴦������ļ�����Ŀ��	
		boolean original_community=true;//original_community�����������״ν��봦������ʱҪ�õ�����Щԭʼ��������Ѿ��Ƿ���Ϊ���������ˡ�
		int []community_devide_if=new int[max];//�ж��������Ƿ���գ���û�б�Ҫ�ٷ֣�ֵλ1ʱ��ʾ���գ������ٷ֡�
		int num=-1,con=0;//num��һ�����ŵļ��źţ�con��������ŵ�Ԫ�ظ�����
		int di=-1,dj=-1;//di��dj�������Ҫ�������ߵ������㡣
		double BC_max=0.0;//BC_max��Ž������ֵ��
		double []E=new double[max*(max+1)/2];//����E[][]�������eij�����������������
		//double [][] E=new double[max][max];//����E[][]�������eij��
		double ai=0.0;//����E[][]��ÿ���и�Ԫ�غ���ai��ʾ
		double Q_temp=0.0, Q=0.0;//Q_temp�Ǵ��Qֵ���м�ֵ��������ֵ������ڱ���Q��,���嵽��������У���Qֵ���ָ�ֵʱ����ֹͣ���ŵķ��Ѵ���

		//�����Ǵ������ŷ������ѵĲ�����
		while(community_counter!=max-1){//�ظ�ѭ����ֱ���������Ϊ�˻���һ�������ź��ֹͣ��
			con=0;//ÿ�ζ�Ҫ��con��ʼ��	

			//����Ҫ�ж�ԭʼ�����Ƿ��Ѿ�ʱ�ɼ������������Ź��ɡ�
			int community_counter_clock = 0;//�ж�ԭʼ���ŵĹ������ŵķ����Ƿ����,ͨ�����community_counter_clock�ļ�������ʵ��. community_counter_clock>community_counterʱ��˵���������
			while(original_community){//community_counter_temp = GN_use.community_divide(B, queue1, vec_A_copy, vec_A, r_sign, community_counter,true);
				community_counter_temp = GN_use.community_divide(B, queue1, vec_A, r_sign, community_counter,true);
				//�����������ȥ�ж�,���ص���������				

				if(community_counter_temp > community_counter){
					community_counter = community_counter_temp;	
					community_counter_clock = 0;//���µļ��ŷ����������community_counter_clock������Ҫ��������
					//�����ǰѷ����ļ���������������ȴ�ŵ�����result_temp[][]�
					point_belong point_belo = new point_belong(max,0.001);
					for(j=0; j<max; j++){
						point_belo.point_belong_to[j] = B[j].belong;//��¼������������������������result_temp[][]�С�
					}
					vec_result_temp.add(point_belo);
					result_temp_i++;//���Ҫ��result_temp_i��һ����һ�ֵ�������������result_temp[][]��һ���					

				}
				else{
					//���������community_counter_clock׷����community_counter��˵��ԭʼ����������
					if(community_counter_clock>community_counter){
						original_community=false;
						original_community_alones=result_temp_i;//ע��original_community_alones���±��Ǵ�1��ʼ��
						break;							
					}
					else{
						community_counter_clock++;//û���µ����ŷ��������Ҫʹ��������һ
					}
				}

			}

			int community_all=0;//community_all�Ǽ�¼����queue1�нṹ���ռ��ŵļ�����Ŀ��
			for(i=queue1.front; i<=queue1.rear; i++)
				if(community_devide_if[i]==1)
					community_all++;

			if(community_all==queue1.nItems)//������ѳ������������Žṹ���ܽ��գ�û�б�Ҫ�ٷ֣���ô�Ϳ�������ѭ����
				break;

			num=queue1.peekFront();//�Ѷ���queue1����׵ļ��ź�ȡ��������num.

			if(community_devide_if[num]==1){//���������Žṹ���գ�ִ�����������
				queue1.remove();//�ȰѼ��ź�num�����С�
				queue1.insert(num);//�ٰ�num���ź������ĩβ���Ժ�ʹ����queue1���ֵ�������Ҳ������з��ѡ�
				continue;//���Ŵ������queue1��һ�����źţ�������һ��ѭ����
			}

			//����Ǹ�������ļ��ţ���������Ĭ��Ϊһ�����ţ���Ϊ�ǽ��յģ���ô����ֱ��������һ��ѭ��
			for(int g_i=0; g_i<B.length; g_i++)
				if(B[g_i].belong==num)
					con++;//�Ȱ�������ŵ�Ԫ�ظ���con�����
			if(con==1){
				community_devide_if[num]=1;//��Ϊ���������Ĺ��������ǽ��յ�
				queue1.remove();//�ȰѼ��ź�num�����С�
				queue1.insert(num);//�ٰ�num���ź������ĩβ���Ժ�ʹ����queue1���ֵ�������Ҳ������з��ѡ�
				continue;//���Ŵ������queue1��һ�����źţ�������һ��ѭ����
			}	

			if(community_devide_if[num]!=1){//���Ų���������Ҫ���������Ĵ��������·����������Qֵ�ȡ�
				for(i=0; i<max; i++)
					if(B[i].belong==num){//GN_use.BFS(queue, vec_A_copy, r_sign, B, i+1, max, num);GN_use.BC(queue, vec_A_copy, vec_A, r_sign, B);
						GN_use.BFS(queue, vec_A, r_sign, B, i+1, max, num);//�Ѷ�Ӧ���ź�Ϊnum�ļ�����ÿ��Դ����������·�������,i+1ΪԴ��
						GN_use.BC(queue,vec_A, r_sign, B);//�����Դ��i+1���������бߵĽ��������
						GN_use.BC_count(vec_A);//�����г�����Ľ�����������ŵ�����A2[][]�С�GN_use.BC_count(vec_A1, vec_A2);							
						GN_use.chushihua(B,max);//��������� B[]������Ԫ�ص���Ŀ��ʼ�����Ա���һ��Դ����������·���������ȣ�ֻ��B[].belong��ó�ʼ����
					}
				//int position_BC_max=0;
				int position_BC_max1=0;
				for(i=0; i<max; i++)//�ѽ������ı����ɨ�����
					for(j=i+1; j<max; j++){					
						int index = GN_use.find_vec_index(i, j, r_sign, vec_A);//int index = GN_use.find_vec_index(i, j, r_sign, vec_A2);
						if(index != -1){
							if(vec_A.elementAt(index).value2 > BC_max){//if(vec_A2.elementAt(index).value > BC_max)
								BC_max = vec_A.elementAt(index).value2;//BC_max = vec_A2.elementAt(index).value;
								di=i;
								dj=j;
								position_BC_max1 = index;
							}
						}
					}//���������С���⣬di,dj���û�б���ֵ����ô��ʼ����ֵΪ-1��������Ҫ������
				if(di!=-1){
					int index =position_BC_max1;
					vec_A.elementAt(index).remove_if =true;//�ѽ������ı�ȥ����vec_A_copy.elementAt(index).remove_if =true;
					s_remove_belong v_remove_belo = new s_remove_belong(di+1,dj+1,community_counter+2);
					vec_V_remove_belong.add(v_remove_belo);
					BC_max=0;//��BC_max��ʼ�����´�ѭ��Ҫ�á�	
					di = -1;//di�� djҲҪ��ʼ��Ϊ-1
					dj = -1;
				}

				//A2[]���³�ʼ����ÿ������һ�����ŵ��ܽ���ʱ��ʼ��һ�Ρ�
				for(i = 0; i < vec_A.size(); i++){//for(i = 0; i < vec_A2.size(); i++)
					vec_A.elementAt(i).value2 = 0;//vec_A2.elementAt(i).value = 0;
				}
				/*
				for(i=0;i<A2.length;i++){
					A2[i]=0;//A2[]���³�ʼ����ÿ������һ�����ŵ��ܽ���ʱ��ʼ��һ�Ρ�
				}
				 */
				//�ж��Ƿ����µļ��Ų���������ֵcommunity_counter_tempΪ����(���Ǵ��ݵ�ʵ��community_counter���෴��)ʱ��ʾ���Ѻ�����Žṹ������	
				//community_counter_temp=GN_use.community_divide(B, queue1, vec_A_copy, vec_A, r_sign, community_counter,false);	
				community_counter_temp=GN_use.community_divide(B, queue1,vec_A, r_sign, community_counter,false);	
				//�ж���һ������Ϊ�������ŷ��Ѻ�Ľṹ�Ƿ���գ������������������ѣ�Ҫ�ָ�����ǰ�������
				if(community_counter_temp<0){
					community_counter_temp=0-community_counter_temp;//��community_counter_temp���±��������
					//���泷�����������е����㼯�Ų����ķ��ѡ�
					for(i = vec_V_remove_belong.size()-1; i >= 0; i--){
						if( vec_V_remove_belong.elementAt(i).belong_clique == community_counter+2){
							vec_V_remove_belong.remove(i);
						}
					}
					community_devide_if[num]=1;//ֵΪ1ʱ��ʾ������źܽ��գ������ٷ�,�´ζ���queue1���ֵ������������ʱ����ֱ������������
				}			
				else if(community_counter_temp > community_counter){//������µ����Ų������Ҳ����ļ��Žṹ�ṹ���ա�
					community_counter=community_counter_temp;
					for(i=0; i<=community_counter; i++)
						for(j=i; j<=community_counter; j++){
							GN_use.eij_counter(E, i, j, vec_A,r_sign, B, v_all);//�Ѿ���E[][]�еĸ���eijֵ�������
						}
					for(i=0; i<=community_counter; i++){
						for(j=0; j<=community_counter; j++){
							int position1 = GN_use.position_find_E(i, j);//����eij
							ai+=E[position1];
						}
						int position1 = GN_use.position_find_E(i, i);//����eii�� 
						Q_temp+=(E[position1]-ai*ai);//����ÿ�����ŷ�������µ�Qֵ����ʱ�ȴ���ڱ���Q_temp�
						ai=0;//ÿ�ζ�Ҫ��ai��ʼ������ֵΪ0
					}	
					if(Q_temp<0)//����QֵΪ����ʱ����ֱ��������ѭ���������Լ��ŷ��ѵĲ�������Ϊ�����·��ѻ��ǻ����Qֵ�Ǹ���
						break;
					if(Q_temp>=Q){//����һ���ϴ��Qֵ
						Q=Q_temp;//������Qֵ��ŵ�����Q�
						Q_i=result_temp_i;//��¼����result_temp[][]�����Ӧ���к�result_temp_i
					}

					//�����ǰ����ַ�������µĽ���ȴ�ŵ�����result_temp[][]�
					point_belong point_belo = new point_belong(max,Q_temp);//�Ȱ���������µ�Qֵ��ŵ�ÿ���±�Ϊmax��λ����
					Q_temp=0;//Q_tempҪ���㣬��һ��Ҫ�á�
					for(j=0; j<max; j++){
						point_belo.point_belong_to[j] = B[j].belong;//��¼������������������������result_temp[][]�С�
					}
					vec_result_temp.add(point_belo);
					result_temp_i++;//���Ҫ��result_temp_i��һ����һ�ֵ�������������result_temp[][]��һ���
				}
			}
		}
	}
//	--------------------------------------------------------------	
//	��������������������������ֵ�ģ���Ϊ�������ͨ�������������ݿռ䣬����һ�����ֻ��ͨ������ֵ���õ��ı��ֵ

//	��������ԭʼ���籾�������ɼ���������������ɣ���ô���ͷ��ع������ŵ���Ŀ��ע��original_community_alones���±��Ǵ�1��ʼ�ġ�
	int get_original_community_alones(){
		return original_community_alones;
	}
//	--------------------------------------------------------------	
//	���ص��Ƿ�������һ������µ�����result_temp[][]���±ꡣ
	int get_result_temp_i(){
		return result_temp_i;
	}
//	--------------------------------------------------------------	
//	���ص���Qֵ�������µ�����result_temp[][]���������±�
	int get_Q_i(){
		return Q_i;
	}

}

