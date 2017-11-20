package net.mizobogames.fhbgds.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import net.mizobogames.fhbgds.BigSausage;
import net.mizobogames.fhbgds.Command;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class CommandTts extends Command {

	public CommandTts(String commandString, String helpString) {
		super(commandString, helpString);
	}

	@Override
	public void execute(IChannel channel, IUser commandAuthor, IGuild guild, List<String> command, IMessage message) {
		SecureRandom rand = new SecureRandom();
		rand.setSeed(System.nanoTime());
		File ttsFile = new File("guilds/" + guild.getStringID() + "/tts.txt");
		List<String> ttses;
		try {
			ttses = Files.readAllLines(ttsFile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
			ttses = new ArrayList<String>();
		}
		if(!ttses.isEmpty()){
			channel.sendMessage(ttses.get(rand.nextInt(ttses.size())), true);
		}else{
			channel.sendMessage("There are currently no tts strings in the list, try adding some with `" + BigSausage.PREFIX + " add-tts <tts string>`");
		}
	}

}
