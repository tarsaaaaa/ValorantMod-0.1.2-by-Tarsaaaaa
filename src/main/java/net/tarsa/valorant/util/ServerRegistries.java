package net.tarsa.valorant.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.tarsa.valorant.ValorantMod;
import net.tarsa.valorant.custom.Items;
import net.tarsa.valorant.custom.ItemsGroup;
import net.tarsa.valorant.custom.Sounds;

public class ServerRegistries {
    public static Identifier SYNC_AGENTS = new Identifier(ValorantMod.MOD_ID,"sync-valorant-agents");
    public static Identifier SELECT_AGENTS = new Identifier(ValorantMod.MOD_ID,"select-valorant-agent");
    public static Identifier VALORANT_GAMERULE_SET = new Identifier(ValorantMod.MOD_ID,"valorant-gamerule-set");
    public static Identifier VALORANT_GAMERULE_GET = new Identifier(ValorantMod.MOD_ID,"valorant-gamerule-get");
    public static Identifier EDIT_AGENTS = new Identifier(ValorantMod.MOD_ID,"edit-valorant-agents");
    public static Identifier FILE_WRITE_PACKET = new Identifier(ValorantMod.MOD_ID,"file-write-packet");
    public static Identifier FILE_MODIFY_PACKET = new Identifier(ValorantMod.MOD_ID,"file-modify-packet");
    public static Identifier SUMMON_JETT_CLOUDBURST = new Identifier(ValorantMod.MOD_ID,"summon-jett-cloudburst");
    public static Identifier SUMMON_JETT_BLADESTORM = new Identifier(ValorantMod.MOD_ID,"summon-jett-bladestorm");


    public static final RegistryKey<DamageType> BLADESTORM_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(ValorantMod.MOD_ID, "bladestorm-damage-type"));

    public static void RegisterServerStuff(){
        RegisterCommands();
        ServerPacketHandler.registerServerPacketReceiver();
        Items.registerModItems();
        ItemsGroup.registerItemGroups();
        Sounds.registerSounds();
    }

    private static void RegisterCommands(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ValorantCommands.register(dispatcher);
        });
    }


    public static DamageSource of(World world, RegistryKey<DamageType> key) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
    }
}
