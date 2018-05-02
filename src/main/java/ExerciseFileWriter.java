import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

public class ExerciseFileWriter {

    private final static String template = "%s_%s.txt";
    private final static String directory = "files";

    public static void saveWorkoutToFile(TreeMap<Integer, ArrayList<Exercise>> exerciseTreeMap, String username) {
        // Create filename string
        String date = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
        String filename = String.format(template, username, date);

        // Save the workout information to the file
        try (BufferedWriter bfw = new BufferedWriter(new FileWriter(directory + File.separator + filename, false))) {
            // How many rounds?
            int numRounds = exerciseTreeMap.size();
            // How many body parts were selected to workout per round?
            int numExercisesPerRound = exerciseTreeMap.get(1).size();

            bfw.write(username + "'s workout for " + date + "\n\n");

            for (int i = 1 ; i < exerciseTreeMap.size() + 1 ; i++) {

                bfw.write(" ---------- ROUND " + i + " ---------- \n");

                for ( int j = 0 ; j < numExercisesPerRound ; j++ ) {

                    Exercise e = exerciseTreeMap.get(i).get(j);

                    bfw.write(i + "." + j + " " + e.getBodyPart() + " exercise: " + e.getName() + "\n");
                    bfw.write("\t" + "description: " + e.getDescription() + "\n");
                    bfw.write("\t" + "reps: " + e.getSuggestedRepetitions() + "\n");
                    bfw.write("\t" + "weight: " + e.getSuggestedWeight() + "\n");
                    bfw.write("\n");
                }
            }
            bfw.close();

        } catch (IOException ioe) {
            System.out.println("There was an error writing to the file " + filename);
            System.out.println(ioe.toString());
        }
    }
}
