package com.callor.iolist.config;

public class HelpMessage {

	public static final void ALERT(String messeage) {
		System.out.println(AnsiConsol.GREEN(Line.sLine(70)));
		System.out.println(AnsiConsol.GREEN(messeage));
		System.out.println(AnsiConsol.GREEN(Line.sLine(70)));
	}

	public static final void OK(String messeage) {
		System.out.println(AnsiConsol.PURPLE(Line.sLine(70)));
		System.out.println(AnsiConsol.PURPLE(messeage));
		System.out.println(AnsiConsol.PURPLE(Line.sLine(70)));
	}

	public static final void ERROR(String messeage) {
		ERROR(messeage, "");
	}

	public static final void ERROR(String messeage, String prompt) {
		System.out.println(AnsiConsol.RED(Line.sLine(70)));
		System.out.println(AnsiConsol.RED(messeage));
		if (prompt.equals("")) {
			System.out.print(AnsiConsol.RED(prompt));
		} else {
			System.out.println(AnsiConsol.RED(Line.sLine(70)));
		}
	}

	public static final void CONFIRM(String messeage, String prompt) {
		System.out.println(AnsiConsol.CYAN(Line.sLine(70)));
		System.out.println(AnsiConsol.CYAN(messeage));
		if (!prompt.equals("")) {
			System.out.print(AnsiConsol.CYAN(prompt));
		} else {
			System.out.println(AnsiConsol.CYAN(Line.sLine(70)));
		}
	}
}
