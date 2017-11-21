package net.mizobogames.fhbgds.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.mizobogames.fhbgds.BigSausage;
import net.mizobogames.fhbgds.Command;
import net.mizobogames.fhbgds.Util;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class CommandListFiles extends Command {

	public CommandListFiles(String commandString, String helpString) {
		super(commandString, helpString);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(IChannel channel, IUser commandAuthor, IGuild guild, List<String> command, IMessage message) {
		boolean doAudio = false;
		boolean doImages = false;
		if (command.size() < 3) {
			doAudio = true;
			doImages = true;
		} else {
			String type = command.get(2);
			if (type.toLowerCase().contentEquals("images")) doImages = true;
			if (type.toLowerCase().contentEquals("audio")) doAudio = true;
		}
		File filesDir = new File("guilds/" + guild.getStringID() + "/files/");
		if (filesDir.exists()) {
			File indexDir = new File("guilds/" + guild.getStringID() + "/files/indices");
			File audioFileIndex = Util.getAudioIndexFile(guild);
			File imageFileIndex = Util.getImageIndexFile(guild);
			List<String> imageIndexStrings = new ArrayList<String>();
			List<String> audioIndexStrings = new ArrayList<String>();
			if (indexDir.exists()) {
				if (audioFileIndex.exists() && doAudio) {
					JSONObject audioIndex = Util.getJsonObjectFromFile(guild, audioFileIndex);
					JSONArray ja = (JSONArray) audioIndex.get("index");
					if(ja == null) ja = new JSONArray();
					ja.forEach(s -> audioIndexStrings.add(String.valueOf(s)));
				}
				if (imageFileIndex.exists() && doImages) {
					JSONObject imageIndex = Util.getJsonObjectFromFile(guild, imageFileIndex);
					JSONArray ja = (JSONArray) imageIndex.get("index");
					if(ja == null) ja = new JSONArray();
					ja.forEach(s -> imageIndexStrings.add(String.valueOf(s)));
				}
				String audioList = "Audio Clips:\n";
				if (!audioIndexStrings.isEmpty()) {
					audioList += "```" + Util.getCommaSeparatedFormattedList(audioIndexStrings) + "```";
				}else{
					audioList = "There are currently no audio clips.";
				}
				String imageList = "Images:\n";
				if (!imageIndexStrings.isEmpty()) {
					imageList += "```" + Util.getCommaSeparatedFormattedList(imageIndexStrings) + "```";
				}else{
					imageList = "There are currently no images.";
				}
				String output = "Error. If you're seeing this, please report the incident using `" + BigSausage.PREFIX + " bugreport <information about the issue>`";
				if (doImages && !doAudio) {
					output = "Here are all the image names for this server:\n" + imageList.replace("Images:\n", "");
				} else if (!doImages && doAudio) {
					output = "Here are all the audio clip names for this server:\n" + audioList.replace("Audio Clips:\n", "");
				} else if (doImages && doAudio) {
					output = "Here are all the image and audio clip names for this server:\n\n" + imageList + "\n" + audioList;
				}
				channel.sendMessage(output);
			}
		} else {
			channel.sendMessage("The files directory is empty.");
		}
	}

}
