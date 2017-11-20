package net.mizobogames.fhbgds;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class BigSausage {

	public static final String VERSION = "1.0";
	public static final String CHANGELOG = "Completely rewrote the entire bot from the ground up.";

	private static String TOKEN;
	public static final String PREFIX = "!bs";
	private static IDiscordClient client;
	public static Commands commands;

	public static void main(String[] args) throws IOException {
		commands = new Commands();
		TOKEN = Files.readAllLines(new File("TOKEN.token").toPath()).get(0);
		System.out.println("Logging in...");
		client = new ClientBuilder().withToken(TOKEN).withRecommendedShardCount().build();
		client.getDispatcher().registerListener(new BigSausage());
		client.login();
	}

	@EventSubscriber
	public void onGuildJoin(GuildCreateEvent event) {
		IGuild guild = event.getGuild();
		File guildDir = new File("guilds/" + guild.getStringID());
		if (!guildDir.exists()) {
			guildDir.mkdirs();
			SettingsManager.setupDefaultSettingsForGuild(guild);
			guild.getDefaultChannel().sendMessage("Hello, I am BigSausage! To enable me, please type `" + PREFIX + " enable` and for help, type `" + PREFIX + " help`");
		}
	}

	@EventSubscriber
	public void onReady(ReadyEvent event) {
		if (new File("DEBUG.token").exists()) {
			// client.changeUsername("BigSausage - Beta");
			client.changePlayingText("under maintinence");
		} else {
			client.changeUsername("BigSausage");
		}
		System.out.println("BigSausage is ready for mouths.");
	}

	@EventSubscriber
	public void onMessage(MessageReceivedEvent event) {
		SecureRandom rand = new SecureRandom();
		rand.setSeed(System.nanoTime());
		IMessage message = event.getMessage();
		IUser user = message.getAuthor();
		String[] words = message.getContent().split(" ");
		List<String> wordList = Arrays.asList(words);

		IChannel channel = message.getChannel();
		IGuild guild = message.getGuild();

		commands.findAndExecuteCommand(wordList, channel, user, guild);
	}
}
