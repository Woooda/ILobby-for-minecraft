import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ILobby extends JavaPlugin implements Listener {

    private Location lobbyLocation;

    @Override
    public void onEnable() {
        getLogger().info("ILobby has been enabled!");
        saveDefaultConfig();
        loadLobbyLocation();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("ILobby has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setlobby") && sender.hasPermission("ilobby.admin")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command!");
                return true;
            }

            Player player = (Player) sender;
            lobbyLocation = player.getLocation();
            saveLobbyLocation();
            player.sendMessage("Lobby location set!");
            return true;
        } else if (cmd.getName().equalsIgnoreCase("lobby")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command!");
                return true;
            }

            Player player = (Player) sender;
            if (lobbyLocation == null) {
                player.sendMessage("Lobby location is not set!");
                return true;
            }

            player.teleport(lobbyLocation);
            player.sendMessage("You have been teleported to the lobby!");
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore() && lobbyLocation != null) {
            player.teleport(lobbyLocation);
        }
    }

    private void loadLobbyLocation() {
        FileConfiguration config = getConfig();
        if (config.contains("lobbyLocation")) {
            World world = Bukkit.getWorld(config.getString("lobbyLocation.world"));
            double x = config.getDouble("lobbyLocation.x");
            double y = config.getDouble("lobbyLocation.y");
            double z = config.getDouble("lobbyLocation.z");
            float yaw = (float) config.getDouble("lobbyLocation.yaw");
            float pitch = (float) config.getDouble("lobbyLocation.pitch");
            lobbyLocation = new Location(world, x, y, z, yaw, pitch);
        }
    }

    private void saveLobbyLocation() {
        if (lobbyLocation != null) {
            FileConfiguration config = getConfig();
            config.set("lobbyLocation.world", lobbyLocation.getWorld().getName());
            config.set("lobbyLocation.x", lobbyLocation.getX());
            config.set("lobbyLocation.y", lobbyLocation.getY());
            config.set("lobbyLocation.z", lobbyLocation.getZ());
            config.set("lobbyLocation.yaw", lobbyLocation.getYaw());
            config.set("lobbyLocation.pitch", lobbyLocation.getPitch());
            saveConfig();
        }
    }
}
