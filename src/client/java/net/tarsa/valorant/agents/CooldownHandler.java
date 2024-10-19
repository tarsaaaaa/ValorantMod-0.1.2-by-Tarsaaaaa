package net.tarsa.valorant.agents;

public class CooldownHandler {
    public static boolean doCooldown;

    public static void ruleCooldown(boolean doCooldown) {
        CooldownHandler.doCooldown = doCooldown;
    }

    public static boolean isDoCooldown() {
        return doCooldown;
    }


}
