package net.tarsa.valorant.custom.commands;


import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ValGameRules {
    private static Boolean justKilled;
    public static Map<String, Boolean> RULES_BOOL = new HashMap<>();
    public static Map<String, Integer> RULES_INT = new HashMap<>();
    public static Map<String, String> RULES_SUPER = new HashMap<>();
    public static void setDefaults(){
        RULES_SUPER.put("doCooldown","bool");
        RULES_BOOL.put("doCooldown", true);
    }
    public static void setValRule(String name, Boolean value){
        RULES_BOOL.put(name, value);
    }
    public static void setValRule(String name, int value){

    }
    public static boolean getValRule(String name){
        return RULES_BOOL.get(name);
    }
    public static boolean isDoCooldown() {
        return RULES_BOOL.get("doCooldown");
    }


    public static void setJustKilled(Boolean bool){
        justKilled = bool;
    }
    public static Boolean getJustKilled(){
        return justKilled != null && justKilled;
    }
}
