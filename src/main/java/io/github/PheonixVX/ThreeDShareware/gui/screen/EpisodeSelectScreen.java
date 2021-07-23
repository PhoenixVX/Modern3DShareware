package io.github.PheonixVX.ThreeDShareware.gui.screen;

import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class EpisodeSelectScreen extends Screen {

	protected EpisodeSelectScreen (Text title) {
		super(title);
	}

	protected void init () {
		super.init();
		int var2 = height / 4;
		this.addDrawableChild(new ButtonWidget(width / 2 - 100, var2, 200, 20, Text.of("The player is you!"), (var1x) -> {
			//client.setScreen(new DifficultySelectScreen());
			this.client.setScreen(CreateWorldScreen.create(null));
		}));
		this.addDrawableChild(new CustomButton(width / 2 - 100, var2 + 24, "Knee-deep in lava"));
		this.addDrawableChild(new CustomButton(width / 2 - 100, var2 + 48, "Not just the endermen"));
		this.addDrawableChild(new CustomButton(width / 2 - 100, var2 + 72, "Removing Herobrine"));
		this.addDrawableChild(new CustomButton(width / 2 - 100, var2 + 96, "All these worlds are yours except..."));
	}

	public void render (MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		drawCenteredText(matrices, textRenderer, getTitle().getString(), width / 2, height / 4 - 60 + 20, 16777215);
		super.render(matrices, mouseX, mouseY, delta);

		for (Element element : this.children()) {
			if (element instanceof CustomButton customButton && customButton.isHovered()) {
				this.renderTooltip(matrices, new LiteralText("Available in registered version"), mouseX, mouseY);
				break;
			}
		}

	}

	public void onClose () {
		client.setScreen(new CustomTitleScreen());
	}

	static class CustomButton extends ButtonWidget {
		public CustomButton (int var1, int var2, String var3) {
			super(var1, var2, 200, 20, Text.of(var3), (var0) -> {
			});
			active = false;
		}
	}
}
