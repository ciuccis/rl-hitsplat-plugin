package org.ciuccis.hitsplat;

import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
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
import net.runelite.client.game.SpriteManager;
import net.runelite.client.game.SpriteOverride;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.ciuccis.hitsplat.overlays.HitsplatOverlay;
import org.ciuccis.hitsplat.utils.DamageType;
import org.ciuccis.hitsplat.utils.HitsplatManager;
import org.ciuccis.hitsplat.utils.ManagedHitsplat;
import org.ciuccis.hitsplat.utils.Sprites;

@Slf4j
@PluginDescriptor(
	name = "Typed Hitsplats"
)
public class HitsplatPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private HitsplatConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private HitsplatOverlay overlay;

	@Inject
	private SpriteManager spriteManager;
	private final SpriteOverride[] overrides = new SpriteOverride[Sprites.ALL_SPRITES.length];

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
		if (overrides[0] == null) {
			for (int i = 0; i < Sprites.ALL_SPRITES.length; i++) {
				int id = Sprites.ALL_SPRITES[i];
				overrides[i] = new SpriteOverride() {
					@Override
					public int getSpriteId() {
						return id;
					}

					@Override
					public String getFileName() {
						return "/com/hitsplats/blank.png";
					}
				};
			}
		}
		spriteManager.addSpriteOverrides(overrides);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		spriteManager.removeSpriteOverrides(overrides);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
	}
	@Subscribe
	public void onGameTick(GameTick tick) {
		int clientGameCycle = client.getGameCycle();
		if (HitsplatManager.hitsplatList.isEmpty()){
			return;
		}

		List<Actor> toRemoveActors = new ArrayList<>();
		List<ManagedHitsplat> toRemoveSplats = new ArrayList<>();
		for (Map.Entry<Actor, CopyOnWriteArrayList<ManagedHitsplat>> entry: HitsplatManager.hitsplatList.entrySet()) {
			Actor actor = entry.getKey();
			for (ManagedHitsplat managedHitsplat : entry.getValue()) {
				int disappear = managedHitsplat.hitsplat.getDisappearsOnGameCycle();
				if (clientGameCycle > disappear) {
					toRemoveSplats.add(managedHitsplat);
					HitsplatManager.releasePosition(actor, managedHitsplat.position);
				}
			}
			if (HitsplatManager.hitsplatList.get(actor).isEmpty()) {
				toRemoveActors.add(actor);
			}
			HitsplatManager.hitsplatList.get(actor).removeAll(toRemoveSplats);
		}

		for (Actor actor : toRemoveActors) {
			HitsplatManager.hitsplatList.remove(actor);
			HitsplatManager.takenPositions.remove(actor);
		}
	}

	@Subscribe
	public void onCommandExecuted(CommandExecuted commandExecuted) {
		if (commandExecuted.getCommand().equals("skullme")) {
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Skulled", null);
			client.getLocalPlayer().setSkullIcon(1);
		}

	}

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied hitsplatApplied) {
		Hitsplat hitsplat = hitsplatApplied.getHitsplat();
		Hitsplat override = null; // new Hitsplat(hitsplat.getHitsplatType(), 420 ,hitsplat.getDisappearsOnGameCycle());

		HitsplatManager.add(hitsplatApplied, DamageType.MAGIC, override);
	}


	@Provides
	HitsplatConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HitsplatConfig.class);
	}
}
