package org.ciuccis.hitsplat;

import java.awt.Color;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface HitsplatConfig extends net.runelite.client.config.Config
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

	@ConfigItem(
		position = 4,
		keyName = "meleeColorConfig",
		name = "Melee Hitsplat Color",
		description = "Color of Melee hitsplats"
	)
	default Color meleeColorConfig() { return Color.RED; }

	@ConfigItem(
		position = 4,
		keyName = "rangedColorConfig",
		name = "Ranged Hitsplat Color",
		description = "Color of Ranged hitsplats"
	)
	default Color rangedColorConfig() { return Color.GREEN; }

	@ConfigItem(
		position = 4,
		keyName = "magicColorConfig",
		name = "Magic Hitsplat Color",
		description = "Color of Magic hitsplats"
	)
	default Color colorConfig() { return Color.BLUE; }
}
