package com.wk.excel.entity;

/**
 * Created by gaige on 2017/9/23.
 */
public class CustomerMasterData {

    private String phone;
    private String no;
    private String name;
    private String code;
    private String area;
    private String addr;
    private String type;
    private String orderStatus;
    private String firstValidOrderDate;
    private String source;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getFirstValidOrderDate() {
        return firstValidOrderDate;
    }

    public void setFirstValidOrderDate(String firstValidOrderDate) {
        this.firstValidOrderDate = firstValidOrderDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
