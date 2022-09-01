package fr.twizox.kinkocore.menus;

import dev.simplix.cirrus.common.business.PlayerWrapper;
import dev.simplix.cirrus.common.configuration.MultiPageMenuConfiguration;
import dev.simplix.cirrus.common.menus.MultiPageMenu;
import fr.twizox.kinkocore.KinkoCore;
import fr.twizox.kinkocore.prime.Prime;
import fr.twizox.kinkocore.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class PrimeMenu extends MultiPageMenu {

    public PrimeMenu(PlayerWrapper player, MultiPageMenuConfiguration configuration) {
        super(player, configuration, Locale.FRENCH);
        // addItems();
    }

    private void addItems() {
        List<Prime> primes = KinkoCore.primeManager.getSortedPrimesByAmount();

        int i = 1;
        for (Prime prime : primes) {
            ItemBuilder itemBuilder = new ItemBuilder(new ItemStack(Material.SKULL_ITEM, 1, (short) 3))
                    .setSkullOfflineOwner(Bukkit.getOfflinePlayer(prime.getTarget()))
                    .setName("§l§e§o#" + i + " §b" + Bukkit.getOfflinePlayer(prime.getTarget()).getName())
                    .setLore("",
                            "§7Montant: §6§l" + prime.getAmount() + " berrys",
                            "",
                            "§7" + prime.getLocalDate().format(DateTimeFormatter.ofPattern("'Le' dd/MM 'à' HH'h'", Locale.FRANCE)));
            add(wrapItemStack(itemBuilder.toItemStack()), "", Collections.emptyList());
            i++;
        }

    }

}
