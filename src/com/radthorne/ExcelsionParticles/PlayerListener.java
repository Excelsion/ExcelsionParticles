package com.radthorne.ExcelsionParticles;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public
class PlayerListener
		implements Listener
{

	ExcelsionParticles plugin;

	PlayerListener( ExcelsionParticles plugin )
	{
		this.plugin = plugin;
	}

	@EventHandler
	public
	void onPlayerMove( PlayerMoveEvent pme )
	{
		Player player = pme.getPlayer();
		if ( plugin.hasTrail( player ) )
		{
			Location to = pme.getTo();
			Location from = pme.getFrom();
			if ( ! LocationUtil.isLocationSame( to, from ) )
			{
				Location loc = player.getLocation();
				ExcelsionParticles.TrailType type = plugin.getType( player );
				if ( type == null || type.getEffect() == null || type.getSpeed() < 0 || type.getAmount() <= 0 )
				{
					return;
				}
				type.getEffect().display( loc.add( 0F, 0.1F, 0F ), 0.2F, 0.1F, 0.2F, type.getSpeed(), type.getAmount() );
			}
		}
	}
}