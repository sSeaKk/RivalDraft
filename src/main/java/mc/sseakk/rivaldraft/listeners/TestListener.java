package mc.sseakk.rivaldraft.listeners;

import api.sseakk.rocketapi.RocketAPI;
import api.sseakk.rocketapi.util.MessageUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import mc.sseakk.rivaldraft.RivalDraft;
import me.realized.duels.api.event.match.MatchEndEvent;
import me.realized.duels.api.event.match.MatchStartEvent;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class TestListener implements Listener {
    private static ArrayList<Player> players = new ArrayList<Player>();
    private MongoCollection<Document> col;

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        this.col = RivalDraft.getInstance().getDatabase().getCollection("Players");
        this.players.add(event.getPlayer());

        if(this.col.find(Filters.eq("uuid", event.getPlayer().getUniqueId().toString())).first() != null){ return; }
        AtomicInteger count = new AtomicInteger();
        this.col.find().forEach((Consumer<Document>) document ->{count.getAndIncrement();});

        Document doc = new Document("id", count.intValue())
                .append("username", event.getPlayer().getName())
                .append("uuid", event.getPlayer().getUniqueId().toString());

        this.col.insertOne(doc);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        this.players.remove(event.getPlayer());
    }

    @EventHandler
    public void onMatch(MatchStartEvent event){
        RivalDraft.info("Match started with: " + event.getMatch().getPlayers());
        Bukkit.getServer().getScheduler().runTaskLater(RocketAPI.getInstance(), new Runnable() {
            public void run() {
                RivalDraft.getInstance().getDuels().getQueueManager().remove(event.getMatch().getKit(), event.getMatch().getBet());
                MessageUtil.infoMessage(RivalDraft.getInstance(), "Deleted queue | Kit: " + event.getMatch().getKit() + " | Bet: " + event.getMatch().getBet());
            }
        }, 1L);
    }

    public static ArrayList<Player> getPlayers(){
        return players;
    }
}