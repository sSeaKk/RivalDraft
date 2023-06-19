package mc.sseakk.rivaldraft;

import org.bukkit.entity.Player;

public class Competitor {
    private Player player;
    private Team team;
    private int ranking;

    private Competitor(Player player){
        this.player = player;
        this.team = null;
        this.ranking = -1;
    }

    private Competitor(Player player, Team team){
        this.player = player;
        this.team = team;
        this.ranking = -1;
    }

    public boolean assignTeam(Team team){
        if(this.team != null){
            return false;
        }

        this.team = team;
        return true;
    }

    public boolean removeTeam(){
        if(this.team == null){
            return false;
        }

        this.team = null;
        return true;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getRanking() {
        return ranking;
    }

    public Player getPlayer() {
        return player;
    }

    public Team getTeam() {
        return team;
    }
}