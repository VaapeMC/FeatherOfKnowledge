package me.vaape.featherofknowledge;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class FeatherOfKnowledge extends JavaPlugin implements Listener{
	
	private ArrayList<UUID> inInventory = new ArrayList<UUID>();
	
	public static FeatherOfKnowledge plugin;
	
	public void onEnable() {
		plugin = this;
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info(ChatColor.GREEN + "FeatherOfKnowledge has been enabled!");
	}
	
	public void onDisable(){
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			
			if (cmd.getName().equalsIgnoreCase("feather")) {
				
				if (player.getGameMode() == GameMode.CREATIVE) {
					
					ItemStack feather = new ItemStack(Material.FEATHER);
					feather.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
					ItemMeta meta = feather.getItemMeta();
					meta.setDisplayName(ChatColor.AQUA + "Feather of Knowledge");
					List<String> lore = new ArrayList<String>();
					lore.add(ChatColor.DARK_AQUA + "A feather taken from Ra");
					meta.setLore(lore);
					feather.setItemMeta(meta);
					player.getInventory().addItem(feather);
				}
				else {
					player.sendMessage("Unknown command. Type \"/help\" for help.");
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		
		if(event.getHand().equals(EquipmentSlot.HAND)) {
		
			if (event.getRightClicked() instanceof Player) {
			
				ItemStack hand = player.getInventory().getItemInMainHand();
			
				if (hand.getType() == Material.FEATHER) {
				
					if (!(hand.getItemMeta().getLore().isEmpty())) {
			
						Player clicked = (Player) event.getRightClicked();
						ItemStack[] inv = clicked.getInventory().getContents();
						Inventory display = Bukkit.createInventory(null, 45, clicked.getDisplayName());
						display.setContents(inv);
					
						hand.setAmount(hand.getAmount() - 1);
			
						inInventory.add(player.getUniqueId());
						player.openInventory(display);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClick (InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			if (inInventory.contains(player.getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onInventoryExit (InventoryCloseEvent event) {
		if (event.getPlayer() instanceof Player) {
			Player player = (Player) event.getPlayer();
			if (inInventory.contains(player.getUniqueId())) {
				inInventory.remove(player.getUniqueId());
			}
		}
	}
}
