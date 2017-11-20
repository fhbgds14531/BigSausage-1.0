package net.mizobogames.fhbgds;

import java.util.List;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public abstract class Command {

	private String command;
	private String help;
	private boolean hidden;
	
	public Command(String commandString, boolean hiddenFromHelp, String helpString){
		this.command = commandString;
		this.hidden = hiddenFromHelp;
		this.help = helpString.replace("%n%", commandString).replace("%p%", BigSausage.PREFIX);
	}
	
	public Command(String commandString, String helpString){
		this(commandString, false, helpString);
	}
	
	public abstract void execute(IChannel channel, IUser commandAuthor, IGuild guild, List<String> command, IMessage message);
	
	public String getCommandString(){
		return this.command;
	}
	
	public String getHelpString(){
		return this.help;
	}
	
	public boolean getIsHidden(){
		return this.hidden;
	}
	
}
