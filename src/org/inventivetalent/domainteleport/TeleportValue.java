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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class TeleportValue {

	public final Location location;
	public final Set<TeleportOption> teleportOptions = new HashSet<>();

	public TeleportValue(Location location) {
		this.location = location;
	}

	public void addOption(TeleportOption option) {
		this.teleportOptions.add(option);
	}

	public void removeOption(TeleportOption option) {
		this.teleportOptions.remove(option);
	}

	public Set<TeleportOption> getOptions() {
		return teleportOptions;
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();

		json.put("location", new JSONObject() {
			{
				put("world", location.getWorld().getName());
				put("x", location.getX());
				put("y", location.getY());
				put("z", location.getZ());
				put("yaw", location.getYaw());
				put("pitch", location.getPitch());
			}
		});
		if (!teleportOptions.isEmpty()) {
			json.put("options", new JSONArray() {
				{
					for (TeleportOption option : teleportOptions) {
						put(option.name());
					}
				}
			});
		}

		return json;
	}

	public static TeleportValue fromJSON(JSONObject json) {
		JSONObject jsonLocation = json.getJSONObject("location");
		String world = jsonLocation.getString("world");
		double x = jsonLocation.getDouble("x");
		double y = jsonLocation.getDouble("y");
		double z = jsonLocation.getDouble("z");
		float yaw = (float) jsonLocation.getDouble("yaw");
		float pitch = (float) jsonLocation.getDouble("pitch");

		Location location = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
		TeleportValue value = new TeleportValue(location);

		if (json.has("options")) {
			JSONArray optionArray = json.getJSONArray("options");
			for (int i = 0; i < optionArray.length(); i++) {
				value.addOption(TeleportOption.valueOf(optionArray.getString(i).toUpperCase()));
			}
		}

		return value;
	}

	@Override
	public String toString() {
		return "TeleportValue{" +
				"location=" + location +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }

		TeleportValue that = (TeleportValue) o;

		return !(location != null ? !location.equals(that.location) : that.location != null);

	}

	@Override
	public int hashCode() {
		return location != null ? location.hashCode() : 0;
	}
}