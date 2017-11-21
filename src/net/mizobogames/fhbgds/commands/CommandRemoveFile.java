package net.mizobogames.fhbgds.commands;

import java.io.File;
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

public class CommandRemoveFile extends Command {

	public CommandRemoveFile(String commandString, String helpString) {
		super(commandString, helpString);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(IChannel channel, IUser commandAuthor, IGuild guild, List<String> command, IMessage message) {
		if (Util.hasPermission(1, guild, commandAuthor)) {
			if(command.size() != 3){
				channel.sendMessage("Invalid number of arguments! Usage: `" + BigSausage.PREFIX + " " + getCommandString() + " <file_name>`");
			}else{
				File audioFileIndex = Util.getAudioIndexFile(guild);
				File imageFileIndex = Util.getImageIndexFile(guild);
				String fileTitle = command.get(2);
				JSONObject audioIndexContents = Util.getJsonObjectFromFile(guild, audioFileIndex);
				JSONObject imageIndexContents = Util.getJsonObjectFromFile(guild, imageFileIndex);
				JSONArray audioIndex = (JSONArray) audioIndexContents.get("index");
				JSONArray imageIndex = (JSONArray) imageIndexContents.get("index");
				String filename;
				if(audioIndex.contains(fileTitle)){
					filename = (String) audioIndexContents.get(fileTitle + "_name");
					audioIndexContents.remove(fileTitle);
					audioIndexContents.remove(fileTitle + "_name");
					audioIndex.remove(fileTitle);
					audioIndexContents.put("index", audioIndex);
					Util.saveJsonToFile(audioFileIndex, audioIndexContents);
				}else if(imageIndex.contains(fileTitle)){
					filename = (String) imageIndexContents.get(fileTitle + "_name");
					imageIndexContents.remove(fileTitle);
					imageIndexContents.remove(fileTitle + "_name");
					imageIndex.remove(fileTitle);
					imageIndexContents.put("index", imageIndex);
					Util.saveJsonToFile(imageFileIndex, imageIndexContents);
				}else{
					filename = "";
				}
				File fileToDelete = new File("guilds/" + guild.getStringID() + "/files/" + filename);
				if(!filename.isEmpty()){
					fileToDelete.delete();
				}
			}
		}
	}

}
