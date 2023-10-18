package mc.sseakk.rivaldraft.commands;

import api.sseakk.rocketapi.util.MessageUtil;
import mc.sseakk.rivaldraft.RivalDraft;
import mc.sseakk.rivaldraft.TournamentManager;
import mc.sseakk.rivaldraft.listeners.TestListener;
import mc.sseakk.rivaldraft.tournaments.Tournament;
import mc.sseakk.rivaldraft.tournaments.gamemodes.OrdinaryTournament;
import me.realized.duels.api.Duels;
import me.realized.duels.api.arena.Arena;
import me.realized.duels.api.kit.Kit;
import me.realized.duels.api.queue.DQueue;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TestCommand implements TabExecutor {
    private Duels duels = RivalDraft.getInstance().getDuels();
    private TournamentManager tm = RivalDraft.getInstance().getTournamentManager();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            MessageUtil.infoMessage(RivalDraft.getInstance(), "You can't execute this command in console!");
            return true;
        }
        Player player = (Player) sender;

        if(duels == null){
            MessageUtil.playerMessage(player, "Duels is not instaled on the server.");
            return true;
        }

        if(args.length > 0){
            if(args[0].equalsIgnoreCase("arena")){
                for(Arena arena : duels.getArenaManager().getArenas()) {
                    MessageUtil.playerMessage(player, arena.getName());
                }
                return true;
            }

            if(args[0].equalsIgnoreCase("queue")) {
                ArrayList<Player> players = new ArrayList<>();
                if(TestListener.getPlayers().size() <= 1){
                    MessageUtil.playerMessage(player, "No hay suficientes jugadores conectados al servidor!");
                    return true;
                }

                for(int i=0; players.size()<2; i++){
                    int random = new Random().nextInt(TestListener.getPlayers().size());
                    MessageUtil.playerMessage(player, "Selected: " + TestListener.getPlayers().get(random).getName());
                    if(!players.contains(TestListener.getPlayers().get(random))) {
                        players.add(TestListener.getPlayers().get(random));
                        MessageUtil.playerMessage(player, "Added");
                    }
                }
                MessageUtil.playerMessage(player, "Players Selected!");

                Kit kit = duels.getKitManager().create(player, "testkit");
                if(duels.getQueueManager().get(kit, 0) != null){
                    duels.getQueueManager().remove(kit, 0);
                }
                DQueue queue = duels.getQueueManager().create(kit, 0);

                for(Player dplayer : players){
                    duels.getQueueManager().addToQueue(dplayer, queue);
                    MessageUtil.playerMessage(player, dplayer.getName() + " added to the queue");
                }
            }

            if(args[0].equalsIgnoreCase("tournaments")){
                if(tm.getTournament("test") == null){
                    tm.newTournament(new OrdinaryTournament("test", new Date(2023-1900, Calendar.SEPTEMBER,23,20,00)));
                }

                if(tm.getTournament("test2") == null){
                    tm.newTournament(new OrdinaryTournament("test2", new Date(2023-1900, Calendar.JULY,10,18,00)));
                }

                if(RivalDraft.getInstance().getTournamentManager().getTournament("test3") == null){
                    tm.newTournament(new OrdinaryTournament("test3", new Date(2023-1900, Calendar.DECEMBER,7,16,00)));
                }

                MessageUtil.playerMessage(player, "Command executed.");
                return true;
            }
        }

        MessageUtil.playerMessage(player, "/test arena" +
                "\n/test queue" +
                "\n/test tournaments");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1) {
            List<String> tabList = Arrays.asList("arena", "queue","tournaments");
            String input = args[0].toLowerCase();
            List<String> completions = null;

            for (String s : tabList) {
                if (s.startsWith(input)) {
                    if (completions == null) {
                        completions = new ArrayList<>();
                    }
                    completions.add(s);
                }
            }

            if (completions != null) {
                Collections.sort(completions);
            }
            return completions;
        }

        return null;
    }
}