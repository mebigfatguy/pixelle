package com.mebigfatguy.pixelle;

import java.util.ResourceBundle;

public class Version {
	private static String version;
	static {
		try {
			ResourceBundle rb = ResourceBundle.getBundle("com/mebigfatguy/pixelle/resources/version");
			version = rb.getString("pixelle.version");
		} catch (Exception e) {
			version = "?.?.?";
		}
	}
	
	private Version() {
	}
	
	public static String getVersion() {
		return version;
	}
}
