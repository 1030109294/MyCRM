package com.changsan.settings.domain;

/**
 * @author 17925
 * @date 2020/11/21 16:18
 */
public class DicValue {
    private String id;
    private String value;
    private String text;
    private String orderNo;
    private String typeCod;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTypeCod() {
        return typeCod;
    }

    public void setTypeCod(String typeCod) {
        this.typeCod = typeCod;
    }
}
