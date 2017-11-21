package net.mizobogames.fhbgds.commands;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;

import net.mizobogames.fhbgds.Command;
import net.mizobogames.fhbgds.SettingsManager;
import net.mizobogames.fhbgds.Util;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class CommandRemoveTrust extends Command {

	public CommandRemoveTrust(String commandString, String helpString) {
		super(commandString, helpString);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(IChannel channel, IUser commandAuthor, IGuild guild, List<String> command, IMessage message) {
		if (Util.hasPermission(2, guild, commandAuthor)) {
			JSONArray ja = (JSONArray) SettingsManager.getSettingForGuild(guild, "trusted_users");
			List<String> trusted = new ArrayList<String>();
			ja.forEach(s -> trusted.add(String.valueOf(s)));
			if ((command.size() > 2)) {
				String mention = command.get(2);
				mention = mention.replace("<@", "").replace(">", "");
				if (trusted.contains(mention)) {
					trusted.remove(mention);
					channel.sendMessage("Removed <@" + mention + "> from the trusted users list.");
				}
			}
		}
	}

}
