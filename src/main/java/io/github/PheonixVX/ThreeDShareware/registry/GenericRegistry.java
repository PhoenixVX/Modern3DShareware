package io.github.PheonixVX.ThreeDShareware.registry;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class GenericRegistry {
	public static final SoundEvent AWESOME_INTRO = new SoundEvent(new Identifier("awesome_intro"));

	public static void initialize() {
		Registry.register(Registry.SOUND_EVENT, new Identifier("awesome_intro"), AWESOME_INTRO);
	}
}
