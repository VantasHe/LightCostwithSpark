
public class CheckT {
	int CheckT(int MatrixRow,int i[])
	{
		float T=0;

		for(int k=0;k<(LightCostSpark.Dimension);k++)
		{
		        T+=LightCostSpark.FitnessParameter[MatrixRow][k]*LightCostSpark.SpaceFitness[i[k]][1];
		}
		if(T<LightCostSpark.BasicNeeds[MatrixRow])
			return 0;   
		else 
			return 1;
	}
}

