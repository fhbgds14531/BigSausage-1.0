package net.mizobogames.fhbgds;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import org.json.simple.JSONArray;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class Util {

	public static int getMaxLength(String[] in) {
		int c = 0;
		if (in != null && in.length > 0) {
			for (String i : in) {
				i = (i != null) ? i.trim() : "";
				if (i.length() > c) {
					c = i.length();
				}
			}
		}
		return c;
	}

	public static String getCommaSeparatedFormattedList(List<String> strings) {
		String[] array = new String[strings.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = strings.get(i);
		}
		return getCommaSeparatedFormattedList(array);
	}

	public static String getCommaSeparatedFormattedList(String[] strings) {
		List<String> names = Arrays.asList(strings);
		String[] nameArray = new String[names.size()];
		names.toArray(nameArray);
		int maxLength = Util.getMaxLength(nameArray);
		String out = "";
		int index = 0;
		for (String s : names) {
			out += s + " ";
			for (int i = s.length(); i < maxLength; i++) {
				out += " ";
			}
			index++;
			if (index == 4) {
				out += "\n";
				index = 0;
			}
		}
		return out.trim();
	}

	public static boolean hasPermission(int permissionLevel, IGuild guild, IUser user) {
		JSONArray ja = (JSONArray) SettingsManager.getSettingForGuild(guild, "trusted_users");
		if (permissionLevel < 1) return true;
		if (permissionLevel == 1) {
			return ja.toString().contains(user.getStringID()) || user.getStringID().contentEquals(BigSausage.ME) || user.getPermissionsForGuild(guild).contains(Permissions.ADMINISTRATOR);
		} else {
			return user.getStringID().contentEquals(BigSausage.ME) || user.getPermissionsForGuild(guild).contains(Permissions.ADMINISTRATOR);
		}
	}
	
	public static String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit) return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	public static class RunningAverage {

		private final Queue<BigDecimal> queue = new ArrayDeque<BigDecimal>();
		private final int maxSize;
		private BigDecimal sum = BigDecimal.ZERO;

		public RunningAverage(int maxSize) {
			this.maxSize = maxSize;
		}

		public void add(BigDecimal num) {
			sum = sum.add(num);
			queue.add(num);
			if (queue.size() > maxSize) {
				sum = sum.subtract(queue.remove());
			}
		}

		public BigDecimal getAverage() {
			if (queue.isEmpty()) return BigDecimal.ZERO;
			BigDecimal divisor = BigDecimal.valueOf(queue.size());
			return sum.divide(divisor, 2, RoundingMode.HALF_UP);
		}
	}

	public static String getCommaSeparatedString(@SuppressWarnings("rawtypes") List list) {
		String out = "";
		for (Object o : list) {
			out += String.valueOf(o) + ", ";
		}
		return out.substring(0, out.lastIndexOf(", "));
	}

}
