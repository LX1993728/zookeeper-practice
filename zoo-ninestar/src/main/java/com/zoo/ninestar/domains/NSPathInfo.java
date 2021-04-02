package com.zoo.ninestar.domains;

import com.zoo.ninestar.enums.NSHurtType;
import com.zoo.ninestar.enums.NSMasterType;
import lombok.Data;

@Data
public class NSPathInfo {
    private String pkId;
    private String masterId;
    private NSHurtType hurtType;
    private NSMasterType masterType;
    private Boolean isBuff;
}
