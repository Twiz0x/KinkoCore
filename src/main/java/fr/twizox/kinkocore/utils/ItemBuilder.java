package fr.twizox.kinkocore.utils;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.*;
import java.util.function.Function;

public class ItemBuilder {

    private ItemStack itemStack;
    private static int minecraftVersion = -1;
    private static boolean usePotionSetColor = false;

    /**
     *
     */
    private static void init() {
        try {
            String[] split = Bukkit.getServer().getClass().getPackage().getName().substring(23).split("_");
            minecraftVersion = Integer.parseInt(split[1]);
        } catch (Exception var7) {
            minecraftVersion = 11;
            var7.printStackTrace();
        }

        try {
            Class color = Class.forName("org.bukkit.Color");
            usePotionSetColor = PotionMeta.class.getMethod("setColor", color) != null;
        } catch (Exception e) {
            usePotionSetColor = false;
        }
    }

    /**
     * Create a new ItemBuilder from scratch.
     *
     * @param m The material to create the ItemBuilder with.
     */
    public ItemBuilder(Material m) {
        this(m, 1);
    }

    /**
     * Create a new ItemBuilder over an existing itemstack.
     *
     * @param itemStack The itemstack to create the ItemBuilder over.
     */
    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Create a new ItemBuilder from scratch.
     *
     * @param m      The material of the item.
     * @param amount The evaluate of the item.
     */
    public ItemBuilder(Material m, int amount) {
        this(m, amount, (short) 0);
    }

    /**
     * Create a new ItemBuilder from scratch.
     *
     * @param m          The material of the item.
     * @param amount     The evaluate of the item.
     * @param durability The durability of the item.
     */
    public ItemBuilder(Material m, int amount, short durability) {
        this(m, amount, durability, null);
    }

    /**
     * Create a new ItemBuilder from scratch.
     *
     * @param m          The material of the item.
     * @param amount     The evaluate of the item.
     * @param durability The durability of the item.
     */
    public ItemBuilder(Material m, int amount, short durability, Byte data) {
        if (minecraftVersion == -1) init();

        if (minecraftVersion >= 13) {
            if (data != null && data > 0 && data <= 15) {
                itemStack = new ItemStack(m, amount);
                setDurability(durability);

                // Ensure that we only allow applying data on valid objects.
                if (itemStack.getType().name().contains("STAINED_GLASS")) {
                    setData(GlassColor.values()[data]);
                } else if (itemStack.getType().name().contains("WOOL")) {
                    setData(WoolColor.values()[data]);
                } else if (itemStack.getType().name().contains("DYE") || DyeColor.is(itemStack.getType())) {
                    setData(DyeColor.values()[data]);
                }
            } else {
                // if (!m.name().startsWith("LEGACY"))
                //     m = Material.getMaterial("LEGACY_" + m.name());
                itemStack = new ItemStack(m, amount);
                setDurability(durability);
            }
        } else {
            itemStack = new ItemStack(m, amount, durability, data);
        }

        if (itemStack == null) throw new IllegalStateException();
    }

    public static ItemBuilder create(ItemStack from) {
        return new ItemBuilder(from);
    }

    public ItemBuilder setData(GlassColor color) {
        if (itemStack.getType().name().contains("STAINED_GLASS")) {
            color.apply(this);
        } else
            throw new IllegalArgumentException("Cannot apply " + color.getClass().getSimpleName() + " on " + itemStack.getType());
        return this;
    }

    public ItemBuilder setData(WoolColor color) {
        if (itemStack.getType().name().contains("WOOL")) {
            color.apply(this);
        } else
            throw new IllegalArgumentException("Cannot apply " + color.getClass().getSimpleName() + " on " + itemStack.getType());
        return this;
    }

    public ItemBuilder setData(DyeColor color) {
        if (itemStack.getType().name().contains("DYE") || DyeColor.is(itemStack.getType())) {
            color.apply(this);
        } else
            throw new IllegalArgumentException("Cannot apply " + color.getClass().getSimpleName() + " on " + itemStack.getType());
        return this;
    }

    public ItemBuilder setData(PotionColor color) {
        if (itemStack.getType().name().contains("POTION")) {
            color.apply(this);
        } else
            throw new IllegalArgumentException("Cannot apply " + color.getClass().getSimpleName() + " on " + itemStack.getType());
        return this;
    }

    /**
     * Clone the ItemBuilder into a new one.
     *
     * @return The cloned instance.
     */
    public ItemBuilder clone() {
        return new ItemBuilder(itemStack.clone());
    }

    /**
     * Change the durability of the item.
     *
     * @param dur The durability to set it to.
     */
    public ItemBuilder setDurability(short dur) {
        itemStack.setDurability(dur);
        return this;
    }

    /**
     * Change the durability of the item.
     *
     * @param data The durability to set it to.
     */
    @Deprecated
    public ItemBuilder setData(byte data) {
        itemStack.setData(new MaterialData(itemStack.getType(), data));
        return this;
    }

    /**
     * Change the unbreakable of the item.
     *
     * @param unbreakable The unbreakable to set it to.
     */
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        if (itemStack.getType() == Material.AIR) return this;
        ItemMeta meta = itemStack.getItemMeta();
        if (minecraftVersion >= 13) {
            meta.setUnbreakable(unbreakable);
        } else meta.spigot().setUnbreakable(unbreakable);
        itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Change the evaluate of the item.
     *
     * @param amount The evaluate to set it to.
     */
    public ItemBuilder setAmount(int amount) {
        if (itemStack.getType() == Material.AIR) return this;
        itemStack.setAmount(amount);
        return this;
    }

    /**
     * Add an item flag
     *
     * @param flags item flagS
     */
    public ItemBuilder addFlags(ItemFlag... flags) {
        if (itemStack.getType() == Material.AIR) return this;
        ItemMeta im = itemStack.getItemMeta();
        im.addItemFlags(flags);
        itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Remove an item flag
     *
     * @param flags item flagS
     */
    public ItemBuilder removeFlags(ItemFlag... flags) {
        if (itemStack.getType() == Material.AIR) return this;
        ItemMeta im = itemStack.getItemMeta();
        im.removeItemFlags(flags);
        itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Set the displayname of the item.
     *
     * @param name The name to change it to.
     */
    public ItemBuilder setName(String name) {
        if (itemStack.getType() == Material.AIR || name == null || name.isEmpty()) return this;
        ItemMeta im = itemStack.getItemMeta();
        im.setDisplayName(name);
        itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Add an unsafe enchantment.
     *
     * @param ench  The enchantment to add.
     * @param level The level to put the enchant on.
     */
    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level) {
        if (itemStack.getType() == Material.AIR) return this;
        itemStack.addUnsafeEnchantment(ench, level);
        return this;
    }

    /**
     * Remove a certain enchant from the item.
     *
     * @param ench The enchantment to remove
     */
    public ItemBuilder removeEnchantment(Enchantment ench) {
        if (itemStack.getType() == Material.AIR) return this;
        itemStack.removeEnchantment(ench);
        return this;
    }

    /**
     * Set the skull getOwner for the item. Works on skulls only.
     *
     * @param owner The name of the skull's getOwner.
     */
    public ItemBuilder setSkullOwner(String owner) {
        if (itemStack.getType() != Material.SKULL_ITEM) return this;
        SkullMeta im = (SkullMeta) itemStack.getItemMeta();
        im.setOwner(owner);
        itemStack.setItemMeta(im);
        return this;
    }

    public ItemBuilder setSkullOfflineOwner(OfflinePlayer owner) {
        if (itemStack.getType() != Material.SKULL_ITEM) return this;
        SkullMeta im = (SkullMeta) itemStack.getItemMeta();
        im.setOwningPlayer(owner);
        itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Add an enchant to the item.
     *
     * @param ench  The enchant to add
     * @param level The level
     */
    public ItemBuilder addEnchant(Enchantment ench, int level) {
        if (itemStack.getType() == Material.AIR) return this;
        ItemMeta im = itemStack.getItemMeta();
        im.addEnchant(ench, level, true);
        itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Add multiple enchants at once.
     *
     * @param enchantments The enchants to add.
     */
    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
        if (itemStack.getType() == Material.AIR) return this;
        itemStack.addEnchantments(enchantments);
        return this;
    }

    /**
     * Sets infinity durability on the item by setting the durability to Short.MAX_VALUE.
     */
    public ItemBuilder setInfinityDurability() {
        if (itemStack.getType() == Material.AIR) return this;
        itemStack.setDurability(Short.MAX_VALUE);
        return this;
    }

    /**
     * Re-sets the lore.
     *
     * @param lore The lore to set it to.
     */
    public ItemBuilder setLore(String... lore) {
        ItemMeta im = itemStack.getItemMeta();
        im.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Re-sets the lore.
     *
     * @param lore The lore to set it to.
     */
    public ItemBuilder setLore(List<String> lore) {
        if (itemStack.getType() == Material.AIR) return this;
        ItemMeta im = itemStack.getItemMeta();
        im.setLore(lore);
        itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Remove a lore line.
     *
     * @param line The lore to remove.
     */
    public ItemBuilder removeLoreLine(String line) {
        if (itemStack.getType() == Material.AIR) return this;
        ItemMeta im = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if (!lore.contains(line)) return this;
        lore.remove(line);
        im.setLore(lore);
        itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Remove a lore line.
     *
     * @param index The index of the lore line to remove.
     */
    public ItemBuilder removeLoreLine(int index) {
        if (itemStack.getType() == Material.AIR) return this;
        ItemMeta im = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if (index < 0 || index > lore.size()) return this;
        lore.remove(index);
        im.setLore(lore);
        itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Add a lore line.
     *
     * @param line The lore line to add.
     */
    public ItemBuilder addLoreLine(String line) {
        if (itemStack.getType() == Material.AIR) return this;
        ItemMeta im = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (im.hasLore()) lore = new ArrayList<>(im.getLore());
        lore.add(line);
        im.setLore(lore);
        itemStack.setItemMeta(im);
        return this;
    }

    public ItemBuilder addLoreLine(String... lines) {
        if (itemStack.getType() == Material.AIR) return this;
        for (String line : lines) {
            addLoreLine(line);
        }
        return this;
    }

    public ItemBuilder addLoreLine(Collection<String> lines) {
        for (String line : lines) {
            addLoreLine(line);
        }
        return this;
    }

    /**
     * Add a lore line.
     *
     * @param line The lore line to add.
     * @param pos  The index of where to put it.
     */
    public ItemBuilder addLoreLine(String line, int pos) {
        ItemMeta im = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        lore.set(pos, line);
        im.setLore(lore);
        itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Direct access to the item meta
     *
     * @param modifier function to modify the meta.
     */
    public ItemBuilder writeMeta(
            Function<ItemMeta, ItemMeta> modifier
    ) {
        if (itemStack.getType() == Material.AIR) return this;
        itemStack.setItemMeta(modifier.apply(itemStack.getItemMeta()));
        return this;
    }

    /**
     * Retrieves the itemstack from the ItemBuilder.
     *
     * @return The itemstack created/modified by the ItemBuilder instance.
     */
    public ItemStack toItemStack() {
        return itemStack;
    }

    public enum GlassColor {
        WHITE(0),
        ORANGE(1),
        MAGENTA(2),
        LIGHT_BLUE(3),
        YELLOW(4),
        LIME(5),
        PINK(6),
        GRAY(7),
        LIGHT_GRAY(8),
        CYAN(9),
        PURPLE(10),
        BLUE(11),
        BROWN(12),
        GREEN(13),
        RED(14),
        BLACK(15);

        private final byte dataColor;

        GlassColor(int dataColor) {
            this.dataColor = (byte) (dataColor);
        }

        private void apply(ItemBuilder builder) {
            if (minecraftVersion >= 13) {
                boolean panel = builder.itemStack.getType().name().contains("STAINED_GLASS_PANE");
                builder.itemStack.setType(Material.valueOf(name() + (panel ? "_STAINED_GLASS_PANE" : "STAINED_GLASS")));
            } else {
                builder.itemStack.setData(new MaterialData(builder.itemStack.getType(), dataColor));
            }
        }
    }

    public enum WoolColor {
        WHITE(0x0),
        ORANGE(0x1),
        MAGENTA(0x2),
        LIGHT_BLUE(0x3),
        YELLOW(0x4),
        LIME(0x5),
        PINK(0x6),
        GRAY(0x7),
        LIGHT_GRAY(0x8),
        CYAN(0x9),
        PURPLE(0xA),
        BLUE(0xB),
        BROWN(0xC),
        GREEN(0xD),
        RED(0xE),
        BLACK(0xF);

        private final byte dataColor;

        WoolColor(int dataColor) {
            this.dataColor = (byte) (dataColor);
        }

        private void apply(ItemBuilder builder) {
            if (minecraftVersion >= 13) {
                builder.itemStack.setType(Material.valueOf(name() + "_WOOL"));
            } else {
                builder.itemStack.setData(new MaterialData(builder.itemStack.getType(), dataColor));
            }
        }
    }

    public enum DyeColor {
        BLACK(0, "INK_SACK"),
        RED(1, "ROSE_RED"),
        GREEN(2, "CACTUS_GREEN"),
        BROWN(3, "COCOA_BEANS"),
        BLUE(4, "LAPIS_LAZULI"),
        PURPLE(5, "PURPLE_DYE"),
        CYAN(6, "CYAN_DYE"),
        LIGHT_GRAY(7, "LIGHT_GRAY_DYE"),
        GRAY(8, "GRAY_DYE"),
        PINK(9, "PINK_DYE"),
        LIME(10, "LIME_DYE"),
        YELLOW(11, "DANDELION_YELLOW"),
        LIGHT_BLUE(12, "LIGHT_BLUE_DYE"),
        MAGENTA(13, "MAGENTA_DYE"),
        ORANGE(14, "ORANGE_DYE"),
        WHITE(15, "BONE_MEAL");

        private final byte data;
        private final String coded;

        DyeColor(int data, String coded) {
            this.data = (byte) data;
            this.coded = coded;
        }

        static boolean is(Material mat) {
            if (mat.name().contains("DYE")) return true;
            if (mat.name().contains("INK_SAC")) return true;

            for (DyeColor dyeColor : values()) {
                if (dyeColor.name().equalsIgnoreCase(mat.name()))
                    return true;
            }

            return false;
        }

        private void apply(ItemBuilder builder) {
            if (minecraftVersion >= 13) {
                builder.itemStack.setType(Material.valueOf(coded));
            } else {
                builder.itemStack.setData(new MaterialData(builder.itemStack.getType(), data));
            }
        }
    }

    public enum PotionColor {
        PINK("REGENERATION", "REGEN", 0xF442E2),
        CYAN("SPEED", "SPEED", 0x42f1f4),
        GOLD("FIRE_RESISTANCE", "FIRE_RESISTANCE", 0xf4b942),
        DARK_GREEN("POISON", "POISON", 0x365b0e),
        RED("HEAL", "INSTANT_HEAL", 0xe5251b),
        DARK_BLUE("NIGHT_VISION", "NIGHT_VISION", 0x092366),
        DARK_GRAY("WEAKNESS", "WEAKNESS", 0x2e2f33),
        DARK_RED("INCREASE_DAMAGE", "STRENGTH", 0x4f0a01),
        GRAY("SLOW", "SLOWNESS", 0x939393),
        LIGHT_GREEN("JUMP", "JUMP", 0x42f4b0),
        BROWN("HARM", "INSTANT_DAMAGE", 0xa54126),
        BLUE("WATER_BREATHING", "WATER_BREATHING", 0x0e59ef), // TEAL
        LIGHT_GRAY("INVISIBILITY", "INVISIBILITY", 0xc4c4c4),
        GREEN("LUCK", "LUCK", 0x1f890f),
        BLACK("WITHER", null, 0x161616),
        LIGHT_BROWN("SLOW", "TURTLE_MASTER", 0xad581b),
        SILVER("SLOW_FALLING", "SLOW_FALLING", 0xd8ccc3);

        private final String potionEffectType;
        private final String potionType;
        private final int rgb;

        PotionColor(String potionEffectType, String potionType, int rgb) {
            this.potionEffectType = potionEffectType;
            this.potionType = potionType;
            this.rgb = rgb;
        }

        private void apply(ItemBuilder builder) {
            PotionMeta meta = (PotionMeta) builder.itemStack.getItemMeta();
            if (usePotionSetColor) {
                meta.setColor(Color.fromRGB(rgb));
            } else if (minecraftVersion >= 13 && potionType != null) {
                try {
                    meta.setBasePotionData(new org.bukkit.potion.PotionData(PotionType.valueOf(potionType)));
                } catch (Exception ignored) {
                }
            } else {
                try {
                    meta.setMainEffect(PotionEffectType.getByName(potionEffectType));
                } catch (Exception ignored) {}
            }
            builder.itemStack.setItemMeta(meta);
        }
    }

}
