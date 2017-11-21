package net.mizobogames.fhbgds.commands;

import java.util.List;

import net.mizobogames.fhbgds.BigSausage;
import net.mizobogames.fhbgds.Command;
import net.mizobogames.fhbgds.SettingsManager;
import net.mizobogames.fhbgds.Util;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class CommandMaxClips extends Command {

	public CommandMaxClips(String commandString, String helpString) {
		super(commandString, helpString);
	}

	@Override
	public void execute(IChannel channel, IUser commandAuthor, IGuild guild, List<String> command, IMessage message) {
		if (command.size() == 3) {
			if (Util.hasPermission(2, guild, commandAuthor)) {
				try {
					long oldValue = (long) SettingsManager.getSettingForGuild(guild, "max_clips_per_message");
					long newValue = Math.abs(Long.valueOf(command.get(2)));
					SettingsManager.setSettingForGuild(guild, "max_clips_per_message", newValue);
					channel.sendMessage("Successfully changed the maximum number of clips per message from " + oldValue + " to " + newValue);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					channel.sendMessage("Invalid number. This command should be written in the format `" + BigSausage.PREFIX + " " + getCommandString() + " <number>`");
				}
			} else {
				channel.sendMessage("You don't have permission to change that setting.");
			}
		} else {
			channel.sendMessage("Maximum clips queueable per message recieved: " + SettingsManager.getSettingForGuild(guild, "max_clips_per_message"));
		}
	}
}
