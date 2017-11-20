package net.mizobogames.fhbgds;

import java.util.ArrayList;
import java.util.List;

import net.mizobogames.fhbgds.commands.CommandEnable;
import net.mizobogames.fhbgds.commands.CommandHelp;
import net.mizobogames.fhbgds.commands.CommandStatus;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class Commands {

	private static final Command help = new CommandHelp("help", "For specific help use `%p% %n% <command name>`. for a list of commands, use `%p% commands`");
	private List<Command> commands = new ArrayList<Command>();
	
	public Commands(){
		commands.add(help);
		commands.add(new CommandEnable("enable", "Enables the functionality of the bot. Usage: `%p% %n%`", true));
		commands.add(new CommandEnable("disable", "Disables the functionality of the bot. Usage: `%p% %n%`", false));
		commands.add(new CommandStatus("status", "Checks the status of the bot. Usage: `%p% %n%`"));
	}
	
	public Command getFromString(String command){
		for(Command c : commands){
			if(c.getCommandString().contentEquals(command)){
				return c;
			}
		}
		return help;
	}
	
	public void findAndExecuteCommand(List<String> messageContent, IChannel channel, IUser author, IGuild guild){
		if(messageContent.get(0).contentEquals(BigSausage.PREFIX)){
			if(messageContent.size() > 1){
				this.getFromString(messageContent.get(1)).execute(channel, author, guild, messageContent);
			}else{
				help.execute(channel, author, guild, messageContent);
			}
		}else{
			return;
		}
	}
}
