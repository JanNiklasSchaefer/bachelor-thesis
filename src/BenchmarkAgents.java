/**
 * This code was created as part of Jan Niklas Schäfer's bachelor thesis
 * Author: Jan Niklas Schäfer
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import engine.core.MarioGame;
import engine.core.MarioResult;

public class BenchmarkAgents {

    public static void printResults(MarioResult result) {
        System.out.println("****************************************************************");
        System.out.println("Game Status: " + result.getGameStatus().toString() +
                " Percentage Completion: " + result.getCompletionPercentage());
        System.out.println("Lives: " + result.getCurrentLives() + " Coins: " + result.getCurrentCoins() +
                " Remaining Time: " + (int) Math.ceil(result.getRemainingTime() / 1000f));
        System.out.println("Mario State: " + result.getMarioMode() +
                " (Mushrooms: " + result.getNumCollectedMushrooms() + " Fire Flowers: " + result.getNumCollectedFireflower() + ")");
        System.out.println("Total Kills: " + result.getKillsTotal() + " (Stomps: " + result.getKillsByStomp() +
                " Fireballs: " + result.getKillsByFire() + " Shells: " + result.getKillsByShell() +
                " Falls: " + result.getKillsByFall() + ")");
        System.out.println("Bricks: " + result.getNumDestroyedBricks() + " Jumps: " + result.getNumJumps() +
                " Max X Jump: " + result.getMaxXJump() + " Max Air Time: " + result.getMaxJumpAirTime());
        System.out.println("****************************************************************");
    }
    public static String getLevel(String filepath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filepath)));
        } catch (IOException e) {
        }
        return content;
    }

    /**
     * Create a CSV and create the Headers to add further rows for each level.
     * @param agentName
     * @return CSV File with Headers already written into the File
     */
    private static File createCsv(String agentName)  {
        // Get current Date in specific Format for the File Name
        ZonedDateTime date = ZonedDateTime.now();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm");

        String formattedDate = date.format(dateFormatter);

        File csv = new File("./benchmarkingResults/" + agentName +"_" + formattedDate + ".csv");

        try{
            csv.createNewFile();
        }
        catch (IOException e){
            System.out.println("CSV File Creation of Monte Mario Failed.");
            e.printStackTrace();
        }


        try{
            //Create CSV Headers
            FileWriter fileWriter = new FileWriter(csv, true);
            fileWriter.write("Agent Name;Level Name;Game Status;Final X Position;Completion Percentage;Remaining Lives;Remaining Coins;Collected Coins;Remaining Time in MS;Mario Mode;Mario Damage Taken;Mushrooms Collected;Fireflowers Collected;Total Kills;Kill by Stomp;Kills by Fire;Kills by Shell;Kills by Fall;Destroyed Bricks;Jumps;Max X Jump;Max Air Time;Overtime"
                    + System.lineSeparator());
            fileWriter.close();
        }
        catch (IOException e){
            System.out.println("CSV can't be written to.");
            e.printStackTrace();
        }

        return csv;
    }

    /**
     * Add a Row to Benchmarking CSV. Positions of value inside String correspond to Headers of createCsv() function
     * @param gameResult Mario Game Result Object after Benchmark
     * @param csv File to write into
     * @param agentName Name of Agent
     */
    private static void addCsvRow(MarioResult gameResult, File csv, String agentName, String levelName) {
        try{
            FileWriter fileWriter = new FileWriter(csv, true);
            //Write new Row in CSV File
            fileWriter.write(agentName + ";" + levelName+ ";" +gameResult.getGameStatus() +";"+ gameResult.getFinalMarioXPosition()+
                    ";" + gameResult.getCompletionPercentage() + ";" +gameResult.getCurrentLives()+ ";"
                    +gameResult.getCurrentCoins() + ";" +gameResult.getNumCollectedTileCoins()+ ";"
                    +gameResult.getRemainingTime()+ ";" +gameResult.getMarioMode()+ ";" +gameResult.getMarioNumHurts()
                    + ";" +gameResult.getNumCollectedMushrooms()+ ";" +gameResult.getNumCollectedFireflower()+ ";"
                    +gameResult.getKillsTotal()+ ";" +gameResult.getKillsByStomp()+ ";" +gameResult.getKillsByFire()
                    + ";" +gameResult.getKillsByShell()+ ";" +gameResult.getKillsByFall()+ ";"
                    +gameResult.getNumDestroyedBricks()+";"+gameResult.getNumJumps()+";"
                    +gameResult.getMaxXJump()+";"+gameResult.getMaxJumpAirTime()+";"+ gameResult.getOvertimeSum() + System.lineSeparator());
            fileWriter.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        MarioGame game = new MarioGame();
        /**
         Set the directory for the Folder of Levels you want to run the benchmark in
         **/
        String benchmarkingPathNotch = "./benchmarkingLevels/randomParamNotchSet";
        String benchmarkingPathOriginal = "./benchmarkingLevels/originalLevelSet";


        ArrayList<Float> completionPercentages = new ArrayList<Float>();

        File f = new File(benchmarkingPathNotch);
        File[] randomNotchPath = f.listFiles();

        if(randomNotchPath==null){
            throw new Exception("Wrong File Directory for Benchmarking Levels set");
        }

        f = new File(benchmarkingPathOriginal);
        File[] originalBenchmarkPath = f.listFiles();

        if(originalBenchmarkPath==null){
            throw new Exception("Wrong File Directory for Benchmarking Levels set");
        }

        String agentName = "mctsHD";

        //Repeat benchmarking process multiple times
        for(int i = 0 ; i < 5; i ++) {

            File monteMarioCsv = createCsv(agentName+"_Notch_");


            for (File path : randomNotchPath) {
                MarioResult gameResult = game.runGame(new agents.janMonteMario.Agent(), getLevel(path.toString()), 60, 0, false, false);
                completionPercentages.add(gameResult.getCompletionPercentage());
                addCsvRow(gameResult, monteMarioCsv, agentName, path.toString().substring(path.toString().lastIndexOf('\\') + 1));
            }

            float sum = 0f;

            for (float completion : completionPercentages) {
                sum += completion;
            }

            float averageCompletion = sum / completionPercentages.size();

            System.out.println("Agent " + agentName + " hat im Ordner " + benchmarkingPathNotch + " durchschnittlich: " + averageCompletion + " erreicht.");

            completionPercentages.clear();

            File secondMonteMarioCsv = createCsv(agentName+"_OgOre_");

            for (File path : originalBenchmarkPath) {
                MarioResult gameResult = game.runGame(new agents.janMonteMario.Agent(), getLevel(path.toString()), 60, 0, false, false);
                completionPercentages.add(gameResult.getCompletionPercentage());
                addCsvRow(gameResult, secondMonteMarioCsv, agentName, path.toString().substring(path.toString().lastIndexOf('\\') + 1));
            }
            sum = 0f;

            for (float completion : completionPercentages) {
                sum += completion;
            }

            averageCompletion = sum / completionPercentages.size();

            System.out.println("Agent " + agentName + " hat im Ordner " + benchmarkingPathOriginal + " durchschnittlich: " + averageCompletion + " erreicht.");
            completionPercentages.clear();
        }
    }
}
