package mc.sseakk.rivaldraft;

import com.mongodb.client.model.Filters;
import mc.sseakk.rivaldraft.tournaments.Tournament;
import mc.sseakk.rivaldraft.tournaments.gamemodes.OrdinaryTournament;
import org.bson.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
public class TournamentManager {
    private RivalDraft plugin;
    private ArrayList<Tournament> tournaments;
    public TournamentManager(RivalDraft plugin){
        this.plugin = plugin;
        this.tournaments = new ArrayList<Tournament>();
    }

    public void newTournament(Tournament tournament){
        this.tournaments.add(tournament);
        this.saveTournament(tournament);
        this.plugin.getLogger().info("Created tournament '"+tournament.getName()+"' dated for: "+tournament.getDateString());
    }

    public void loadTournament(Tournament tournament){
        this.tournaments.add(tournament);
        this.plugin.getLogger().info("Loaded tournament '"+tournament.getName()+"' dated for: "+tournament.getDateString());
    }
    public Tournament getTournament(String name){
        for(Tournament tournament : this.tournaments){
            if(tournament.getName().equals(name)){
                return tournament;
            }
        }

        return null;
    }

    public void removeTournament(String name){
        for(Tournament tournament : this.tournaments){
            if(tournament.getName().equals(name)){
                this.tournaments.remove(tournament);
                RivalDraft.getInstance().getDatabase().getCollection("tournaments").findOneAndDelete(Filters.eq("name",name));
            }
        }
    }

    public void saveTournament(Tournament tournament){
        if(RivalDraft.getInstance().isDatabaseConnected()){
            Document doc = new Document("name",tournament.getName())
                    .append("date",tournament.getDateString());

            RivalDraft.getInstance().getDatabase().getCollection("Tournaments").insertOne(doc);
        }
    }

    public void loadTournaments(){
        this.plugin.getDatabase().getCollection("Tournaments").find().forEach(doc ->{
            try {
                loadTournament(new OrdinaryTournament(doc.getString("name"), new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(doc.getString("date"))));
            } catch (ParseException e) {
                this.plugin.getLogger().warning("Can't parse the date of '"+doc.getString("name")+"' tournament. Skipping it.");
            }
        });
    }
}