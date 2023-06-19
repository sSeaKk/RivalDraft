package mc.sseakk.rivaldraft;

import api.sseakk.rocketapi.FileManager;
import api.sseakk.rocketapi.RocketAPI;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import mc.sseakk.rivaldraft.commands.TestCommand;
import mc.sseakk.rivaldraft.listeners.TestListener;
import me.realized.duels.api.Duels;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public final class RivalDraft extends JavaPlugin {
    private String configPath, dbUsername, dbPassword;
    private Duels duels;
    private static RivalDraft instance;
    private MongoDatabase database = null;
    private FileManager fileManager;
    private RocketAPI rocketapi;
    private TournamentManager tournamentManager;
    @Override
    public void onEnable() {
        if(Bukkit.getPluginManager().getPlugin("RocketAPI") == null){
            getLogger().severe("CANNOT LOAD ROCKET API! DISABLING PLUGIN.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        rocketapi = RocketAPI.getInstance();
        registerConfig();

        if(Integer.valueOf(rocketapi.getVersion()) < 20){
            getLogger().severe("THIS PLUGIN NEED RocketAPI-20 OR NEWER. DISABLING PLUGIN.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.fileManager = new FileManager(this);

        this.duels = (Duels) Bukkit.getServer().getPluginManager().getPlugin("Duels");
        if(duels != null){
            getLogger().info("Duels: Founded");
        } else {
            getLogger().info(("Duels: Not Founded"));
        }

        connectDatabase();

        this.tournamentManager = new TournamentManager(this);
        this.instance = this;

        this.tournamentManager.loadTournaments();
        commandManager();
        eventManager();

        //test tournament
        //new Tournament("test2", new Date(2023-1900, 07-1,10,18,00));
    }

    @Override
    public void onDisable() {

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
    }

    public void commandManager(){
        this.getCommand("test").setExecutor(new TestCommand());
    }

    public void eventManager(){
        this.getServer().getPluginManager().registerEvents(new TestListener(), this);
    }
    public static RivalDraft getInstance(){
        return instance;
    }

    public RocketAPI api(){
        return this.rocketapi;
    }

    public Duels getDuels() {
        return duels;
    }

    public FileManager getFileManager(){
        return fileManager;
    }

    public TournamentManager getTournamentManager() {
        return tournamentManager;
    }
    public MongoDatabase getDatabase(){ return this.database;};

    public boolean isCollection(String name){
        AtomicBoolean bool = new AtomicBoolean(false);
        this.database.listCollectionNames().forEach((Consumer<String>) collection -> {
            if(collection.equalsIgnoreCase(name)){ bool.set(true); }
        });
        return bool.get();
    }
}