package net.tarsa.valorant.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.tarsa.valorant.ValorantMod;
import org.slf4j.Logger;

public class AgentEditorCommand {
    private static final Logger logger = ValorantMod.LOGGER;
    private static String ABILITY;
    private static String SELECTED_AGENT;
    private static int INTENSITY;
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("agent")
                //needTodo inspect
                .then(CommandManager.literal("edit")
                        .then(CommandManager.argument("agent", StringArgumentType.string())
                                .suggests(AGENTS_SUGGESTIONS)
                                .then(CommandManager.argument("ability", BoolArgumentType.bool())
                                        .suggests(ABILITY_SUGGESTIONS)
                                                .then(CommandManager.argument("intensity", ))
                                        ))
                ));
    }
    ///agent edit jett tailwind
    private static final SuggestionProvider<ServerCommandSource> ABILITY_SUGGESTIONS = (context, builder) -> {
        String AGENT = StringArgumentType.getString(context, "agent");
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
}
