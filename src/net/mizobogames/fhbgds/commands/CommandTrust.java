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

public class CommandTrust extends Command {

	public CommandTrust(String commandString, String helpString) {
		super(commandString, helpString);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(IChannel channel, IUser commandAuthor, IGuild guild, List<String> command, IMessage message) {
		if (Util.hasPermission(2, guild, commandAuthor)) {
			JSONArray ja = (JSONArray) SettingsManager.getSettingForGuild(guild, "trusted_users");
			List<String> trusted = new ArrayList<String>();
			ja.forEach(s -> trusted.add(String.valueOf(s)));
			if (command.size() == 2) {
				String out = "";
				List<String> formattedTrusted = new ArrayList<String>();
				trusted.forEach(s -> formattedTrusted.add("<@" + s + ">"));
				out = Util.getCommaSeparatedFormattedList(formattedTrusted);
				channel.sendMessage("Trusted users: \n" + out + "");
			} else {
				String mention = command.get(2);
				mention = mention.replace("<@", "").replace(">", "");
				trusted.add(mention);
				channel.sendMessage("Added <@" + mention + "> to the trusted users list.");
			}
			ja = new JSONArray();
			ja.addAll(trusted);
			SettingsManager.setSettingForGuild(guild, "trusted_users", ja);
		}
	}
}
