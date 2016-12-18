/*
 * Copyright 2015-2016 inventivetalent. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and contributors and should not be interpreted as representing official policies,
 *  either expressed or implied, of anybody else.
 */

package org.inventivetalent.domainteleport;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONArray;
import org.mcstats.MetricsLite;

import java.io.*;

public class DomainTeleport extends JavaPlugin {

	public static DomainTeleport instance;

	public File dataFile = new File(getDataFolder(), "data.json");

	public TeleportManager teleportManager = new TeleportManager(this);

	public long    TELEPORT_DELAY = 20;
	public boolean DEBUG          = false;
	public boolean BUNGEECORD     = false;
	public boolean IGNORE_CASE    = false;

	@Override
	public void onEnable() {
		instance = this;
		Bukkit.getPluginManager().registerEvents(new LoginListener(this), this);

		PluginCommand command = getCommand("domainteleport");
		CommandHandler commandHandler;
		command.setExecutor(commandHandler = new CommandHandler(this));
		command.setTabCompleter(commandHandler);

		saveDefaultConfig();
		TELEPORT_DELAY = getConfig().getLong("teleportDelay");
		DEBUG = getConfig().getBoolean("debug");
		BUNGEECORD = getConfig().getBoolean("bungeecord");
		IGNORE_CASE = getConfig().getBoolean("ignoreCase", false);

		if (!dataFile.exists()) {
			if (!dataFile.getParentFile().exists()) {
				dataFile.getParentFile().mkdirs();
			}

			try {
				dataFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				getLogger().info("Loading data...");
				loadData();
				getLogger().info("Loaded " + teleportManager.size() + " teleport values");
			}
		}, 20);

		try {
			MetricsLite metrics = new MetricsLite(this);
			if (metrics.start()) {
				getLogger().info("Metrics started");
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void onDisable() {
		saveData();
	}

	void saveData() {
		JSONArray data = teleportManager.toJSON();

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));
			writer.write(data.toString(2));
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void loadData() {
		String content = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(dataFile));
			String line;
			while ((line = reader.readLine()) != null) {
				content += line;
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (content.length() > 2 && content.startsWith("[") && content.endsWith("]")) {
			JSONArray data = new JSONArray(content);
			teleportManager.loadJSON(data);
		}
	}

}
