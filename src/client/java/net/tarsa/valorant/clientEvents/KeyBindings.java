package net.tarsa.valorant.clientEvents;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.tarsa.valorant.agents.AgentHandler;
import net.tarsa.valorant.agents.Jett;
import net.tarsa.valorant.custom.commands.ValGameRules;
import net.tarsa.valorant.util.SpecialCharactersExt;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    private static final String VALORANT_KEY_CATEGORY = "tarsa.valorant.key.category";

    private static final String LEFT_CLICK = "valorant-left-click";
    private static final String TestingKey = "valorant-testing-key";
    private static final String MainMenuKey = "valorant-main-menu-key";
    private static final String BuyMenuKey = "valorant-buy-menu-key";
    private static final String Ability1Key = "valorant-ability-1-key";
    private static final String Ability2Key = "valorant-ability-2-key";
    private static final String Ability3Key = "valorant-ability-3-key";
    private static final String Ability4Key = "valorant-ability-4-key";
    private static final String UtilityKey = "valorant-utility-key";
    private static final String AgentsMenuKey = "valorant-agents-menu-key";
    private static final String AgentInfoKey = "valorant-agents-info-key";


    private static KeyBinding LeftKeyBind;
    private static KeyBinding TestingKeyBind;
    private static KeyBinding MainMenuKeyBind;
    private static KeyBinding BuyMenuKeyBind;
    private static KeyBinding Ability1KeyBind;
    private static KeyBinding Ability2KeyBind;
    private static KeyBinding Ability3KeyBind;
    private static KeyBinding Ability4KeyBind;
    private static KeyBinding UtilityKeyBind;
    private static KeyBinding AgentsMenuKeyBind;
    private static KeyBinding AgentsInfoKeyBind;

    private static void registerKeyInputs(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (LeftKeyBind.wasPressed()) {
                AgentHandler.LeftClick(client.player);
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (TestingKeyBind.wasPressed()) {
                PlayerEntity player = client.player;
                System.out.println(ValGameRules.getJustKilled());
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (MainMenuKeyBind.wasPressed()) {
                System.out.println("Yo");
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (BuyMenuKeyBind.wasPressed()) {
                System.out.println("Yo");
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (Ability1KeyBind.wasPressed()) {
                AgentHandler.FirstAbility(client.player);
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (Ability2KeyBind.wasPressed()) {
                AgentHandler.SecondAbility(client.player,1);
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (Ability3KeyBind.wasPressed()) {
                AgentHandler.ThirdAbility(client.player,1);
            }
            if (Jett.TailWindActive && (System.currentTimeMillis() - Jett.TailWindTimer) >= Jett.TailWindDuration) {
                Jett.TailWindActive = false;
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (Ability4KeyBind.wasPressed()) {
                AgentHandler.Ult(client.player);
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (UtilityKeyBind.wasPressed()) {
                System.out.println("Yo");
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (AgentsMenuKeyBind.wasPressed()) {
                System.out.println("Yo");
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (AgentsInfoKeyBind.wasPressed()) {
                System.out.println("Yo");
            }
        });
    }
    public static void registerKeyBindings(){
        LeftKeyBind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        LEFT_CLICK,
                        InputUtil.Type.MOUSE,
                        GLFW.GLFW_MOUSE_BUTTON_1,
                        VALORANT_KEY_CATEGORY
                )
        );
        TestingKeyBind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        TestingKey,
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_Z,
                        VALORANT_KEY_CATEGORY
                )
        );

        MainMenuKeyBind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        MainMenuKey,
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_P,
                        VALORANT_KEY_CATEGORY
                )
        );

        BuyMenuKeyBind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        BuyMenuKey,
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_B,
                        VALORANT_KEY_CATEGORY
                )
        );

        Ability1KeyBind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        Ability1Key,
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_UNKNOWN,
                        VALORANT_KEY_CATEGORY
                )
        );

        Ability2KeyBind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        Ability2Key,
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_UNKNOWN,
                        VALORANT_KEY_CATEGORY
                )
        );

        Ability3KeyBind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        Ability3Key,
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_UNKNOWN,
                        VALORANT_KEY_CATEGORY
                )
        );

        Ability4KeyBind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        Ability4Key,
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_UNKNOWN,
                        VALORANT_KEY_CATEGORY
                )
        );

        UtilityKeyBind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        UtilityKey,
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_UNKNOWN,
                        VALORANT_KEY_CATEGORY
                )
        );

        AgentsMenuKeyBind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        AgentsMenuKey,
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_F,
                        VALORANT_KEY_CATEGORY
                )
        );

        AgentsInfoKeyBind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        AgentInfoKey,
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_F2,
                        VALORANT_KEY_CATEGORY
                )
        );
        registerKeyInputs();
    }
}
