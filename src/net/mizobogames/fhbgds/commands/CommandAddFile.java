package net.mizobogames.fhbgds.commands;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
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
import sx.blah.discord.handle.obj.IMessage.Attachment;
import sx.blah.discord.handle.obj.IUser;

public class CommandAddFile extends Command {

	public CommandAddFile(String commandString, String helpString) {
		super(commandString, helpString);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(IChannel channel, IUser commandAuthor, IGuild guild, List<String> command, IMessage message) {
		File indexDir = new File("guilds/" + guild.getStringID() + "/files/indices");
		File audioFileIndex = Util.getAudioIndexFile(guild);
		File imageFileIndex = Util.getImageIndexFile(guild);
		try {
			if (!indexDir.exists()) {
				indexDir.mkdirs();
			}
			if (!audioFileIndex.exists()) {
				audioFileIndex.createNewFile();
			}
			if (!imageFileIndex.exists()) {
				imageFileIndex.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (Util.hasPermission(1, guild, commandAuthor)) {
			List<Attachment> attachments = message.getAttachments();
			if (attachments.size() != 1) {
				channel.sendMessage("Invalid number of attachments. Please attach 1 file at a time. If you believe you are seeing this in error, use `" + BigSausage.PREFIX
						+ " bugreport <description>` to report the situation.");
				return;
			}
			if (command.size() < 4) {
				channel.sendMessage("Invalid number of parameters, make sure you include a name for the file and at least 1 trigger for it.");
				return;
			}
			if (attachments.get(0).getFilename().endsWith(".wav")) {
				String filename = attachments.get(0).getFilename().toLowerCase();
				String extension = filename.substring(filename.indexOf("."));
				File attachment = new File("guilds/" + guild.getStringID() + "/files/" + filename);
				if(attachment.exists()){
					int i = 0;
					filename = filename.replace(extension, "_(0)" + extension);
					while(attachment.exists()){
						filename = filename.replace("_(" + i + ")" + extension, "_(" + (i+1) + ")" + extension);
						attachment = new File("guilds/" + guild.getStringID() + "/files/" + filename);
						i++;
					}
				}
				this.downloadAttachment(attachments.get(0), filename, guild);
				List<String> triggers = new ArrayList<String>();
				int i = 0;
				for(String word : command){
					if(i >= 3){
						triggers.add(word);
					}
					i++;
				}
				JSONObject obj = Util.getJsonObjectFromFile(guild, audioFileIndex);
				JSONArray index = (JSONArray) obj.get("index");
				if(index == null){
					index = new JSONArray();
				}
				index.add(command.get(2).toLowerCase());
				JSONArray ja = new JSONArray();
				for(String s : triggers){
					ja.add(s);
				}
				obj.put("index", index);
				obj.put(command.get(2).toLowerCase() + "_name", filename);
				obj.put(command.get(2).toLowerCase(), ja);
				Util.saveJsonToFile(audioFileIndex, obj);
				channel.sendMessage("Succesfully added file under the name \"" + command.get(2).toLowerCase() + "\"");
			} else if (isImageFilenameValid(attachments.get(0).getFilename())) {
				String filename = attachments.get(0).getFilename().toLowerCase();
				String extension = filename.substring(filename.indexOf("."));
				File attachment = new File("guilds/" + guild.getStringID() + "/files/" + filename);
				if(attachment.exists()){
					int i = 0;
					filename = filename.replace(extension, "_(0)" + extension);
					while(attachment.exists()){
						filename = filename.replace("_(" + i + ")" + extension, "_(" + (i+1) + ")" + extension);
						attachment = new File("guilds/" + guild.getStringID() + "/files/" + filename);
						i++;
					}
				}
				this.downloadAttachment(attachments.get(0), filename, guild);
				List<String> triggers = new ArrayList<String>();
				int i = 0;
				for(String word : command){
					if(i >= 3){
						triggers.add(word);
					}
					i++;
				}
				JSONObject obj = Util.getJsonObjectFromFile(guild, imageFileIndex);
				JSONArray index = (JSONArray) obj.get("index");
				if(index == null){
					index = new JSONArray();
				}
				index.add(command.get(2).toLowerCase());
				JSONArray ja = new JSONArray();
				for(String s : triggers){
					ja.add(s);
				}
				obj.put("index", index);
				obj.put(command.get(2).toLowerCase() + "_name", filename);
				obj.put(command.get(2).toLowerCase(), ja);
				Util.saveJsonToFile(imageFileIndex, obj);
				channel.sendMessage("Succesfully added file under the name \"" + command.get(2).toLowerCase() + "\"");
			}else{
				channel.sendMessage("Invalid file type. Audio clips must be of the `.wav` format and images must be one of the following: `.jpg` `.jpeg` `.png` `.bmp`");
			}
		}
	}

	private void downloadAttachment(Attachment a, String filename, IGuild guild){
		try {
			URL url = new URL(a.getUrl());
	        BufferedInputStream bis = new BufferedInputStream(url.openStream());
	        FileOutputStream fis = new FileOutputStream(new File("guilds/" + guild.getStringID() + "/files/" + filename));
	        byte[] buffer = new byte[1024];
	        int count=0;
	        while((count = bis.read(buffer,0,1024)) != -1)
	        {
	            fis.write(buffer, 0, count);
	        }
	        fis.close();
	        bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isImageFilenameValid(String filename) {
		return filename.matches("([^\\s]+(\\.(?i)(jpg|png|bmp|jpeg))$)");
	}

}
