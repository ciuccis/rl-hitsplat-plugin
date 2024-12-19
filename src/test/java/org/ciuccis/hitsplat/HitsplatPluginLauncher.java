package org.ciuccis.hitsplat;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class HitsplatPluginLauncher
{
	public static void main(String[] args) throws Exception
	{
		assert(true);
		ExternalPluginManager.loadBuiltin(HitsplatPlugin.class);
		RuneLite.main(args);
	}
}