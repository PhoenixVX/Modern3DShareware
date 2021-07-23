package io.github.PheonixVX.ThreeDShareware.gui.screen;

import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelInfo;

public class DifficultySelectScreen extends Screen {
	//private static final LevelInfo field_19196 = new LevelInfo("North Carolina".hashCode(), GameMode.SURVIVAL, true, false, LevelGeneratorType.DEFAULT).setBonusChest();
	private CustomButton field_19197;
	private int field_19198;

	public DifficultySelectScreen () {
		super(new LiteralText("Select difficulty"));
	}

	@Override
	protected void init () {
		super.init();
		int w = height / 4;
		this.addDrawableChild(new CustomButton(width / 2 - 100, w, "Hello, NoobVille", Difficulty.PEACEFUL, true));
		this.addDrawableChild(new CustomButton(width / 2 - 100, w + 24, "Filthy casual!", Difficulty.EASY, true));
		this.addDrawableChild(new CustomButton(width / 2 - 100, w + 48, "Lemon curry?", Difficulty.NORMAL, true));
		this.addDrawableChild(new CustomButton(width / 2 - 100, w + 72, "eXtreme to the MaXxXxX!", Difficulty.HARD, true));
		field_19197 = this.addDrawableChild(new CustomButton(width / 2 - 100, w + 96, "Obligatory nightmare mode", Difficulty.HARD, true));
	}

	@Override
	public void render (MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		drawCenteredText(matrices, textRenderer, getTitle(), width / 2, height / 4 - 60 + 20, 0xFFFFFF);
		super.render(matrices, mouseX, mouseY, delta);
		field_19198 = field_19197 != null && field_19197.isHovered() ? Math.min(255, field_19198 + 3) : Math.max(0, field_19198 - 3);
		if (field_19198 != 0) {
			fillGradient(matrices, 0, 0, width, height, 0, field_19198 << 24 | 0xFF0000);
		}
	}

	@Override
	public void onClose () {
		client.setScreen(new EpisodeSelectScreen(Text.of("Select Episode")));
	}

	class CustomButton extends ButtonWidget {

		public CustomButton (int n, int n2, String string, Difficulty theDifficulty, boolean bl) {
			super(n, n2, 200, 20, Text.of(string), onPress -> {
				/*client.startIntegratedServer("shareware");
				IntegratedServer integratedServer = client.getServer();
				integratedServer.setupServer();
				integratedServer.setDifficulty(theDifficulty, true);
				integratedServer.setDifficultyLocked(true);
				integratedServer.setDemo(bl);*/

				client.method_29970(new SaveLevelScreen(new TranslatableText("createWorld.preparing")));
				long seed = "North Carolina".hashCode();
				GeneratorOptions generatorOptions = new GeneratorOptions(seed, true, true, null);
				LevelInfo levelInfo2;
				GameRules gameRules = new GameRules();
				gameRules.get(GameRules.DO_DAYLIGHT_CYCLE).set(false, null);
				levelInfo2 = new LevelInfo("3D Shareware", GameMode.SURVIVAL, false, Difficulty.HARD, true, gameRules, DataPackSettings.SAFE_MODE);

				client.createWorld("3D Shareware", levelInfo2, null, generatorOptions);
			});
		}
	}
}