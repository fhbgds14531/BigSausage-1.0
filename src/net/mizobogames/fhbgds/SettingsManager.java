package net.mizobogames.fhbgds;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import sx.blah.discord.handle.obj.IGuild;

public class SettingsManager {

	private SettingsManager() {
	}

	public static Object getSettingForGuild(IGuild guild, String setting) {
		JSONObject obj = getSettingsForGuild(guild);
		return obj.get(setting);
	}

	@SuppressWarnings("unchecked")
	public static void setSettingForGuild(IGuild guild, String setting, Object newValue) {
		JSONObject obj = getSettingsForGuild(guild);
		obj.put(setting, newValue);
		saveSettingsForGuild(guild, obj);
	}
	
	private static void saveSettingsForGuild(IGuild guild, JSONObject settings){
		File file = new File("guilds/" + guild.getStringID() + "/settings/settings.txt");
		if(file.exists()) file.delete();
		try {
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(settings.toJSONString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private static JSONObject getSettingsForGuild(IGuild guild) {
		checkForFileAndCreateAndSetupDefaults(guild);
		JSONParser p = new JSONParser();
		JSONObject obj;
		try {
			obj = (JSONObject) p.parse(new FileReader(new File("guilds/" + guild.getStringID() + "/settings/settings.txt")));
		} catch (Exception e) {
			e.printStackTrace();
			return new JSONObject();
		}
		if (obj != null) {
			return obj;
		} else {
			JSONObject o = new JSONObject(new HashMap<String, String>());
			JSONArray trusted = new JSONArray();
			o.put("trusted_users", trusted);
			o.put("enabled", false);
			o.put("max_clips_per_message", 4L);
			return o;
		}
	}

	private static void checkForFileAndCreateAndSetupDefaults(IGuild guild) {
		File guildSettingsDir = new File("guilds/" + guild.getStringID() + "/settings");
		File guildSettingsFile = new File("guilds/" + guild.getStringID() + "/settings/settings.txt");
		if (!guildSettingsDir.exists()) {
			guildSettingsDir.mkdirs();
			setupDefaultSettingsForGuild(guild);
			return;
		}
		if (!guildSettingsFile.exists()) {
			setupDefaultSettingsForGuild(guild);
			return;
		}
	}

	@SuppressWarnings("unchecked")
	public static void setupDefaultSettingsForGuild(IGuild guild) {
		File settingsDir = new File("guilds/" + guild.getStringID() + "/settings");
		File guildSettingsFile = new File("guilds/" + guild.getStringID() + "/settings/settings.txt");
		if (guildSettingsFile.exists()) {
			guildSettingsFile.delete();
		}
		try {
			settingsDir.mkdirs();
			guildSettingsFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Map<String, String> settings = new HashMap<String, String>();
		JSONObject o = new JSONObject(settings);
		JSONArray trusted = new JSONArray();
		o.put("trusted_users", trusted);
		o.put("enabled", false);
		o.put("max_clips_per_message", 4L);
		try {
			FileWriter writer = new FileWriter(guildSettingsFile);
			writer.write(o.toJSONString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}