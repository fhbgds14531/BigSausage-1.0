package net.mizobogames.fhbgds.commands;

import java.util.List;

import net.mizobogames.fhbgds.BigSausage;
import net.mizobogames.fhbgds.Command;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class CommandChangelog extends Command {

	public CommandChangelog(String commandString, String helpString) {
		super(commandString, helpString);
	}

	@Override
	public void execute(IChannel channel, IUser commandAuthor, IGuild guild, List<String> command, IMessage message) {
		if(command.get(1).contains("version")){
			channel.sendMessage("Version: `" + BigSausage.VERSION + "`");
		}else{
			channel.sendMessage("Changelog: `" + BigSausage.CHANGELOG + "`");
		}
	}

}
