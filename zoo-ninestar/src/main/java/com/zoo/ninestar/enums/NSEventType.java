package com.zoo.ninestar.enums;

/**
 * nine start event type
 */
public enum NSEventType {
    ADDED("ADDED", 1), UPDATED("UPDATED", 2), REMOVED("REMOVED", 3);

    private String name;
    private int index;

    NSEventType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index){
        for (NSEventType eventType : NSEventType.values()){
            if (eventType.getIndex() == index){
                return eventType.getName();
            }
        }
        return null;
    }

    public static NSEventType getType(String name){
        for (NSEventType eventType : NSEventType.values()){
            if (eventType.getName().equals(name)){
                return eventType;
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
