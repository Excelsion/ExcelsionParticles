package com.radthorne.ExcelsionParticles;

import org.bukkit.Location;

public
class LocationUtil
{

	public static
	Boolean isLocationSame( Location loc1, Location loc2 )
	{
		return loc1.getX() == loc2.getX() && loc1.getY() == loc2.getY() && loc1.getZ() == loc2.getZ();
	}

	public static
	Boolean isLocationSimilar( Location loc1, Location loc2 )
	{
		return ( loc1.getX() - loc2.getX() < 0.25 && loc1.getX() - loc2.getX() > - 0.25 ) &&
				( loc1.getY() - loc2.getY() < 0.25 && loc1.getY() - loc2.getY() > - 0.25 ) &&
				( loc1.getZ() - loc2.getZ() < 0.25 && loc1.getZ() - loc2.getZ() > - 0.25 );
	}
}
