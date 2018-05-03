import java.io.File;
import java.sql.*;
import java.util.Vector;

public class ExerciseDB {

    private static final String DB_CONNECTION_URL = "jdbc:sqlite:database" + File.separator + "exercise_database";

    private static final String NAME_COLUMN = "name";
    private static final String DESCRIPTION_COLUMN = "description";
    private static final String BODY_PART_COLUMN = "body_part";
    private static final String PERFORM_BOTH_SIDES_COLUMN = "perform_both_sides";
    private static final String REPETITIONS_COLUMN = "repetitions";
    private static final String WEIGHT_COLUMN = "weight";

    private static final String LEGS_BUTT = "legs/butt";
    private static final String ARMS = "arms";
    private static final String CORE = "core";
    private static final String BACK_HIIT = "back/hiit";

    // SQL Statements
    private static final String SELECT_ARM_EXERCISES = "SELECT * FROM exercises WHERE " + BODY_PART_COLUMN + " LIKE '" + ARMS + "'";
    private static final String SELECT_BACK_HIIT_EXERCISES = "SELECT * FROM exercises WHERE " + BODY_PART_COLUMN + " LIKE '" + BACK_HIIT + "'";
    private static final String SELECT_CORE_EXERCISES = "SELECT * FROM exercises WHERE " + BODY_PART_COLUMN + " LIKE '" + CORE + "'";
    private static final String SELECT_LEGS_BUTT_EXERCISES = "SELECT * FROM exercises WHERE " + BODY_PART_COLUMN + " LIKE '" + LEGS_BUTT + "'";

    // Constructor
    ExerciseDB() {}

    // Get all exercises
    Vector<Vector> getAllExercises(String bodyPart) {

        String selectStatement;
        if (bodyPart.equals(ARMS)){ selectStatement = SELECT_ARM_EXERCISES; }
        else if (bodyPart.equals(BACK_HIIT)) { selectStatement = SELECT_BACK_HIIT_EXERCISES; }
        else if (bodyPart.equals(CORE)) { selectStatement = SELECT_CORE_EXERCISES; }
        else { selectStatement = SELECT_LEGS_BUTT_EXERCISES; }

        try (Connection conn = DriverManager.getConnection(DB_CONNECTION_URL);
             Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(selectStatement);
            Vector<Vector> vectors = new Vector<>();

            String name, description;
            boolean performBothSides;
            int repetitions, weight;

            while (rs.next()){
                name = rs.getString(NAME_COLUMN);
                description = rs.getString(DESCRIPTION_COLUMN);
                performBothSides = rs.getBoolean(PERFORM_BOTH_SIDES_COLUMN);
                repetitions = rs.getInt(REPETITIONS_COLUMN);
                weight = rs.getInt(WEIGHT_COLUMN);

                Vector v = new Vector();
                v.add(name); v.add(description); v.add(bodyPart);
                v.add(performBothSides); v.add(repetitions); v.add(weight);

                vectors.add(v);
            }

            rs.close();

            return vectors;
        }

        catch (SQLException sqle){
            throw new RuntimeException(sqle);
        }
    }

    // Get column names
    Vector getColumnNames() {

        Vector colNames = new Vector();

        colNames.add("name");
        colNames.add("description");
        colNames.add("body_part");
        colNames.add("perform_both_sides");
        colNames.add("repetitions");
        colNames.add("weight");

        return colNames;
    }
}
