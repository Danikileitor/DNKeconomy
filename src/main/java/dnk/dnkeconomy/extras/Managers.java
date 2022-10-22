package dnk.dnkeconomy.extras;

import dnk.dnkeconomy.Main;
import java.util.List;
import java.util.UUID;
import java.lang.Math;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class Managers implements Economy {
    private final Main plugin = (Main) Main.getPlugin(Main.class);

    public boolean isEnabled() {
        return false;
    }

    public String getName() {
        return "DNKeconomy";
    }

    public boolean hasBankSupport() {
        return false;
    }

    public int fractionalDigits() {
        return Integer.parseInt(this.plugin.getConfig().getString("Decimals"));
    }

    public String format(double v) {
        return null;
    }

    public String currencyNamePlural() {
        return this.plugin.getConfig().getString("MoneyNamePlural");
    }

    public String currencyNameSingular() {
        return this.plugin.getConfig().getString("MoneyNameSingular");
    }

    public boolean hasAccount(String s) {
        return false;
    }

    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    public boolean hasAccount(String s, String s1) {
        return false;
    }

    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }

    public double getBalance(String s) {
        Player player = Bukkit.getPlayer(s);
        UUID uuid = player.getUniqueId();
        return Main.get().getDouble(uuid + ".money");
    }

    public double getBalance(OfflinePlayer offlinePlayer) {
        UUID uuid = offlinePlayer.getUniqueId();
        return Main.get().getDouble(uuid + ".money");
    }

    public double getBalance(String s, String s1) {
        Player player = Bukkit.getPlayer(s);
        UUID uuid = player.getUniqueId();
        return Main.get().getDouble(uuid + ".money");
    }

    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        UUID uuid = offlinePlayer.getUniqueId();
        return Main.get().getDouble(uuid + ".money");
    }

    public boolean has(String s, double v) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(s);
        UUID uuid = offlinePlayer.getUniqueId();
        Double cool = Double.valueOf(Main.get().getDouble(uuid + ".money", Double.valueOf(0.0D).doubleValue()));
        return (cool.doubleValue() >= v);
    }

    public boolean has(OfflinePlayer offlinePlayer, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        Double cool = Double.valueOf(Main.get().getDouble(uuid + ".money", Double.valueOf(0.0D).doubleValue()));
        return (cool.doubleValue() >= v);
    }

    public boolean has(String s, String s1, double v) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(s);
        UUID uuid = offlinePlayer.getUniqueId();
        Double cool = Double.valueOf(Main.get().getDouble(uuid + ".money", Double.valueOf(0.0D).doubleValue()));
        return (cool.doubleValue() >= v);
    }

    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        Double cool = Double.valueOf(Main.get().getDouble(uuid + ".money", Double.valueOf(0.0D).doubleValue()));
        return (cool.doubleValue() >= v);
    }

    public EconomyResponse withdrawPlayer(String s, double v) {
        Player target = this.plugin.getServer().getPlayer(s);
        return withPlayerStuff((OfflinePlayer) target, (Math.round(v * Math.pow(10, fractionalDigits()) ) / Math.pow(10, fractionalDigits())));
    }

    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        return withPlayerStuff(offlinePlayer, (Math.round(v * Math.pow(10, fractionalDigits()) ) / Math.pow(10, fractionalDigits())));
    }

    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        Player player = Bukkit.getPlayer(s);
        return withPlayerStuff((OfflinePlayer) player, (Math.round(v * Math.pow(10, fractionalDigits()) ) / Math.pow(10, fractionalDigits())));
    }

    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return withPlayerStuff(offlinePlayer, (Math.round(v * Math.pow(10, fractionalDigits()) ) / Math.pow(10, fractionalDigits())));
    }

    private EconomyResponse withPlayerStuff(OfflinePlayer player, double amt) {
        if (getBalance(player) - amt >= 0.0D) {
            UUID uuid = player.getUniqueId();
            double balold = Main.get().getDouble(uuid + ".money");
            Main.get().set(player.getUniqueId() + ".money", Double.valueOf(balold - (Math.round(amt * Math.pow(10, fractionalDigits()) ) / Math.pow(10, fractionalDigits()))));
            Main.save();
            return responseSuccess(amt, getBalance(player));
        }
        return responseFailed(amt, getBalance(player));
    }

    public EconomyResponse depositPlayer(String s, double v) {
        Player player = Bukkit.getPlayerExact(s);
        UUID uuid = player.getUniqueId();
        double balold = Main.get().getDouble(uuid + ".money");
        this.plugin.playerBank.put(uuid, Double.valueOf(balold + (Math.round(v * Math.pow(10, fractionalDigits()) ) / Math.pow(10, fractionalDigits()))));
        Main.get().set(player.getUniqueId() + ".money", Double.valueOf(balold + (Math.round(v * Math.pow(10, fractionalDigits()) ) / Math.pow(10, fractionalDigits()))));
        Main.save();
        return responseSuccess(v, getBalance((OfflinePlayer) player));
    }

    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        double balold = Main.get().getDouble(uuid + ".money");
        this.plugin.playerBank.put(uuid, Double.valueOf(balold + (Math.round(v * Math.pow(10, fractionalDigits()) ) / Math.pow(10, fractionalDigits()))));
        Main.get().set(uuid + ".money", Double.valueOf(balold + (Math.round(v * Math.pow(10, fractionalDigits()) ) / Math.pow(10, fractionalDigits()))));
        Main.save();
        return responseSuccess(v, getBalance(offlinePlayer));
    }

    public EconomyResponse depositPlayer(String s, String s1, double v) {
        Player player = Bukkit.getPlayerExact(s);
        UUID uuid = player.getUniqueId();
        double balold = Main.get().getDouble(uuid + ".money");
        this.plugin.playerBank.put(uuid, Double.valueOf(balold + (Math.round(v * Math.pow(10, fractionalDigits()) ) / Math.pow(10, fractionalDigits()))));
        Main.get().set(uuid + ".money", Double.valueOf(balold + (Math.round(v * Math.pow(10, fractionalDigits()) ) / Math.pow(10, fractionalDigits()))));
        Main.save();
        return responseSuccess(v, getBalance((OfflinePlayer) player));
    }

    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        double balold = Main.get().getDouble(uuid + ".money");
        this.plugin.playerBank.put(uuid, Double.valueOf(balold + (Math.round(v * Math.pow(10, fractionalDigits()) ) / Math.pow(10, fractionalDigits()))));
        Main.get().set(uuid + ".money", Double.valueOf(balold + (Math.round(v * Math.pow(10, fractionalDigits()) ) / Math.pow(10, fractionalDigits()))));
        Main.save();
        return responseSuccess(v, getBalance(offlinePlayer));
    }

    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    public EconomyResponse deleteBank(String s) {
        return null;
    }

    public EconomyResponse bankBalance(String s) {
        return null;
    }

    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }

    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }

    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    public List<String> getBanks() {
        return null;
    }

    public boolean createPlayerAccount(String s) {
        return false;
    }

    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    public boolean createPlayerAccount(String s, String s1) {
        return false;
    }

    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }

    private EconomyResponse responseSuccess(double amountChange, double balance) {
        return new EconomyResponse(amountChange, balance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    private EconomyResponse responseFailed(double amountChange, double balance) {
        return new EconomyResponse(amountChange, balance, EconomyResponse.ResponseType.FAILURE, null);
    }
}