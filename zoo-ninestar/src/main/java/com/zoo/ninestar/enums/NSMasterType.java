package com.zoo.ninestar.enums;

public enum NSMasterType {
    INITIATOR("initiator", 1), ACCEPTOR("acceptor", 2);

    private String name;
    private int index;

    NSMasterType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index){
        for (NSMasterType nmStar : NSMasterType.values()){
            if (nmStar.getIndex() == index){
                return nmStar.getName();
            }
        }
        return null;
    }

    // getter and setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
