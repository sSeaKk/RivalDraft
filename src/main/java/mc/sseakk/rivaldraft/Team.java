package mc.sseakk.rivaldraft;

import java.util.ArrayList;

public class Team {
    private String name;
    private int ranking;
    private ArrayList<Competitor> competitors;

    public Team(String name){
        this.name = name;
        this.competitors = new ArrayList<Competitor>();
        this.ranking = -1;
    }

    public boolean addCompetitor(Competitor competitor){
        if(this.competitors.contains(competitor)){
            return false;
        }

        this.competitors.add(competitor);
        return true;
    }

    public boolean removeCompetitor(Competitor competitor){
        if(this.competitors.contains(competitor)){
            return false;
        }

        this.competitors.add(competitor);
        return true;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getRanking() {
        return ranking;
    }

    public ArrayList<Competitor> getCompetitors() {
        return competitors;
    }

    public String getName() {
        return name;
    }
}
