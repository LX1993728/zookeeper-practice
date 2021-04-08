package com.zoo.ninestar.config.zoo;

import com.zoo.ninestar.domains.NSPathInfo;
import com.zoo.ninestar.enums.NSHurtType;
import com.zoo.ninestar.enums.NSMasterType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;

import java.util.Map;

@Slf4j
public class ZooPkPathConfig {
    // 所有有关九星神器的统一前缀
    private static final String PREFIX = "nine";
    private static final String LEADER = "leader";
    private static final String PKS = "pks";
    private static final String BUFFS = "buffs";
    private static final String ACCUMULATE = "accumulate";
    private static final String TIMEOUT = "timeout";

    private static final AntPathMatcher matcher = new AntPathMatcher();


    static {
        // 从properties 或者 数据库加载配置
    }
    // get leader path (election)
    public static String getLeaderPath(){
        return "/" + PREFIX + "/" + LEADER;
    }

    // (distributed queue)
    public static String getTimeoutPath(){
        return "/" + PREFIX + "/" + TIMEOUT;
    }
    // get the prefix of all pk
    public static String getPksPrefix(){
        return getPksPrefix(true);
    }

    public static String getPksPrefix(boolean includeSuffix){
        String p =  "/" + PREFIX + "/" + PKS;
        return includeSuffix ? p + "/" : p;
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
        Assert.isTrue(hurtType != null, "the hurtType cannot be null!!!");
        String prefix = getMasterPrefixInOnePK(masterType, pkId, masterId, false) + "/" + ACCUMULATE + "/" + hurtType.getName();
        return includeSuffix ? prefix + "/" : prefix;
    }

    // get path of buffs
    public static  String getBuffsPath(NSMasterType masterType,
                                            NSHurtType hurtType,
                                            String pkId,
                                            String masterId,
                                            boolean includeSuffix){
        Assert.isTrue(hurtType != null, "the hurtType cannot be null!!!");
        String prefix = getMasterPrefixInOnePK(masterType, pkId, masterId, false) + "/" + BUFFS + "/" + hurtType.getName();
        return includeSuffix ? prefix + "/" : prefix;
    }

    // generate one buff path
    public static String generateOneBuffPath(NSMasterType masterType,
                                             NSHurtType hurtType,
                                             String pkId,
                                             String masterId,
                                             boolean includeSuffix){
        String prefix = getBuffsPath(masterType, hurtType, pkId, masterId, false) + "/" + System.currentTimeMillis();
        return includeSuffix ? prefix + "/" : prefix;
    }

    // use the given time millis generate one buff path
    public static String generateOneBuffPath(NSMasterType masterType,
                                             NSHurtType hurtType,
                                             String pkId,
                                             String masterId,
                                             Long timeMillis,
                                             boolean includeSuffix){
        Assert.isTrue(timeMillis != null && timeMillis > 0, "time millis must more than the zero !!! ");
        String prefix = getBuffsPath(masterType, hurtType, pkId, masterId, false) + "/" + timeMillis;
        return includeSuffix ? prefix + "/" : prefix;
    }

    // ------------------------ the follow methods is used to resolve pattern of path -----------------------------------

    // the path is or not need be resolved
    public static boolean isPathNeedBeResolved(String path){
        Assert.isTrue(path != null && path.length() > 0, "the path cannot be empty !!!");
        if (!matcher.matchStart(getPksPrefix() + "/**", path)){
            return false;
        }
        if (matcher.match(getOnePkPrefix("{pkId}", true) + "/**", path)){
            return true;
        }
        return false;
    }

    /**
     * get info from the path which is triggered
     * @param path
     * @return
     */
    public static NSPathInfo getInfoFromPath(String path){
        if (!isPathNeedBeResolved(path)){
            return null;
        }
        NSPathInfo nsPathInfo = new NSPathInfo();
        String pkIdName = "pkId";
        String masterIdName = "masterId";
        String masterTypeName = "masterType";
        String hurtTypeName = "hurtType";

        String pattern1 =  String.format("/%s/%s/{%s}/{%s}/{%s}/%s/{%s}/**",
                PREFIX, PKS, pkIdName,masterTypeName, masterIdName, ACCUMULATE, hurtTypeName);
        String pattern2 =  String.format("/%s/%s/{%s}/{%s}/{%s}/%s/{%s}/**",
                PREFIX, PKS, pkIdName,masterTypeName, masterIdName, BUFFS, hurtTypeName);
        String matchedPattern;
        if (matcher.matchStart(pattern1, path)){
            nsPathInfo.setIsBuff(false);
            matchedPattern = pattern1;
        }else if (matcher.matchStart(pattern2, path)){
            nsPathInfo.setIsBuff(true);
            matchedPattern = pattern2;
        }else {
            log.info("the path {} is ignored!", path);
            return null;
        }
        final Map<String, String> variableMap = matcher.extractUriTemplateVariables(matchedPattern, path);
        Assert.notEmpty(variableMap, "the variable map of be extracted error, cannot be empty");
        String pkId = variableMap.get(pkIdName);
        String masterId = variableMap.get(masterIdName);
        String masterTypeStr = variableMap.get(masterTypeName);
        String hurtTypeStr = variableMap.get(hurtTypeName);
        Assert.isTrue(pkId != null && masterId != null && masterTypeStr != null && hurtTypeStr != null,
                "the variable which extracted from map valid");

        final NSMasterType masterType = NSMasterType.getType(masterTypeStr);
        final NSHurtType hurtType = NSHurtType.getType(hurtTypeStr);
        Assert.notNull(masterType, "not invalid masterType from path");
        Assert.notNull(hurtType, "not invalid hurtType from path");

        nsPathInfo.setHurtType(hurtType);
        nsPathInfo.setPkId(pkId);
        nsPathInfo.setMasterId(masterId);
        nsPathInfo.setMasterType(masterType);
        nsPathInfo.setHurtType(hurtType);
        return nsPathInfo;
    }


}
