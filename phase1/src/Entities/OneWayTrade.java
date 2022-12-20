package Entities;

public class OneWayTrade extends Trade{

    Integer item;

    /**
     * Constructor for Trade which takes in item name
     * @param item ID of the trade item
     * @param id1 ID of the first user
     * @param id2 ID of the second user
     */
    public OneWayTrade(Integer item, Integer id1, Integer id2){
        super(id1, id2);
        this.item = item;
    }

    /**
     * Gets the ID of the trade item
     * @return the ID of the trade item
     */
    public int getItemID() {
        return this.item;
    }
}

