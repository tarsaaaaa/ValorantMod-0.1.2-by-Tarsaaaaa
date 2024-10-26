package net.tarsa.valorant.util;

import net.tarsa.valorant.custom.commands.ValGameRules;

import java.util.HashMap;
import java.util.Map;
public class CooldownHandler {
    private static final Map<String, Long> cooldowns = new HashMap<>();
    public void setCooldown(String ability, long cooldownTimeInMillis) {
        if (!ValGameRules.isDoCooldown()){
            return;
        }
        long cooldownEnd = System.currentTimeMillis() + cooldownTimeInMillis;
        cooldowns.put(ability, cooldownEnd);
    }

    public boolean isNotOnCooldown(String ability) {
        if (!ValGameRules.isDoCooldown()){
            return true;
        }
        Map<String, Long> abilityCooldowns = cooldowns;
        long cooldownEnd = abilityCooldowns.get(ability)==null? 0 : abilityCooldowns.get(ability);
        if (cooldownEnd == 0) {
            return true;
        }
        if (System.currentTimeMillis() > cooldownEnd) {
            cooldowns.put(ability, 0L);
            return true;
        }
        return false;
    }

    public long getRemainingCooldown(String ability) {
        if (!ValGameRules.isDoCooldown()){
            return 0;
        }
        Map<String, Long> abilityCooldowns = cooldowns;
        long cooldownEnd = abilityCooldowns.get(ability)==null? 0 : abilityCooldowns.get(ability);
        if (cooldownEnd == 0) {
            return 0;
        }
        return Math.max(0, cooldownEnd - System.currentTimeMillis());
    }
}
