package com.hanheldpos.data.api.pojo.table

internal fun TableResp.getModel() = this.model?.firstOrNull();

internal fun TableResp.getTableStatusList() = this.getModel()?.tableStatus;

//region Resolution

internal fun TableResp.getResolutionList() = this.getModel()?.resolutionLists;

//endregion

//region FLoor

internal fun TableResp.getFloorList() = this.getModel()?.floor;

internal fun TableResp.getFloorItem(floorGuid: String?) = this.getFloorList()?.find {
    it?.id.equals(floorGuid);
};

//endregion

//region Table

internal fun TableResp.getFloorTableList() = this.getModel()?.floorTable;

internal fun TableResp.getTableWithFloorGuid(floorGuid: String?) =
    this.getFloorTableList()?.filter {
        it?.floorGuid.equals(floorGuid);
    }

//endregion