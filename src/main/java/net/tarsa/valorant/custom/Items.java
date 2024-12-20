package net.tarsa.valorant.custom;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.tarsa.valorant.ValorantMod;
import net.tarsa.valorant.custom.items.CloudBurstItem;
import net.tarsa.valorant.custom.items.JettKnifeItem;
import net.tarsa.valorant.custom.items.TarsaValorantItem;

public class Items {
    public static final Item TARSA_VAL_LOGO = registerItem("tarsa-valorant-item", new TarsaValorantItem(new FabricItemSettings()));
    public static final Item CloudBurstItem = registerItem("cloudburst-item", new CloudBurstItem(new FabricItemSettings()));
    public static final Item JettKnifeItem = registerItem("jett-knife-item", new JettKnifeItem(new FabricItemSettings()));

    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries) {

    }
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(ValorantMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        ValorantMod.LOGGER.info("Registering Mod Items for " + ValorantMod.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(Items::addItemsToIngredientItemGroup);
    }
}
