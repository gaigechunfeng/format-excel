package com.wk.excel.entity;

/**
 * Created by gaige on 2017/9/23.
 */
public class MaterialMasterData {

    private String title;
    private float netWeight;
    private float grossWeight;

    public void setGrossWeight(float grossWeight) {
        this.grossWeight = grossWeight;
    }

    public void setNetWeight(float netWeight) {
        this.netWeight = netWeight;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getGrossWeight() {
        return grossWeight;
    }

    public float getNetWeight() {
        return netWeight;
    }

    public String getTitle() {
        return title;
    }
}
