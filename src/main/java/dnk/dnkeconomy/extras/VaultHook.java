package dnk.dnkeconomy.extras;

import dnk.dnkeconomy.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;

public class VaultHook {
  private Main plugin = (Main)Main.getPlugin(Main.class);
  
  private Managers provider;
  
  public void hook() {
    this.provider = this.plugin.managers;
    Bukkit.getServicesManager().register(Managers.class, this.provider, (Plugin)this.plugin, ServicePriority.Highest);
    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "VaultAPI hooked into " + ChatColor.AQUA + this.plugin.getName());
  }
  
  public void unhook() {
    Bukkit.getServicesManager().unregister(Main.class, this.provider);
    Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "VaultAPI unhooked from " + ChatColor.AQUA + this.plugin.getName());
  }
}