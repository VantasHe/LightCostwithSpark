import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;		//Can use key-value pair

import java.lang.Object.*;
import java.util.*;
//import java.util.Iterator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.InetAddress;


public class LightCostSpark {

	static int SpaceScale;
	static double[][] SpaceFitness;			//Instruction(Cost,Lightness)
	static int Dimension;					//The num of Lights
	static double[][] FitnessParameter;		//K <-> The parameter of position in enviroment
	static int NumofBasicNeeds;			 
	static double[] BasicNeeds; 			//T <-> Basic Target need.
	static double ImpossibleResume=99999;
	
	static int VelocityLimit;
	static int CircleofPSO;
	
	static double InitialW = 0.9;			//Coefficient of inertance, descending.
	static double FinalW = 0.4;
	static double InitialCOne = 2.5;		//Coefficient C of particle self, descending. Because emphasize ONE in early searching.
	static double FinalCOne = 1;
	static double InitialCTwo = 1;			//Coefficient C of Group, ascending. Because emphasize Group in later searching.
	static double FinalCTwo = 2.5;

	static int NumofParticle = 100000;
	static int numOfSlice = 4;

	public String initializeParticle(String s) {
		int _Dimension = Dimension;
		int _SpaceScale = SpaceScale;
		int _NumofBasicNeeds = NumofBasicNeeds;
		int _VelocityLimit = VelocityLimit;
		double[][] _SpaceFitness = SpaceFitness;

		int[] RandomVelocity=new int [_Dimension];
    	CheckT Checkt=new CheckT();
    	RandomValue RandomNumber = new RandomValue();
    	
		int[] j=new int[_Dimension];
		//Particle position random generate.
		for(int h=0;h<_Dimension;h++)                                       
	    {                                                                                                                                                    //cout<<StartPointCheck<<",";
		   	j[h]=RandomNumber.RandomNumber(0,_SpaceScale-1);   
	    }
		//End of random generate.

    	double Power = 0;

	    for(int MatrixRow = 0;MatrixRow < _NumofBasicNeeds;MatrixRow++)
	    {
			int T=Checkt.CheckT(MatrixRow,j);
			int PoN;	//Passive of Negitive,+1 or -1.
	    	if(T==0)    //Reject，Power Set ImpossibleResume as initial value.
		    {
		    	for(int h=0;h<_Dimension;h++)
	            {
		    		if(RandomNumber.RandomNumber(0,1)==1)
		    			PoN=1;
		    		else
		    			PoN=-1;
		            RandomVelocity[h]=PoN*RandomNumber.RandomNumber(0,_VelocityLimit);
	            }
			    //particle = new Particle(j,ImpossibleResume,RandomVelocity);
	   			for (int i = 0; i <_Dimension;i++){
					s = s + Integer.toString(j[i]) + ",";
	    		}
	    		for (int i = 0; i <_Dimension;i++){
					s = s + Integer.toString(RandomVelocity[i] + ",");
	    		}
	    		s = s + Double.toString(ImpossibleResume) + "\n";
				
				System.out.println(s);
			    
			    	return s;
		    }
		    else { T=0; }

		    if(MatrixRow==_NumofBasicNeeds-1)	//全部Row都符合條件，開始計算耗能
		    {                                          
		        for(int k=0;k<_Dimension;k++)	//計算耗能
	    	    { 
		    	    Power +=_SpaceFitness[j[k]][0];          
		        }                                                                     
	    		for(int h=0;h<_Dimension;h++)	//隨機初始速度
                {
	    			if(RandomNumber.RandomNumber(0,1)==1)
	    				PoN=1;
	    			else
	    				PoN=-1;
	                RandomVelocity[h]=PoN*RandomNumber.RandomNumber(0,_VelocityLimit);
                }                                                                                   
                //particle =new Particle(j,Power,RandomVelocity);                //產生粒子
                for (int i = 0; i <_Dimension;i++){
					s = s + Integer.toString(j[i]) + ",";
	    		}
	    		for (int i = 0; i <_Dimension;i++){
					s = s + Integer.toString(RandomVelocity[i] + ",");
	    		}
	    		s = s + Double.toString(Power) + "\n";
				
				System.out.println(s);
                
                Power=0;
                
                return s;
		    }
		}
		String s = " ";
		return s;

	}

	public static void main(String[] args) throws Exception{

		//Initialize Spark driver and conference.
		SparkConf sparkConf = new SparkConf().setAppName("LightCostSpark");
    	JavaSparkContext ctx = new JavaSparkContext(sparkConf);

		//Loading Setting of project.
    	RWFile File=new RWFile();								//Need RWFile.java
    	File.ReadFile("//usr//lib//spark//work//LightInstruction.txt",1);	/**Readfile(Path,mode)	*/
    	File.ReadFile("//usr//lib//spark//work//Td.txt",2);				/**						*/
    	File.ReadFile("//usr//lib//spark//work//KParameter.txt",3);		/**						*/

    	CircleofPSO=(int) ((int)NumofParticle*(0.02));
    	if(CircleofPSO<10)   //Iterative loop too less may cause bad solution.
    	    CircleofPSO=10;
    	VelocityLimit=(int) ((int)SpaceScale*(0.4));  //Velocity Limit is SpaceScale*40%
    	if(VelocityLimit<3)   //Velocity Limit too strick may cause fixed particle.
    		VelocityLimit=3; 

    	//=================================Section of Executor.==================================
    	List<Integer> particle = new ArrayList<Integer>(NumofParticle);
    	JavaRDD<Integer> data = ctx.parallelize(particle, numOfSlice);

    	//initialize
    	data.map(new Function<Integer, String>(){
    		@Override
    		public String call(Integer particle){
    			String s = "";
    			return initializeParticle(s);
    		}
    	}).cache().saveAsTextFile("Particle.txt");
/*
    	//mapPartition:Update Gbest,and Generate pbest.; reduce:return Gbest.
    	JavaRDD<Particle> Gbest = dataset.mapPartition(new Function<Particle, Particle>(){
    		@Override
    		public Particle call(Particle particle){

    		}
    	}).reduce(new Function2<Particle, Particle, Particle>(){
    		@Override
    		public Particle call(Particle particle1, Particle particle2){
    			
    			}
    	});
    	//Set Gbest as a broadcast value.
    	Broadcast<Particle> broadcastGbest = ctx.broadcast(Gbest);
    	//=================================End of Executor.==================================
*/
    	//Driver end.
    	ctx.stop();
	}
}
