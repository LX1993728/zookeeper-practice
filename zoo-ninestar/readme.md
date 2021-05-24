---
### 数据库表结构设计
#### t_ns_pk: PK赛记录表
- id: 主键递增 `PKId`
- master_id: PK发起者masterId
- target_master_id: PK接受方的masterId
- start_time: PK 发起时间(时间戳)
- end_time: PK结束时间(时间戳)
- total_buff: 双方的初始总血量
- winner_master_id: PK获胜方的主播masterId (`为-1表示平局`) 
- status: PK赛的状态(0- 发起PK邀请 1- 接受PK邀请进行PK中 2- 拒绝PK 3- PK已结束) 

#### t_ns_pk_detail: PK赛记录详情表
- id：主键递增 `detailId`
- pk_id: 关联PK赛的ID
- master_id: 粉丝所在直播间对应主播的masterId
- fan_master_id: 粉丝的masterId
- fan_role_id: 粉丝用户当前登录的角色ID
- skill_id: 粉丝使用的技能Id
- hurting: 粉丝使用技能为对方造成的伤害
- plussing: 粉丝使用技能为己方增加的生命值或者减少的伤害值  
- release_time: 技能释放时间
- end_time: 技能被终止的时间

#### t_ns_pk_skill: PK赛用户端技能配置表
- id: 主键递增 `skillId`
- name: 技能名称
- icon：技能对应的图片地址  
- for_master: 是否是主播端技能(1-是主播侧技能 0-用户侧技能)  
- ingots: 使用改技能需要扣除的元宝值  
- point_num: 增加心意币数量`送礼的粉丝用户`
- feel_num: 增加的好感值`送礼的粉丝用户`
- wealth_num: 增加的财富值`送礼的粉丝用户`
- master_score: 增加的主播经验值`当前的主播`
- delay_minute: 在PK开始后至少多长时间(分钟)后才可释放此技能
- max_times: 在一场PK中可以使用的最大次数
- description: 技能描述
- hurting: PK中减少对方的生命值
- plussing: PK中恢复己方的生命值
- buff_time: 血条延时伤害时间(单位:秒)
- buff_max_count: 血条的最多条数
- buff_addition: 重叠后每条增加的附加伤害。  
- clear_self_buff: 表示清除己方血条(1-表示清除己方血条 0/NULL-不具备该项功能)  
- ref_skill_id: 此技能的关联技能ID (表示该技能是辅助节能(大招))
- ref_skill_count: 表示关联技能的释放次数阈值(方可激活此技能)





