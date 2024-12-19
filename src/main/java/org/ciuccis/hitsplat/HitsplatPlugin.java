package org.ciuccis.hitsplat;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.AnimationID;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Hitsplat;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Hitsplat Recoloring"
)
public class HitsplatPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private HitsplatConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Example started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "baa", null);
		}
	}
	@Subscribe
	public void onGameTick(GameTick tick) {

	}

	@Subscribe
	public void onCommandExecuted(CommandExecuted commandExecuted) {
		if (commandExecuted.getCommand().equals("skullme")) {
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Plugin: " + String.valueOf(client.getLocalPlayer().getCombatLevel()), null);
			client.getLocalPlayer().setSkullIcon(1);
			int[] ids = client.getLocalPlayer().getPlayerComposition().getEquipmentIds();
			log.info(ids.toString());
		}

	}

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied hitsplatApplied) {
		Player player = client.getLocalPlayer();
		Actor actor = hitsplatApplied.getActor();
		Hitsplat hitsplat = hitsplatApplied.getHitsplat();
		final int npcId = ((NPC) actor).getId();

		Hitsplat splat = new Hitsplat(hitsplat.getHitsplatType(), 99 ,hitsplat.getDisappearsOnGameCycle());

		if (hitsplat.isMine())
		{
			int hit = hitsplat.getAmount();
			hitsplatApplied.setHitsplat(splat);
		}

	}


	@Provides
	HitsplatConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HitsplatConfig.class);
	}
}
