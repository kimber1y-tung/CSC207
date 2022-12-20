package Entities;

public class TwoWayTrade extends Trade{

    Integer item1;
    Integer item2;

    /**
     * Constructor for Trade which takes in item name
     * @param item1 ID of the first trade item
     * @param item2 ID of the second trade item
     * @param id1 ID of the first user
     * @param id2 ID of the second user
     */
    public TwoWayTrade(Integer item1, Integer item2, Integer id1, Integer id2){
        super(id1, id2);
        this.item1 = item1;
        this.item2 = item2;
    }

    /**
     * Gets the ID of the first item
     * @return the ID of the first item
     */
    public int getItem1ID() {
        return this.item1;
    }

    /**
     * Gets the ID of the second item
     * @return the ID of the second item
     */
    public int getItem2ID() {
        return this.item2;
    }

}
