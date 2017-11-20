package net.mizobogames.fhbgds.commands;

import java.util.List;

import net.mizobogames.fhbgds.Command;
import net.mizobogames.fhbgds.SettingsManager;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class CommandStatus extends Command{

	public CommandStatus(String commandString, String helpString) {
		super(commandString, helpString);
	}

	@Override
	public void execute(IChannel channel, IUser commandAuthor, IGuild guild, List<String> command, IMessage message) {
		boolean status = (boolean) SettingsManager.getSettingForGuild(guild, "enabled");
		channel.sendMessage("BigSausage is currently " + (status ? "enabled." : " disabled."));
	}
	

}
