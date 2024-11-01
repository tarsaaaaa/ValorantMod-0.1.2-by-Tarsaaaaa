package net.tarsa.valorant.screens;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.tarsa.valorant.agents.Jett;

public class AgentEditor extends CottonClientScreen {
    public AgentEditor(GuiDescription description) {
        super(description);
    }

    public static class AgentEditorGUI extends LightweightGuiDescription {
        public AgentEditorGUI(){
            MinecraftClient client = MinecraftClient.getInstance();
            WGridPanel main = new WGridPanel();
            setRootPanel(main);
            setFullscreen(true);
            int UI_Width = main.getWidth();
            int UI_Height = main.getHeight();
            WText title = new WText(Text.literal("Agent editor"), 0xFFFFFFFF);
            WButton JettEditorButton = new WButton(Text.literal("Jett editor"));

            main.add(title, 1,1,50,1);
            main.add(JettEditorButton, 1, 3, 10, 1);

            JettEditorButton.setOnClick(()->{
                client.setScreen(null);
                client.setScreen(new JettEditorScreen(new JettEditorScreen.JettEditorGUI()));
            });
        }
    }
}

@Environment(EnvType.CLIENT)
class JettEditorScreen extends CottonClientScreen{
    public JettEditorScreen(GuiDescription description) {
        super(description);
    }
    public static class JettEditorGUI extends LightweightGuiDescription {
        public JettEditorGUI(){
            WGridPanel main = new WGridPanel();
            setRootPanel(main);
            setFullscreen(true);
            int UI_Width = main.getWidth();
            int UI_Height = main.getHeight();

            WText title = new WText(Text.literal("Jett editor."), 0xFFFFFFFF);
            WText UpdraftIntensity = new WText(Text.literal("Updraft intensity: "), 0xFFFFFFFF);
            WTextField updraftIntensity = new WTextField(Text.literal("Default 1"));
            WText TailwindIntensity = new WText(Text.literal("Tailwind intensity: "), 0xFFFFFFFF);
            WTextField tailwindIntensity = new WTextField(Text.literal("Default 1"));
            WText BladeStormSpeed = new WText(Text.literal("BladeStorm Speed: "), 0xFFFFFFFF);
            WTextField bladestormSpeed = new WTextField(Text.literal("Default 1"));

            main.add(title, (UI_Width/2)+6, 1,10,1);
            main.add(UpdraftIntensity, (UI_Width/2)-6, 3,10,2);
            main.add(updraftIntensity, (UI_Width/2)+6, 3, 10, 2);
            main.add(TailwindIntensity, (UI_Width/2)-6, 5,10,2);
            main.add(tailwindIntensity, (UI_Width/2)+6, 5, 10, 2);
            main.add(BladeStormSpeed, (UI_Width/2)-6, 7,10,2);
            main.add(bladestormSpeed, (UI_Width/2)+6, 7, 10, 2);
        }
    }
}
