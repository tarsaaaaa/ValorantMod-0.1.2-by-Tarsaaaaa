package net.tarsa.valorant.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.tarsa.valorant.ValorantMod;
import net.tarsa.valorant.util.ServerPacketHandler;
import org.slf4j.Logger;

public class ValorantCommands {
    private static Logger logger = ValorantMod.LOGGER;
    private static String ABILITY,AGENT,SELECTED_AGENT;
    private static int INTENSITY;
    private static final SuggestionProvider<ServerCommandSource> AGENTS_SUGGESTIONS = (context, builder) -> {
        builder.suggest("jett");
        builder.suggest("chamber");
        builder.suggest("sage");
        builder.suggest("phoenix");
        builder.suggest("sova");
        builder.suggest("viper");
        builder.suggest("omen");
        builder.suggest("reyna");
        return builder.buildFuture();
    };
    private static final SuggestionProvider<ServerCommandSource> VALORANT_GAMERULE_SUGGESTIONS = (context, builder) -> {
        builder.suggest("doCooldown");
        return builder.buildFuture();
    };
    private static final SuggestionProvider<ServerCommandSource> BOOLEAN_SUGGESTIONS = (context, builder) -> {
        builder.suggest("true");
        builder.suggest("false");
        return builder.buildFuture();
    };

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("valorant")
                .then(CommandManager.literal("select")
                        .then(CommandManager.argument("agent", StringArgumentType.string())
                                .suggests(AGENTS_SUGGESTIONS)
                                .executes(ValorantCommands::select)))
                .then(CommandManager.literal("editor")
                        .then(CommandManager.argument("val-gamerule", StringArgumentType.string())
                                .suggests(VALORANT_GAMERULE_SUGGESTIONS)
                                .executes(ValorantCommands::getRule)
                                .then(CommandManager.argument("rule", BoolArgumentType.bool())
                                        .suggests(BOOLEAN_SUGGESTIONS)
                                        .executes(ValorantCommands::setRule)))
        ));
    }

    private static final SuggestionProvider<ServerCommandSource> ABILITY_SUGGESTIONS = (context, builder) -> {
        AGENT = StringArgumentType.getString(context,"agent");
        switch (AGENT){
            case "jett" ->{
                builder.suggest("cloudburst");
                builder.suggest("updraft");
                builder.suggest("tailwind");
                builder.suggest("bladestorm");
            }
            case "chamber" ->{
                builder.suggest("trademark");
                builder.suggest("headhunter");
                builder.suggest("rendezvous");
                builder.suggest("tourdeforce");
            }
            case "sage" ->{
                builder.suggest("sloworb");
                builder.suggest("healingorb");
                builder.suggest("barrierorb");
                builder.suggest("resurrection");
            }
            case "phoenix" ->{
                builder.suggest("curveball");
                builder.suggest("hothands");
                builder.suggest("blaze");
                builder.suggest("runitback");
            }
            case "sova" ->{
                builder.suggest("shockbolt");
                builder.suggest("reconbolt");
                builder.suggest("owldrone");
                builder.suggest("huntersfury");
            }
            case "viper" ->{
                builder.suggest("poisoncloud");
                builder.suggest("toxicscreen");
                builder.suggest("snakebite");
                builder.suggest("viperspit");
            }
            case "reyna" ->{
                builder.suggest("leer");
                builder.suggest("devour");
                builder.suggest("dismiss");
                builder.suggest("empress");
            }
            case "omen" ->{
                builder.suggest("shroudedstep");
                builder.suggest("paranoia");
                builder.suggest("darkcover");
                builder.suggest("fromtheshadows");
            }
        }
        return builder.buildFuture();
    };

    private static int setRule(CommandContext<ServerCommandSource> context){
        ServerPlayerEntity player = context.getSource().getPlayer();
        String ValRule = StringArgumentType.getString(context, "val-gamerule");
        Boolean setRule = BoolArgumentType.getBool(context, "rule");
        ValGameRules.setValRule(ValRule,setRule);
        if (player!=null) {
            player.sendMessage(Text.of("Gamerule " + ValRule + " is set to: " + setRule));
        }
        return 0;
    }

    private static int getRule(CommandContext<ServerCommandSource> context){
        ServerPlayerEntity player = context.getSource().getPlayer();
        String ValRule = StringArgumentType.getString(context, "val-gamerule");
        ValGameRules.getValRule(ValRule);
        logger.info("ValRule " + ValRule + " is set to: " + ValGameRules.getValRule(ValRule));
        if (player!=null){
            player.sendMessage(Text.of("ValRule " + ValRule + " is set to: " + ValGameRules.getValRule(ValRule)));
        }
        return 0;
    }

    private static int editor(CommandContext<ServerCommandSource> context){
        ServerPlayerEntity player = context.getSource().getPlayer();
        AGENT = StringArgumentType.getString(context,"agent");
        ABILITY = StringArgumentType.getString(context,"ability");
        INTENSITY = IntegerArgumentType.getInteger(context,"intensity");
        ServerPacketHandler.sendAgentEditPacket(player,AGENT,ABILITY,INTENSITY);
        return 0;
    }

    private static int select(CommandContext<ServerCommandSource> context){
        ServerPlayerEntity player = context.getSource().getPlayer();
        SELECTED_AGENT = StringArgumentType.getString(context,"agent");
        ServerPacketHandler.sendAgentSelectPacket(player,SELECTED_AGENT);
        return 0;
    }

    public static String getAGENT(){
        return AGENT;
    }
    public static String getSelectedAgent(){
        return SELECTED_AGENT;
    }
}
