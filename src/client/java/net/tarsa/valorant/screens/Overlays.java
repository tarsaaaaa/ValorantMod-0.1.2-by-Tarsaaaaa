package net.tarsa.valorant.screens;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.tarsa.valorant.util.CooldownHandler;

import static net.tarsa.valorant.agents.Jett.*;

public class Overlays {
    public static void registerOverlays(){
        HudRenderCallback.EVENT.register(Overlays::renderTailWindBar);
        HudRenderCallback.EVENT.register(Overlays::renderCooldowns);
    }

    static MinecraftClient client = MinecraftClient.getInstance();
    static TextRenderer renderer = MinecraftClient.getInstance().textRenderer;

    private static final CooldownHandler cooldownHandler = new CooldownHandler();


    public static void renderTailWindBar(DrawContext context, float tickDelta) {
        if (!TailWindActive) {
            return;
        }

        if (client.player == null) {
            return;
        }

        long elapsedTime = System.currentTimeMillis() - TailWindTimer;
        float progress = 1.0f - (float) elapsedTime / TailWindDuration;
        if (progress < 0) progress = 0;

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        int barWidth = 100;
        int barHeight = 8;
        int barX = (screenWidth - barWidth) / 2;
        int barY = (screenHeight/2) + barHeight * 10;
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        context.drawCenteredTextWithShadow(renderer, Text.of("Press again to dash"),screenWidth / 2 , barY + barHeight + 4,0xFFFFFFFF);
        context.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFF000000);
        int filledWidth = (int) (barWidth * progress);
        context.fill(barX, barY, barX + filledWidth, barY + barHeight, 0xFF64C4E2);
    }

    public static void renderCooldowns(DrawContext context, float tickDelta) {
        if (client.player == null) {
            return;
        }

        int screenWidth = client.getWindow().getWidth();
        int screenHeight = client.getWindow().getHeight();

        if (cooldownHandler.getRemainingCooldown("first") > 1 || cooldownHandler.getRemainingCooldown("first") == 0){
            double remainingCooldownFirst = (double) cooldownHandler.getRemainingCooldown("first")/1000;
            context.drawCenteredTextWithShadow(renderer, Text.of(Double.toString(remainingCooldownFirst)),(screenWidth/2) + (screenWidth/8), (screenHeight/2 + 9),0xFFFFFFFF);
        } else if (cooldownHandler.getRemainingCooldown("first") < 1 && cooldownHandler.getRemainingCooldown("first") > 0) {
            double remainingCooldownFirst = (double) cooldownHandler.getRemainingCooldown("first")/1000;
            context.drawCenteredTextWithShadow(renderer, Text.of(Double.toString(remainingCooldownFirst)),(screenWidth/2) + (screenWidth/8), (screenHeight/2 + 9),0xFFFFFFFF);
        }

        int remainingCooldownSecond;
        if (cooldownHandler.getRemainingCooldown("second") > 0){
            remainingCooldownSecond = (int) cooldownHandler.getRemainingCooldown("second")/1000;
        } else {
            remainingCooldownSecond = 0;
        }

        int remainingCooldownThird;
        if (cooldownHandler.getRemainingCooldown("third") > 0){
            remainingCooldownThird = (int) cooldownHandler.getRemainingCooldown("third")/1000;
        } else {
            remainingCooldownThird = 0;
        }

        int remainingCooldownUlt;
        if (cooldownHandler.getRemainingCooldown("ult") > 0){
            remainingCooldownUlt = (int) cooldownHandler.getRemainingCooldown("ult")/1000;
        } else {
            remainingCooldownUlt = 0;
        }

    }
}
