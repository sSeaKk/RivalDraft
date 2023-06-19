package mc.sseakk.rivaldraft;

import api.sseakk.rocketapi.FileManager;
import api.sseakk.rocketapi.util.MessageUtil;
import mc.sseakk.rivaldraft.tournaments.Tournament;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class TournamentManager {
    private FileManager fm;
    private RivalDraft plugin;
    private ArrayList<Tournament> tournaments;
    public TournamentManager(RivalDraft plugin){
        this.plugin = plugin;
        this.fm = this.plugin.getFileManager();
        this.tournaments = new ArrayList<Tournament>();
    }

    public void addTournament(Tournament tournament){
        this.tournaments.add(tournament);
        this.saveTournament(tournament);
        MessageUtil.infoMessage(this.plugin, "Created or loaded tournament '"+tournament.getName()+"' dated for: "+tournament.getDateString());
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
            }
        }
    }

    public void saveTournament(Tournament tournament){
        fm.createFile("\\tournaments", tournament.getName()+".txt");
        BufferedWriter writer = fm.getBufferedWriter();

        try{
            writer.write("name="+tournament.getName()); writer.newLine();
            writer.write("mode="); writer.newLine();
            writer.write("date="+tournament.getDateString()); writer.newLine();
            writer.write("fee="); writer.newLine();
            writer.write("status="); writer.newLine();
            writer.write("tier="); writer.newLine();
            writer.write("type=");
            writer.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void loadTournaments(){
        if(!(new File(this.plugin.getDataFolder()+"\\tournaments")).exists()){
            new File(this.plugin.getDataFolder()+"\\tournaments").mkdirs();
        }
        try {
            File folder = this.fm.getFolder("\\tournaments");
            File[] files = folder.listFiles();

            for(File file : files){

                Scanner scn = new Scanner(file);
                String name = null;
                Date date = null;

                while(scn.hasNextLine()){
                    String line = scn.nextLine(), value = null;

                    if(line.startsWith("name=")){
                        name = line.replaceFirst("name=", "");
                    }

                    if(line.startsWith("date=")){
                        date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(line.replaceFirst("date=", ""));
                    }
                }

                Tournament tournament = new Tournament(name, date);
                tournament.setHasFile(true);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}