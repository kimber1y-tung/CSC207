package Entities;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ItemInventory {
    private Map<Integer, Item> approvedItems = new HashMap<>(); // Items approved by Admin and their ID.
    private Map<Integer, Item> pendingItems = new HashMap<>(); // Items awaiting approval and their IDs.


    /**
     * The constructor.
     * @param systemItems a list of items to add.
     */
    public ItemInventory(List<Item> systemItems){
        for(Item systemItem: systemItems){
            Integer itemID = systemItem.getItemID();
            if (systemItem.isApproved()) {
                approvedItems.put(itemID, systemItem);
            } else {
                pendingItems.put(itemID, systemItem);
            }
        }
    }

    /**
     * get the item with the given ID.
     * @param itemID the id of the item to get.
     * @return the Item with itemID.
     */
    public Item getItemWithID(Integer itemID){
        if (approvedItems.containsKey(itemID)) {
            return approvedItems.get(itemID);
        } else if (pendingItems.containsKey(itemID)){
            return pendingItems.get(itemID);
        } else {
            return null;
        }

    }

    /**
     * return an ArrayList of all approved items.
     * @return an ArrayList of all approved Items.
     */

    public List<Item> getApprovedItems(){
        List<Item> approvedList = new ArrayList<>();
        for(Integer key: approvedItems.keySet()){
            approvedList.add(approvedItems.get(key));
        }
        return approvedList;
    }

    /**
     * an ArrayList of all pending items.
     * @return an ArrayList of all pending Items.
     */

    public List<Item> getPendingItems(){
        List<Item> pendingList = new ArrayList<>();
        for(Integer key: pendingItems.keySet()){
            pendingList.add(pendingItems.get(key));
        }
        return pendingList;
    }

    /**
     * add requestedItem to the list of pending items.
     * @param requestedItem the item to be added.
     */
    public void addToPending(Item requestedItem){
        pendingItems.put(requestedItem.getItemID(), requestedItem);
    }

    /**
     * add the name of itemToApprove to the list of names of approved items.
     * @param itemToApprove the item to be added
     */
    public void addToApproved(Item itemToApprove){
        approvedItems.put(itemToApprove.getItemID(), itemToApprove);
    }

    /**
     * remove itemToRemove from all approved items.
     * @param itemToRemove the item to be removed from all approved items.
     */
    public void removeFromApproved(Item itemToRemove){
        approvedItems.remove(itemToRemove.getItemID());
    }

    /**
     * remove itemToRemove from all pending items.
     * @param itemToRemove the item to be removed from all pending items.
     */
    public void removeFromPending(Item itemToRemove){
        pendingItems.remove(itemToRemove.getItemID());
    }

}
