SELECT MES_SHOPFLOOR_NAME,AREA_NAME,
       ZENGGUO_NAME,ZENGGUO_CODE,
       MES_ZENGGUO_RECORD.id AS zengguoRecordID,
       CASE
           WHEN MES_ZENGGUO_RECORD.ZAOPEI_TYPE = 1 THEN '面糟'
           WHEN MES_ZENGGUO_RECORD.ZAOPEI_TYPE = 2 THEN '中上粮糟'
           WHEN MES_ZENGGUO_RECORD.ZAOPEI_TYPE = 3 THEN '下层粮糟'
           WHEN MES_ZENGGUO_RECORD.ZAOPEI_TYPE = 4 THEN '双轮底（丢糟：水醅）'
           WHEN MES_ZENGGUO_RECORD.ZAOPEI_TYPE = 5 THEN '红糟'
           WHEN MES_ZENGGUO_RECORD.ZAOPEI_TYPE = 6 THEN '升池糟'
           ELSE '未知'
           END AS ZAOPEI_TYPE1,
       CASE
           WHEN MES_ZENGGUO_RECORD.ENUM_SHIFT = 'DAY_SHIFT' THEN '白班'
           WHEN MES_ZENGGUO_RECORD.ENUM_SHIFT = 'SHORT_NIGHT_SHIFT' THEN '小夜班'
           WHEN MES_ZENGGUO_RECORD.ENUM_SHIFT = 'LONG_NIGHT_SHIFT' THEN '大夜班'
           ELSE '未知'
           END AS shiftName,
       MES_SHIFT_TEAM.TEAM_NAME,
       CONCAT(MES_SHOPFLOOR_NAME,MES_SHIFT_TEAM.TEAM_NAME) as shopTeam,
       CONCAT(MES_SHOPFLOOR_NAME,MES_ZENGGUO_RECORD.ENUM_SHIFT) as shopShift,
       CASE
           WHEN MES_ZENGGUO_RECORD.LIANGSHI_TYPE_DOWN = 0 THEN '粗粮'
           WHEN MES_ZENGGUO_RECORD.LIANGSHI_TYPE_DOWN = 1 THEN '细粮'
           ELSE '未知'
           END AS liangshiTypeDown,
       CASE
           WHEN MES_ZENGGUO_RECORD.LIANGSHI_TYP_UP = 0 THEN '粗粮'
           WHEN MES_ZENGGUO_RECORD.LIANGSHI_TYP_UP = 1 THEN '细粮'
           ELSE '未知'
           END AS liangshiTypeUp,
       phaseConfig.PHASE_NAME,
       MES_ZENGGUO_RECORD.*,
       MES_ZENG_SUM_DATA.TANLIANG_ZAOPEI_WEIGHT,
       MES_ZENG_SUM_DATA.TANLIANG_JIAQU_WEIGHT,
       MES_ZENG_SUM_DATA.TANLIANG_MAX_TEMP,
       MES_ZENG_SUM_DATA.TANLIANG_MIN_TEMP,
       MES_ZENG_SUM_DATA.TANLIANG_AVG_TEMP
FROM MES_ZENGGUO_RECORD
         LEFT JOIN MES_ZENGTONG ON MES_ZENGGUO_RECORD.MES_ZENGGUO_ID = MES_ZENGTONG.id
         LEFT JOIN MES_AREA ON MES_ZENGTONG.MES_AREA_ID = MES_AREA.id
         LEFT JOIN MES_SHOPFLOOR  on MES_AREA.MES_SHOPFLOOR_ID = MES_SHOPFLOOR.id
         LEFT JOIN MES_ZENG_SUM_DATA ON MES_ZENGGUO_RECORD.SUM_DATA_RECORD_ID = MES_ZENG_SUM_DATA.id
         LEFT JOIN MES_SHIFT_TEAM on MES_SHIFT_TEAM.ID = MES_ZENGGUO_RECORD.SHIFT_TEAM_ID
             Left join jsywineryMES.dbo.MES_ZENGGOU_PHASE_CONFIG phaseConfig on MES_ZENGGUO_RECORD.ZENGGUO_PHASE_ID = phaseConfig.ID;
-- WHERE MES_ZENGGUO_RECORD.END_TIME_TALL > '2022-10-24T13:41:39';

select
    MES_SHOPFLOOR.MES_SHOPFLOOR_NAME,
    MES_AREA.AREA_NAME,
    MES_TANLIANGJI.TANLIANGJI_CODE,
    MES_SHIFT_TEAM.TEAM_NAME,

    CASE
        WHEN record.ENUM_SHIFT = 'DAY_SHIFT' THEN '白班'
        WHEN record.ENUM_SHIFT = 'SHORT_NIGHT_SHIFT' THEN '小夜班'
        WHEN record.ENUM_SHIFT = 'LONG_NIGHT_SHIFT' THEN '长夜班'
        ELSE '未知'
        END AS shiftName,
    CASE
        WHEN record.ZAOPEI_TYPE = 1 THEN '入池'
        WHEN record.ZAOPEI_TYPE = 2 THEN '入池'
        WHEN record.ZAOPEI_TYPE = 3 THEN '入池'
        WHEN record.ZAOPEI_TYPE = 4 THEN '丢糟'
        WHEN record.ZAOPEI_TYPE = 5 THEN '入池'
        WHEN record.ZAOPEI_TYPE = 6 THEN '入池'
        ELSE '未知'
        END AS diuzaoOrRuchi,
    record.PHASE_START_TIME AS START_TIME_TOTAL,
    record.*
from MES_TANLIANGJI_RECORD AS record
         left join MES_TANLIANGJI on MES_TANLIANGJI.id = record.MES_TANLIANGJI_ID
         left join MES_AREA on MES_AREA.id = MES_TANLIANGJI.MES_AREA_ID
         left join MES_SHOPFLOOR on MES_SHOPFLOOR.id = MES_AREA.MES_SHOPFLOOR_ID
         Left join MES_SHIFT_TEAM  on MES_SHIFT_TEAM.id = record.SHIFT_TEAM_ID
where record.ZENG_SEQUENCE > 0 and record.RESOURCE_ZENGGUO_ID > 0
