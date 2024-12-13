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
    record.PHASE_START_TIME AS START_TIME_TOTAL,
    record.*
from MES_TANLIANGJI_RECORD AS record
         left join MES_TANLIANGJI on MES_TANLIANGJI.id = record.MES_TANLIANGJI_ID
         left join MES_AREA on MES_AREA.id = MES_TANLIANGJI.MES_AREA_ID
         left join MES_SHOPFLOOR on MES_SHOPFLOOR.id = MES_AREA.MES_SHOPFLOOR_ID
         Left join MES_SHIFT_TEAM  on MES_SHIFT_TEAM.id = record.SHIFT_TEAM_ID
where record.ZENG_SEQUENCE > 0


WITH RankedRecords AS (
    SELECT record.*,
           ROW_NUMBER() OVER (PARTITION BY MES_RUNLIANGDOU_ID, START_TIME ORDER BY RUNLIANG_DURATION DESC) AS rn
    FROM MES_RUNLIANGDOUDOU_RECORD record
    WHERE record.RUNLIANG_DURATION > 0
)
SELECT
    MES_SHOPFLOOR.MES_SHOPFLOOR_NAME,
    MES_AREA.AREA_NAME,
    MES_RUNLIANGDOU.RUNLIANGDOU_CODE,

    itemConfig.VALUE_NAME AS currentLocation,
    itemConfig2.VALUE_NAME AS emptyOrFull,
    itemConfig3.VALUE_NAME AS diuZaoOrLiangZao,
    CASE
        WHEN RankedRecords.ZAOPEI_TYPE = 1 THEN '面糟'
        WHEN RankedRecords.ZAOPEI_TYPE = 2 THEN '中上粮糟'
        WHEN RankedRecords.ZAOPEI_TYPE = 3 THEN '下层粮糟'
        WHEN RankedRecords.ZAOPEI_TYPE = 4 THEN '双轮底（丢糟：水醅）'
        WHEN RankedRecords.ZAOPEI_TYPE = 5 THEN '红糟'
        WHEN RankedRecords.ZAOPEI_TYPE = 6 THEN '升池糟'
        ELSE '未知'
        END AS ZAOPEITYPE,
    CASE
        WHEN RankedRecords.LIANGSHI_TYPE = 0 THEN '粗粮'
        WHEN RankedRecords.LIANGSHI_TYPE = 1 THEN '细粮'
        ELSE '未知'
        END AS LIANGSHITYPE,
    CASE
        WHEN RankedRecords.ENUM_SHIFT = 'DAY_SHIFT' THEN '白班'
        WHEN RankedRecords.ENUM_SHIFT = 'SHORT_NIGHT_SHIFT' THEN '小夜班'
        WHEN RankedRecords.ENUM_SHIFT = 'LONG_NIGHT_SHIFT' THEN '长夜班'
        ELSE '未知'
        END AS shiftName,
    MES_SHIFT_TEAM.TEAM_NAME,
    RankedRecords.START_TIME AS START_TIME_TOTAL,
    RankedRecords.*
FROM RankedRecords
         LEFT JOIN MES_RUNLIANGDOU ON MES_RUNLIANGDOU.ID = RankedRecords.MES_RUNLIANGDOU_ID
         LEFT JOIN MES_AREA ON MES_AREA.ID = MES_RUNLIANGDOU.MES_AREA_ID
         LEFT JOIN MES_SHOPFLOOR ON MES_SHOPFLOOR.ID = MES_AREA.MES_SHOPFLOOR_ID
         LEFT JOIN MES_WINCC_ITEM_CONFIG itemConfig ON RankedRecords.LOCATION_ID = itemConfig.ID
         LEFT JOIN MES_WINCC_ITEM_CONFIG itemConfig2 ON itemConfig2.ID = RankedRecords.EMPTY_OR_FULL_ID
         LEFT JOIN MES_WINCC_ITEM_CONFIG itemConfig3 ON itemConfig3.ID = RankedRecords.DIU_ZAO_ORLIANGZAO_ID
         LEFT JOIN MES_SHIFT_TEAM ON RankedRecords.SHIFT_TEAM_ID = MES_SHIFT_TEAM.ID
WHERE rn = 1
