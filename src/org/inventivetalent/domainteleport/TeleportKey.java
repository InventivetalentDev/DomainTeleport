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
import org.json.JSONObject;

public class TeleportKey {

	public final String host;
	public final int    port;

	public TeleportKey(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public TeleportKey(String address) {
		if (!address.contains(":")) {
			this.host = address;
			this.port = Bukkit.getPort();
		} else {
			String[] split = address.split(":");
			if (split.length < 2) { throw new IllegalArgumentException("Invalid address"); }
			if (DomainTeleport.instance.BUNGEECORD || split.length > 2) {//Bungeecord uses '<host> <origin> <player uuid> <player textures>:<port>' instead of '<host>:<port>' (the textures usually contain multiple ':')
				this.port = Integer.parseInt(split[split.length - 1]);
				String split0 = split[0];
				String[] split1 = split0.split("\0");

				if (DomainTeleport.instance.IGNORE_CASE) { split1[0] = split1[0].toLowerCase(); }

				this.host = split1[0];

				if (!DomainTeleport.instance.BUNGEECORD) {
					DomainTeleport.instance.getLogger().warning("This server seems to be running behind a Bungeecord proxy, but 'bungeecord' is set to 'false' in the configuration.");
					DomainTeleport.instance.getLogger().warning("Please enable 'bungeecord' in your configuration.");
				}
				if (DomainTeleport.instance.DEBUG) {
					DomainTeleport.instance.getLogger().info("Actual address: " + host + ":" + port);
				}
			} else {
				if (DomainTeleport.instance.IGNORE_CASE) { split[0] = split[0].toLowerCase(); }

				this.host = split[0];
				this.port = Integer.valueOf(split[1]);
			}
		}
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("host", host);
		json.put("port", port);

		return json;
	}

	public static TeleportKey fromJSON(JSONObject json) {
		String host = json.getString("host");
		int port = json.getInt("port");

		return new TeleportKey(host, port);
	}

	@Override
	public String toString() {
		return "TeleportKey{" +
				"host='" + host + '\'' +
				", port=" + port +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }

		TeleportKey that = (TeleportKey) o;

		if (port != that.port) { return false; }
		return !(host != null ? !host.equals(that.host) : that.host != null);

	}

	@Override
	public int hashCode() {
		int result = host != null ? host.hashCode() : 0;
		result = 31 * result + port;
		return result;
	}
}