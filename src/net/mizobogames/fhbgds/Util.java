package net.mizobogames.fhbgds;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class Util {

	public static int getMaxLength(String[] sample) {
		int maxLength = 0;
		if (sample != null && sample.length > 0) {
			for (String word : sample) {
				word = (word != null) ? word.trim() : "";
				if (word.length() > maxLength) {
					maxLength = word.length();
				}
			}
		}
		return maxLength;
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
		int maxLength = getMaxLength(nameArray);
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
	
	@SuppressWarnings("unchecked")
	public static List<String> getTriggersFor(IGuild guild, String name){
		File audioFileIndex = Util.getAudioIndexFile(guild);
		File imageFileIndex = new File("guilds/" + guild.getStringID() + "/files/indices/imageIndex.json");
		JSONObject audioIndexContents = getJsonObjectFromFile(guild, audioFileIndex);
		JSONObject imageIndexContents = getJsonObjectFromFile(guild, imageFileIndex);
		JSONArray audioIndex = (JSONArray) audioIndexContents.get("index");
		JSONArray imageIndex = (JSONArray) imageIndexContents.get("index");
		if(audioIndex.contains(name)){
			JSONArray triggers = (JSONArray) audioIndexContents.get(name);
			List<String> triggerStrings = new ArrayList<String>();
			if(triggers == null){
				return triggerStrings;
			}
			triggers.forEach(j -> triggerStrings.add(String.valueOf(j)));
			return triggerStrings;
		}else if(imageIndex.contains(name)){
			JSONArray triggers = (JSONArray) imageIndexContents.get(name);
			List<String> triggerStrings = new ArrayList<String>();
			if(triggers == null){
				return triggerStrings;
			}
			triggers.forEach(j -> triggerStrings.add(String.valueOf(j)));
			return triggerStrings;
		}else{
			return new ArrayList<String>();
		}
	}
	
	public static File getAudioIndexFile(IGuild guild){
		return new File("guilds/" + guild.getStringID() + "/files/indices/audioIndex.json");
	}
	
	public static File getImageIndexFile(IGuild guild){
		return new File("guilds/" + guild.getStringID() + "/files/indices/imageIndex.json");
	}
	
	public static boolean isAudioClip(String name, IGuild guild){
		JSONObject audioIndexContents = getJsonObjectFromFile(guild, getAudioIndexFile(guild));
		JSONArray audioIndex = (JSONArray) audioIndexContents.get("index");
		return audioIndex.contains(name);
	}
	
	public static boolean isImageFile(String name, IGuild guild){
		JSONObject imageIndexContents = getJsonObjectFromFile(guild, getImageIndexFile(guild));
		JSONArray imageIndex = (JSONArray) imageIndexContents.get("index");
		return imageIndex.contains(name);
	}
	
	public static void saveJsonToFile(File file, JSONObject obj){
		if(file.exists()) file.delete();
		try {
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(obj.toJSONString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static JSONObject getJsonObjectFromFile(IGuild guild, File file) {
		JSONParser p = new JSONParser();
		JSONObject obj;
		try {
			obj = (JSONObject) p.parse(new FileReader(file));
		} catch (Exception e) {
			return new JSONObject(new HashMap<String, String>()); //the file is probably empty or corrupt so return an empty object.
		}
		if (obj != null) {
			return obj;
		} else {
			return new JSONObject(new HashMap<String, String>());
		}
	}

}
