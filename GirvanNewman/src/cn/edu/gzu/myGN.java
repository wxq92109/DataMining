package cn.edu.gzu;

import java.util.Vector;


class dian{//网络中点的结构
	int belong;//点属于哪个社团
	int d;//离源节点s的距离
	int w;//权值，即最短路径的条数
	int leaves;//标记是否是叶节点，值为1时是叶节点 
//	--------------------------------------------------------------
	dian( int belong, int d, int w, int leaves){//构造方法初始化
		this.belong =belong;
		this.d=d;
		this.w=w;
		this.leaves=leaves;
	}
}



public class myGN {
	int original_community_alones;//若所给的原始网络本来就是由几个孤立的社团组成，那么，就返回孤立社团的数目，注意original_community_alones的下标是从1开始的
	int result_temp_i;//分裂最后的一种情况下的数组result_temp[][]行下标
	int Q_i;	//Q值最大情况下的数组result_temp[][]的数组行下标
//	--------------------------------------------------------------	   
	myGN(int t1){//构造方法
		original_community_alones=t1;
		result_temp_i=t1;
		Q_i=t1;

	}

//	--------------------------------------------------------------	
//	V_remove_belong[][]数组用来存放每次去除介数最大的边的隶属情况，比如v(i,j)是在分裂为两个集团时去除的，则V_remove_belong[i-1][j-1]值为2，若是分裂为三个集团时去除的，则V_remove_belong[i-1][j-1]值为3
//	数组A[][]是网络中边连接情况的邻接矩阵。
//	result_temp[][]数组每行下标0到max-1的是存放每个点隶属的集团号，但是集团号必须先转化为double型再存放。	
//	存放每种Q 值下的分裂情况，每行对应一种情况，每行最后一个元素result_temp[i][max]存放对应的Q值，
//	当Q值最大时，Q_i存放数组result_temp[][]中对应的行号i，以便在输出结果的时候容易找到。
//	若原始网络本身就是由几个孤立的社团构成的，original_community_alones是记录原始网络的孤立社团数目
//	result_temp_i是在存储各种Q值下分裂得到各个点的社团隶属情况要用到的下标变量。

	void GN_deal( Vector<v_side> vec_A, int[] r_sign, Vector< point_belong> vec_result_temp, Vector< s_remove_belong > vec_V_remove_belong){//Vector<v_side> vec_A1,, int length_array
		int i,j;//position;
		int max=r_sign.length;//网络中点的数量
		int v_all = vec_A.size();//记录边的总数

		dian[] B=new dian[max];		 
		for(i=0; i<max; i++){
			dian g=new dian(0,0,0,0);//构造方法初始化，此处创建一个类为dian的对象，再逐个赋给类为数组B[]
			B[i]=g;//
		}

		Queue queue=new Queue(max);//申请队列，用来存放广度优先搜索找到的最短路径树；队列的长度为max
		Queue queue1=new Queue(max);//此队列用来存放的是社团先后处理问题，集团的分裂构成一颗二叉树，也是采用广度搜索法来处理，所有集团号按队列先后顺序进行处理。
		for(i=0; i<max; i++){
			queue1.queArray[i]=-1;
			queue.queArray[i]=-1;//把队列queue和queue1里的内容都置-1
		}

		queue1.insert(0);//先把集团号0插入队列queue1里。
		
		int community_counter=0, community_counter_temp=0;//分裂为几个社团？社团数最终存放在变量community_counter中，community_temp只是用来接受中间处理产生的集团数目。	
		boolean original_community=true;//original_community用来在下面首次进入处理网络时要用到。有些原始网络可能已经是分裂为几个网络了。
		int []community_devide_if=new int[max];//判断在社团是否紧凑，有没有必要再分，值位1时表示紧凑，不必再分。
		int num=-1,con=0;//num是一个集团的集团号，con是这个集团的元素个数。
		int di=-1,dj=-1;//di和dj用来存放要介数最大边的两个点。
		double BC_max=0.0;//BC_max存放介数最大值。
		double []E=new double[max*(max+1)/2];//矩阵E[][]用来存放eij。改用下三角来存放
		//double [][] E=new double[max][max];//矩阵E[][]用来存放eij。
		double ai=0.0;//数组E[][]中每行中各元素和用ai表示
		double Q_temp=0.0, Q=0.0;//Q_temp是存放Q值的中间值。而最大的值最后存放在变量Q中,具体到下面程序中，当Q值出现负值时，就停止社团的分裂处理。

		//下面是处理社团反复分裂的操作。
		while(community_counter!=max-1){//重复循环，直到网络分裂为退化的一个个集团后才停止。
			con=0;//每次都要对con初始化	

			//首先要判断原始网络是否已经时由几个孤立的社团构成。
			int community_counter_clock = 0;//判断原始社团的鼓励社团的分析是否完毕,通过这个community_counter_clock的计数器来实现. community_counter_clock>community_counter时则说明分析完毕
			while(original_community){//community_counter_temp = GN_use.community_divide(B, queue1, vec_A_copy, vec_A, r_sign, community_counter,true);
				community_counter_temp = GN_use.community_divide(B, queue1, vec_A, r_sign, community_counter,true);
				//调用这个方法去判断,返回的是社团数				

				if(community_counter_temp > community_counter){
					community_counter = community_counter_temp;	
					community_counter_clock = 0;//有新的集团分离出来，则community_counter_clock计数器要重新置零
					//下面是把分析的几个孤立社团情况先存放到数组result_temp[][]里。
					point_belong point_belo = new point_belong(max,0.001);
					for(j=0; j<max; j++){
						point_belo.point_belong_to[j] = B[j].belong;//记录各个点的社团隶属情况到数组result_temp[][]中。
					}
					vec_result_temp.add(point_belo);
					result_temp_i++;//最后要把result_temp_i加一，下一种的情况存放在数组result_temp[][]下一行里。					

				}
				else{
					//如果计数器community_counter_clock追上了community_counter，说明原始网络分析完毕
					if(community_counter_clock>community_counter){
						original_community=false;
						original_community_alones=result_temp_i;//注意original_community_alones的下标是从1开始的
						break;							
					}
					else{
						community_counter_clock++;//没有新的社团分离出来则要使计数器加一
					}
				}

			}

			int community_all=0;//community_all是记录队列queue1中结构紧凑集团的集团数目。
			for(i=queue1.front; i<=queue1.rear; i++)
				if(community_devide_if[i]==1)
					community_all++;

			if(community_all==queue1.nItems)//如果分裂出来的所有社团结构都很紧凑，没有必要再分，那么就可以跳出循环。
				break;

			num=queue1.peekFront();//把队列queue1里队首的集团号取出来赋给num.

			if(community_devide_if[num]==1){//如果这个集团结构紧凑，执行下面操作。
				queue1.remove();//先把集团号num出队列。
				queue1.insert(num);//再把num集团号入队列末尾。以后即使队列queue1里轮到处理它也不会进行分裂。
				continue;//接着处理队列queue1下一个集团号，进入下一个循环。
			}

			//如果是个孤立点的集团，这个点就是默认为一个集团，认为是紧凑的，那么可以直接跳入下一层循环
			for(int g_i=0; g_i<B.length; g_i++)
				if(B[g_i].belong==num)
					con++;//先把这个集团的元素个数con求出来
			if(con==1){
				community_devide_if[num]=1;//认为这个单个点的孤立集团是紧凑的
				queue1.remove();//先把集团号num出队列。
				queue1.insert(num);//再把num集团号入队列末尾。以后即使队列queue1里轮到处理它也不会进行分裂。
				continue;//接着处理队列queue1下一个集团号，进入下一个循环。
			}	

			if(community_devide_if[num]!=1){//集团不紧凑则需要进行正常的处理，求最短路径，介数，Q值等。
				for(i=0; i<max; i++)
					if(B[i].belong==num){//GN_use.BFS(queue, vec_A_copy, r_sign, B, i+1, max, num);GN_use.BC(queue, vec_A_copy, vec_A, r_sign, B);
						GN_use.BFS(queue, vec_A, r_sign, B, i+1, max, num);//把对应集团号为num的集团里每个源点出发的最短路径求出来,i+1为源点
						GN_use.BC(queue,vec_A, r_sign, B);//从这个源点i+1出发，所有边的介数求出来
						GN_use.BC_count(vec_A);//把所有出发点的介数加起来存放到数组A2[][]中。GN_use.BC_count(vec_A1, vec_A2);							
						GN_use.chushihua(B,max);//下面把数组 B[]里所有元素的项目初始化，以便下一个源点出发求最短路径，介数等，只有B[].belong项不用初始化。
					}
				//int position_BC_max=0;
				int position_BC_max1=0;
				for(i=0; i<max; i++)//把介数最大的边情况扫描出来
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
					}//这里出现了小问题，di,dj如果没有被赋值，那么会始终是值为-1，这里需要改正。
				if(di!=-1){
					int index =position_BC_max1;
					vec_A.elementAt(index).remove_if =true;//把介数最大的边去掉。vec_A_copy.elementAt(index).remove_if =true;
					s_remove_belong v_remove_belo = new s_remove_belong(di+1,dj+1,community_counter+2);
					vec_V_remove_belong.add(v_remove_belo);
					BC_max=0;//把BC_max初始化，下次循环要用。	
					di = -1;//di和 dj也要初始化为-1
					dj = -1;
				}

				//A2[]重新初始化，每处理完一个社团的总介数时初始化一次。
				for(i = 0; i < vec_A.size(); i++){//for(i = 0; i < vec_A2.size(); i++)
					vec_A.elementAt(i).value2 = 0;//vec_A2.elementAt(i).value = 0;
				}
				/*
				for(i=0;i<A2.length;i++){
					A2[i]=0;//A2[]重新初始化，每处理完一个社团的总介数时初始化一次。
				}
				 */
				//判断是否有新的集团产生，返回值community_counter_temp为负数(就是传递的实参community_counter的相反数)时表示分裂后的社团结构不紧凑	
				//community_counter_temp=GN_use.community_divide(B, queue1, vec_A_copy, vec_A, r_sign, community_counter,false);	
				community_counter_temp=GN_use.community_divide(B, queue1,vec_A, r_sign, community_counter,false);	
				//判断在一个社团为两个社团分裂后的结构是否紧凑，不紧凑则不能这样分裂，要恢复分裂前的情况。
				if(community_counter_temp<0){
					community_counter_temp=0-community_counter_temp;//把community_counter_temp重新变回正数。
					//下面撤销分裂这种有单个点集团产生的分裂。
					for(i = vec_V_remove_belong.size()-1; i >= 0; i--){
						if( vec_V_remove_belong.elementAt(i).belong_clique == community_counter+2){
							vec_V_remove_belong.remove(i);
						}
					}
					community_devide_if[num]=1;//值为1时表示这个社团很紧凑，不能再分,下次队列queue1里轮到处理这个集团时可以直接跳过不处理。
				}			
				else if(community_counter_temp > community_counter){//如果有新的社团产生，且产生的集团结构结构紧凑。
					community_counter=community_counter_temp;
					for(i=0; i<=community_counter; i++)
						for(j=i; j<=community_counter; j++){
							GN_use.eij_counter(E, i, j, vec_A,r_sign, B, v_all);//把矩阵E[][]中的各个eij值算出来。
						}
					for(i=0; i<=community_counter; i++){
						for(j=0; j<=community_counter; j++){
							int position1 = GN_use.position_find_E(i, j);//计算eij
							ai+=E[position1];
						}
						int position1 = GN_use.position_find_E(i, i);//计算eii； 
						Q_temp+=(E[position1]-ai*ai);//计算每种社团分裂情况下的Q值，暂时先存放在变量Q_temp里，
						ai=0;//每次都要对ai初始化，赋值为0
					}	
					if(Q_temp<0)//出现Q值为负数时候，则直接跳出大循环，结束对集团分裂的操作，因为再往下分裂还是会出现Q值是负数
						break;
					if(Q_temp>=Q){//出现一个较大的Q值
						Q=Q_temp;//把最大的Q值存放到变量Q里。
						Q_i=result_temp_i;//记录数组result_temp[][]中相对应的行号result_temp_i
					}

					//下面是把这种分裂情况下的结果先存放到数组result_temp[][]里。
					point_belong point_belo = new point_belong(max,Q_temp);//先把这种情况下的Q值存放到每行下标为max的位置中
					Q_temp=0;//Q_temp要置零，下一次要用。
					for(j=0; j<max; j++){
						point_belo.point_belong_to[j] = B[j].belong;//记录各个点的社团隶属情况到数组result_temp[][]中。
					}
					vec_result_temp.add(point_belo);
					result_temp_i++;//最后要把result_temp_i加一，下一种的情况存放在数组result_temp[][]下一行里。
				}
			}
		}
	}
//	--------------------------------------------------------------	
//	下面三个方法都是用来返回数值的，因为数组可以通过数组名来传递空间，但是一般变量只能通过返回值来得到改变的值

//	若所给的原始网络本来就是由几个孤立的社团组成，那么，就返回孤立社团的数目，注意original_community_alones的下标是从1开始的。
	int get_original_community_alones(){
		return original_community_alones;
	}
//	--------------------------------------------------------------	
//	返回的是分裂最后的一种情况下的数组result_temp[][]行下标。
	int get_result_temp_i(){
		return result_temp_i;
	}
//	--------------------------------------------------------------	
//	返回的是Q值最大情况下的数组result_temp[][]的数组行下标
	int get_Q_i(){
		return Q_i;
	}

}

