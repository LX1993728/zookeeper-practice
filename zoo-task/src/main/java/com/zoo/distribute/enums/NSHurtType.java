package com.zoo.distribute.enums;

/**
 * nine start hurt type
 */
public enum NSHurtType {
    A("hurt_a", 1), B("hurt_b", 2);

    private String name;
    private int index;

    NSHurtType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index){
        for (NSHurtType hurtType : NSHurtType.values()){
            if (hurtType.getIndex() == index){
                return hurtType.getName();
            }
        }
        return null;
    }

    public static NSHurtType getType(String name){
        for (NSHurtType hurtType : NSHurtType.values()){
            if (hurtType.getName().equals(name)){
                return hurtType;
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
