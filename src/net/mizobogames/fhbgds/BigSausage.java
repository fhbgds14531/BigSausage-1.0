package net.mizobogames.fhbgds;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.mizobogames.fhbgds.commands.CommandAddFile;
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
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.audio.AudioPlayer;
import sx.blah.discord.util.audio.AudioPlayer.Track;
import sx.blah.discord.util.audio.events.TrackFinishEvent;

public class BigSausage {

	public static final String VERSION = "1.0";
	public static final String CHANGELOG = "Completely rewrote the entire bot from the ground up.";
	public static final String ME = "198575970624471040";

	private static String TOKEN;
	public static final String PREFIX = "!bs";
	public static IDiscordClient client;
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
	public void onGuildJoin(GuildCreateEvent event) throws IOException {
		IGuild guild = event.getGuild();
		final String guildLocString = "guilds/" + guild.getStringID();
		File guildDir = new File(guildLocString);
		if (!guildDir.exists()) {
			guildDir.mkdirs();
			SettingsManager.setupDefaultSettingsForGuild(guild);
			guild.getDefaultChannel().sendMessage("Hello, I am BigSausage! To enable me, please type `" + PREFIX + " enable` and for help, type `" + PREFIX + " help`");
		}
		File ttsFile = new File(guildLocString + "/tts.txt");
		if (!ttsFile.exists()) {
			ttsFile.createNewFile();
			System.out.println("Created new tts file for guild \"" + guild.getName() + "\"");
		}
		File filesDir = new File(guildLocString + "/files");
		if (!filesDir.exists()) {
			filesDir.mkdirs();
			System.out.println("Created files directory for guild \"" + guild.getStringID() + "\"");
		}
	}

	@EventSubscriber
	public void onReady(ReadyEvent event) {
		if (new File("DEBUG.token").exists() && client.getApplicationName().contentEquals("BigSausage")) {
			client.changeUsername("BigSausage - Beta");
			client.changePlayingText("under maintinence");
		} else if (client.getApplicationName().contentEquals("BigSausage - Beta")) {
			client.changeUsername("BigSausage");
		}
		System.out.println("BigSausage is ready for mouths.");
	}

	public static void shutdown() {
		client.logout();
	}

	@EventSubscriber
	public void onMessage(MessageReceivedEvent event) throws FileNotFoundException {
		SecureRandom rand = new SecureRandom();
		rand.setSeed(System.nanoTime());
		IMessage message = event.getMessage();
		IUser user = message.getAuthor();
		String[] words = message.getContent().split(" ");
		List<String> wordList = Arrays.asList(words);

		IChannel channel = message.getChannel();
		IGuild guild = message.getGuild();

		if (!commands.findAndExecuteCommand(wordList, channel, user, guild, message)) {
			if ((boolean) SettingsManager.getSettingForGuild(guild, "enabled")) {
				File indexDir = new File("guilds/" + guild.getStringID() + "/files/indices");
				File audioFileIndex = new File("guilds/" + guild.getStringID() + "/files/indices/audioIndex.json");
				File imageFileIndex = new File("guilds/" + guild.getStringID() + "/files/indices/imageIndex.json");

				if (indexDir.exists()) {
					if (audioFileIndex.exists()) {
						JSONObject audioIndex = CommandAddFile.getIndexFileContents(guild, audioFileIndex);
						JSONArray ja = (JSONArray) audioIndex.get("index");
						List<String> indexStrings = new ArrayList<String>();
						ja.forEach(s -> indexStrings.add(String.valueOf(s)));
						for (String clipName : indexStrings) {
							JSONArray triggers = (JSONArray) audioIndex.get(clipName);
							List<String> triggerStrings = new ArrayList<String>();
							triggers.forEach(object -> triggerStrings.add(String.valueOf(object)));
							for (String trigger : triggerStrings) {
								for (String word : wordList) {
									if (word.toLowerCase().contains(trigger)) {
										for (IVoiceChannel vChannel : guild.getVoiceChannels()) {
											if (vChannel.getConnectedUsers().contains(user)) {
												String filename = (String) audioIndex.get(clipName + "_name");
												File file = new File("guilds/" + guild.getStringID() + "/files/" + filename);
												this.queueFile(file, guild, vChannel, user, false);
											}
										}
									}
								}
							}
						}
					}
					if (imageFileIndex.exists()) {
						JSONObject imageIndex = CommandAddFile.getIndexFileContents(guild, imageFileIndex);
						JSONArray ja = (JSONArray) imageIndex.get("index");
						List<String> indexStrings = new ArrayList<String>();
						ja.forEach(s -> indexStrings.add(String.valueOf(s)));
						for (String imageName : indexStrings) {
							JSONArray triggers = (JSONArray) imageIndex.get(imageName);
							List<String> triggerStrings = new ArrayList<String>();
							triggers.forEach(object -> triggerStrings.add(String.valueOf(object)));
							for (String trigger : triggerStrings) {
								for (String word : wordList) {
									if (word.toLowerCase().contains(trigger)) {
										String filename = (String) imageIndex.get(imageName + "_name");
										File file = new File("guilds/" + guild.getStringID() + "/files/" + filename);
										channel.sendFile(file);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@EventSubscriber
	public void onTrackFinish(TrackFinishEvent event) throws RateLimitException, DiscordException, MissingPermissionsException {
		Optional<Track> newT = event.getNewTrack();
		if (!newT.isPresent()) {
			event.getPlayer().getGuild().getConnectedVoiceChannel().leave();
		} else {
		}
	}

	private void join(IVoiceChannel channel, IUser user, boolean commanded) throws RateLimitException, DiscordException, MissingPermissionsException {
		if (channel == null || user == null) {
			return;
		} else if (user.getVoiceStateForGuild(channel.getGuild()) == null) {
			return;
		} else if (user.getVoiceStateForGuild(channel.getGuild()).getChannel() == null) {
			channel.join();
		} else if (user.getVoiceStateForGuild(channel.getGuild()).getChannel().getConnectedUsers().contains(user) || commanded) {
			IVoiceChannel voice;
			if (!commanded) {
				voice = user.getVoiceStateForGuild(channel.getGuild()).getChannel();
			} else {
				voice = channel;
			}
			if (!voice.getModifiedPermissions(client.getOurUser()).contains(Permissions.VOICE_CONNECT)) {
			} else {
				voice.join();
			}
		}
	}

	private AudioPlayer getPlayer(IGuild guild) {
		return AudioPlayer.getAudioPlayerForGuild(guild);
	}

	private void queueFile(File f, IGuild guild, IVoiceChannel channelToJoin, IUser triggerUser, boolean commanded) {
		int queueLength = 0;
		try {
			AudioPlayer player = getPlayer(guild);
			queueLength = player.getPlaylist().size();
			if (queueLength < (long) SettingsManager.getSettingForGuild(guild, "max_clips_per_message")) {
				player.setLoop(false);
				player.setPaused(false);
				if (!channelToJoin.isConnected()) join(channelToJoin, triggerUser, commanded);
				player.queue(f);
				player.setVolume(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
