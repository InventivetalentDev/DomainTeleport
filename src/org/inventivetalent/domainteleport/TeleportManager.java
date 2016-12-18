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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TeleportManager {

	private DomainTeleport plugin;

	private HashMap<TeleportKey, TeleportValue> teleportMap = new HashMap<>();

	public TeleportManager(DomainTeleport plugin) {
		this.plugin = plugin;
	}

	public boolean registerTarget(TeleportKey key, TeleportValue value) {
		if (!teleportMap.containsKey(key)) {
			teleportMap.put(key, value);
			return true;
		}
		return false;
	}

	public boolean removeTarget(TeleportKey key) {
		if (teleportMap.containsKey(key)) {
			teleportMap.remove(key);
			return true;
		}
		return false;
	}

	public TeleportValue getTeleportTarget(TeleportKey key) {
		if (teleportMap.containsKey(key)) {
			return teleportMap.get(key);
		}
		return null;
	}

	public TeleportValue getTeleportTarget(String hostName) {
		return getTeleportTarget(new TeleportKey(hostName));
	}

	public int size() {
		return teleportMap.size();
	}

	public Set<Map.Entry<TeleportKey, TeleportValue>> entries() {
		return teleportMap.entrySet();
	}

	public JSONArray toJSON() {
		JSONArray array = new JSONArray();

		for (Map.Entry<TeleportKey, TeleportValue> entry : teleportMap.entrySet()) {
			final JSONObject key = entry.getKey().toJSON();
			final JSONObject value = entry.getValue().toJSON();

			array.put(new JSONObject() {
				{
					put("key", key);
					put("value", value);
				}
			});
		}

		return array;
	}

	public void loadJSON(JSONArray array) {
		for (int i = 0; i < array.length(); i++) {
			JSONObject current = array.getJSONObject(i);

			JSONObject key = current.getJSONObject("key");
			JSONObject value = current.getJSONObject("value");

			registerTarget(TeleportKey.fromJSON(key), TeleportValue.fromJSON(value));
		}
	}

}
