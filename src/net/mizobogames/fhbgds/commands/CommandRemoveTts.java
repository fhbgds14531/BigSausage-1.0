package net.mizobogames.fhbgds.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import net.mizobogames.fhbgds.BigSausage;
import net.mizobogames.fhbgds.Command;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class CommandRemoveTts extends Command{

	public CommandRemoveTts(String commandString, String helpString) {
		super(commandString, helpString);
	}

	@Override
	public void execute(IChannel channel, IUser commandAuthor, IGuild guild, List<String> command, IMessage message) {
		File ttsFile = new File("guilds/" + guild.getStringID() + "/tts.txt");
		String ttsRemoveString = "";
		for (String s : command) {
			if (command.indexOf(s) > 1) {
				ttsRemoveString += s + " ";
			}
		}
		List<String> ttses;
		try {
			ttses = Files.readAllLines(ttsFile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
			ttses = new ArrayList<String>();
		}
		boolean removed = false;
		if(!ttses.isEmpty()){
			removed = ttses.remove(ttsRemoveString.trim());
		}else{
			channel.sendMessage("There are currently no tts strings in the list, you can't remove what isn't there.");
		}
		String reply = "";
		if (removed) {
			reply = "Successfully removed string \"" + ttsRemoveString + "\" from the tts list.";
		} else {
			reply = "Failed to remove string \"" + ttsRemoveString + "\" from the tts list. Is it exactly the same as an existing string? If not, this command won't work.";
		}
		try{
		ttsFile.delete();
		ttsFile.createNewFile();
		Files.write(ttsFile.toPath(), ttses, StandardOpenOption.WRITE);
		}catch(Exception e){
			e.printStackTrace();
			channel.sendMessage("Something went wrong while removing the tts string, use `" + BigSausage.PREFIX + " bugreport <description>` to report bugs.");
		}
		channel.sendMessage(reply);
	}

}
