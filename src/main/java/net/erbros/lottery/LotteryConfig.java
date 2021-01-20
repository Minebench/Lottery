package net.erbros.lottery;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;


public class LotteryConfig
{

    final private Lottery plugin;
    private FileConfiguration config;
    private double cost;
    private double hours;
    private long nextexec;
    private boolean useiConomy;
    private Material material;
    private double extraInPot;
    private boolean broadcastBuying;
    private int broadcastBuyingTime;
    private boolean welcomeMessage;
    private double netPayout;
    private boolean clearExtraInPot;
    private int maxTicketsEachUser;
    private int ticketsAvailable;
    private double jackpot;
    private String lastwinner;
    private double lastwinneramount;
    private boolean buyingExtendDeadline;
    private int buyingExtendRemaining;
    private double buyingExtendBase;
    private double buyingExtendMultiplier;
    private String taxTarget;

    private HashMap<String, List<String>> messages;


    public LotteryConfig( final Lottery plugin )
    {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }


    public void loadConfig()
    {
        plugin.reloadConfig();
        config = plugin.getConfig();

        debugMsg( "Loading Lottery configuration" );

        hours = config.getDouble( "config.hours", 24 );

        useiConomy = config.getBoolean( "config.useiConomy", true );
        material = Material.matchMaterial(config.getString( "config.material", "gold_ingot" ).toUpperCase());
        broadcastBuying = config.getBoolean( "config.broadcastBuying", true );
        broadcastBuyingTime = config.getInt( "config.broadcastBuyingTime", 120 );
        welcomeMessage = config.getBoolean( "config.welcomeMessage", true );
        extraInPot = config.getDouble( "config.extraInPot", 0 );
        clearExtraInPot = config.getBoolean( "config.clearExtraInPot", true );
        netPayout = config.getDouble( "config.netPayout", 100 );
        maxTicketsEachUser = config.getInt( "config.maxTicketsEachUser", 1 );
        ticketsAvailable = config.getInt( "config.numberOfTicketsAvailable", 0 );
        nextexec = config.getLong( "config.nextexec" );
        cost = Etc.formatAmount( config.getDouble( "config.cost", 5 ), useiConomy );
        jackpot = config.getDouble( "config.jackpot", 0 );
        lastwinner = config.getString( "config.lastwinner", "" );
        lastwinneramount = config.getDouble( "config.lastwinneramount", 0 );
        buyingExtendDeadline = config.getBoolean( "config.buyingExtend.enabled", true );
        buyingExtendRemaining = config.getInt( "config.buyingExtend.secondsRemaining", 30 );
        buyingExtendBase = config.getDouble( "config.buyingExtend.extendBase", 15 );
        buyingExtendMultiplier = config.getDouble( "config.buyingExtend.extendMultiplier", 1.5 );
        taxTarget = config.getString( "config.taxTarget", "" );

        // Load messages?
        loadCustomMessages();
        // Then lets save this stuff :)
        plugin.saveConfig();
    }

    public void set( final String path, final Object value )
    {
        config.set( path, value );
        plugin.saveConfig();
    }

    public void loadCustomMessages()
    {
        messages = new HashMap<>();
        ConfigurationSection messageConfig = config.getConfigurationSection( "message" );
        for ( String key : messageConfig.getKeys( false ))
        {
            messages.put( key, Arrays.asList( messageConfig.getString( key ).split( "%newline%" ) ) );
        }
    }

    public String getPlural( String word, int amount )
    {
        String tlkey = "";

        if ( word.equalsIgnoreCase( "ticket" ) )
        {
            tlkey = amount == 1 ? "ticket" : "tickets";
        }
        if ( word.equalsIgnoreCase( "player" ) )
        {
            tlkey = amount == 1 ? "player" : "players";
        }
        if ( word.equalsIgnoreCase( "day" ) )
        {
            tlkey = amount == 1 ? "day" : "days";
        }
        if ( word.equalsIgnoreCase( "hour" ) )
        {
            tlkey = amount == 1 ? "hour" : "hours";
        }
        if ( word.equalsIgnoreCase( "minute" ) )
        {
            tlkey = amount == 1 ? "minute" : "minutes";
        }
        if ( word.equalsIgnoreCase( "second" ) )
        {
            tlkey = amount == 1 ? "second" : "seconds";
        }

        String pluralTl = "";
        try
        {
            for ( String message : getMessage( tlkey ) )
            {
                pluralTl = message;
            }
            return pluralTl;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return pluralTl;
    }

    public List<String> getMessage( final String topic ) throws Exception
    {
        if ( !messages.containsKey( topic ) )
        {
            throw new Exception( "Invalid Translation key" );
        }
        return messages.get( topic );
    }

    // Enable some debugging?
    public void debugMsg( final String msg )
    {
        if ( config.getBoolean( "config.debug" ) && msg != null )
        {
            plugin.getLogger().log( Level.INFO, msg );
        }
    }

    public double getCost()
    {
        return cost;
    }

    public void setCost( final double cost )
    {
        this.cost = cost;
        set( "config.cost", cost );
    }

    public double getHours()
    {
        return hours;
    }

    public void setHours( final double hours )
    {
        this.hours = hours;
        set( "config.hours", hours );
    }

    public long getNextexec()
    {
        return nextexec;
    }

    public void setNextexec( final long nextexec )
    {
        this.nextexec = nextexec;
        set( "config.nextexec", nextexec );
    }

    public boolean useEconomy()
    {
        return useiConomy;
    }

    public Material getMaterial()
    {
        return material;
    }

    public double getExtraInPot()
    {
        return extraInPot;
    }

    public void setExtraInPot( final double extraInPot )
    {
        this.extraInPot = extraInPot;
        set( "config.extraInPot", extraInPot );
    }

    public void addExtraInPot( final double extra )
    {
        extraInPot += extra;
        setExtraInPot( extraInPot );
    }

    public boolean useBroadcastBuying()
    {
        return broadcastBuying;
    }

    public int getBroadcastBuyingTime()
    {
        return broadcastBuyingTime;
    }

    public boolean useWelcomeMessage()
    {
        return welcomeMessage;
    }

    public double getNetPayout()
    {
        return netPayout;
    }

    public void setNetPayout( final double netPayout )
    {
        this.netPayout = netPayout;
        set( "config.netPayout", netPayout );
    }

    public boolean clearExtraInPot()
    {
        return clearExtraInPot;
    }

    public int getMaxTicketsEachUser()
    {
        return maxTicketsEachUser;
    }

    public void setMaxTicketsEachUser( final int maxTicketsEachUser )
    {
        this.maxTicketsEachUser = maxTicketsEachUser;
        set( "config.maxTicketsEachUser", maxTicketsEachUser );
    }

    public int getTicketsAvailable()
    {
        return ticketsAvailable;
    }

    public double getJackpot() {
        return jackpot;
    }

    public String getLastwinner()
    {
        return lastwinner;
    }

    public void setLastwinner( final String lastwinner )
    {
        this.lastwinner = lastwinner;
        set( "config.lastwinner", lastwinner );
    }

    public double getLastwinneramount()
    {
        return lastwinneramount;
    }

    public void setLastwinneramount( final double lastwinneramount )
    {
        this.lastwinneramount = lastwinneramount;
        set( "config.lastwinneramount", lastwinneramount );
    }

    public boolean isBuyingExtendDeadline()
    {
        return buyingExtendDeadline;
    }

    public int getBuyingExtendRemaining()
    {
        return buyingExtendRemaining;
    }

    public double getBuyingExtendBase()
    {
        return buyingExtendBase;
    }

    public double getBuyingExtendMultiplier()
    {
        return buyingExtendMultiplier;
    }

    public String getTaxTarget()
    {
        return taxTarget;
    }

    public String formatCurrency( double amount )
    {
        return plugin.getEconomy().format( amount );
    }
}
