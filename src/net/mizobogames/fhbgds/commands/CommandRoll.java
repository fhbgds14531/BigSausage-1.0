package net.mizobogames.fhbgds.commands;

import java.util.List;

import net.mizobogames.fhbgds.BigSausage;
import net.mizobogames.fhbgds.Command;
import net.mizobogames.fhbgds.Dice;
import net.mizobogames.fhbgds.Util;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class CommandRoll extends Command{

	public CommandRoll(String commandString, String helpString) {
		super(commandString, helpString);
	}

	@Override
	public void execute(IChannel channel, IUser commandAuthor, IGuild guild, List<String> command, IMessage message) {
		if(command.size() < 3){
			channel.sendMessage("Incorrect number of arguments. Use `" + BigSausage.PREFIX + " help " + getCommandString() + "` for usage");
			return;
		}
		Dice d = Dice.getDice(command.get(2));
		List<Integer> roll = d.rollAndReturnListOfResults();
		int result = 0;
		for(int i : roll){
			result += i;
		}
		String rolls = Util.getCommaSeparatedString(roll);
		if(rolls.length() > 40){
			rolls = rolls.substring(0, 40);
			rolls = rolls.substring(0, rolls.lastIndexOf(", "));
			rolls += "...";
		}
		channel.sendMessage("`" + command.get(2) + "`  = " + result + "  `(" + rolls + ")`");
	}

}
