package dnk.dnkeconomy.extras;

import dnk.dnkeconomy.Main;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class Commands implements CommandExecutor, TabCompleter {
  private Main plugin = (Main)Main.getPlugin(Main.class);
  
  public boolean onCommand(CommandSender player, Command command, String label, String[] args) {
    if (command.getName().equalsIgnoreCase("eco")) {
      try {
        if (args[0].equalsIgnoreCase("deposit")) {
          if (player.hasPermission("DNKeconomy.admin.deposit") && 
            args.length == 3)
            try {
              if (args[1] == null) {
                player.sendMessage(thumbNail() + ChatColor.RED + " Usage: /eco deposit <Player> <money>");
                return true;
              } 
              if (args[2] == null) {
                player.sendMessage(thumbNail() + ChatColor.RED + " Usage: /eco deposit <Player> <money>");
                return true;
              } 
              OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
              if (target == null) {
                player.sendMessage(thumbNail() + ChatColor.RED + " That Player Doesn't exist");
                return true;
              } 
              Double withdraw = getDouble(args[2]);
              this.plugin.managers.depositPlayer(target, withdraw.doubleValue());
              player.sendMessage(thumbNail() + ChatColor.GRAY + " You have deposited §a$" + withdraw + "§7 into §a" + target.getName() + "'s §7account");
            } catch (NumberFormatException exception) {
              player.sendMessage(thumbNail() + ChatColor.RED + " Error: Too much money cannot deposit that");
            }  
          return true;
        } 
        if (args[0].equalsIgnoreCase("balance"))
          try {
            if (player.hasPermission("DNKeconomy.use.balance")) {
              if (args.length == 2) {
                try {
                  if (args[1] == null)
                    return true; 
                  OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                  if (target == null) {
                    player.sendMessage(thumbNail() + ChatColor.RED + " That player doesn't exist");
                    return true;
                  } 
                  player.sendMessage(thumbNail() + ChatColor.GREEN + target.getName() + " §7has §a%" + this.plugin.managers.getBalance(target) + "§7 in their account");
                } catch (Exception e) {
                  e.printStackTrace();
                } 
              } else if (player instanceof Player) {
                Player player1 = (Player)player;
                player.sendMessage(thumbNail() + ChatColor.GREEN + player.getName() + " §7has §a$" + this.plugin.managers.getBalance((OfflinePlayer)player1) + "§7 in their account");
              } 
              return true;
            } 
            player.sendMessage(thumbNail() + ChatColor.RED + " You don't have the permission to use this command");
          } catch (ArrayIndexOutOfBoundsException exception) {
            player.sendMessage(thumbNail() + ChatColor.RED + " Usage: /eco balance");
            player.sendMessage(thumbNail() + ChatColor.RED + " Usage: /eco balance <Player>");
          }  
        if (args[0].equalsIgnoreCase("remove") && 
          player.hasPermission("DNKeconomy.admin.remove"))
          try {
            if (args.length == 3) {
              if (args[1] == null) {
                player.sendMessage(thumbNail() + ChatColor.RED + " Usage: /eco remove <Player> <money>");
                return true;
              } 
              if (args[2] == null) {
                player.sendMessage(thumbNail() + ChatColor.RED + " Usage: /eco remove <Player> <money>");
                return true;
              } 
              OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
              Double withdraw = getDouble(args[2]);
              if (target == null) {
                player.sendMessage(ChatColor.RED + "That player doesn't exist");
                return true;
              } 
              this.plugin.managers.withdrawPlayer(target, withdraw.doubleValue());
              player.sendMessage(thumbNail() + ChatColor.GREEN + target.getName() + " now has " + this.plugin.managers.getBalance(target));
            } else {
              sendMessage(player, thumbNail() + "&4Usage: /eco remove <player> <money?");
            } 
          } catch (NumberFormatException exception) {
            player.sendMessage(thumbNail() + ChatColor.RED + " Error: Too much money cannot remove that");
          } catch (Exception exception) {
            exception.printStackTrace();
          }  
        if (player instanceof Player) {
          if (args[0].equalsIgnoreCase("pay"))
            try {
              if (args.length == 3) {
                if (player.hasPermission("DNKeconomy.use.pay")) {
                  Player player1 = (Player)player;
                  if (args[1] == null) {
                    player.sendMessage(thumbNail() + ChatColor.RED + " Usage: /eco pay <Player> <money>");
                    return true;
                  } 
                  if (args[2] == null) {
                    player.sendMessage(thumbNail() + ChatColor.RED + " Usage: /eco pay <Player> <money>");
                    return true;
                  } 
                  Player target = Bukkit.getPlayer(args[1]);
                  if (args[2].isEmpty())
                    return true; 
                  Double payment = Double.valueOf(getDouble(args[2]).doubleValue());
                  if (target == null) {
                    player.sendMessage(thumbNail() + ChatColor.RED + "That player doesn't exist");
                    return true;
                  } 
                  if (this.plugin.managers.getBalance((OfflinePlayer)player1) >= payment.doubleValue()) {
                    this.plugin.managers.withdrawPlayer((OfflinePlayer)player1, payment.doubleValue());
                    this.plugin.managers.depositPlayer((OfflinePlayer)target, payment.doubleValue());
                    player1.sendMessage(ChatColor.GREEN + target.getName() + "§7 has §a$" + this.plugin.managers.getBalance((OfflinePlayer)target) + "§7 in their account");
                    target.sendMessage(thumbNail() + ChatColor.GREEN + player1.getName() + " §7has paid you §a$"+ payment);
                  } else {
                    player1.sendMessage(thumbNail() + ChatColor.RED + " You do not have the money to pay to the person" + target.getName());
                  } 
                } else {
                  player.sendMessage(thumbNail() + ChatColor.RED + " You don't have permission to use this command".trim());
                } 
              } else {
                sendMessage(player, thumbNail() + "Usage: /eco pay <player> <money>");
              } 
            } catch (NumberFormatException exception) {
              player.sendMessage(ChatColor.RED + " Error: Too much money cannot transfer that".trim());
            } catch (Exception exception) {
              exception.printStackTrace();
            }  
        } else {
          System.out.println("You have to be a player");
        } 
        if (args[0].equalsIgnoreCase("reload") && 
          player.hasPermission("DNKeconomy.admin.reload")) {
          Main.reload();
          Main.save();
          this.plugin.reloadConfig();
          this.plugin.saveConfig();
          player.sendMessage(ChatColor.RED + this.plugin.getName() + "reloaded.");
        } 
        if (args[0].equalsIgnoreCase("disable") && 
          player.hasPermission("DNKeconomy.admin.disable"))
          this.plugin.onDisable(); 
      } catch (ArrayIndexOutOfBoundsException exception) {
        sendMessage(player, "&3Commands:");
        if (!player.hasPermission("DNKeconomy.admin.help")) {
          sendMessage(player, "&3/eco pay");
          sendMessage(player, "&3/eco balance");
        } else {
          sendMessage(player, "&3/eco deposit <Player> <money>");
          sendMessage(player, "&3/eco remove <Player> <money>");
          sendMessage(player, "&3/eco disable");
          sendMessage(player, "&3/eco reload");
        } 
      } 
      return true;
    } 
    return true;
  }
  
  private Double getDouble(String args) {
    String number = args.replaceAll("[^0-9]", "");
    double amount = Double.parseDouble(number);
    if (args.substring(args.length() - 1).equalsIgnoreCase("k"))
      amount *= 1000.0D; 
    return Double.valueOf(amount);
  }
  
  private String thumbNail() {
    String args = new String();
    try {
      args = ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Messages.thumbnail"));
    } catch (NullPointerException exception) {
      System.out.println("Something in the config is wrong in the plugin" + this.plugin.getName());
    } 
    return args;
  }
  
  private void sendMessage(CommandSender player, String string) {
    player.sendMessage(ChatColor.translateAlternateColorCodes('&', string));
  }
  
  public void sendMessage(Player player, String string) {
    player.sendMessage(ChatColor.translateAlternateColorCodes('&', string));
  }
  
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    List<String> completions = new ArrayList<>();
    if (args.length == 1) {
      completions.add("pay");
      completions.add("balance");
      if (sender.hasPermission("DNKeconomy.admin.help")) {
        completions.add("deposit");
        completions.add("remove");
        completions.add("disable");
        completions.add("reload");
      } 
      return completions;
    } 
    List<String> playerNames = new ArrayList<>();
    if (args.length == 2) {
      if (args[0].equalsIgnoreCase("balance") || args[0].equalsIgnoreCase("pay")) {
        Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
        Bukkit.getServer().getOnlinePlayers().toArray((Object[])players);
        for (int i = 0; i < players.length; i++)
          playerNames.add(players[i].getName()); 
        return playerNames;
      } 
      if (sender.hasPermission("NoEconomy.admin.help") && (
        args[0].equalsIgnoreCase("deposit") || args[0].equalsIgnoreCase("remove"))) {
        Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
        Bukkit.getServer().getOnlinePlayers().toArray((Object[])players);
        for (int i = 0; i < players.length; i++)
          playerNames.add(players[i].getName()); 
        return playerNames;
      } 
    } 
    if (args.length == 3 && (
      args[0].equalsIgnoreCase("deposit") || args[0].equalsIgnoreCase("remove"))) {
      List<String> completion = new ArrayList<>();
      completion.add("1");
      completion.add("10");
      completion.add("100");
      completion.add("1000");
      completion.add("10000");
      return completion;
    } 
    return null;
  }
}