package Entities;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Trade {
    private Integer tradeID;
    boolean isPermanent;
    int numEdits = 0;   // numEdits is initially 0
    LocalDateTime time;
    String location;
    String status;
    private Integer lastEditorID; // the userID of the user who last edited time & location.
    Integer user1ID;
    Integer user2ID;

    /**
     * Constructor for Trade which takes in item name
     * @param id1 ID of the first user
     * @param id2 ID of the second user
     */
    public Trade(Integer id1, Integer id2) {
        user1ID = id1;
        user2ID = id2;
        numEdits = 0;
        lastEditorID = 0;
    }

    /**
     * Gets the ID of the trade
     * @return the Trade's ID
     */
    public Integer getTradeID(){ return tradeID; }

    /**
     * Sets the ID of the trade
     * @param tradeID the ID of the Trade
     */
    public void setTradeID(Integer tradeID){ this.tradeID = tradeID; }

    /**
     * Gets the ID of the first user
     * @return User's 1 ID
     */
    public Integer getUser1ID(){
        return user1ID;
    }

    /**
     * Gets the ID of the second user
     * @return User's 2 ID
     */
    public Integer getUser2ID() {
        return user2ID;
    }

    /**
     * Gets the status of the trade
     * @return status of the trade
     */
    public String getStatus(){
        return status;
    }

    /**
     * Sets the status of the trade
     * @param inputStatus status of the trade
     */
    public void setStatus(String inputStatus){
        status = inputStatus;
    }

    /**
     * Gets if the Trade is permanent
     * @return if the Trade is permanent
     */
    public boolean getIsPermanent(){
        return isPermanent;
    }

    /**
     * Sets if the Trade is permanent
     * @param inputPermanent if the Trade is permanent
     */
    public void setIsPermanent(boolean inputPermanent){
        isPermanent = inputPermanent;
    }

    /**
     * Gets the location of the Trade
     * @return the location of the Trade
     */
    public String getLocation(){
        return location;
    }

    /**
     * Sets the location of the Trade
     * @param inputLocation the location of the Trade
     */
    public void setLocation(String inputLocation){
        location = inputLocation;
    }

    /**
     * Gets the number of edits the Trade has made
     * @return the number of edits the Trade has made
     */
    public int getNumEdits(){
        return numEdits;
    }

    /**
     * Sets the number of edits the Trade has made
     * @param inputNumEdits the number of edits the Trade has made
     */
    public void setNumEdits(int inputNumEdits){
        numEdits = inputNumEdits;
    }

    /**
     * Gets the time when the Trade is made
     * @return the time when the Trade is made
     */
    public LocalDateTime getTime(){
        return time;
    }

    /**
     * Sets the time when the Trade is made
     * @param inputTime the time when the Trade is made
     */
    public void setTime(LocalDateTime inputTime){
        time = inputTime;
    }

    /**
     * update the userID of the User who last edited time and location.
     * @param lastEditorID the userID of the User who just edited time and location.
     */
    public void setLastEditorID(Integer lastEditorID){
        this.lastEditorID = lastEditorID;
    }

    /**
     * get the userID of the User who last edited time and location.
     * @return the userID of the User who just edited time and location.
     */
    public Integer getLastEditorID(){
        return this.lastEditorID;
    }

    @Override
    public String toString() {
        ArrayList<Integer> traderIDs = new ArrayList<>();
        traderIDs.add(getUser1ID());
        traderIDs.add(getUser2ID());
        return isPermanent + "Trade{" +
                "tradeID=" + tradeID +
                ", numEdits:" + numEdits +
                ", time:" + time +
                ", location:" + location + '\'' +
                ", status:" + status + '\'' +
                ", last user who edited:" + lastEditorID +
                "ids of traders:" + traderIDs +
                '}';
    }
}
