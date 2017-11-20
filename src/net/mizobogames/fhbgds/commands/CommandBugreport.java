package net.mizobogames.fhbgds.commands;

import java.util.List;

import net.mizobogames.fhbgds.BigSausage;
import net.mizobogames.fhbgds.Command;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class CommandBugreport extends Command {

	public CommandBugreport(String commandString, String helpString) {
		super(commandString, helpString);
	}

	@Override
	public void execute(IChannel channel, IUser commandAuthor, IGuild guild, List<String> command, IMessage msg) {
		String message = "";
		message += "User: " + commandAuthor.getName() + " (" + commandAuthor.getStringID() + ", " + commandAuthor.mention() + ")\n";
		message += "Server: " + guild.getName() + " (" + guild.getStringID() + "): Owned by: " + guild.getOwner().getName() + " (" + guild.getOwner().getStringID() + "), " + 
				guild.getUsers().size() + " members.\n";
		message += "Description: ";
		for(String s : command){
			message += s + " ";
		}
		message = message.replace("!bs bugreport ", "");
		message += "\n";
		IGuild supportGuild = BigSausage.client.getGuildByID(382053109788049429L);
		IChannel reportChannel = supportGuild.getChannelByID(382053168042737674L);
		reportChannel.sendMessage(message.trim());
		channel.sendMessage("Your message has been recieved, thank you for your feedback! We may be in touch with you if we need more information.");
	}

}
