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
                System.out.println("\n======LightInstruction:======\n:");
                while (read.ready())
                {
                    String[] ReadBuffer = read.readLine().split(",");
                    LightCostSpark.SpaceFitness[i][0] = Double.parseDouble(ReadBuffer[0]);
                    LightCostSpark.SpaceFitness[i][1] = Double.parseDouble(ReadBuffer[1]);
                    System.out.println(LightCostSpark.SpaceFitness[i][0]+","+LightCostSpark.SpaceFitness[i][1]);
                    i++;
                }// End While
                System.out.println("\n======End of LightInstruction======\n");              
                read.close();
            }

            // Read "Td.txt" Target demand.
            if (ReadNo == 2)
            {
                BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf8")); // Set utf8 format  
                LightCostSpark.NumofBasicNeeds = Integer.parseInt(read.readLine());
                LightCostSpark.BasicNeeds = new double[LightCostSpark.NumofBasicNeeds];
                int i = 0;
                System.out.println("\n======Target:======\n");
                while (read.ready())
                {
                    LightCostSpark.BasicNeeds[i] = Double.parseDouble(read.readLine());
                    System.out.println(LightCostSpark.BasicNeeds[i]);
                    i++;
                }// End While
                System.out.println("\n======End of Target======\n");              
                read.close();
            }

            // Read "KParameter.txt"
            if (ReadNo == 3)
            {
                BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf8")); // Set utf8 format 
                LightCostSpark.Dimension = Integer.parseInt(read.readLine());
                LightCostSpark.FitnessParameter = new double[LightCostSpark.NumofBasicNeeds][LightCostSpark.SpaceScale];
                int i = 0;
                System.out.println("\n======KParameter:======\n");
                while (read.ready())
                {
                    String[] ReadBuffer = read.readLine().split(",");
                    for (int j = 0; j<LightCostSpark.Dimension; j++)
                    {
                        LightCostSpark.FitnessParameter[i][j] = Double.parseDouble(ReadBuffer[j]);
                        System.out.print(LightCostSpark.FitnessParameter[i][j]+",");
                    }
                    i++;
                    //System.out.print("\n");
                }// End While
                System.out.println("\n======End of KParameter======\n");   
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
