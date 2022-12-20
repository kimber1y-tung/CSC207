package Adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


public class LoginSignUpPresenter implements Iterator<String> {

    private List<String> userProperties = new ArrayList<>();
    private int current = 0;

    /**
     * Constructor for LoginSignUpPresenter
     */
    public LoginSignUpPresenter(){
//        userProperties.add("Enter '1' to login to an existing account " +
//                            "or '2' to create a new account: ");
        userProperties.add("UserID: ");
        userProperties.add("Password: ");
    }

    /**
     * @return boolean if there is a next string in user_properties
     */
    @Override
    public boolean hasNext() {
        return current < userProperties.size();
    }

    /**
     * Print the next string in user_properties
     */
    @Override
    public String next() {
        String res;

        // List.get(i) throws an IndexOutBoundsException if
        // we call it with i >= properties.size().
        // But Iterator's next() needs to throw a
        // NoSuchElementException if there are no more elements.
        try {
            res = userProperties.get(current);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
        current += 1;
        return res;
    }

    /**
     * Prints the user's userID
     * @param userID the unique Id of the user to print
     */
    public void printUserName(Integer userID) {
        System.out.println("Your userID is " + userID.toString());
    }


    /**
     * Print a string indicating the entered information is incorrect
     */
    public void incorrectInfoError(){
        System.out.println("The information entered is incorrect!");
    }

    /**
     * Print a string indicating that the user needs to sign up first
     */
    public void signUpFirst(){
        System.out.println("Please Sign Up First!");
    }

    /**
     * Print a string indicating that there was an error in finding the user
     * @param userID the ID of the user that was not found
     */
    public void ioFindError(int userID){
        System.out.println("There was an error in finding the userID: " + userID);
    }


    /**
     * Print a string indicating that there was an error in adding a User
     */
    public void ioAddError(){
        System.out.println("There was an error in adding your information. Please try again!");
    }


    /**
     * Print a string indicating that there was an error in finding the user
     */
    public void nullPointerError(){
        System.out.println("Please enter a valid entry");
    }


    public void invalidOptionError() {
        System.out.println("Please choose a valid option.");
    }

    /**
     * print a message on screen to inform the user to select to either login or sign up a new account.
     */
    public void selectLoginSignUp(){
        System.out.println("Enter '1' to login to an existing account or '2' to create a new account: ");
    }

    /**
     * print a message on screen to inform the user to enter password when signing up for the first time.
     */
    public void signUpPrompt(){
        System.out.println("Your userID will be generated, please enter a password to sign up: ");
    }

    /**
     * Shows an error if the user leaves the password blank
     */
    public void blankPasswordError() {
        System.out.println("Please enter a password: ");
    }

}
