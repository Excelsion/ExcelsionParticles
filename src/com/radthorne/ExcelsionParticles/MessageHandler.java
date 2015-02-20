package com.radthorne.ExcelsionParticles;

import org.bukkit.ChatColor;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public
class MessageHandler
{

	private static MessageHandler instance;
	public static            Map<String, String>        messageMap         = new HashMap<String, String>();
	private static transient Map<String, MessageFormat> messageFormatCache = new HashMap<String, MessageFormat>();

	public
	MessageHandler()
	{
		MessageHandler.instance = this;
	}

	public static
	String msg( final String key, final Object... objects )
	{
		if ( instance == null )
		{
			return "";
		}
		if ( objects.length == 0 )
		{
			return format( key );
		}
		else
		{
			return format( key, objects );
		}
	}

	private static
	String format( final String key )
	{
		if ( messageMap.size() <= 0 )
		{
			return "ERROR: message not found";
		}
		return ChatColor.translateAlternateColorCodes( '&', messageMap.get( key ) );
	}

	private static
	String format( final String key, final Object[] objects )
	{
		String format = format( key );
		MessageFormat messageFormat = messageFormatCache.get( format );
		if ( messageFormat == null )
		{
			try
			{
				messageFormat = new MessageFormat( format );
			}
			catch ( IllegalArgumentException e )
			{
				ExcelsionParticles.instance.getLogger().log( Level.SEVERE, "Invalid Translation key: '" + key + "': " + e.getMessage() );
				format = format.replaceAll( "\\{(\\D*?)\\}", "\\[$1\\]" );
				messageFormat = new MessageFormat( format );
			}
			messageFormatCache.put( format, messageFormat );
		}
		return messageFormat.format( objects );
	}
}
