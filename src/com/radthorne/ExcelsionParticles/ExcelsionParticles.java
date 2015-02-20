package com.radthorne.ExcelsionParticles;

import net.ess3.api.IEssentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public
class ExcelsionParticles extends JavaPlugin
		implements CommandExecutor
{

	public static ExcelsionParticles instance;
	private           Map<String, TrailType> userList = new HashMap<String, TrailType>();
	private transient IEssentials            ess      = null;
	Logger log;

	public
	void onEnable()
	{
		this.log = getLogger();
		instance = this;
		saveDefaultConfig();
		this.log.info( "ExcelsionParticles has Been Enabled!" );
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents( new PlayerListener( this ), this );
		final List<Command> commands = PluginCommandYamlParser.parse( this );
		for ( Command command : commands )
		{
			getCommand( command.getName() ).setExecutor( new CommandHandler( this ) );
		}
		initMessages();
		getServer().getScheduler().scheduleSyncDelayedTask( this, new Runnable()
		{
			public
			void run()
			{
				hookEss();
			}
		} );
	}

	private
	void initMessages()
	{
		new MessageHandler();
		MessageHandler.messageMap = new HashMap<String, String>();
		if ( getConfig().isConfigurationSection( "messages" ) )
		{
			ConfigurationSection messagesSection = getConfig().getConfigurationSection( "messages" );
			Map<String, String> messageMap = new HashMap<String, String>();
			for ( String key : messagesSection.getKeys( false ) )
			{
				String message = messagesSection.getString( key );
				messageMap.put( key, message );
			}
			MessageHandler.messageMap = messageMap;
		}
		else
		{
			log.info( "NO MESSAGES LOADED" );
		}
	}

	public
	void onDisable()
	{
		this.log.info( "ExcelsionParticles has been disabled!" );
	}

	public
	void hookEss()
	{
		PluginManager pm = getServer().getPluginManager();
		Plugin essPlugin = pm.getPlugin( "Essentials" );
		if ( ( essPlugin == null ) || ( ! essPlugin.isEnabled() ) )
		{
			setEnabled( false );
			getLogger().warning( "Couldn't hook Ess, disabling." );
			return;
		}
		this.ess = ( ( IEssentials ) essPlugin );
	}

	public
	IEssentials getEss()
	{
		if ( this.ess == null )
		{
			getLogger().warning( "Ess object was null" );
		}
		return this.ess;
	}

	public
	boolean hasTrail( Player player )
	{
		return userList.containsKey( player.getName() );
	}

	public
	void setType( Player player, TrailType type )
	{
		userList.put( player.getName(), type );
	}

	public
	void removeType( Player player )
	{
		userList.remove( player.getName() );
	}

	public
	TrailType getType( Player player )
	{
		return userList.get( player.getName() );
	}

	public static
	enum TrailType
	{
		FADEDRAINBOWPOTION( "fadedrainbowpotion", ParticleEffect.MOB_SPELL_AMBIENT, 5, 1F ),
		RAINBOWPOTION( "rainbowpotion", ParticleEffect.MOB_SPELL, 5, 1F ),
		BLACKPOTION( "blackpotion", ParticleEffect.MOB_SPELL, 5, 0F ),
		WHITEPOTION( "whitepotion", ParticleEffect.SPELL, 5, 0F ),
		RAINBOWNOTES( "rainbownotes", ParticleEffect.NOTE, 2, 1F ),
		NOTES( "notes", ParticleEffect.NOTE, 2, 0F ),
		BLACKSMOKE( "blacksmoke", ParticleEffect.LARGE_SMOKE, 2, 0F ),
		WHITESMOKE( "whitesmoke", ParticleEffect.EXPLODE, 2, 0F ),
		FLAME( "flame", ParticleEffect.FLAME, 3, 0.02F ),
		ERUPT( "erupt", ParticleEffect.LAVA, 2, 0F ),
		WHITESPARKLE( "whitesparkle", ParticleEffect.FIREWORKS_SPARK, 3, 0F ),
		PURPLESPARKLE( "purplesparkle", ParticleEffect.WITCH_MAGIC, 3, 0F ),
		GREENSPARKLE( "greensparkle", ParticleEffect.HAPPY_VILLAGER, 3, 0F ),
		CRITSPARKLE( "critsparkle", ParticleEffect.CRIT, 10, 0.1F ),
		MAGICCRIT( "magiccrit", ParticleEffect.MAGIC_CRIT, 10, 0.1F ),
		HEART( "heart", ParticleEffect.HEART, 1, 0F ),
		ANGRY( "angry", ParticleEffect.ANGRY_VILLAGER, 1, 0F ),
		REDDUST( "reddust", ParticleEffect.RED_DUST, 7, 0F ),
		BLACKDUST( "blackdust", ParticleEffect.RED_DUST, 7, 0.01F ),
		WHITEDUST( "whitedust", ParticleEffect.SNOW_SHOVEL, 7, 0F ),
		RAINBOWDUST( "rainbowdust", ParticleEffect.RED_DUST, 7, 1F ),
		SPLASH( "splash", ParticleEffect.SPLASH, 20, 0F ),
		DRIPWATER( "dripwater", ParticleEffect.DRIP_WATER, 5, 0F ),
		DRIPLAVA( "driplava", ParticleEffect.DRIP_LAVA, 5, 0F ),
		PORTAL( "portal", ParticleEffect.PORTAL, 25, 0.2F ),
		FOOTSTEP( "footstep", ParticleEffect.FOOTSTEP, 1, 0F ),
		ENCHANT( "enchant", ParticleEffect.ENCHANTMENT_TABLE, 25, 0.2F ),
		SLIME( "slime", ParticleEffect.SLIME, 10, 0F ),
		MYCELIUM( "mycelium", ParticleEffect.TOWN_AURA, 50, 0F ),
		NONE( "none", null, 0, 0F ),
		OFF( "off", null, 0, 0F );

		private String         name;
		private ParticleEffect effect;
		private int            amount;
		private float          speed;
		private static final Map<String, TrailType> NAME_MAP = new HashMap<String, TrailType>();

		TrailType( String name, ParticleEffect effect, int amount, float speed )
		{
			this.name = name;
			this.effect = effect;
			this.amount = amount;
			this.speed = speed;
		}

		static
		{
			for ( TrailType type : values() )
			{
				NAME_MAP.put( type.getName(), type );
			}
		}

		public
		String getName()
		{
			return name;
		}

		public static
		TrailType fromName( String name )
		{
			if ( name != null )
			{
				for ( Map.Entry e : NAME_MAP.entrySet() )
				{
					if ( ( ( String ) e.getKey() ).equalsIgnoreCase( name ) )
					{
						return ( TrailType ) e.getValue();
					}
				}
			}
			return null;
		}

		public
		ParticleEffect getEffect()
		{
			return effect;
		}

		public
		int getAmount()
		{
			return amount;
		}

		public
		float getSpeed()
		{
			return speed;
		}
	}
}