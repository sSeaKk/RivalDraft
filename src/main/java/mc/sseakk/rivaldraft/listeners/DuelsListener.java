package mc.sseakk.rivaldraft.listeners;

import com.mongodb.client.MongoCollection;
import mc.sseakk.rivaldraft.RivalDraft;
import me.realized.duels.api.event.match.MatchEndEvent;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class DuelsListener implements Listener {
    private MongoCollection<Document> collection;
    @EventHandler
    public void onFinalizedMatch(MatchEndEvent event){
        if(event.getReason() == MatchEndEvent.Reason.PLUGIN_DISABLE){ return; }
        this.collection = RivalDraft.getInstance().getDatabase().getCollection("Match");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date startDateTime = new Date(event.getMatch().getStart());
        Date endDateTime = new Date(System.currentTimeMillis());

        AtomicInteger count = new AtomicInteger();
        this.collection.find().forEach((Consumer<Document>) document ->{count.getAndIncrement();});

        List<Document> playerList = new ArrayList<>();
        Document winner = new Document("name", Bukkit.getPlayer(event.getWinner()).getName())
                .append("uuid", event.getWinner().toString())
                .append("result", "winner");
        playerList.add(winner);

        Document loser = new Document("name", Bukkit.getPlayer(event.getLoser()).getName())
                .append("uuid", event.getLoser().toString())
                .append("result", "loser");
        playerList.add(loser);

        Document document = new Document("id", count.intValue())
                .append("players", playerList)
                .append("started", formatter.format(startDateTime).toString())
                .append("ended", formatter.format(endDateTime).toString())
                .append("reason", event.getReason().toString());
        this.collection.insertOne(document);
    }
}
