package com.zoo.ninestar.domains;

import com.zoo.ninestar.enums.NSEventType;
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
}
