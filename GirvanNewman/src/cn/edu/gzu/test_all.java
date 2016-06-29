package cn.edu.gzu;

import java.io.*;
import java.util.Vector;
//--------------------------------------------------------------
//�����ӵıߣ�x��yΪ�ߵĶ˵㣬valueΪȨֵ
class v_side{
	int x;
	int y;
	double value;//���value������������ż����������ĳ��Դ������Ը��ߵĽ���
	double value2;//value2��������������������еĽ���֮��
	boolean remove_if;//��GN�㷨�о���ȥ���������ıߣ���һ�������Ƿ�ȥ����ȥ�������ߵ�ʱ����ڴ�������Ϊtrue

	v_side(int x,int y, double value){
		int x1 = ( (x< y) ? x:y);
		int y1 = ( (x> y) ? x:y);
		this.x = x1;//С��ֵ��this.x
		this.y = y1;//���ֵ��this.y
		this.value = value;
		this.value2 = value;
		this.remove_if = false;//�������Ƿ����㷨��������б�ɾ����
	}

}
//--------------------------------------------------------------
/*��������������ÿ�η��ѵ����������*/
class point_belong{
	double Q;//��¼���ַ�������µ�Qֵ��
	int []point_belong_to;//��¼���ַ�������£�ÿ����������ĸ������������0��������2������point_belong_to[0]=2
	point_belong(int num, double Q){
		this.Q = Q;
		this.point_belong_to = new int[num];
	}
}
//--------------------------------------------------------------
/*�����������¼ÿ�α�ɾ���ı�*/
class s_remove_belong{
	int x;//�ߵĵ�ֵx��y��
	int y;
	int belong_clique;//���������ڷ���Ϊ����������ʱ��ɾ�����ġ�����v(i,j)���ڷ���Ϊ��������ʱȥ����,��x=i,y=j;belong_clique=2;

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
		Vector<v_side> vec_relationship  = new Vector<v_side>();//��¼�����ӵıߡ�
		int[] r_sign;//��¼�������к�Ϊi��Ԫ�أ��ߣ���vec_relationship �г��ֵ�������
		int max=0;//�����max�ǵ�ĸ���

		//���ļ�input.txt������
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

		r_sign = new int[max];//��r_sign���������С
		//r_sign������ͳһ��ֵΪ-1
		for(i = 0; i < max; i++){
			r_sign[i] = -1;
		} 
		GN_use.v_order( vec_relationship, r_sign);//��vec_relationship�е����ݽ�����������,

		/*������GN�㷨��ʵ��*/

		//��Qֵ���ʱ��Q_i�������result_temp[][]�ж�Ӧ���к�i���Ա�����������ʱ�������ҵ���		
		int Q_i=0;
		//result_temp_i���ڴ洢����Qֵ�·��ѵõ�������������������Ҫ�õ����к��±������Ҳ��������result_temp[][]���к��±꣬���һ�εķ�������µ��кż�¼��result_temp_i�С�
		int result_temp_i=0;
		//vec_result_temp����ÿ���±�0��max-1���Ǵ��ÿ���������������ţ����ÿ��Q ֵ�µķ��������ÿ�ж�Ӧһ�������Ԫ��vec_result_temp.Q��Ŷ�Ӧ��Qֵ��		
		Vector< point_belong> vec_result_temp = new Vector< point_belong>(); 
		//vec_V_remove_belong�����������ÿ��ȥ���������ıߵ��������������v(i,j)���ڷ���Ϊ��������ʱȥ���ģ���vec_V_remove_belong.x=i-1,vec_V_remove_belong.x=j-1�����Ƿ���Ϊ��������ʱȥ���ģ���vec_V_remove_belong.belong_cliqueֵΪ3
		Vector< s_remove_belong > vec_V_remove_belong = new Vector< s_remove_belong >();
		//��ԭʼ���籾������ɼ������������Ź��ɵģ�original_community_alones+2�Ǽ�¼ԭʼ����Ĺ���������Ŀ��
		//��vec_result_temp�����У�0��original_community_alones-1���кż�¼����ԭʼ�����е����ż����������к�Ϊoriginal_community_alones��¼��������ȥ���������ߵķ��������
		int original_community_alones=0;

		myGN GN_DEAL=new myGN(0);
		GN_DEAL.GN_deal(vec_relationship,r_sign,vec_result_temp, vec_V_remove_belong);//GN�㷨����ں���
		original_community_alones=GN_DEAL.get_original_community_alones();
		Q_i=GN_DEAL.get_Q_i();
		result_temp_i=GN_DEAL.get_result_temp_i();
		System.out.println("");

		//���ԭʼ����ʱ�ɼ����������Ź��ɣ�Ҫ���
		//��ԭʼ���籾�����ɼ���������������ɵģ�ҪŪ����������������Ŀ��original_community_alones+1����Ϊoriginal_community_alones���±��Ǵ�1��ʼ��
		int k3,out_temp=0;
		PrintWriter out1 = new PrintWriter(new FileWriter("d:\\mypicture\\original.dot"));	
		if(original_community_alones!=0){
			//дoriginal.dot
			out1.println("graph A{");
			label0:	
				for(int r_num = 0; r_num < original_community_alones+1; r_num++ ){// r_num��������
					int con_num = 0;
					for(int s_num = 0; s_num < max; s_num++){
						if(vec_result_temp.elementAt(original_community_alones-1).point_belong_to[s_num] == r_num){
							con_num++;//��ɨ�����������Ԫ�ظ���
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
			//dotд���
			System.out.printf("\nԭʼ��������%d�����������Ź��ɵģ�\n",original_community_alones+1);
			out.printf("ԭʼ��������%d�����������Ź��ɵģ�",original_community_alones+1);
			out.println("");
			for(j=0; j<original_community_alones+1; j++){
				System.out.printf("\n����%d��",(j+1));
				out.println("");
				out.printf("����%d��",(j+1));
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
			//дoriginal.dot
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
			//dotд���

			System.out.println("\nԭʼ����Ϊ��");
			out.println("");
			out.println("ԭʼ����Ϊ��");
			for(i=0; i<max; i++){
				System.out.printf("%d  ",i+1);
				out.printf("%d  ",i+1);
			}

			System.out.printf("\n\n");
			out.println("");
		}

		//���ѷ��ѵ������������ע��Q_i���±��Ǵ�0��ʼ�ġ�
		//ע����Ѻ������������Q_i+2,����ڱ���final_community_num�С�
		if(result_temp_i>original_community_alones){//ͨ��result_temp_i>original_community_alones���ж�ԭʼ�����Ƿ񾭹�GN�㷨���ѡ�
			int final_community_num=Q_i+2;

			System.out.println("\nʹ��Girvan-Newman�㷨������ԭʼ�������缫�п��ܷ���Ϊ"+final_community_num+"��������");
			out.println("");
			out.println("");
			out.println("ʹ��Girvan-Newman�㷨����"+final_community_num+"��������");

			for(j=0; j<final_community_num; j++){
				System.out.printf("\n����%d��",(j+1));
				out.println("");
				out.printf("����%d��",(j+1));
				for(i=0; i<max; i++){
					out_temp = (int)vec_result_temp.elementAt(Q_i).point_belong_to[i];
					if(out_temp==j){
						System.out.printf("%d  ",i+1);
						out.printf("%d  ",i+1);
					}

				}
			}
			//���������������ѵĹ��̡�
			//��ʵoriginal_community_alones�����㷨���Ѻ�ĵ�һ������[][] result_temp�����±ꡣ
			System.out.println("\n���·��ѵĹؼ��ߺͷ��ѵĹ���Ϊ��");    	
			out.println("");
			out.println("���·��ѵıߺͷ��ѵĹ���Ϊ��");   
			for(i=original_community_alones; i<=Q_i; i++){//����ĿΪoriginal_community_alones+1��ԭʼ�����¿�ʼ���ѣ�������ΪQ_i+2�����Ž�����
				//i�Ǽ�¼����������кš�i+2�Ƿ������ŵĸ���
				if(i!=original_community_alones){
					System.out.printf("\n\n��");
					out.println("");
					out.println("");
					out.printf("��");
				}

				else{
					System.out.printf("\n��ԭʼ����Ļ�����");
					out.println("");
					out.println("");
					out.printf("��ԭʼ����Ļ�����");
				}


				System.out.printf("ȥ����(�������ı�)��");
				out.printf("ȥ����(�������ı�)��");
				for(int k1 = 0; k1 < vec_V_remove_belong.size(); k1++){
					if(vec_V_remove_belong.elementAt(k1).belong_clique == i+2){
						System.out.printf("v(%d,%d),",vec_V_remove_belong.elementAt(k1).x,vec_V_remove_belong.elementAt(k1).y);
						out.printf("v(%d,%d),",vec_V_remove_belong.elementAt(k1).x,vec_V_remove_belong.elementAt(k1).y);
					}
				}
				System.out.printf("\n������Ϊ%d�����������Ѻ�����Ϊ��",i+2);
				out.println("");
				out.printf("������Ϊ%d�����������Ѻ�����Ϊ��",i+2);

				//�ȶ�Ӧ����һ��dot
				String str_r = "d:\\mypicture\\devided"+Integer.toString(i);
				str_r += ".dot";
				PrintWriter out2 = new PrintWriter(new FileWriter(str_r));	//"d:\\mypicture\\devided.dot"
				//дdevided.dot
				out2.println("graph A{");

				boolean[] print_if = new boolean[vec_relationship.size()];//��¼��Щ���Ǵ�ӡ�˵ģ�û�д�ӡ�ı�˵���Ǳ�ȥ���ıߣ�Ҳ���ǽ������ı�
				for(int p_s = 0; p_s < print_if.length; p_s++){
					print_if[p_s] = false;
				}						


				label1:
					for(j=0; j<=i+1; j++){
						System.out.printf("\n����%d��",(j+1));
						out.println("");
						out.printf("����%d��",(j+1));
						for(k3=0; k3<max; k3++){
							out_temp = (int)vec_result_temp.elementAt(i).point_belong_to[k3];
							if(out_temp==j){
								System.out.printf("%d  ",k3+1);
								out.printf("%d  ",k3+1);
							}

						}

						//j�������ţ�i�Ǽ�¼��ǰ����������кš�
						int con_num = 0;//��¼��ǰ������Ԫ�ظ���
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

			//����ͼƬ������ʾ

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
			System.out.println("\n\nԭʼ���粻���ٷ����ˣ���������յġ�");
			out.println("");
			out.println("");
			out.println("ԭʼ���粻���ٷ����ˣ���������յġ�");
		}
		out.close();

	}
}
