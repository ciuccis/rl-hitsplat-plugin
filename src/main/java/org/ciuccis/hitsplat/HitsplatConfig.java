package org.ciuccis.hitsplat;

import java.awt.Color;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("hitsplatColors")
public interface HitsplatConfig extends net.runelite.client.config.Config
{
	@ConfigItem(
		position = 1,
		keyName = "meleeColorConfig",
		name = "Melee Hitsplat Color",
		description = "Color of Melee hitsplats"
	)
	default Color meleeColorConfig() { return Color.RED; }

	@ConfigItem(
		position = 2,
		keyName = "rangedColorConfig",
		name = "Ranged Hitsplat Color",
		description = "Color of Ranged hitsplats"
	)
	default Color rangedColorConfig() { return Color.GREEN; }

	@ConfigItem(
		position = 3,
		keyName = "magicColorConfig",
		name = "Magic Hitsplat Color",
		description = "Color of Magic hitsplats"
	)
	default Color colorConfig() { return Color.BLUE; }
}
