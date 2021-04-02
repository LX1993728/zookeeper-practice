package com.zoo.ninestar.enums;

/**
 * nine star master type
 */
public enum NSMasterType {
    INITIATOR("initiator", 1), ACCEPTOR("acceptor", 2);

    private String name;
    private int index;

    NSMasterType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index){
        for (NSMasterType masterType : NSMasterType.values()){
            if (masterType.getIndex() == index){
                return masterType.getName();
            }
        }
        return null;
    }

    public static NSMasterType getType(String name){
        for (NSMasterType masterType : NSMasterType.values()){
            if (masterType.getName().equals(name)){
                return masterType;
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
