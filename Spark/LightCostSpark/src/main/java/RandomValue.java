import java.util.Random;

public class RandomValue {
	int RandomNumber(int MinValue,int MaxValue)   //Both MinValue and MaxValue are included
	{
		Random rand=new Random();
		int R=( rand.nextInt(MaxValue+1-MinValue) )+MinValue;
		//System.out.print("\n"+MinValue+","+MaxValue+","+R+"\n");
		return R;
	}
}
