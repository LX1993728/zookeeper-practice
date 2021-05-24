package com.zoo.distribute.domains;

import com.zoo.distribute.enums.NSHurtType;
import com.zoo.distribute.enums.NSMasterType;
import lombok.Data;

@Data
public class NSPathInfo {
    private String pkId;
    private String masterId;
    private NSHurtType hurtType;
    private NSMasterType masterType;
    private Boolean isBuff;
}
