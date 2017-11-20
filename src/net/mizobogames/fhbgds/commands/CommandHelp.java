package net.mizobogames.fhbgds.commands;

import java.util.List;

import net.mizobogames.fhbgds.BigSausage;
import net.mizobogames.fhbgds.Command;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class CommandHelp extends Command{

	public CommandHelp(String commandString, String helpString){
		super(commandString, helpString);
	}

	@Override
	public void execute(IChannel channel, IUser commandAuthor, IGuild guild, List<String> command) {
		if(command.size() == 2){
			channel.sendMessage(getHelpString());
		}else if(command.size() == 3){
			String s = BigSausage.commands.getFromString(command.get(2)).getHelpString();
			channel.sendMessage(s);
		}
	}
}
