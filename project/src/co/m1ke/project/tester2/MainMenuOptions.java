package co.m1ke.project.tester2;

import java.util.Arrays;

public enum MainMenuOptions {

    ADD_TITLE("add"), DELETE_TITLE("delete"), SEARCH_TITLE("search"), MODIFY_TITLE("modify"), EXIT("exit");

    private String option;

    MainMenuOptions(String option) {
        this.option = option;
    }

    public String getOption() {
        return this.option;
    }

    public static MainMenuOptions fromString(String option) {
        return Arrays.stream(values())
                .filter(o -> o.getOption().equalsIgnoreCase(option))
                .findFirst()
                .orElse(null);
    }

}
