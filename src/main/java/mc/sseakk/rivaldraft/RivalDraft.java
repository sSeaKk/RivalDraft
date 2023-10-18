package mc.sseakk.rivaldraft;

import api.sseakk.rocketapi.RocketAPI;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import io.papermc.paper.plugin.configuration.PluginMeta;
import mc.sseakk.rivaldraft.commands.RivalDraftCommand;
import mc.sseakk.rivaldraft.commands.TestCommand;
import mc.sseakk.rivaldraft.listeners.DuelsListener;
import mc.sseakk.rivaldraft.listeners.TestListener;
import mc.sseakk.rivaldraft.tournaments.Tournament;
import me.realized.duels.api.Duels;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
public final class RivalDraft extends JavaPlugin {
    private static RivalDraft instance;
    private static PluginMeta pluginMeta;
    private RocketAPI rocketapi;
    private String configPath, dbUsername, dbPassword;
    private Duels duels;
    private boolean isDatabaseConnected = false;
    private MongoDatabase database = null;
    private TournamentManager tournamentManager;
    @Override
    public void onEnable() {
        this.instance = this;
        this.pluginMeta = this.getPluginMeta();
        registerConfig();

        //Duels dependency
        this.duels = (Duels) Bukkit.getServer().getPluginManager().getPlugin("Duels");
        if(duels != null){
            getLogger().info("Duels: Founded");
        } else {
            getLogger().info(("Duels: Not Founded"));
        }

        //Database
        connectDatabase();
        if(isDatabaseConnected){ getLogger().info("Database connected."); } else { getLogger().severe("Database is not connected, avoiding all database methods."); }

        //Tournamnets
        this.tournamentManager = new TournamentManager(this);
        this.tournamentManager.loadTournaments();

        commandManager();
        eventManager();
    }

    @Override
    public void onDisable() {

    }

    public void reloadPlugin(){
        reloadConfig();
        getLogger().info("Config reloaded.");
        connectDatabase();
        getLogger().info("Database reloaded.");
    }

    public void registerConfig(){
        File fileConfig = new File(this.getDataFolder(), "config.yml");
        this.configPath = fileConfig.getPath();
        if(!fileConfig.exists()){ this.getConfig().options().copyDefaults(true); }
        saveConfig();
    }

    public void connectDatabase(){
        this.dbUsername = this.getConfig().getString("Database.user");
        this.dbPassword = this.getConfig().getString("Database.password");

        if(this.dbUsername == "" || this.dbPassword == ""){
            this.getLogger().warning("Database credentials is not set. Configure it in config.yml file. Omitting connection.");
            return;
        }

        MongoClient mongoClient = MongoClients.create("mongodb+srv://"+this.dbUsername+":"+this.dbPassword+"@sseakk-testcluster.d5pdcxc.mongodb.net/?retryWrites=true&w=majority");
        if(mongoClient == null){ this.getLogger().severe("ERROR TO CONNECT TO THE DATABASE. MAY USER OR PASSWORD WRONG. DISABLING PLUGIN"); Bukkit.getPluginManager().disablePlugin(this);}

        this.database = mongoClient.getDatabase("RivalDraft");

        this.database.getCollection("Players").insertOne(new Document("test","connection"));
        this.database.getCollection("Players").findOneAndDelete(Filters.eq("test","connection"));
        this.isDatabaseConnected = true;
    }

    public void commandManager(){
        this.getCommand("test").setExecutor(new TestCommand());
        this.getCommand("rivaldraft").setExecutor(new RivalDraftCommand());
    }

    public void eventManager(){
        this.getServer().getPluginManager().registerEvents(new TestListener(), this);
        if(this.duels != null){ this.getServer().getPluginManager().registerEvents(new DuelsListener(), this); }
    }

    public static RivalDraft getInstance(){ return instance; }

    public Duels getDuels() { return duels; }

    public TournamentManager getTournamentManager() { return tournamentManager; }

    public MongoDatabase getDatabase(){ return this.database;};

    public boolean isDatabaseConnected(){return this.isDatabaseConnected; }

    public static void info(String message){ instance.getLogger().info(message); }

    public static String getStyledName(){ return "&6["+instance.getName()+"] &r"; }

    public static String getPluginVersion(){ return instance.getPluginMeta().getVersion(); }
}