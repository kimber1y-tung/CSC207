package Entities;

public class Account {
    private Integer userID;
    private String password;

    /**
     * UserAccount constructor
     * @param password password
     */
    public Account(String password){
        this.password = password;
    }

    /**
     * Gets the user id
     * @return a user id
     */
    public Integer getUserID() {
        return userID;
    }

    /**
     * Sets the userID
     * @param userID the unique user id to set
     */
    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    /**
     * Gets the password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
