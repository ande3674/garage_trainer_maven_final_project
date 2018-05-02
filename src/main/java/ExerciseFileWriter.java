import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

public class ExerciseFileWriter {

    private final static String template = "%s_%s.txt";

    public static void saveWorkoutToFile(TreeMap<Integer, ArrayList<Exercise>> exerciseTreeMap, String username) {
        // Create filename string
        String date = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
        String filename = String.format(template, username, date);





    }
}
