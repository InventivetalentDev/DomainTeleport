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
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.inventivetalent.domainteleport.event.PlayerDomainPreTeleportEvent;
import org.inventivetalent.domainteleport.event.PlayerDomainTeleportEvent;

import java.util.Set;

public class LoginListener implements Listener {

	private DomainTeleport plugin;

	LoginListener(DomainTeleport plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onLogin(PlayerLoginEvent event) {
		if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
			final Player player = event.getPlayer();
			final String hostName = event.getHostname();

			if (plugin.DEBUG) {
				plugin.getLogger().info("Player " + player.getName() + "(" + player.getUniqueId().toString() + ") is logging in via '" + hostName + "'");
			}

			final boolean ignore;
			if (ignore = player.hasPermission("domainteleport.ignore")) {
				if (plugin.DEBUG) {
					plugin.getLogger().info("Ignoring " + player.getName() + "");
				}
			}

			final TeleportKey key = new TeleportKey(hostName);
			final TeleportValue value = plugin.teleportManager.getTeleportTarget(key);
			if (value != null && value.location != null) {
				PlayerDomainPreTeleportEvent preTeleportEvent = new PlayerDomainPreTeleportEvent(player, key.host + ":" + key.port, value.location, value.teleportOptions);
				Bukkit.getPluginManager().callEvent(preTeleportEvent);
				if (preTeleportEvent.isCancelled()) {
					event.setKickMessage(preTeleportEvent.getMessage());
					event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
					return;
				}

				Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
					@Override
					public void run() {
						if (player.isOnline()) {
							if (plugin.DEBUG) {
								plugin.getLogger().info("Teleporting " + player.getName() + " to " + value.location);
							}

							PlayerDomainTeleportEvent domainTeleportEvent = new PlayerDomainTeleportEvent(player, key.host + ":" + key.port, value.location, value.teleportOptions);
							domainTeleportEvent.setCancelled(ignore);

							Bukkit.getPluginManager().callEvent(domainTeleportEvent);

							if (!domainTeleportEvent.isCancelled()) { doTeleport(player, domainTeleportEvent.getLocation(), domainTeleportEvent.getTeleportOptions()); }
						}
					}
				}, plugin.TELEPORT_DELAY);
			}
		}
	}

	void doTeleport(Player player, Location location, Set<TeleportOption> options) {
		if (options.contains(TeleportOption.NO_TELEPORT)) {
			return;
		}
		player.teleport(location);
	}

}
