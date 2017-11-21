package net.mizobogames.fhbgds.commands;

import java.util.List;

import net.mizobogames.fhbgds.Command;
import net.mizobogames.fhbgds.SettingsManager;
import net.mizobogames.fhbgds.Util;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class CommandEnable extends Command{

	private boolean stateToSet;
	
	public CommandEnable(String commandString, String helpString, boolean stateToSet) {
		super(commandString, helpString);
		this.stateToSet = stateToSet;
	}

	@Override
	public void execute(IChannel channel, IUser commandAuthor, IGuild guild, List<String> command, IMessage message) {
		if(Util.hasPermission(1, guild, commandAuthor)){
			SettingsManager.setSettingForGuild(guild, "enabled", stateToSet);
			channel.sendMessage("BigSausage is now " + (stateToSet ? "enabled." : " disabled."));
		}
	}

}
