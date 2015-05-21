import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class RWFile {
    public static void ReadFile(String path, int ReadNo)
    {
        try
        {
            // Read "LightInstruction.txt"
            if (ReadNo == 1)
            {
                BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf8")); // Set utf8 format 
                LightCostSpark.SpaceScale = Integer.parseInt(read.readLine());       //SpaceScale = The amount of lights
                LightCostSpark.SpaceFitness = new double[LightCostSpark.SpaceScale][2];
                int i = 0;
                while (read.ready())
                {
                    String[] ReadBuffer = read.readLine().split(",");
                    LightCostSpark.SpaceFitness[i][0] = Double.parseDouble(ReadBuffer[0]);
                    LightCostSpark.SpaceFitness[i][1] = Double.parseDouble(ReadBuffer[1]);
                    //System.out.print(PSO.SpaceFitness[i][0]+","+PSO.SpaceFitness[i][1]+"\n");
                    i++;
                }// End While              
                read.close();
            }

            // Read "Td.txt" Target demand.
            if (ReadNo == 2)
            {
                BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf8")); // Set utf8 format  
                LightCostSpark.NumofBasicNeeds = Integer.parseInt(read.readLine());
                LightCostSpark.BasicNeeds = new double[LightCostSpark.NumofBasicNeeds];
                int i = 0;
                while (read.ready())
                {
                    LightCostSpark.BasicNeeds[i] = Double.parseDouble(read.readLine());
                    //System.out.print(PSO.BasicNeeds[i]+"\n");
                    i++;
                }// End While              
                read.close();
            }

            // Read "KParameter.txt"
            if (ReadNo == 3)
            {
                BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf8")); // Set utf8 format 
                LightCostSpark.Dimension = Integer.parseInt(read.readLine());
                LightCostSpark.FitnessParameter = new double[LightCostSpark.NumofBasicNeeds][LightCostSpark.SpaceScale];
                int i = 0;
                while (read.ready())
                {
                    String[] ReadBuffer = read.readLine().split(",");
                    for (int j = 0; j<LightCostSpark.Dimension; j++)
                    {
                        LightCostSpark.FitnessParameter[i][j] = Double.parseDouble(ReadBuffer[j]);
                        //System.out.print(PSO.FitnessParameter[i][j]+",");
                    }
                    i++;
                    //System.out.print("\n");
                }// End While   
                read.close();
            }
            //read file over
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println(path + "Read Error!!");
        }
    }

}
