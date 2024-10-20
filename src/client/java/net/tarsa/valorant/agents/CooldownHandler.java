package net.tarsa.valorant.agents;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CooldownHandler {
    public static boolean doCooldown;

    public static void ruleCooldown(boolean doCooldown) {
        CooldownHandler.doCooldown = doCooldown;
    }

    public static boolean isDoCooldown() {
        return doCooldown;
    }

    private static long cooldowns[] = {0,0,0,0};

    public void setCooldown(String ability, long cooldownTimeInMillis) {
        if (!doCooldown){
            return;
        }
        long cooldownEnd = System.currentTimeMillis() + cooldownTimeInMillis;
        switch (ability) {
            case "first" -> {
                cooldowns[0] = cooldownEnd;
            }
            case "second" -> {
                cooldowns[1] = cooldownEnd;
            }
            case "third" -> {
                cooldowns[2] = cooldownEnd;
            }
            case "ult" -> {
                cooldowns[3] = cooldownEnd;
            }
        }
    }

    public boolean isOnCooldown(String ability) {
        if (!doCooldown){
            return false;
        }
        long abilityCooldowns[] = {this.cooldowns[0],cooldowns[1],cooldowns[2],cooldowns[3]};
        if (abilityCooldowns == null) {
            return false;
        }
        long cooldownEnd = 0;
        switch (ability) {
            case "first" -> cooldownEnd = cooldowns[0];
            case "second" -> cooldownEnd = cooldowns[1];
            case "third" -> cooldownEnd = cooldowns[2];
            case "ult" -> cooldownEnd = cooldowns[3];
        }
        if (cooldownEnd == 0) {
            return false;
        }
        if (System.currentTimeMillis() > cooldownEnd) {
            switch (ability) {
                case "first" -> cooldowns[0] = 0;
                case "second" -> cooldowns[1] = 0;
                case "third" -> cooldowns[2] = 0;
                case "ult" -> cooldowns[3] = 0;
            }
            return false;
        }
        return true;
    }

    public long getRemainingCooldown(String ability) {
        if (!doCooldown){
            return 0;
        }
        long abilityCooldowns[] = cooldowns;
        if (abilityCooldowns == null) {
            return 0;
        }
        long cooldownEnd = 0;
        switch (ability) {
            case "first" -> cooldownEnd = cooldowns[0];
            case "second" -> cooldownEnd = cooldowns[1];
            case "third" -> cooldownEnd = cooldowns[2];
            case "ult" -> cooldownEnd = cooldowns[3];
        }
        if (cooldownEnd == 0) {
            return 0;
        }
        return Math.max(0, cooldownEnd - System.currentTimeMillis());
    }
}
