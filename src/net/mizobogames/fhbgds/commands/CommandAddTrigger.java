package net.mizobogames.fhbgds.commands;

import java.security.SecureRandom;
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

public class CommandAddTrigger extends Command {

	public CommandAddTrigger(String commandString, String helpString) {
		super(commandString, helpString);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(IChannel channel, IUser commandAuthor, IGuild guild, List<String> command, IMessage message) {
		if(Util.hasPermission(1, guild, commandAuthor)){
			if(command.size() != 4){
				channel.sendMessage("Invalid number of arguments! Usage: `" + BigSausage.PREFIX + " " + getCommandString() + " <name> <new_trigger>`");
			}else{
				List<String> currentTriggers = Util.getTriggersFor(guild, command.get(2).toLowerCase());
				currentTriggers.add(command.get(3).toLowerCase());
				JSONArray ja = new JSONArray();
				ja.addAll(currentTriggers);
				JSONObject indexFileContents;
				boolean audio = false;
				boolean image = false;
				if(Util.isAudioClip(command.get(2).toLowerCase(), guild)){
					indexFileContents = Util.getJsonObjectFromFile(guild, Util.getAudioIndexFile(guild));
					audio = true;
				}else if(Util.isImageFile(command.get(2).toLowerCase(), guild)){
					indexFileContents = Util.getJsonObjectFromFile(guild, Util.getImageIndexFile(guild));
					image = true;
				}else{
					indexFileContents = new JSONObject();
				}
				if(!indexFileContents.isEmpty() && (audio || image)){
					indexFileContents.put(command.get(2).toLowerCase(), ja);
					if(image){
						Util.saveJsonToFile(Util.getImageIndexFile(guild), indexFileContents);
						channel.sendMessage("Successfully added the trigger `" + command.get(3) + "` to the image `" + command.get(2) + "`");
						return;
					}else{
						Util.saveJsonToFile(Util.getAudioIndexFile(guild), indexFileContents);
						channel.sendMessage("Successfully added the trigger `" + command.get(3) + "` to the audio clip `" + command.get(2) + "`");
						return;
					}
				}else{
					channel.sendMessage("Something went wrong, sorry. You can report bugs with `" + BigSausage.PREFIX + " bugreport <description>`");
				}
			}
		}else{
			String addon = "";
			if(new SecureRandom().nextFloat() < 0.1){
				if(command.size() != 4){
					addon += " And even if you did, you typed it wrong. LOL";
				}
			}
			channel.sendMessage("You don't have permission to use that command!" + addon);
		}
	}

}
