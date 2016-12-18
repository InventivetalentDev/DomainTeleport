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

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandHandler implements CommandExecutor, TabCompleter {

	private DomainTeleport plugin;

	public CommandHandler(DomainTeleport plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if (args.length == 0) {
			if (sender.hasPermission("domainteleport.create")) {
				sender.sendMessage("§a/dtp create <host>[:port]");
			}
			if (sender.hasPermission("domainteleport.remove")) {
				sender.sendMessage("§a/dtp remove <host>[:port]");
			}
			if (sender.hasPermission("domainteleport.list")) {
				sender.sendMessage("§a/dtp list");
			}
			return false;
		}

		if ("create".equalsIgnoreCase(args[0])) {
			if (!sender.hasPermission("domainteleport.create")) {
				sender.sendMessage("§cNo permission");
				return false;
			}
			if (args.length < 2) {
				sender.sendMessage("§c/dtp create <host>[:port]");
				return false;
			}
			if (sender instanceof Player) {
				TeleportKey key = new TeleportKey(args[1]);
				TeleportValue value = new TeleportValue(((Player) sender).getLocation());

				if (plugin.teleportManager.registerTarget(key, value)) {
					sender.sendMessage("§aRegistered teleport for '" + args[1] + "' to " + LocationToString(value.location));
					return true;
				} else {
					sender.sendMessage("§cCould not register teleport (already registered)");
					return false;
				}
			} else {
				sender.sendMessage("§cYou must be a player to create");
				return false;
			}
		}

		if ("remove".equalsIgnoreCase(args[0])) {
			if (!sender.hasPermission("domainteleport.remove")) {
				sender.sendMessage("§cNo permission");
				return false;
			}
			if (args.length < 2) {
				sender.sendMessage("§c/dtp remove <host>[:port]");
				return false;
			}
			TeleportKey key = new TeleportKey(args[1]);
			if (plugin.teleportManager.removeTarget(key)) {
				sender.sendMessage("§aRemoved teleport for " + args[1]);
				return true;
			} else {
				sender.sendMessage("§cCould not remove teleport");
				return false;
			}
		}

		if ("list".equalsIgnoreCase(args[0])) {
			if (!sender.hasPermission("domainteleport.list")) {
				sender.sendMessage("§cNo permission");
				return false;
			}
			sender.sendMessage("§aRegistered domains:");
			for (Map.Entry<TeleportKey, TeleportValue> entry : plugin.teleportManager.entries()) {
				TeleportKey key = entry.getKey();
				TeleportValue value = entry.getValue();

				sender.sendMessage("§a> '§7" + key.host + ":" + key.port + "§a' teleports to §7" + LocationToString(value.location));
			}
			if (plugin.teleportManager.size() == 0) {
				sender.sendMessage("§cNone");
			}
			return true;
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
		List<String> list = new ArrayList<>();

		if (args.length == 1) {
			if (sender.hasPermission("domainteleport.create")) {
				list.add("create");
			}
			if (sender.hasPermission("domainteleport.remove")) {
				list.add("remove");
			}
			if (sender.hasPermission("domainteleport.list")) {
				list.add("list");
			}
		}

		if (args.length == 2) {
			if ("remove".equalsIgnoreCase(args[0])) {
				for (Map.Entry<TeleportKey, TeleportValue> entry : plugin.teleportManager.entries()) {
					list.add(entry.getKey().host + ":" + entry.getKey().port);
				}
			}
		}

		return TabCompletionHelper.getPossibleCompletionsForGivenArgs(args, list.toArray(new String[list.size()]));
	}

	String LocationToString(Location location) {
		return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ();
	}
}
