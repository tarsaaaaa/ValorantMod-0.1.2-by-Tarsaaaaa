package net.tarsa.valorant.custom;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.tarsa.valorant.ValorantMod;

public class ItemsGroup {
    public static final ItemGroup RUBY_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(ValorantMod.MOD_ID, "valorant"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.valorant"))
                    .icon(() -> new ItemStack(Items.TARSA_VAL_LOGO)).entries((displayContext, entries) -> {
                        entries.add(Items.CloudBurstItem);
                        entries.add(Items.JettKnifeItem);
                    }).build());


    public static void registerItemGroups() {
        ValorantMod.LOGGER.info("Registering Item Groups for " + ValorantMod.MOD_ID);
    }
}
