package ru.elken.Rey_rHideTags.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.potion.PotionEffectType;
import ru.elken.Rey_rHideTags.Main;

import java.util.HashMap;
import java.util.Map;

import static ru.elken.Rey_rHideTags.Main.colors;

public class Interact implements Listener {
    private Map<String, Boolean> triggered = new HashMap();

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        if (!(e.getRightClicked() instanceof Player) ||
                (((Player) e.getRightClicked()).getActivePotionEffects().stream().anyMatch(
                        potion -> potion.getType().equals(PotionEffectType.INVISIBILITY)
                ) && !e.getPlayer().isOp() //&& Arrays.stream(((Player) e.getRightClicked()).getEquipment().getArmorContents()).noneMatch(Objects::nonNull)
                  && !e.getPlayer().getGameMode().equals(GameMode.SPECTATOR))
        ) return;

        Player p = Bukkit.getPlayer(e.getRightClicked().getUniqueId());
        if (Main.actionbar) {
            e.getPlayer().sendActionBar(
                    LegacyComponentSerializer.legacySection().deserialize(
                            colors(
                                    PlaceholderAPI.setPlaceholders(p,
                                            Main.actionbar_message.replace("$name", p.getName())
                                    )
                            )
                    )
            );
        }

        if (Main.chat) {
            if (!triggered.getOrDefault(
                    e.getPlayer().getName(), false
            )) {
                e.getPlayer().sendMessage(
                        colors(
                                PlaceholderAPI.setPlaceholders(p,
                                        Main.chat_message.replace("$name", p.getName())
                                )
                        )
                );
                triggered.put(
                        e.getPlayer().getName(), true
                );
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.main, () -> {
                    triggered.put(
                            e.getPlayer().getName(), false
                    );
                }, 20);
            }
        }
    }
}
