package Adapter;

public class MainMenuPresenter {

    /***
     * Presents the main menu for user's to create or modify trades
     * or view account details
     */
    public void mainMenuPrompts() {
        String mainMenu =
                "Please select an option or type 'exit':\n" +
                        "\t 1. Create or modify trades\n" +
                        "\t 2. View account details\n";
        System.out.println(mainMenu);
    }

    /***
     * Shows an error if there is an issue reading to csv file
     */
    public void readDataError(){
        System.out.println("Something went wrong when reading from the .CSV files.");
    }

    /**
     * Shows an error if there is an issue with logging in
     */
    public void loginError() {
        System.out.println("Something went wrong while logging in.");
    }

    /**
     * Shows an error if there is an issue with adding a user in the system
     */
    public void signUpError() {
        System.out.println("User couldn't be added in the system");
    }
}
