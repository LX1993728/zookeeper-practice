package com.zoo.ninestar.config;

import com.zoo.ninestar.enums.NSHurtType;
import com.zoo.ninestar.enums.NSMasterType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Slf4j
public class ZooPkPathConfig {
    // 所有有关九星神器的统一前缀
    private static final String PREFIX = "/nine";
    private static final String LEADER = "/leader";
    private static final String PKS = "/pks";
    private static final String BUFFS = "/buffs";
    private static final String ACCUMULATE = "/accumulate";
    private static final String HURT_A = "/hurt_a";
    private static final String HURT_B = "/hurt_b";
    private static final String TIMEOUT = "/timeout";

    // path patterns
//    private static final String



    static {
        // 从properties 或者 数据库加载配置
    }
    // get leader path
    public static String getLeaderPath(){
        return PREFIX + LEADER;
    }

    public static String getTimeoutPath(){
        return PREFIX + TIMEOUT;
    }
    // get the prefix of all pk
    public static String getPksPrefix(){
        return PREFIX + PKS + "/";
    }

    // get the prefix of one pk
    public static String getOnePkPrefix(String pkId, boolean includeSuffix){
        Assert.isTrue(!StringUtils.isEmpty(pkId), "pkId  cannot is empty!!!");
        return includeSuffix ? getPksPrefix() + pkId + "/" : getPksPrefix() + pkId;
    }

    // get master prefix
    public static String getMasterPrefixInOnePK(NSMasterType masterType,
                                                String pkId,
                                                String masterId,
                                                boolean includeSuffix){
        Assert.isTrue(masterType != null , "masterType cannot be null");
        Assert.isTrue(!StringUtils.isEmpty(masterId), "masterId  cannot be empty!!!");
        String prefix = getOnePkPrefix(pkId, false) + "/" + masterType.getName() + "/" + masterId;
        return includeSuffix ? prefix + "/" : prefix;
    }

    // get path of accumulate
    public static  String getAccumulatePath(NSMasterType masterType,
                                            NSHurtType hurtType,
                                            String pkId,
                                            String masterId,
                                            boolean includeSuffix){
        String prefix = getMasterPrefixInOnePK(masterType, pkId, masterId, false) + "/" + ACCUMULATE + "/" + hurtType.getName();
        return includeSuffix ? prefix + "/" : prefix;
    }

    // get path of buffs
    public static  String getBuffsPath(NSMasterType masterType,
                                            NSHurtType hurtType,
                                            String pkId,
                                            String masterId,
                                            boolean includeSuffix){
        String prefix = getMasterPrefixInOnePK(masterType, pkId, masterId, false) + "/" + BUFFS + "/" + hurtType.getName();
        return includeSuffix ? prefix + "/" : prefix;
    }



}
