public class Exercise {

    private String name;
    private String description;
    private String bodyPart;
    boolean performBothSides;
    private int suggestedRepetitions;
    private int suggestedWeight;

    public Exercise(String name, String description, String bodyPart, boolean performBothSides, int suggestedRepetitions, int suggestedWeight) {
        this.name = name;
        this.description = description;
        this.bodyPart = bodyPart;
        this.performBothSides = performBothSides;
        this.suggestedRepetitions = suggestedRepetitions;
        this.suggestedWeight = suggestedWeight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public boolean isPerformBothSides() {
        return performBothSides;
    }

    public void setPerformBothSides(boolean performBothSides) {
        this.performBothSides = performBothSides;
    }

    public int getSuggestedRepetitions() {
        return suggestedRepetitions;
    }

    public void setSuggestedRepetitions(int suggestedRepetitions) {
        this.suggestedRepetitions = suggestedRepetitions;
    }

    public int getSuggestedWeight() {
        return suggestedWeight;
    }

    public void setSuggestedWeight(int suggestedWeight) {
        this.suggestedWeight = suggestedWeight;
    }

    @Override
    public String toString() {
        return "Exercise: " + name + '\n' +
                "Description: " + description + '\n' +
                "Body part worked: " + bodyPart + '\n' +
                "Suggested repetitions: " + suggestedRepetitions + '\n' +
                "Suggested weight: " + suggestedWeight + " pounds";
    }
}
