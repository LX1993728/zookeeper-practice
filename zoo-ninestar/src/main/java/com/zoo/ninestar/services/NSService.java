package com.zoo.ninestar.services;

import com.zoo.ninestar.domains.NSEventData;

/**
 * the operations for nine star
 */
public interface NSService {
    void postNotifyTaskEventDataToSlave(NSEventData eventData);
}
