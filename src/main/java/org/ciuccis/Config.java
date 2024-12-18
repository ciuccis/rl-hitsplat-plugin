package org.ciuccis;

import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface Config extends net.runelite.client.config.Config
{
	@ConfigItem(
		keyName = "asdf",
		name = "Beep Beep",
		description = "I'm a sheep"
	)
	default String greeting()
	{
		return "Boop Boop";
	}
}
