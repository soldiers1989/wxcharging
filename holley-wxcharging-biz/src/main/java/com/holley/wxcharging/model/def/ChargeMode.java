package com.holley.wxcharging.model.def;

public class ChargeMode {

    private String name;
    private int    value;
    private double    chaLen;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

	public double getChaLen() {
		return chaLen;
	}

	public void setChaLen(double chaLen) {
		this.chaLen = chaLen;
	}



}
