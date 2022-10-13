package dnk.dnkeconomy;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;
import dnk.dnkeconomy.extras.Managers;
import dnk.dnkeconomy.extras.VaultHook;
import dnk.dnkeconomy.extras.Commands;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.command.CommandExecutor;

public class Main extends JavaPlugin implements Listener {

	private static File file;
	private static FileConfiguration customFile;
	public HashMap<UUID, Double> playerBank = new HashMap<>();
	public Managers managers;
	private VaultHook vaultHook;
	private Economy Econ;

	public static void setup() {
		file = new File(Bukkit.getServer().getPluginManager().getPlugin("DNKeconomy").getDataFolder(), "data.yml");
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException iOException) {
			}
		customFile = (FileConfiguration) YamlConfiguration.loadConfiguration(file);
	}

	public static FileConfiguration get() {
		return customFile;
	}

	public static void save() {
		try {
			customFile.save(file);
		} catch (IOException e) {
			System.out.println("Couldn't save file");
		}
	}

	public static void reload() {
		customFile = (FileConfiguration) YamlConfiguration.loadConfiguration(file);
	}

	@Override
	public void onEnable() {
		Logger log = Bukkit.getLogger();
		this.Econ = (Economy) new Managers();
		getServer().getServicesManager().register(Economy.class, this.Econ,
				getServer().getPluginManager().getPlugin("Vault"), ServicePriority.Normal);
		getConfig().options().copyDefaults(true);
		saveConfig();
		saveDefaultConfig();
		this.managers = new Managers();
		this.vaultHook = new VaultHook();
		try {
			getCommand("eco").setExecutor((CommandExecutor) new Commands());
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		getServer().getPluginManager().registerEvents(this, this);
		this.vaultHook.hook();
		Main.setup();
		Main.get().options().copyDefaults(true);
		Main.save();
		log.info("[DNKeconomy] $Ready to cash!$");
	}

	@Override
	public void onDisable() {
		Logger log = Bukkit.getLogger();
		log.info("[DNKeconomy] Que pase buena tarde");
	}

	@EventHandler
	public void onJoinEvent(PlayerJoinEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		if (Main.get().get(uuid + ".money") == null) {
			Main.get().set(uuid + ".money", Integer.valueOf(0));
			Main.save();
		}
	}
}