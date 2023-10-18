package mc.sseakk.rivaldraft.tournaments;

import mc.sseakk.rivaldraft.RivalDraft;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public abstract class Tournament {
    private String name;
    private Date date;
    private int fee;
    private ArrayList<Object> prices;
    private TournamentState state;
    private TournamentType type;
    private TournamentTier tier;

    public Tournament(String name, Date date){
        this.name = name;
        this.date = date;
        this.fee = -1;
        this.prices = new ArrayList<Object>();
        this.state = TournamentState.DISABLED;
        this.type = null;
        this.tier = null;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {this.date = date;}

    public int getFee() {return fee;}

    public void setFee(int fee) {this.fee = fee;}

    public ArrayList<Object> getPrices() {return prices;}

    public void setPrices(ArrayList<Object> prices) {this.prices = prices;}

    public TournamentState getState() {return state;}

    public void setState(TournamentState state) {this.state = state;}

    public TournamentType getType() {return type;}

    public void setType(TournamentType type) {this.type = type;}

    public TournamentTier getTier() {return tier;}

    public void setTier(TournamentTier tier) {this.tier = tier;}
}