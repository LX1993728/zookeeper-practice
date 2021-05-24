package com.zoo.distribute.domains;

import com.zoo.distribute.enums.NSEventType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * wrap zk event data
 */
@Data
@RequiredArgsConstructor
public class NSEventData {
    private String path;
    private String data;
    private String address;
    private NSEventType type;

    @Override
    public String toString() {
        return "NSEventData{" +
                "path='" + path + '\'' +
                ", data='" + data + '\'' +
                ", address='" + address + '\'' +
                ", type=" + type +
                '}';
    }
}
