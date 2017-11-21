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

public class CommandListTriggers extends Command {

	public CommandListTriggers(String commandString, String helpString) {
		super(commandString, helpString);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(IChannel channel, IUser commandAuthor, IGuild guild, List<String> command, IMessage message) {
		if(command.size() != 3){
			channel.sendMessage("Invalid number of arguments! Usage: `" + BigSausage.PREFIX + " " + getCommandString() + " <name>`");
		}else{
			File audioFileIndex = Util.getAudioIndexFile(guild);
			File imageFileIndex = Util.getImageIndexFile(guild);
			String fileTitle = command.get(2);
			JSONObject audioIndexContents = Util.getJsonObjectFromFile(guild, audioFileIndex);
			JSONObject imageIndexContents = Util.getJsonObjectFromFile(guild, imageFileIndex);
			JSONArray audioIndex = (JSONArray) audioIndexContents.get("index");
			JSONArray imageIndex = (JSONArray) imageIndexContents.get("index");
			if(audioIndex.contains(fileTitle)){
				JSONArray triggers = (JSONArray) audioIndexContents.get(fileTitle);
				if(triggers == null){
					channel.sendMessage("Couldn't find any triggers for `" + fileTitle + "`");
					return;
				}
				List<String> triggerStrings = new ArrayList<String>();
				triggers.forEach(j -> triggerStrings.add(String.valueOf(j)));
				channel.sendMessage("The triggers for `" + fileTitle + "` are as follows:\n```" + Util.getCommaSeparatedFormattedList(triggerStrings) + "```");
			}else if(imageIndex.contains(fileTitle)){
				JSONArray triggers = (JSONArray) imageIndexContents.get(fileTitle);
				if(triggers == null){
					channel.sendMessage("Couldn't find any triggers for `" + fileTitle + "`");
					return;
				}
				List<String> triggerStrings = new ArrayList<String>();
				triggers.forEach(j -> triggerStrings.add(String.valueOf(j)));
				channel.sendMessage("The triggers for `" + fileTitle + "` are as follows:\n```" + Util.getCommaSeparatedFormattedList(triggerStrings) + "```");
			}else{
				channel.sendMessage("Error. Couldn't find any triggers for `" + fileTitle + "`");
			}
		}
	}

}
