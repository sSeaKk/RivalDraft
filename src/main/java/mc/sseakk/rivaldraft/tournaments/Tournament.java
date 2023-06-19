package mc.sseakk.rivaldraft.tournaments;

import mc.sseakk.rivaldraft.RivalDraft;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Tournament {
    private String name;
    private Date date;
    private int fee;
    private ArrayList<Object> prices;
    private TournamentState state;
    private TournamentType type;
    private TournamentTier tier;
    private boolean hasFile;
//TODO:make it abstract, non abstract for test propouse.
    public Tournament(String name, Date date){
        this.name = name;
        this.date = date;
        this.fee = -1;
        this.name = name;
        this.date = date;
        this.fee = -1;
        this.prices = new ArrayList<Object>();
        this.state = TournamentState.UPCOMING;
        this.type = null;
        this.tier = null;
        this.hasFile = false;
        RivalDraft.getInstance().getTournamentManager().addTournament(this);
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public String getDateString(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return formatter.format(this.date);
    }

    public void setHasFile(boolean hasFile) {
        this.hasFile = hasFile;
    }

    public boolean hasFile() {
        return hasFile;
    }
}