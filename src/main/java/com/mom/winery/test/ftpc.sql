
SELECT * FROM
    (
        SELECT ROW_NUMBER() OVER (ORDER BY creationTime desc) AS RowNum, * FROM (
                                                                                    SELECT
                                                                                        t1.atr_key as sublotEventLogId,
                                                                                        t1.inventoryOrderLineKey_I as inventoryOrderLineKey_I,
                                                                                        t1.mesSublotKey_I as mesSublotKey_I,
                                                                                        t1.originQualityStatus_S as originQualityStatus,
                                                                                        t1.currentQualityStatus_S as currentQualityStatus,
                                                                                        t1.originQuantity_D as originQuantity,
                                                                                        t1.currentQuantity_D as currentQuantity,
                                                                                        t1.creation_time as creationTime,
                                                                                        t1.last_modified_time as updateTime,
                                                                                        t2.atr_key as inventoryOrderLineKey,
                                                                                        t2.mesLineCode_S as mesLineCode,
                                                                                        t2.inventoryOrderLineType_S as inventoryOrderLineType,
                                                                                        t3.atr_key as inventoryOrderKey,
                                                                                        t3.inventoryOrderType_S as inventoryOrderType,
                                                                                        t3.mesOrderCode_S as mesOrderCode,
                                                                                        t4.atr_key as mesSublotKey,
                                                                                        t4.mesSublotCode_S as mesSublotCode,
                                                                                        origin_lot.atr_key as originLotKey,
                                                                                        origin_lot.mesLotCode_S as originLotCode,
                                                                                        origin_part.part_key as originPartKey,
                                                                                        origin_part.part_number as originPartCode,
                                                                                        origin_part.description as originPartName,
                                                                                        current_lot.atr_key as currentLotKey,
                                                                                        current_lot.mesLotCode_S as currentMesLotCode,
                                                                                        current_part.part_key as currentPartKey,
                                                                                        current_part.part_number as currentPartCode,
                                                                                        current_part.description as currentPartName,
                                                                                        origin_storage_unit.storage_unit_key as originStorageUnitKey,
                                                                                        origin_storage_unit.storage_unit_name as originStorageUnitCode,
                                                                                        origin_storage_unit.description as originStorageUnitName,
                                                                                        current_storage_unit.storage_unit_key as currentStorageUnitKey,
                                                                                        current_storage_unit.storage_unit_name as currentStorageUnitCode,
                                                                                        current_storage_unit.description as currentStorageUnitName,
                                                                                        t2.planQuantity_D as planQuantity,
                                                                                        t1.status_S as status,
                                                                                        current_part.unit_of_measure as unitOfMeasure,
                                                                                        origin_storage_zone.storage_zone_key as originStorageZoneKey,
                                                                                        origin_storage_zone.storage_zone_name as originStorageZoneCode,
                                                                                        origin_storage_zone.description as originStorageZoneName,
                                                                                        origin_area.area_key as originAreaKey,
                                                                                        origin_area.area_name as originAreaCode,
                                                                                        origin_area.description as originAreaName,
                                                                                        origin_area.category as originAreaCategory,
                                                                                        current_storage_zone.storage_zone_key as currentStorageZoneKey,
                                                                                        current_storage_zone.storage_zone_name as currentStorageZoneCode,
                                                                                        current_storage_zone.description as currentStorageZoneName,
                                                                                        current_area.area_key as currentAreaKey,
                                                                                        current_area.area_name as currentAreaCode,
                                                                                        current_area.description as currentAreaName,
                                                                                        current_area.category as currentAreaCategory,
                                                                                        origin_lot.productionDate_T as originProductionDate,
                                                                                        origin_lot.expiredDate_T as originExpiredDate,
                                                                                        current_lot.productionDate_T as currentProductionDate,
                                                                                        current_lot.expiredDate_T as currentExpiredDate,
                                                                                        t2.inventoryOrderLineCode_S as inventoryOrderLineCode
                                                                                    FROM AT_MesSublotEventLog as t1 WITH(NOLOCK)
LEFT JOIN AT_InventoryOrderLine as t2 WITH(NOLOCK) on t1.inventoryOrderLineKey_I = t2.atr_key
                                                                                        LEFT JOIN AT_InventoryOrder as t3 WITH(NOLOCK) on t2.inventoryOrderKey_I = t3.atr_key
                                                                                        LEFT JOIN AT_MesSublot as t4 WITH(NOLOCK) on t1.mesSublotKey_I = t4.atr_key
                                                                                        LEFT JOIN AT_MesLot as origin_lot WITH(NOLOCK) on origin_lot.atr_key = t1.mesOriginLotKey_I
                                                                                        LEFT JOIN PART as origin_part WITH(NOLOCK) on origin_part.part_key = origin_lot.partNumber_I
                                                                                        LEFT JOIN AT_MesLot as current_lot WITH(NOLOCK) on current_lot.atr_key = t1.mesCurrentLotKey_I
                                                                                        LEFT JOIN PART as current_part WITH(NOLOCK) on current_part.part_key = current_lot.partNumber_I
                                                                                        LEFT JOIN STORAGE_UNIT as origin_storage_unit WITH(NOLOCK) on origin_storage_unit.storage_unit_key = t1.mesOriginLocationKey_I
                                                                                        LEFT JOIN STORAGEZONE_STORAGEUNIT as origin_storage_zone_unit WITH(NOLOCK) on origin_storage_unit.storage_unit_key = origin_storage_zone_unit.child_key
                                                                                        LEFT JOIN STORAGE_ZONE as origin_storage_zone WITH(NOLOCK) on origin_storage_zone.storage_zone_key = origin_storage_zone_unit.parent_key
                                                                                        LEFT JOIN AREA_STORAGE_ZONE as origin_area_zone WITH(NOLOCK) on origin_storage_zone.storage_zone_key = origin_area_zone.child_key
                                                                                        LEFT JOIN AREA as origin_area WITH(NOLOCK) on origin_area_zone.parent_key = origin_area.area_key
                                                                                        LEFT JOIN STORAGE_UNIT as current_storage_unit WITH(NOLOCK) on current_storage_unit.storage_unit_key = t1.mesCurrentLocationKey_I
                                                                                        LEFT JOIN STORAGEZONE_STORAGEUNIT as current_storage_zone_unit WITH(NOLOCK) on current_storage_unit.storage_unit_key = current_storage_zone_unit.child_key
                                                                                        LEFT JOIN STORAGE_ZONE as current_storage_zone WITH(NOLOCK) on current_storage_zone.storage_zone_key = current_storage_zone_unit.parent_key
                                                                                        LEFT JOIN AREA_STORAGE_ZONE as current_area_zone WITH(NOLOCK) on current_storage_zone.storage_zone_key = current_area_zone.child_key
                                                                                        LEFT JOIN AREA as current_area WITH(NOLOCK) on current_area_zone.parent_key = current_area.area_key
                                                                                    WHERE 1=1
                                                                                      AND t3.inventoryOrderType_S  = 'PURCHASE_ORDER' AND t3.mesOrderCode_S = 'DH241128002' )tt1
    )tt2
ORDER BY creationTime desc, mesOrderCode desc, inventoryOrderLineType;


SELECT
    t1.atr_key AS sublotEventLogId,
    t1.inventoryOrderLineKey_I AS inventoryOrderLineKey_I,
    t1.mesSublotKey_I AS mesSublotKey_I,
    t1.originQualityStatus_S AS originQualityStatus,
    t1.currentQualityStatus_S AS currentQualityStatus,
    t1.originQuantity_D AS originQuantity,
    t1.currentQuantity_D AS currentQuantity,
    t1.creation_time AS creationTime,
    t1.last_modified_time AS updateTime,
    t2.atr_key AS inventoryOrderLineKey,
    t2.mesLineCode_S AS mesLineCode,
    t2.inventoryOrderLineType_S AS inventoryOrderLineType,
    t3.atr_key AS inventoryOrderKey,
    t3.inventoryOrderType_S AS inventoryOrderType,
    t3.mesOrderCode_S AS mesOrderCode,
    t4.atr_key AS mesSublotKey,
    t4.mesSublotCode_S AS mesSublotCode,
    origin_lot.atr_key AS originLotKey,
    origin_lot.mesLotCode_S AS originLotCode,
    origin_part.part_key AS originPartKey,
    origin_part.part_number AS originPartCode,
    origin_part.description AS originPartName,
    current_lot.atr_key AS currentLotKey,
    current_lot.mesLotCode_S AS currentMesLotCode,
    current_part.part_key AS currentPartKey,
    current_part.part_number AS currentPartCode,
    current_part.description AS currentPartName,
    origin_storage_unit.storage_unit_key AS originStorageUnitKey,
    origin_storage_unit.storage_unit_name AS originStorageUnitCode,
    origin_storage_unit.description AS originStorageUnitName,
    current_storage_unit.storage_unit_key AS currentStorageUnitKey,
    current_storage_unit.storage_unit_name AS currentStorageUnitCode,
    current_storage_unit.description AS currentStorageUnitName,
    t2.planQuantity_D AS planQuantity,
    t1.status_S AS status,
    current_part.unit_of_measure AS unitOfMeasure,
    origin_storage_zone.storage_zone_key AS originStorageZoneKey,
    origin_storage_zone.storage_zone_name AS originStorageZoneCode,
    origin_storage_zone.description AS originStorageZoneName,
    origin_area.area_key AS originAreaKey,
    origin_area.area_name AS originAreaCode,
    origin_area.description AS originAreaName,
    origin_area.category AS originAreaCategory,
    current_storage_zone.storage_zone_key AS currentStorageZoneKey,
    current_storage_zone.storage_zone_name AS currentStorageZoneCode,
    current_storage_zone.description AS currentStorageZoneName,
    current_area.area_key AS currentAreaKey,
    current_area.area_name AS currentAreaCode,
    current_area.description AS currentAreaName,
    current_area.category AS currentAreaCategory,
    origin_lot.productionDate_T AS originProductionDate,
    origin_lot.expiredDate_T AS originExpiredDate,
    current_lot.productionDate_T AS currentProductionDate,
    current_lot.expiredDate_T AS currentExpiredDate,
    t2.inventoryOrderLineCode_S AS inventoryOrderLineCode
FROM
    AT_MesSublotEventLog AS t1 WITH(NOLOCK)
    LEFT JOIN AT_InventoryOrderLine AS t2 WITH(NOLOCK) ON t1.inventoryOrderLineKey_I = t2.atr_key
    LEFT JOIN AT_InventoryOrder AS t3 WITH(NOLOCK) ON t2.inventoryOrderKey_I = t3.atr_key
    LEFT JOIN AT_MesSublot AS t4 WITH(NOLOCK) ON t1.mesSublotKey_I = t4.atr_key
    LEFT JOIN AT_MesLot AS origin_lot WITH(NOLOCK) ON origin_lot.atr_key = t1.mesOriginLotKey_I
    LEFT JOIN PART AS origin_part WITH(NOLOCK) ON origin_part.part_key = origin_lot.partNumber_I
    LEFT JOIN AT_MesLot AS current_lot WITH(NOLOCK) ON current_lot.atr_key = t1.mesCurrentLotKey_I
    LEFT JOIN PART AS current_part WITH(NOLOCK) ON current_part.part_key = current_lot.partNumber_I
    LEFT JOIN STORAGE_UNIT AS origin_storage_unit WITH(NOLOCK) ON origin_storage_unit.storage_unit_key = t1.mesOriginLocationKey_I
    LEFT JOIN STORAGEZONE_STORAGEUNIT AS origin_storage_zone_unit WITH(NOLOCK) ON origin_storage_unit.storage_unit_key = origin_storage_zone_unit.child_key
    LEFT JOIN STORAGE_ZONE AS origin_storage_zone WITH(NOLOCK) ON origin_storage_zone.storage_zone_key = origin_storage_zone_unit.parent_key
    LEFT JOIN AREA_STORAGE_ZONE AS origin_area_zone WITH(NOLOCK) ON origin_storage_zone.storage_zone_key = origin_area_zone.child_key
    LEFT JOIN AREA AS origin_area WITH(NOLOCK) ON origin_area_zone.parent_key = origin_area.area_key
    LEFT JOIN STORAGE_UNIT AS current_storage_unit WITH(NOLOCK) ON current_storage_unit.storage_unit_key = t1.mesCurrentLocationKey_I
    LEFT JOIN STORAGEZONE_STORAGEUNIT AS current_storage_zone_unit WITH(NOLOCK) ON current_storage_unit.storage_unit_key = current_storage_zone_unit.child_key
    LEFT JOIN STORAGE_ZONE AS current_storage_zone WITH(NOLOCK) ON current_storage_zone.storage_zone_key = current_storage_zone_unit.parent_key
    LEFT JOIN AREA_STORAGE_ZONE AS current_area_zone WITH(NOLOCK) ON current_storage_zone.storage_zone_key = current_area_zone.child_key
    LEFT JOIN AREA AS current_area WITH(NOLOCK) ON current_area_zone.parent_key = current_area.area_key
WHERE
    t3.inventoryOrderType_S = 'PURCHASE_ORDER'
    AND t3.mesOrderCode_S = 'DH241128002'
ORDER BY
    t1.creation_time DESC,
    t3.mesOrderCode_S DESC,
    t2.inventoryOrderLineType_S;




