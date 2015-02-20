package com.radthorne.ExcelsionParticles;

import com.radthorne.ExcelsionParticles.ExcelsionParticles.TrailType;
import net.minecraft.server.v1_7_R3.EntityFireworks;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R3.CraftServer;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftFirework;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftWolf;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Ref;

import static com.radthorne.ExcelsionParticles.MessageHandler.msg;

public class CommandHandler implements CommandExecutor
{

    private ExcelsionParticles plugin;

    public CommandHandler( ExcelsionParticles plugin )
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if ( sender instanceof Player )
        {
            if ( cmd.getName().equalsIgnoreCase( "trail" ) )
            {
                Player player = (Player) sender;
                if ( args.length > 0 )
                {
                    if ( args[0].equalsIgnoreCase( "list" ) )
                    {
                        showList( player );
                    }
                    else if ( args[0].equalsIgnoreCase( "test" ) )
                    {
                        Location loc = player.getLocation();
                        float speed = Float.parseFloat( args[1] );
                        int amount = Integer.parseInt( args[2] );
                        net.minecraft.server.v1_7_R3.World world = (net.minecraft.server.v1_7_R3.World) loc.getWorld();
                        world.a( );
                        net.minecraft.server.v1_7_R3.ItemStack nis = CraftItemStack.asNMSCopy( new ItemStack( Material.FIREWORK ) );
                        ParticleEffect.FIREWORKS_SPARK.display( loc.add( 1F, 1F, 1F ), 0.2F, 0.1F, 0.2F, speed, amount );
                    }
                    else
                    {
                        TrailType trailtype = TrailType.fromName( args[0] );
                        if ( trailtype == null )
                        {
                            sender.sendMessage( msg( "incorrecttype", args[0] ) );
                        }
                        else if ( trailtype.equals( TrailType.NONE ) || trailtype.equals( TrailType.OFF ) )
                        {
                            plugin.removeType( player );
                            sender.sendMessage( msg( "disabled" ) );
                        }
                        else if ( canUseTrail( player, trailtype ) )
                        {
                            plugin.setType( player, trailtype );
                            sender.sendMessage( msg( "enabled", trailtype.getName() ) );
                        }
                        else
                        {
                            sender.sendMessage( msg( "nopermission" ) );
                        }
                    }
                }
                else
                {
                    showList( player );
                }
            }
        }
        else
        {
            sender.sendMessage( "This command is only supported for players." );
        }
        return false;
    }

    private void showList( Player player )
    {
        String list = ChatColor.RED + "values: " + ChatColor.GOLD;
        for ( TrailType type : TrailType.values() )
        {
            if ( canUseTrail( player, type ) )
            {
                list += type.getName() + ", ";
            }
        }
        list = list.substring( 0, list.length() - 2 );
        player.sendMessage( list );
    }

    private boolean canUseTrail( Player player, TrailType trailtype )
    {
        return player.hasPermission( "excelsionparticles.trail.all" ) || player.hasPermission( "excelsionparticles.trail." + trailtype.getName() );
    }
}
