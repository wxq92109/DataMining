package cn.edu.gzu;

import java.io.*;
import java.util.Vector;
//--------------------------------------------------------------
//有链接的边，x和y为边的端点，value为权值
class v_side{
	int x;
	int y;
	double value;//这个value数组是用来存放计算过程中以某个源点出发对各边的介数
	double value2;//value2数组是用来存放最终所有的介数之和
	boolean remove_if;//在GN算法中决定去掉介数最大的边，这一项设置是否去掉，去掉这条边的时候对于此项设置为true

	v_side(int x,int y, double value){
		int x1 = ( (x< y) ? x:y);
		int y1 = ( (x> y) ? x:y);
		this.x = x1;//小的值给this.x
		this.y = y1;//大的值给this.y
		this.value = value;
		this.value2 = value;
		this.remove_if = false;//这条边是否在算法处理过程中被删除掉
	}

}
//--------------------------------------------------------------
/*这个类是用来存放每次分裂点隶属情况，*/
class point_belong{
	double Q;//记录这种分裂情况下的Q值。
	int []point_belong_to;//记录这种分裂情况下，每个点的属于哪个社区。例如点0属于社区2，则有point_belong_to[0]=2
	point_belong(int num, double Q){
		this.Q = Q;
		this.point_belong_to = new int[num];
	}
}
//--------------------------------------------------------------
/*这个类用来记录每次被删除的边*/
class s_remove_belong{
	int x;//边的点值x和y。
	int y;
	int belong_clique;//这条边是在分裂为几个社区的时候被删除掉的。比如v(i,j)是在分裂为两个社区时去除的,则x=i,y=j;belong_clique=2;

	s_remove_belong(int x, int y, int belong_clique){
		this.x = x;
		this.y = y;
		this.belong_clique = belong_clique;
	}
}
//--------------------------------------------------------------
public class test_all {

	public static void main(String args[])throws IOException{

		int i,j;
		Vector<v_side> vec_relationship  = new Vector<v_side>();//记录有链接的边。
		int[] r_sign;//记录矩阵中行号为i的元素（边）在vec_relationship 中出现的首索引
		int max=0;//这个是max是点的个数

		//从文件input.txt读数据
		PrintWriter out=new PrintWriter(new FileWriter("out.txt"));		
		String ecoding = "gbk";
		File file = new File(args[0]);
		if(file.isFile() && file.exists() ){
			InputStreamReader read = new InputStreamReader( new FileInputStream(file),ecoding);
			BufferedReader bufferedreader = new BufferedReader(read);
			String Text_line = null;
			//Text_line = bufferedreader.readLine();
			//String[] max_str=Text_line.split("-");
			//max=Integer.parseInt(max_str[0]);

			while( ( Text_line = bufferedreader.readLine() ) != null ){
				if(Text_line.contains("#"))
					continue;
				//String text = Text_line.replace(",", "");
				//String[] version=text.split("-");
				String[] version = Text_line.split("\t");
				//for(int k = 0; k < version.length; k++ ){
					//String[] version2 = version[k].split("\t");
				int x1=Integer.parseInt(version[0]);
				int x2=Integer.parseInt(version[1]);
				max = x1;
				v_side v_sd = new v_side(x1-1,x2-1,1);
				vec_relationship.add(v_sd);
				//}
			}
			read.close();
			bufferedreader.close();
		}

		r_sign = new int[max];//给r_sign设置数组大小
		//r_sign数组先统一赋值为-1
		for(i = 0; i < max; i++){
			r_sign[i] = -1;
		} 
		GN_use.v_order( vec_relationship, r_sign);//对vec_relationship中的内容进行排序整理,

		/*下面是GN算法的实现*/

		//当Q值最大时，Q_i存放数组result_temp[][]中对应的行号i，以便在输出结果的时候容易找到。		
		int Q_i=0;
		//result_temp_i是在存储各种Q值下分裂得到各个点的社团隶属情况要用到的行号下标变量，也就是数组result_temp[][]的行号下标，最后一次的分裂情况下的行号记录在result_temp_i中。
		int result_temp_i=0;
		//vec_result_temp数组每行下标0到max-1的是存放每个点隶属的社区号，存放每种Q 值下的分裂情况，每行对应一种情况，元素vec_result_temp.Q存放对应的Q值，		
		Vector< point_belong> vec_result_temp = new Vector< point_belong>(); 
		//vec_V_remove_belong数组用来存放每次去除介数最大的边的隶属情况，比如v(i,j)是在分裂为两个社区时去除的，则vec_V_remove_belong.x=i-1,vec_V_remove_belong.x=j-1，若是分裂为三个社区时去除的，则vec_V_remove_belong.belong_clique值为3
		Vector< s_remove_belong > vec_V_remove_belong = new Vector< s_remove_belong >();
		//若原始网络本身就是由几个孤立的社团构成的，original_community_alones+2是记录原始网络的孤立社团数目，
		//在vec_result_temp数组中，0到original_community_alones-1的行号记录的是原始网络中的社团检测情况。从行号为original_community_alones记录的是由于去除最大介数边的分裂情况。
		int original_community_alones=0;

		myGN GN_DEAL=new myGN(0);
		GN_DEAL.GN_deal(vec_relationship,r_sign,vec_result_temp, vec_V_remove_belong);//GN算法的入口函数
		original_community_alones=GN_DEAL.get_original_community_alones();
		Q_i=GN_DEAL.get_Q_i();
		result_temp_i=GN_DEAL.get_result_temp_i();
		System.out.println("");

		//如果原始网络时由几个孤立社团构成，要输出
		//若原始网络本来是由几个孤立的社团组成的，要弄清楚，所求的社团数目是original_community_alones+1，因为original_community_alones的下标是从1开始的
		int k3,out_temp=0;
		PrintWriter out1 = new PrintWriter(new FileWriter("d:\\mypicture\\original.dot"));	
		if(original_community_alones!=0){
			//写original.dot
			out1.println("graph A{");
			label0:	
				for(int r_num = 0; r_num < original_community_alones+1; r_num++ ){// r_num是社区号
					int con_num = 0;
					for(int s_num = 0; s_num < max; s_num++){
						if(vec_result_temp.elementAt(original_community_alones-1).point_belong_to[s_num] == r_num){
							con_num++;//先扫描这个社区的元素个数
						}
					}

					String str1 = Integer.toString(r_num);
					String str ="subgraph cluster"+str1;
					out1.printf("%s", str);
					out1.println();
					out1.printf("{ node[style=filled,color=white];style=filled;color=lightgray;");
					out1.println();
					if(con_num == 1){
						for(int r_num1 = 0; r_num1<max; r_num1++){
							if(vec_result_temp.elementAt(original_community_alones-1).point_belong_to[r_num1] == r_num){
								out1.printf("%d;}",r_num1+1);
								out1.println("");
								continue label0;
							}
						}
					}
					//int index = max;// vec_result_temp.elementAt(original_community_alones-1).point_belong_to.length;
					for(int r_num1 = 0; r_num1 < max; r_num1++){
						if(vec_result_temp.elementAt(original_community_alones-1).point_belong_to[r_num1] == r_num)
							for(int c_num1 = r_num1+1; c_num1 < max; c_num1++){
								if( vec_result_temp.elementAt(original_community_alones-1).point_belong_to[c_num1] == r_num){
									int index = GN_use.find_vec_index(r_num1, c_num1, r_sign, vec_relationship);//int index = GN_use.position_find(r_num1, c_num1);
									if(index != -1){
										out1.printf("%d--%d;", r_num1+1,c_num1+1);
									}
								}

							}
					}
					out1.println();
					out1.println("}");

				}
			out1.println("}");
			out1.close();
			//dot写完毕
			System.out.printf("\n原始网络是由%d个孤立的社团构成的：\n",original_community_alones+1);
			out.printf("原始网络是由%d个孤立的社团构成的：",original_community_alones+1);
			out.println("");
			for(j=0; j<original_community_alones+1; j++){
				System.out.printf("\n社区%d：",(j+1));
				out.println("");
				out.printf("社区%d：",(j+1));
				for(k3=0; k3<max; k3++){
					out_temp = vec_result_temp.elementAt(original_community_alones-1).point_belong_to[k3];
					if(out_temp==j){
						System.out.printf("%d  ",k3+1);
						out.printf("%d  ",k3+1);
					}									
				}

			}	
		}
		else{
			//写original.dot
			out1.println("graph A{ edge[ ];");
			for(int r_t = 0; r_t < max; r_t++)
				for(int c_t = r_t+1; c_t < max; c_t++ ){
					int index = GN_use.find_vec_index(r_t, c_t, r_sign, vec_relationship);//int position = GN_use.position_find(r_t,c_t);
					if(index != -1){//relationship[position]==1
						out1.printf("%d--%d;", r_t+1,c_t+1);
					}
				}
			out1.println();
			out1.println("}");
			out1.close();
			//dot写完毕

			System.out.println("\n原始网络为：");
			out.println("");
			out.println("原始网络为：");
			for(i=0; i<max; i++){
				System.out.printf("%d  ",i+1);
				out.printf("%d  ",i+1);
			}

			System.out.printf("\n\n");
			out.println("");
		}

		//最后把分裂的最佳情况输出，注意Q_i的下标是从0开始的。
		//注意分裂后的社团数就是Q_i+2,存放在变量final_community_num中。
		if(result_temp_i>original_community_alones){//通过result_temp_i>original_community_alones来判断原始网络是否经过GN算法分裂。
			int final_community_num=Q_i+2;

			System.out.println("\n使用Girvan-Newman算法分析，原始社区网络极有可能分裂为"+final_community_num+"个社区：");
			out.println("");
			out.println("");
			out.println("使用Girvan-Newman算法分析"+final_community_num+"个社区：");

			for(j=0; j<final_community_num; j++){
				System.out.printf("\n社区%d：",(j+1));
				out.println("");
				out.printf("社区%d：",(j+1));
				for(i=0; i<max; i++){
					out_temp = (int)vec_result_temp.elementAt(Q_i).point_belong_to[i];
					if(out_temp==j){
						System.out.printf("%d  ",i+1);
						out.printf("%d  ",i+1);
					}

				}
			}
			//下面是输出网络分裂的过程。
			//其实original_community_alones就是算法分裂后的第一个数组[][] result_temp的行下标。
			System.out.println("\n导致分裂的关键边和分裂的过程为：");    	
			out.println("");
			out.println("导致分裂的边和分裂的过程为：");   
			for(i=original_community_alones; i<=Q_i; i++){//从数目为original_community_alones+1的原始网络下开始分裂，到分裂为Q_i+2个社团结束。
				//i是记录分裂情况的行号。i+2是分裂社团的个数
				if(i!=original_community_alones){
					System.out.printf("\n\n再");
					out.println("");
					out.println("");
					out.printf("再");
				}

				else{
					System.out.printf("\n在原始网络的基础上");
					out.println("");
					out.println("");
					out.printf("在原始网络的基础上");
				}


				System.out.printf("去掉边(介数最大的边)：");
				out.printf("去掉边(介数最大的边)：");
				for(int k1 = 0; k1 < vec_V_remove_belong.size(); k1++){
					if(vec_V_remove_belong.elementAt(k1).belong_clique == i+2){
						System.out.printf("v(%d,%d),",vec_V_remove_belong.elementAt(k1).x,vec_V_remove_belong.elementAt(k1).y);
						out.printf("v(%d,%d),",vec_V_remove_belong.elementAt(k1).x,vec_V_remove_belong.elementAt(k1).y);
					}
				}
				System.out.printf("\n将分裂为%d个社区，分裂后的情况为：",i+2);
				out.println("");
				out.printf("将分裂为%d个社区，分裂后的情况为：",i+2);

				//先对应生成一个dot
				String str_r = "d:\\mypicture\\devided"+Integer.toString(i);
				str_r += ".dot";
				PrintWriter out2 = new PrintWriter(new FileWriter(str_r));	//"d:\\mypicture\\devided.dot"
				//写devided.dot
				out2.println("graph A{");

				boolean[] print_if = new boolean[vec_relationship.size()];//记录那些边是打印了的，没有打印的边说明是被去掉的边，也就是介数最大的边
				for(int p_s = 0; p_s < print_if.length; p_s++){
					print_if[p_s] = false;
				}						


				label1:
					for(j=0; j<=i+1; j++){
						System.out.printf("\n社区%d：",(j+1));
						out.println("");
						out.printf("社区%d：",(j+1));
						for(k3=0; k3<max; k3++){
							out_temp = (int)vec_result_temp.elementAt(i).point_belong_to[k3];
							if(out_temp==j){
								System.out.printf("%d  ",k3+1);
								out.printf("%d  ",k3+1);
							}

						}

						//j是社区号，i是记录当前分裂情况的行号。
						int con_num = 0;//记录当前社区的元素个数
						for(int s_sum = 0; s_sum < max; s_sum++){
							if(vec_result_temp.elementAt(i).point_belong_to[s_sum] == j){
								con_num++;
							}
						}
						String str1 = Integer.toString(j);
						String str ="subgraph cluster"+str1;
						out2.printf("%s", str);
						out2.println();
						out2.printf("{ node[style=filled,color=white];style=filled;color=lightgray;");
						out2.println();
						if(con_num == 1){
							for(int r_num1 = 0; r_num1<max; r_num1++){
								if(vec_result_temp.elementAt(i).point_belong_to[r_num1] == j){
									out2.printf("%d;}",r_num1+1);
									out2.println("");
									continue label1;
								}
							}
						}

						for(int r_num1 = 0; r_num1<max; r_num1++){
							if(vec_result_temp.elementAt(i).point_belong_to[r_num1] == j)
								for(int c_num1 = r_num1+1 ; c_num1 < max; c_num1++){
									if( vec_result_temp.elementAt(i).point_belong_to[c_num1] == j){
										int index = GN_use.find_vec_index(r_num1, c_num1, r_sign, vec_relationship);
										if(index != -1){
											print_if[index]=true;
											out2.printf("%d--%d;", r_num1+1,c_num1+1);
										}
									}

								}
						}
						out2.println("");
						out2.println("}");
					}

				for(int i2 = 0; i2 < max; i2++ ){
					for(int j2 = i2+1; j2 < max; j2++){
						int index = GN_use.find_vec_index(i2, j2, r_sign, vec_relationship);//int index = GN_use.position_find(i2, j2);
						if(index != -1 && print_if[index] == false){
							boolean belong_remove_if =false;
							for(int remove_belong_num = 0; remove_belong_num < vec_V_remove_belong.size(); remove_belong_num++){
								if(vec_V_remove_belong.elementAt(remove_belong_num).belong_clique == i+2){
									if(vec_V_remove_belong.elementAt(remove_belong_num).x-1 == i2 
											&& vec_V_remove_belong.elementAt(remove_belong_num).y-1 == j2){
										belong_remove_if = true;
										break;
									}
								}								
							}
							if(belong_remove_if == true){
								out2.printf("%d--%d[style=dotted,color=red];",i2+1,j2+1);
							}
							else{
								out2.printf("%d--%d[style=dotted,color=green];",i2+1,j2+1);
							}
						}

					}

				}
				out2.println();
				out2.printf("}");
				out2.close();

			}

			//生成图片并且显示

			String path = "D:/Graphviz2.38/bin/dot.exe -Tjpg d:\\mypicture\\original.dot -o d:\\mypicture\\original.jpg";
			Runtime run = Runtime.getRuntime();
			try {        
				Process process = run.exec("cmd.exe /c start " + path);       
				process.waitFor();  
			} catch (Exception e) {             
				e.printStackTrace();    
			}
			for(int s=original_community_alones; s<=Q_i; s++){
				String path1 =  "D:/Graphviz2.38/bin/dot.exe -Tjpg d:\\mypicture\\devided"+Integer.toString(s);
				path1 += ".dot  -o d:\\mypicture\\devided";
				path1 += Integer.toString(s);
				path1 += ".jpg";
				try {        
					Process process = run.exec("cmd.exe /c " + path1);       
					process.waitFor();  
				} catch (Exception e) {             
					e.printStackTrace();    
				}
			}
			String path2 = "D:\\XnView\\xnview.exe  d:\\mypicture\\original.jpg";
			run.exec("cmd.exe /c " + path2);
			for(int s = original_community_alones; s<=Q_i; s++){
				String path3 = "D:\\XnView\\xnview.exe  d:\\mypicture\\devided"+Integer.toString(s);
				path3 += ".jpg";
				run.exec("cmd.exe /c " + path3);
			}

			out.println("");
			out.println("");
		}
		else{
			System.out.println("\n\n原始网络不能再分裂了，它是最紧凑的。");
			out.println("");
			out.println("");
			out.println("原始网络不能再分裂了，它是最紧凑的。");
		}
		out.close();

	}
}
