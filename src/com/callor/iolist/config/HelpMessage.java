package com.callor.iolist.config;

public class HelpMessage {

	public static final void ERROR(String messeage) {
		ERROR(messeage,"");
	}
	public static final void ERROR(String messeage, String data) {
		System.out.println(AnsiConsol.RED(Line.sLine(70)));
		System.out.println(AnsiConsol.RED(messeage));
		System.out.println(AnsiConsol.RED(data));
		System.out.println(AnsiConsol.RED(Line.sLine(70)));
	}
}
