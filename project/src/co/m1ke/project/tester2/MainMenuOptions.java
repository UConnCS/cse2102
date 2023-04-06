package co.m1ke.project.tester2;

import java.util.Arrays;

public enum MainMenuOptions {

    // Enum constants for the various possible tester modes
    ADD_TITLE("add"), DELETE_TITLE("delete"), SEARCH_TITLE("search"), MODIFY_TITLE("modify"), EXIT("exit");

    // Private fields for the above enum constants
    private String option;

    // Private constructor for above enum constants
    MainMenuOptions(String option) {
        this.option = option;
    }

    /**
     * Returns the target class for the selected TesterMode.
     * @return The target class for the selected TesterMode.
     */
    public String getOption() {
        return this.option;
    }

    // Helper method to translate a string -> MainMenuOptions constant
    public static MainMenuOptions fromString(String option) {
        return Arrays.stream(values())
                .filter(o -> o.getOption().equalsIgnoreCase(option))
                .findFirst()
                .orElse(null);
    }

}
