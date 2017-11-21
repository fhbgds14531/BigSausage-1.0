package net.mizobogames.fhbgds.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import net.mizobogames.fhbgds.BigSausage;
import net.mizobogames.fhbgds.Command;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class CommandUpdate extends Command {

	public CommandUpdate(String commandString, String helpString) {
		super(commandString, true, helpString);
	}

	@Override
	public void execute(IChannel channel, IUser commandAuthor, IGuild guild, List<String> command, IMessage message) {
		if (commandAuthor.getStringID().contentEquals(BigSausage.ME)) {
			if (command.size() == 2) { // Regular update (only update if the version number doesn't match)
				try {
					URL url = new URL("https://github.com/fhbgds14531/BigSausage/blob/master/newVersion.txt?raw=true");
					File version = new File("newVersion.txt");
					download(url, version);
					String newVersionString = Files.readAllLines(version.toPath()).get(0);
					if (!newVersionString.contentEquals(BigSausage.VERSION)) {
						doUpdate(newVersionString, channel);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (command.size() == 3 && command.get(2).toLowerCase().contentEquals("force")) { // force update
				try {
					URL url = new URL("https://github.com/fhbgds14531/BigSausage/blob/master/newVersion.txt?raw=true");
					File version = new File("newVersion.txt");
					download(url, version);
					String newVersionString = Files.readAllLines(version.toPath()).get(0);
					doUpdate(newVersionString, channel);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (command.size() == 4 && command.get(2).toLowerCase().contentEquals("force")) { // force update to a specific version
				try {
					String newVersionString = command.get(3);
					List<String> content = new ArrayList<String>();
					URL update = new URL("https://github.com/fhbgds14531/BigSausage/blob/master/jar/" + newVersionString + "/BigSausage.jar");
					InputStream in = update.openStream(); // throws an IOException
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					String line;
					while ((line = reader.readLine()) != null) {
						content.add(line);
					}
					if (content.contains("    <title>Page not found &middot; GitHub</title>")) { // if this is true, the supplied version number is invalid.
						channel.sendMessage("The supplied verison string is invalid. I can't update to a version that doesn't exist!");
						return;
					} else {
						doUpdate(newVersionString, channel);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void doUpdate(String newVersionString, IChannel outputChannel) throws Exception {
		URL update = new URL("https://github.com/fhbgds14531/BigSausage/blob/master/jar/" + newVersionString + "/BigSausage.jar?raw=true");
		URLConnection c = update.openConnection();
		InputStream in = c.getInputStream();

		FileOutputStream out = new FileOutputStream(new File("BigSausage_1.jar"));
		int n = -1;
		byte[] buffer = new byte[2048];
		while ((n = in.read(buffer)) != -1) {
			if (n > 0) {
				out.write(buffer, 0, n);
			}
		}
		in.close();
		out.close();
		BigSausage.tryRestartForUpdate(outputChannel);
	}

	public void download(URL url, File location) {
		try {
			URLConnection c = url.openConnection();
			InputStream in = c.getInputStream();

			FileOutputStream out = new FileOutputStream(location);
			int n = -1;
			byte[] buffer = new byte[2048];
			while ((n = in.read(buffer)) != -1) {
				if (n > 0) {
					out.write(buffer, 0, n);
				}
			}
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
