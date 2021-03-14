package io.github.PheonixVX.ThreeDShareware;

import io.github.PheonixVX.ThreeDShareware.registry.BlockRegistry;
import io.github.PheonixVX.ThreeDShareware.registry.EntityRegistry;
import io.github.PheonixVX.ThreeDShareware.registry.GenericRegistry;
import io.github.PheonixVX.ThreeDShareware.registry.ItemRegistry;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreeDShareware implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("3DShareware");

	@Override
	public void onInitialize () {
		LOGGER.info("Thank you for playing my mod: 3D Shareware 1.34!");
		ItemRegistry.initialize();
		BlockRegistry.initialize();
		EntityRegistry.initialize();
		GenericRegistry.initialize();
	}
}
