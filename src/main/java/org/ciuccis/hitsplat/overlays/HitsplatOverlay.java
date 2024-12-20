package org.ciuccis.hitsplat.overlays;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.inject.Inject;
import javax.swing.ImageIcon;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.Hitsplat;
import net.runelite.api.HitsplatID;
import net.runelite.api.Point;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import org.ciuccis.hitsplat.utils.DamageType;
import org.ciuccis.hitsplat.utils.HitsplatCoordinates;
import org.ciuccis.hitsplat.utils.HitsplatManager;
import org.ciuccis.hitsplat.utils.Icons;
import org.ciuccis.hitsplat.utils.ManagedHitsplat;

public class HitsplatOverlay extends Overlay
{
	@Inject
	private HitsplatOverlay(Client client)
	{
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.UNDER_WIDGETS);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (HitsplatManager.hitsplatList.isEmpty()){
			return null;
		}

		for (Map.Entry<Actor, CopyOnWriteArrayList<ManagedHitsplat>> entry : HitsplatManager.hitsplatList.entrySet()){
			Actor actor = entry.getKey();
			CopyOnWriteArrayList<ManagedHitsplat> hitsplats = entry.getValue();
			drawHitsplats(graphics, actor, hitsplats);
		}
		return null;
	}




	private void drawHitsplats(Graphics2D graphics, Actor actor, List<ManagedHitsplat> hitsplats){
		// normal hitsplat construction
		int missOffset = 0;
		for (ManagedHitsplat managedHitsplat : hitsplats) {
			int position = managedHitsplat.position;
			Hitsplat hitsplat = managedHitsplat.hitsplat;
			int damage = hitsplat.getAmount();
			int hitsplatType = hitsplat.getHitsplatType();
			DamageType damageType = managedHitsplat.damageType;


			BufferedImage hitsplatImage = drawHitsplat(hitsplatType, damage, FontManager.getRunescapeSmallFont(), position, damageType);
			Point cPoint = actor.getCanvasImageLocation(hitsplatImage, actor.getLogicalHeight()/2);

			if (cPoint == null){
				continue;
			}

			Point p = new Point(cPoint.getX()+1, cPoint.getY()-1);
			Point k = HitsplatCoordinates.hitsplatCoordinates.get(position);
			OverlayUtil.renderImageLocation(graphics, new Point(p.getX()+k.getX(), p.getY()+k.getY()), hitsplatImage);
		}
	}

	private BufferedImage drawHitsplat(int hitsplatType, int damage, Font font, int position, DamageType damageType){
		ImageIcon hitIcon;
		switch (damageType) {
			case MELEE:
				hitIcon = Icons.CUSTOM_MELEE_HITSPLAT;
				break;
			case RANGED:
				hitIcon = Icons.CUSTOM_RANGED_HITSPLAT;
				break;
			case MAGIC:
				hitIcon = Icons.CUSTOM_MAGIC_HITSPLAT;
				break;
			case TYPELESS:
				hitIcon = Icons.OSRS_SELF_DAMAGE_HITSPLAT;
				break;
			default:
				return new BufferedImage(0,0,0);
		}
//		switch (hitsplatType) {
//			case HitsplatID.BLEED:
//				hitIcon = Icons.OSRS_BLEED_HITSPLAT;
//				break;
//			case HitsplatID.BURN:
//				hitIcon = Icons.OSRS_BURN_HITSPLAT;
//				break;
//			case HitsplatID.BLOCK_ME:
//				hitIcon = Icons.OSRS_SELF_MISS_HITSPLAT;
//				break;
//			case HitsplatID.BLOCK_OTHER:
//				hitIcon = Icons.OSRS_OTHER_MISS_HITSPLAT;
//				break;
//			case HitsplatID.CORRUPTION:
//				hitIcon = Icons.OSRS_CORRUPTION_HITSPLAT;
//				break;
//			case HitsplatID.CYAN_DOWN:
//				hitIcon = Icons.OSRS_ALT_UNCHARGE_HITSPLAT;
//				break;
//			case HitsplatID.CYAN_UP:
//				hitIcon = Icons.OSRS_ALT_CHARGE_HITSPLAT;
//				break;
//			case HitsplatID.DAMAGE_MAX_ME:
//				hitIcon = Icons.OSRS_MAX_HITSPLAT;
//				break;
//			case HitsplatID.DAMAGE_MAX_ME_CYAN:
//				hitIcon = Icons.OSRS_MAX_SHIELD_HITSPLAT;
//				break;
//			case HitsplatID.DAMAGE_MAX_ME_ORANGE:
//				hitIcon = Icons.OSRS_MAX_ARMOUR_HITSPLAT;
//				break;
//			case HitsplatID.DAMAGE_MAX_ME_POISE:
//				hitIcon = Icons.OSRS_MAX_POISE_HITSPLAT;
//				break;
//			case HitsplatID.DAMAGE_MAX_ME_WHITE:
//				hitIcon = Icons.OSRS_MAX_UNCHARGE_HITSPLAT;
//				break;
//			case HitsplatID.DAMAGE_MAX_ME_YELLOW:
//				hitIcon = Icons.OSRS_MAX_CHARGE_HITSPLAT;
//				break;
//			case HitsplatID.DAMAGE_ME:
//				hitIcon = Icons.OSRS_SELF_DAMAGE_HITSPLAT;
//				break;
//			case HitsplatID.DAMAGE_ME_CYAN:
//				hitIcon = Icons.OSRS_SELF_SHIELD_HITSPLAT;
//				break;
//			case HitsplatID.DAMAGE_ME_ORANGE:
//				hitIcon = Icons.OSRS_SELF_ARMOUR_HITSPLAT;
//				break;
//			case HitsplatID.DAMAGE_ME_POISE:
//				hitIcon = Icons.OSRS_SELF_POISE_HITSPLAT;
//				break;
//			case HitsplatID.DAMAGE_ME_WHITE:
//				hitIcon = Icons.OSRS_SELF_UNCHARGE_HITSPLAT;
//				break;
//			case HitsplatID.DAMAGE_ME_YELLOW:
//				hitIcon = Icons.OSRS_SELF_CHARGE_HITSPLAT;
//				break;
//			case HitsplatID.DAMAGE_OTHER:
//				hitIcon = Icons.OSRS_OTHER_DAMAGE_HITSPLAT;
//				break;
//			case HitsplatID.DAMAGE_OTHER_CYAN:
//				hitIcon = Icons.OSRS_OTHER_SHIELD_HITSPLAT;
//				break;
//			case HitsplatID.DAMAGE_OTHER_ORANGE:
//				hitIcon = Icons.OSRS_OTHER_ARMOUR_HITSPLAT;
//				break;
//			case HitsplatID.DAMAGE_OTHER_POISE:
//				hitIcon = Icons.OSRS_OTHER_POISE_HITSPLAT;
//				break;
//			// Does not exist, defaulting to self for future support
//			case HitsplatID.DAMAGE_OTHER_WHITE:
//				hitIcon = Icons.OSRS_SELF_UNCHARGE_HITSPLAT;
//				break;
//			case HitsplatID.DAMAGE_OTHER_YELLOW:
//				hitIcon = Icons.OSRS_OTHER_CHARGE_HITSPLAT;
//				break;
//			case HitsplatID.DISEASE:
//				hitIcon = Icons.OSRS_DISEASE_HITSPLAT;
//				break;
//			case HitsplatID.DOOM:
//				hitIcon = Icons.OSRS_DOOM_HITSPLAT;
//				break;
//			case HitsplatID.HEAL:
//				hitIcon = Icons.OSRS_HEAL_HITSPLAT;
//				break;
//			case HitsplatID.POISON:
//				hitIcon = Icons.OSRS_POISON_HITSPLAT;
//				break;
//			case HitsplatID.PRAYER_DRAIN:
//				hitIcon = Icons.OSRS_PRAYER_DRAIN_HITSPLAT;
//				break;
//			case HitsplatID.SANITY_DRAIN:
//				hitIcon = Icons.OSRS_SANITY_DRAIN_HITSPLAT;
//				break;
//			case HitsplatID.SANITY_RESTORE:
//				hitIcon = Icons.OSRS_SANITY_RESTORE_HITSPLAT;
//				break;
//			case HitsplatID.VENOM:
//				hitIcon = Icons.OSRS_VENOM_HITSPLAT;
//				break;
//			case -1:
//				hitIcon = Icons.OSRS_BIG_HITSPLAT;
//				break;
//			default:
//				return new BufferedImage(0,0,0);
//		}
		BufferedImage bi = iconToBuffered(hitIcon);
		Graphics g = bi.getGraphics();
		bi = drawCenteredDamageNumbers(g, String.valueOf(damage), bi, font);
		g.dispose();
		return bi;
	}

	public BufferedImage drawCenteredDamageNumbers(Graphics g, String text, BufferedImage bi, Font font) {
		FontMetrics metrics = g.getFontMetrics(font);
		int x = (bi.getWidth() - metrics.stringWidth(text)) / 2;
		int y = ((bi.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
		g.setFont(font);
		// draw shadow
		g.setColor(Color.black);
		g.drawString(text, x+1, y+1);
		// draw normal text
		g.setColor(Color.white);
		g.drawString(text, x, y);
		return bi;
	}

	private BufferedImage iconToBuffered(ImageIcon icon){
		Image image = icon.getImage();
		int height = icon.getIconHeight();
		int width = icon.getIconWidth();
		Image tempImage = image.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);
		ImageIcon sizedImageIcon = new ImageIcon(tempImage);

		BufferedImage bi = new BufferedImage(
			sizedImageIcon.getIconWidth(),
			sizedImageIcon.getIconHeight(),
			BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.createGraphics();
		sizedImageIcon.paintIcon(null, g, 0,0);
		g.dispose();
		return bi;
	}

}
