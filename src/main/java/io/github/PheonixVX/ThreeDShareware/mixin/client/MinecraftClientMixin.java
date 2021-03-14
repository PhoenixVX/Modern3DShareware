package io.github.PheonixVX.ThreeDShareware.mixin.client;

import io.github.PheonixVX.ThreeDShareware.gui.screen.CustomSplashScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

	@Shadow
	@Nullable
	public Screen currentScreen;
	@Shadow
	@Final
	public Mouse mouse;
	@Shadow
	public boolean skipGameRender;
	@Shadow
	@Final
	private Window window;
	@Shadow
	@Final
	private SoundManager soundManager;

	@Shadow
	private volatile boolean running;

	@Shadow
	private boolean windowFocused;

	@Shadow
	public abstract void scheduleStop ();

	@Shadow public abstract void setOverlay (@Nullable Overlay overlay);

	@Shadow public abstract void stop ();

	@Inject(at = @At("HEAD"), method = "openScreen", cancellable = true)
	public void openScreen (Screen screen, CallbackInfo ci) {
		/*if (screen instanceof TitleScreen) {
			screen = new CustomTitleScreen();
			this.currentScreen = screen;
			if (screen != null) {
				this.mouse.unlockCursor();
				KeyBinding.unpressAll();
				screen.init(((MinecraftClient) (Object) this), this.window.getScaledWidth(), this.window.getScaledHeight());
				this.skipGameRender = false;
				NarratorManager.INSTANCE.narrate(screen.getNarrationMessage());
			} else {
				this.soundManager.resumeAll();
				this.mouse.lockCursor();
			}

			((MinecraftClient) (Object) this).updateWindowTitle();
			ci.cancel();
		}*/
	}

	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;openScreen(Lnet/minecraft/client/gui/screen/Screen;)V", ordinal = 1), method = "<init>")
	private Screen redirectScreen (Screen originalScreen) {
		return new CustomSplashScreen(Text.of("Awesome Intro"));
	}
}
