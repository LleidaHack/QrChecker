package com.app.qrchecker;

public enum EatOptions {
	lunch_sat(1),
	dinner_sat(2),
	lunch_sun(3);

	public int value;

	EatOptions(int val){
		this.value = val;
	}
}
