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
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to help with the TabCompletion for Bukkit.
 *
 * @author D4rKDeagle
 */

class TabCompletionHelper {

	public static List<String> getPossibleCompletionsForGivenArgs(String[] args, String[] possibilitiesOfCompletion) {
		final String argumentToFindCompletionFor = args[args.length - 1];

		final List<String> listOfPossibleCompletions = new ArrayList<>();
		for (int i = 0; i < possibilitiesOfCompletion.length; i++) {
			final String[] foundString = possibilitiesOfCompletion;
			try {
				if (foundString[i] != null && foundString[i].regionMatches(true, 0, argumentToFindCompletionFor, 0, argumentToFindCompletionFor.length())) {
					listOfPossibleCompletions.add(foundString[i]);
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		Collections.sort(listOfPossibleCompletions);

		return listOfPossibleCompletions;
	}

	public static List<String> getChatColors() {
		List<String> names = new ArrayList<>();
		for (ChatColor cc : ChatColor.values()) {
			if (!cc.isFormat() && cc != ChatColor.RESET) {
				names.add(cc.name());
			}
		}
		return names;
	}

	public static List<String> getDyeColors() {
		List<String> names = new ArrayList<>();
		for (DyeColor cc : DyeColor.values()) {
			names.add(cc.name());
		}
		return names;
	}

	public static List<String> getOnlinePlayerNames() {
		List<String> list = new ArrayList<>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			list.add(p.getName());
		}
		return list;
	}

}
