package org.ciuccis.hitsplat.utils;

import net.runelite.api.Hitsplat;

public class ManagedHitsplat
{
    public Hitsplat hitsplat;
    public int position;
	public DamageType damageType;

    public ManagedHitsplat(Hitsplat hitsplat, int position, DamageType damageType)
    {
        this.hitsplat = hitsplat;
        this.position = position;
		this.damageType = damageType;
    }
}
