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

package org.inventivetalent.domainteleport.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.inventivetalent.domainteleport.TeleportOption;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Event called when a player connects via a custom domain
 */
public class PlayerDomainPreTeleportEvent extends Event implements Cancellable {

	private final Player   player;
	private final String   address;
	private       Location location;
	private Set<TeleportOption> teleportOptions = new HashSet<>();

	private boolean cancelled = false;
	private String  message   = "§cYou are not allowed to connect via this domain.";

	public PlayerDomainPreTeleportEvent(Player player, String address, Location location, Collection<TeleportOption> options) {
		this.player = player;
		this.address = address;
		this.location = location;
		if (options != null) { this.teleportOptions.addAll(options); }
	}

	/**
	 * @return The connecting {@link Player}
	 */
	@Nonnull
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return The address the player used to connect (&lt;host&gt;:&lt;port&gt;)
	 */
	@Nonnull
	public String getAddress() {
		return address;
	}

	/**
	 * @return the current target {@link Location} for the {@link Player}
	 */
	@Nonnull
	public Location getLocation() {
		return location;
	}

	/**
	 * Changes the target {@link Location} for the {@link Player}
	 *
	 * @param location new target {@link Location}
	 */
	public void setLocation(@Nonnull Location location) {
		if (location == null) { throw new IllegalArgumentException("location cannot be null"); }
		this.location = location;
	}

	/**
	 * @return the {@link TeleportOption}s, can be modified using {@link Collection#add(Object)}, {@link Collection#remove(Object)}, ...
	 */
	public Set<TeleportOption> getTeleportOptions() {
		return teleportOptions;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * Cancels the connection of the player
	 */
	@Override
	public void setCancelled(boolean b) {
		cancelled = b;
	}

	/**
	 * @return The current kick-message
	 */
	@Nonnull
	public String getMessage() {
		return message;
	}

	/**
	 * Changes the kick-message sent to the player if the event is cancelled
	 *
	 * @param message
	 */
	public void setMessage(@Nonnull String message) {
		if (message == null) { throw new IllegalArgumentException("message cannot be null"); }
		this.message = message;
	}

	static HandlerList handlerList = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}

	public static HandlerList getHandlerList() {
		return handlerList;
	}

}
