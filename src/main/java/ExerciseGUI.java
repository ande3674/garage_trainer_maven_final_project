import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExerciseGUI extends JFrame {
    private JPanel mainPanel;
    private JTextField nameTextField;
    private JCheckBox armsCheckBox;
    private JCheckBox legsButtCheckBox;
    private JCheckBox backHIITCheckBox;
    private JCheckBox coreCheckBox;
    private JComboBox<Integer> roundsComboBox;
    private JButton generateWorkoutButton;
    private JTable workoutTable;
    private JButton saveButton;
    private JTextField workoutDetailsText;
    private JButton exitProgramButton;

    private ExerciseDB db;

    private static final String LEGS_BUTT = "legs/butt";
    private static final String ARMS = "arms";
    private static final String CORE = "core";
    private static final String BACK_HIIT = "back/hiit";

    private String template = "%s, here is your custom workout for %s:";
    private String[] columnLabels = new String[]{"Round", "Body Part", "Exercise Name", "Description", "Suggested Repetitions", "Suggested Weight"};

    private DefaultTableModel tableModel = new DefaultTableModel();


    protected ExerciseGUI(ExerciseDB db) {

        this.db = db;

        // Regular setup stuff for the window / JFrame
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setVisible(true);
        setTitle("GarageTrainer Application");

        // Set up JComboBox -- add the options for the number of rounds
        // A good workout needs at least 2 and we will go up to 6
        roundsComboBox.addItem(2);
        roundsComboBox.addItem(3);
        roundsComboBox.addItem(4);
        roundsComboBox.addItem(5);
        roundsComboBox.addItem(6);

        // Configure table
        configureTable();

        pack();

        // add listeners
        addListeners();

    }

    // Set up the table
    private void configureTable() {

        tableModel.addColumn("Round");
        tableModel.addColumn("Body Part");
        tableModel.addColumn("Exercise Name");
        tableModel.addColumn("Description");
        tableModel.addColumn("Suggested Repetitions");
        tableModel.addColumn("Suggested Weight");
        //tableModel.addRow(columnLabels);
        workoutTable.setModel(tableModel);

    }


    private void addListeners() {

        // The GUI will use this TreeMap of exercises to populate the JTable
        // and will send this to the Exercise file writer to write the workout to a file

        // the key (Integer) represents the round number ( values 1 thru numExercises )
        // The ArrayList of Exercises will have one exercise of each body part checkBox checked
        TreeMap<Integer, ArrayList<Exercise>> exerciseTreeMap = new TreeMap<>();

        generateWorkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear the JTable
                tableModel.setRowCount(0);

                // Grab the number of each type of exercises to fetch from DB
                int numExercises = (int)roundsComboBox.getSelectedItem();

                // Make sure at least one box is checked
                if (!armsCheckBox.isSelected() && !backHIITCheckBox.isSelected() && !coreCheckBox.isSelected() && !legsButtCheckBox.isSelected()) {
                    // Show an error message
                    JOptionPane.showMessageDialog(ExerciseGUI.this, "Error: Please select at least one body part to exercise.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {

                    // build date string & get username
                    String date = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
                    String username = nameTextField.getText();

                    // set text in the text field
                    workoutDetailsText.setText(String.format(template, username, date));

                    // which body parts are we exercising?
                    ArrayList<String> bodyPartList = new ArrayList<>();
                    if (armsCheckBox.isSelected()){ bodyPartList.add(ARMS); }
                    if (backHIITCheckBox.isSelected()) { bodyPartList.add(BACK_HIIT); }
                    if (coreCheckBox.isSelected()) { bodyPartList.add(CORE); }
                    if (legsButtCheckBox.isSelected()) { bodyPartList.add(LEGS_BUTT); }

                    // add info to TreeMap
                    for (int i = 0 ; i < numExercises ; i++ ) {
                        // Get one exercise for each body part
                        ArrayList<Exercise> exerciseList = getAndOrganizeExercisesForTreeMap(bodyPartList);
                        // Add this list to the TreeMap for the designated round
                        exerciseTreeMap.put(i+1, exerciseList);
                    }

                    // TreeMap is ready : Populate the JTable
                    for (int i : exerciseTreeMap.keySet()){

                        for (Exercise ex : exerciseTreeMap.get(i)){
                            // Add a row to the table
                            // build the row first
                            String[] newRow = new String[]{i + ".", ex.getBodyPart(), ex.getName(), ex.getDescription(), ex.getSuggestedRepetitions() + " reps", ex.getSuggestedWeight() + " pounds"};
                            tableModel.addRow(newRow);

                        }
                    }
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Make sure the JTable is populated (ie: Generate workout button has been pushed)
                //Send the TreeMap to the File Writer class
                if (tableModel.getRowCount() == 0){
                    // Display error
                    JOptionPane.showMessageDialog(ExerciseGUI.this, "Please have the program generate a workout to save.", "Error", JOptionPane.OK_OPTION);
                }
                else {
                    // save this workout to file
                    ExerciseFileWriter.saveWorkoutToFile(exerciseTreeMap, nameTextField.getText());

                    // Confirmation Message
                    JOptionPane.showMessageDialog(ExerciseGUI.this, "This workout was successfully saved!", "Workout Saved", JOptionPane.INFORMATION_MESSAGE);

                }
            }
        });

        exitProgramButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(ExerciseGUI.this, "Are you sure you want to quit GarageTrainer?", "Quit", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    System.exit(0);
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
    }

    // TODO - make sure this works - also find a way to not add duplicates !
    private ArrayList<Exercise> getAndOrganizeExercisesForTreeMap(ArrayList<String> bodyPartList){
        ArrayList<Exercise> exercises = new ArrayList<>();
        // Talk to database and get 1 exercise for each checked body part...
        for (String bpart : bodyPartList) {

            // Get the list of all exercises
            Vector<Vector> vectorOfExercises = db.getAllExercises(bpart);

            Random r = new Random();
            int i = r.nextInt(vectorOfExercises.size());
            Vector exerciseVector = vectorOfExercises.get(i);

            // Build Exercise object
            /*private String name;
              private String description;
              private String bodyPart;
              boolean performBothSides;
              private int suggestedRepetitions;
              private int suggestedWeight;*/
            Exercise exercise = new Exercise((String)exerciseVector.get(0),
                    (String)exerciseVector.get(1),
                    (String)exerciseVector.get(2),
                    (boolean)exerciseVector.get(3),
                    (int)exerciseVector.get(4),
                    (int)exerciseVector.get(5));
            exercises.add(exercise);
        }
        return exercises;
    }

    private void test(){
        TreeMap<Integer, ArrayList<Exercise>> test = new TreeMap<>();
        Exercise e1 = new Exercise("pushup", "", "arms", false, 12, 0);
        Exercise e2 = new Exercise("tricep dip", "", "arms", false, 25, 0);
        Exercise e3 = new Exercise("situp", "", "core", false, 25, 0);
        Exercise e4 = new Exercise("crunch", "", "core", false, 25, 0);
        ArrayList<Exercise> list1 = new ArrayList<>();
        list1.add(e1);
        list1.add(e3);
        ArrayList<Exercise> list2 = new ArrayList<>();
        list2.add(e2);
        list2.add(e4);
        test.put(1, list1);
        test.put(2, list2);

        for (int i : test.keySet()) {

            for (Exercise ex : test.get(i)) {
                // Add a row to the table
                // build the row first
                String[] newRow = new String[]{i + ".", ex.getBodyPart(), ex.getName(), ex.getDescription(), ex.getSuggestedRepetitions() + " reps", ex.getSuggestedWeight() + " pounds"};
                tableModel.addRow(newRow);
            }
        }
    }
}
