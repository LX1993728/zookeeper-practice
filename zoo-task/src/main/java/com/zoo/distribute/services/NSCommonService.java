package com.zoo.distribute.services;

import com.zoo.distribute.domains.NSEventData;

/**
 * the operations for nine star
 * common business
 */
public interface NSCommonService {
    void postNotifyTaskEventDataToSlave(NSEventData eventData);
}
