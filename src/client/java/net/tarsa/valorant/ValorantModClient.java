package net.tarsa.valorant;

import net.fabricmc.api.ClientModInitializer;
import net.tarsa.valorant.util.ClientRegistry;

public class ValorantModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientRegistry.RegisterClientStuff();
	}
}