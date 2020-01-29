package fr.maxlego08.workmc.utils;

public class TimerBuilder {

	public static String getFormatLongDays(long temps) {
		long totalSecs = temps / 1000L;
		return String.format("%02d jour(s) %02d heure(s) %02d minute(s) %02d seconde(s)",
				new Object[] { Long.valueOf(totalSecs / 86400l), Long.valueOf(totalSecs % 86400l / 3600l),
						Long.valueOf(totalSecs % 3600L / 60L), Long.valueOf(totalSecs % 60L) });
	}

	public static String getFormatLongHours(long temps) {
		long totalSecs = temps / 1000L;
		return String.format("%02d heure(s) %02d minute(s) %02d seconde(s)",
				new Object[] { Long.valueOf(totalSecs / 3600L), Long.valueOf(totalSecs % 3600L / 60L),
						Long.valueOf(totalSecs % 60L) });
	}

	public static String getFormatLongHoursSimple(long temps) {
		long totalSecs = temps / 1000L;
		return String.format("%02d:%02d:%02d", new Object[] { Long.valueOf(totalSecs / 3600L),
				Long.valueOf(totalSecs % 3600L / 60L), Long.valueOf(totalSecs % 60L) });
	}

	public static String getFormatLongMinutes(long temps) {
		long totalSecs = temps / 1000L;
		return String.format("%02d minute(s) %02d seconde(s)",
				new Object[] { Long.valueOf(totalSecs % 3600L / 60L), Long.valueOf(totalSecs % 60L) });
	}

	public static String getFormatLongSecondes(long temps) {
		long totalSecs = temps / 1000L;
		return String.format("%02d seconde(s)", new Object[] { Long.valueOf(totalSecs % 60L) });
	}

	public static String getStringTime(int second) {

		if (second < 60)
			return TimerBuilder.getFormatLongSecondes(second * 1000l);
		else if (second >= 60 && second < 3600)
			return TimerBuilder.getFormatLongMinutes(second * 1000l);
		else if (second >= 3600 && second < 86400)
			return TimerBuilder.getFormatLongHours(second * 1000l);
		else
			return TimerBuilder.getFormatLongDays(second * 1000l);

	}

}
