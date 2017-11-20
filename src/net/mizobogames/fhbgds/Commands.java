package net.mizobogames.fhbgds;

import java.util.ArrayList;
import java.util.List;

import net.mizobogames.fhbgds.commands.CommandAddTts;
import net.mizobogames.fhbgds.commands.CommandBugreport;
import net.mizobogames.fhbgds.commands.CommandEnable;
import net.mizobogames.fhbgds.commands.CommandHelp;
import net.mizobogames.fhbgds.commands.CommandRemoveTts;
import net.mizobogames.fhbgds.commands.CommandRestart;
import net.mizobogames.fhbgds.commands.CommandShutdown;
import net.mizobogames.fhbgds.commands.CommandStatus;
import net.mizobogames.fhbgds.commands.CommandTts;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class Commands {

	public static final Command help = new CommandHelp("help", "For specific help use `%p% %n% <command name>`. for a list of commands, use `%p% commands`");
	static List<Command> commands = new ArrayList<Command>();
	
	public Commands(){
		commands.add(help);
		commands.add(new CommandEnable("enable", "Enables the functionality of the bot. Usage: `%p% %n%`", true));
		commands.add(new CommandEnable("disable", "Disables the functionality of the bot. Usage: `%p% %n%`", false));
		commands.add(new CommandStatus("status", "Checks the status of the bot. Usage: `%p% %n%`"));
		commands.add(new CommandCommands("commands", "Lists all the commands in the bot. Usage: `%p% %n%`"));
		commands.add(new CommandTts("tts", "Repeat a random tts string from the tts list. Usage: `%p% %n%`"));
		commands.add(new CommandAddTts("add-tts", "Add a tts string to the tts list. Usage: `%p% %n% <tts string>`"));
		commands.add(new CommandRemoveTts("remove-tts", "Remove a tts string from the tts list. Usage: `%p% %n% <tts string>`"));
		commands.add(new CommandBugreport("bugreport", "Sends a bug report to the official channel, the report includes your user ID and name in case you need to be contacted. " +
				"Usage: `%p% %n% <description of what went wrong>`"));
		commands.add(new CommandShutdown("shutdown", ""));
		commands.add(new CommandRestart("restart", ""));
	}
	
	public Command getFromString(String command){
		for(Command c : commands){
			if(c.getCommandString().contentEquals(command)){
				return c;
			}
		}
		return help;
	}
	
	public boolean findAndExecuteCommand(List<String> messageContent, IChannel channel, IUser author, IGuild guild){
		if(messageContent.get(0).contentEquals(BigSausage.PREFIX)){
			if(messageContent.size() > 1){
				this.getFromString(messageContent.get(1)).execute(channel, author, guild, messageContent);
				return true;
			}else{
				help.execute(channel, author, guild, messageContent);
				return true;
			}
		}else{
			return false;
		}
	}
	
	public class CommandCommands extends Command{

		public CommandCommands(String commandString, String helpString) {
			super(commandString, helpString);
		}

		@Override
		public void execute(IChannel channel, IUser commandAuthor, IGuild guild, List<String> command) {
			List<Command> commands = Commands.commands;
			List<String> names = new ArrayList<String>();
			for(Command c : commands){
				if(!c.getIsHidden()) names.add(c.getCommandString());
			}
			channel.sendMessage("```" + Util.getCommaSeparatedFormattedList(names) + "```");
		}
	}
}
