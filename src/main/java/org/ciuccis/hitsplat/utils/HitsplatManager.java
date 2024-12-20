package org.ciuccis.hitsplat.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import net.runelite.api.Actor;
import net.runelite.api.Hitsplat;
import net.runelite.api.events.HitsplatApplied;

public class HitsplatManager {
	public static ConcurrentHashMap<Actor, CopyOnWriteArrayList<ManagedHitsplat>> hitsplatList = new ConcurrentHashMap<Actor, CopyOnWriteArrayList<ManagedHitsplat>>();
	public static Map<Actor, HashSet<Integer>> takenPositions = new HashMap<>();

	public static int getLowestAvailablePosition(Actor actor) {
		HashSet<Integer> set = takenPositions.get(actor);
		for (int i = 0; i <= 263; i++) {
			if (!set.contains(i))
				return i;
		}
		return 0;
	}

	public static void takePosition(Actor actor, int position) {
		takenPositions.get(actor).add(position);
	}

	public static void releasePosition(Actor actor, int position) {
		takenPositions.get(actor).remove(position);
	}


	public static void add(HitsplatApplied hitsplatApplied, DamageType damageType, Hitsplat override)
	{
		Actor actor = hitsplatApplied.getActor();
		Hitsplat hitsplat;
		if (override != null) {
			hitsplat = override;
		} else {
			hitsplat = hitsplatApplied.getHitsplat();
		}

		int position = 0;
		if (takenPositions.containsKey(actor)) {
			position = getLowestAvailablePosition(actor);
		} else {
			takenPositions.put(actor, new HashSet<Integer>());
		}
		takePosition(actor, position);

		if (!hitsplatList.containsKey(actor)) {
			hitsplatList.put(actor, new CopyOnWriteArrayList<ManagedHitsplat>());
		}

		ManagedHitsplat managedHitsplat = new ManagedHitsplat(hitsplat, position, damageType);
		hitsplatList.get(actor).add(managedHitsplat);
	}


}
