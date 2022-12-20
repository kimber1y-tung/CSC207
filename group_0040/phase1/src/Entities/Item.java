package Entities;

public class Item {
    private Integer ownerID;
    private String itemName;
    private String description;
    private Integer itemID;
    private boolean approved;

    /**
     * Item constructor
     * @param ownerID the ID associated with the UserAccount that owns the item
     * @param itemName name of the item
     * @param description describe the item
     */
    public Item(Integer ownerID, String itemName, String description){
        this.ownerID = ownerID;
        this.itemName = itemName;
        this.description = description;
    }

    public Integer getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Integer ownerID) {
        this.ownerID = ownerID;
    }
    /**
     * Gets the item name
     * @return the item's name
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Gets the item's description
     * @return the item's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the item's description
     * @param description item's description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the item id
     * @return a unique item id
     */
    public Integer getItemID() {
        return itemID;
    }

    /**
     * Sets the item id
     * @param itemID the unique item id
     */
    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    /**
     * Returns true iff the item has been approved by an admin.
     * @return true iff the item is approved
     */
    public boolean isApproved() {
        return approved;
    }

    /**
     * Approves an item.
     */
    public void approve() {
        approved = true;
    }

    public void setApproved(boolean value){
        this.approved = value;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemName:" + itemName + '\'' +
                ", description:" + description + '\'' +
                ", itemID:" + itemID +
                '}';
    }
}
