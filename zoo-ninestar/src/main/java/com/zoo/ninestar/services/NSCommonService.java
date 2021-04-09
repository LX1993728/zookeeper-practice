package com.zoo.ninestar.services;

import com.zoo.ninestar.domains.NSEventData;

/**
 * the operations for nine star
 * common business
 */
public interface NSCommonService {
    void postNotifyTaskEventDataToSlave(NSEventData eventData);
}
