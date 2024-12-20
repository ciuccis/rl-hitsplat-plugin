package org.ciuccis.hitsplat.utils;

import net.runelite.api.Hitsplat;

public class ManagedHitsplat
{
    public Hitsplat hitsplat;
    public int position;

    public ManagedHitsplat(Hitsplat hitsplat, int position)
    {
        this.hitsplat = hitsplat;
        this.position = position;
    }
}
