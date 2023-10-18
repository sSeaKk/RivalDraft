package mc.sseakk.rivaldraft.commands;

import api.sseakk.rocketapi.util.MessageUtil;
import mc.sseakk.rivaldraft.RivalDraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RivalDraftCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0){
            if(args[0].equalsIgnoreCase("reload")){
                RivalDraft.getInstance().reloadPlugin();
                if(!(sender instanceof Player)){
                    RivalDraft.info("Reload confirm.");
                } else {
                    MessageUtil.playerMessage(((Player) sender).getPlayer(), RivalDraft.getStyledName() + "Plugin reloaded.");
                }
                return true;
            }

            if(args[0].equalsIgnoreCase("version")){
                if(!(sender instanceof Player)){
                    RivalDraft.info("RivalDraft version: " + RivalDraft.getPluginVersion());
                } else {
                    MessageUtil.playerMessage(((Player) sender).getPlayer(), RivalDraft.getStyledName() + "RivalDraft version: " + RivalDraft.getPluginVersion());
                }
                return true;
            }


        }

        if(!(sender instanceof Player)) { RivalDraft.info("ERROR: This command can't be executed in console.");}

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1) {
            List<String> tabList = new ArrayList<>();
            Collections.addAll(tabList, "reload", "version");
            if(sender instanceof Player){ Collections.addAll(tabList, "example1", "example2"); }
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
