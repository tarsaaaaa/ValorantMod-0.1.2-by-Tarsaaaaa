package net.tarsa.valorant.custom;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.tarsa.valorant.ValorantMod;

public class Sounds {
    public static final SoundEvent JETT_CLOUDBURST_SOUND = registerSoundEvent("jett-cloudburst-sound");
    public static final SoundEvent JETT_UPDRAFT_SOUND = registerSoundEvent("jett-updraft-sound");
    public static final SoundEvent JETT_TAILWIND_CHARGE_SOUND = registerSoundEvent("jett-tailwind-charge-sound");
    public static final SoundEvent JETT_TAILWIND_TIMEOUT_SOUND = registerSoundEvent("jett-tailwind-timeout-sound");
    public static final SoundEvent JETT_TAILWIND_DASH_SOUND = registerSoundEvent("jett-tailwind-dash-sound");
    public static final SoundEvent JETT_BLADESTORM_SUMMON_SOUND = registerSoundEvent("jett-bladestorm-summon-sound");
    public static final SoundEvent JETT_BLADESTORM_SINGLE_SOUND = registerSoundEvent("jett-bladestorm-single-sound");


    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(ValorantMod.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        ValorantMod.LOGGER.info("Registering Sounds for " + ValorantMod.MOD_ID);
    }
}
