package net.mizobogames.fhbgds.commands;

import java.io.IOException;
import java.util.List;

import net.mizobogames.fhbgds.BigSausage;
import net.mizobogames.fhbgds.Command;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class CommandRestart extends Command {

	public CommandRestart(String commandString, String helpString) {
		super(commandString, true, helpString);
	}

	@Override
	public void execute(IChannel channel, IUser commandAuthor, IGuild guild, List<String> command, IMessage message) {
		if(commandAuthor.getStringID().contentEquals(BigSausage.ME)){
			try {
				channel.sendMessage("Restarting...");
				Runtime.getRuntime().exec("cmd /c start \"\" restart.bat");
				BigSausage.client.logout();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
