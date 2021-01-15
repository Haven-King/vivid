package dev.monarkhes.vivid;

import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Identifier;

public class Vivid implements ClientModInitializer {
	public static final ManagedShaderEffect BLUR = ShaderEffectManager.getInstance().manage(new Identifier("vivid", "shaders/post/blur.json"));

	@Override
	public void onInitializeClient() {
	}
}
