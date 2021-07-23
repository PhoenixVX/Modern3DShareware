package io.github.PheonixVX.ThreeDShareware.gui.screen;

import com.google.common.util.concurrent.Runnables;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.ConfirmChatLinkScreen;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screen.option.LanguageOptionsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.client.realms.gui.screen.RealmsNotificationsScreen;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ChatUtil;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.level.storage.LevelStorage;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Random;

public class CustomTitleScreen extends Screen {
	public static final CubeMapRenderer panoramaCubeMap = new CubeMapRenderer(new Identifier("threedshareware", "textures/gui/title/background/panorama"));
	public static final String OUTDATED_GL_TEXT = "Please click " + Formatting.UNDERLINE + "here" + Formatting.RESET + " for more information.";
	private static final Identifier panoramaOverlay = new Identifier("threedshareware", "textures/gui/title/background/panorama_overlay.png");
	private static final Identifier field_19102 = new Identifier("threedshareware", "textures/gui/accessibility.png");
	private static final Identifier MINECRAFT_TITLE_TEXTURE = new Identifier("threedshareware", "textures/gui/title/minecraft.png");
	private static final Identifier EDITION_TITLE_TEXTURE = new Identifier("threedshareware", "textures/gui/title/edition.png");
	private final Object mutex = new Object();
	private final String warningTitle;
	private final RotatingCubeMapRenderer backgroundRenderer = new RotatingCubeMapRenderer(panoramaCubeMap);
	private final boolean doBackgroundFade;
	private final boolean isMinceraft;
	@Nullable
	private String splashText;
	private ButtonWidget buttonOptions;
	private ButtonWidget buttonResetDemo;
	private int warningTextWidth;
	private int warningTitleWidth;
	private int warningAlignLeft;
	private int warningAlignTop;
	private int warningAlignRight;
	private int warningAlignBottom;
	private String warningText;
	private String warningLink;
	private boolean realmsNotificationsInitialized;
	private Screen realmsNotificationGui;
	private int copyrightTextWidth;
	private int copyrightTextX;
	private long backgroundFadeStart;
	private int field_19251 = 0;

	public CustomTitleScreen () {
		this(false);
	}

	public CustomTitleScreen (boolean var1) {
		super(new TranslatableText("narrator.screen.title", new Object[0]));
		this.warningText = OUTDATED_GL_TEXT;
		this.field_19251 = 0;
		this.doBackgroundFade = var1;
		this.isMinceraft = (double) (new Random()).nextFloat() < 1.0E-4D;
		this.warningTitle = "";
	}

	public boolean isPauseScreen () {
		return false;
	}

	public boolean shouldCloseOnEsc () {
		return false;
	}

	public void init () {
		if (this.splashText == null) {
			this.splashText = this.client.getSplashTextLoader().get();
		}

		this.copyrightTextWidth = this.textRenderer.getWidth("Copyright Mojang AB. Do not distribute!");
		this.copyrightTextX = this.width - this.copyrightTextWidth - 2;
		int var2 = this.height / 4 + 48;
		//if (this.client.isDemo()) {
			this.initWidgetsDemos(var2, 24);
		/* else {
			this.initWidgetsNormals(var2, 24);
		}*/

		this.addDrawableChild(new TexturedButtonWidget(this.width / 2 - 124, var2 + 72 + 12, 20, 20, 0, 106, 20, ButtonWidget.WIDGETS_TEXTURE, 256, 256, (var1x) -> {
			this.client.setScreen(new LanguageOptionsScreen(this, this.client.options, this.client.getLanguageManager()));
		}, new TranslatableText("narrator.button.language")));
		this.buttonOptions = this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, var2 + 72 + 12, 98, 20, new TranslatableText("menu.options"), (var1x) -> {
			this.client.setScreen(new OptionsScreen(this, this.client.options));
		}));
		this.addDrawableChild(new ButtonWidget(this.width / 2 + 2, var2 + 72 + 12, 98, 20, new TranslatableText("menu.quit"), (var1x) -> {
			this.client.scheduleStop();
		}));
		this.addDrawableChild(new TexturedButtonWidget(this.width / 2 + 104, var2 + 72 + 12, 20, 20, 0, 0, 20, field_19102, 32, 64, (var1x) -> {
			this.client.setScreen(new AccessibilityOptionsScreen(this, this.client.options));
		}, new TranslatableText("narrator.button.accessibility")));
		synchronized (this.mutex) {
			this.warningTitleWidth = this.textRenderer.getWidth(this.warningTitle);
			this.warningTextWidth = this.textRenderer.getWidth(this.warningText);
			int var4 = Math.max(this.warningTitleWidth, this.warningTextWidth);
			this.warningAlignLeft = (this.width - var4) / 2;
			this.warningAlignTop = var2 - 24;
			this.warningAlignRight = this.warningAlignLeft + var4;
			this.warningAlignBottom = this.warningAlignTop + 24;
		}

		this.client.setConnectedToRealms(false);
		if (this.client.options.realmsNotifications && this.realmsNotificationGui == null) {
			this.realmsNotificationGui = new RealmsNotificationsScreen();
		}
	}

	private void initWidgetsNormal(int y, int spacingY) {
		this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, y, 200, 20, new TranslatableText("menu.singleplayer", new Object[0]), buttonWidget -> this.client.setScreen(new SelectWorldScreen(this))));
		this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, y + spacingY, 200, 20, new TranslatableText("menu.multiplayer", new Object[0]), buttonWidget -> this.client.setScreen(new MultiplayerScreen(this))));
		this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, y + spacingY * 2, 200, 20, new TranslatableText("menu.online", new Object[0]), buttonWidget -> this.switchToRealms()));
	}

	public boolean canReadDemoWorldDatas () {
		try {
			LevelStorage.Session session = this.client.getLevelStorage().createSession("Demo_World");
			Throwable var2 = null;

			boolean var3;
			try {
				var3 = session.getLevelSummary() != null;
			} finally {
				if (session != null) {
					if (var2 != null) {
						try {
							session.close();
						} catch (Throwable var12) {
							var2.addSuppressed(var12);
						}
					} else {
						session.close();
					}
				}

			}

			return var3;
		} catch (IOException var15) {
			SystemToast.addWorldAccessFailureToast(this.client, "Demo_World");
			return false;
		}
	}

	private void onDemoDeletionConfirmed (boolean delete) {
		if (delete) {
			try {
				LevelStorage.Session session = this.client.getLevelStorage().createSession("Demo_World");
				Throwable var3 = null;

				try {
					session.deleteSessionLock();
				} catch (Throwable var13) {
					var3 = var13;
					throw var13;
				} finally {
					if (session != null) {
						if (var3 != null) {
							try {
								session.close();
							} catch (Throwable var12) {
								var3.addSuppressed(var12);
							}
						} else {
							session.close();
						}
					}

				}
			} catch (IOException var15) {
				SystemToast.addWorldDeleteFailureToast(this.client, "Demo_World");
			}
		}

		this.client.setScreen(this);
	}

	private void initWidgetsDemos(int y, int spacingY) {
		this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, y, 200, 20, new TranslatableText("menu.select_episode", new Object[0]), buttonWidget -> this.client.setScreen(new EpisodeSelectScreen(new TranslatableText("menu.select_episode")))));
		this.buttonResetDemo = this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, y + spacingY, 200, 20, new TranslatableText("menu.modem_play", new Object[0]), buttonWidget -> {
			if (this.field_19251++ == 0) {
				this.buttonResetDemo.setMessage(new TranslatableText("menu.modem_played1", new Object[0]));
			} else {
				this.buttonResetDemo.setMessage(new TranslatableText("menu.modem_played2", new Object[0]));
			}
		}));
	}

	private void switchToRealms() {
		this.client.setScreen(new RealmsMainScreen(this));
	}

	@Override
	public void render (MatrixStack matrices, int mouseX, int mouseY, float delta) {
		if (this.backgroundFadeStart == 0L && this.doBackgroundFade) {
			this.backgroundFadeStart = Util.getMeasuringTimeMs();
		}

		float var4 = this.doBackgroundFade ? (float) (Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000.0F : 1.0F;
		fill(matrices, 0, 0, this.width, this.height, -1);
		this.backgroundRenderer.render(delta, MathHelper.clamp(var4, 0.0F, 1.0F));
		int var5 = 1;
		int var6 = this.width / 2 - 137;
		int var7 = 1;
		RenderSystem.setShaderTexture(0, panoramaOverlay);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.clearColor(1.0F, 1.0F, 1.0F, this.doBackgroundFade ? (float) MathHelper.ceil(MathHelper.clamp(var4, 0.0F, 1.0F)) : 1.0F);
		drawTexture(matrices, 0, 0, this.width, this.height, 0.0F, 0.0F, 16, 128, 16, 128);
		float var8 = this.doBackgroundFade ? MathHelper.clamp(var4 - 1.0F, 0.0F, 1.0F) : 1.0F;
		int var9 = MathHelper.ceil(var8 * 255.0F) << 24;
		if ((var9 & -67108864) != 0) {
			RenderSystem.setShaderTexture(1, MINECRAFT_TITLE_TEXTURE);
			RenderSystem.clearColor(1.0F, 1.0F, 1.0F, var8);
			if (this.isMinceraft) {
				this.drawWithOutline(var6, 30, (integer, integer2) -> {
					this.drawTexture(matrices, integer, integer2, 0, 0, 99, 44);
					this.drawTexture(matrices, integer + 99, integer2, 129, 0, 27, 44);
					this.drawTexture(matrices, integer + 99 + 26, integer2, 126, 0, 3, 44);
					this.drawTexture(matrices, integer + 99 + 26 + 3, integer2, 99, 0, 26, 44);
					this.drawTexture(matrices, integer + 155, integer2, 0, 45, 155, 44);
				});
			} else {
				this.drawWithOutline(var6, 30, (integer, integer2) -> {
					this.drawTexture(matrices, integer, integer2, 0, 0, 155, 44);
					this.drawTexture(matrices, integer + 155, integer2, 0, 45, 155, 44);
				});
			}

			RenderSystem.enableBlend();
			matrices.push();
			matrices.translate(370.0F, 50.0F, 100.0F);
			matrices.scale(80.0F, 80.0F, 80.0F);
			long var10 = Util.getMeasuringTimeMs();

			matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion((float) (var10 / 10L % 360L)));
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float) (var10 / 15L % 360L)));
			matrices.multiply(Vec3f.ZERO.getDegreesQuaternion((float) (var10 / 20L % 360L)));
			//GlStateManager.rotatef((float) (var10 / 10L % 360L), 0.0F, 1.0F, 0.0F);
			//GlStateManager.rotatef((float) (var10 / 15L % 360L), 1.0F, 0.0F, 0.0F);
			//GlStateManager.rotatef((float) (var10 / 20L % 360L), 0.0F, 0.0F, 1.0F);
			// TODO: Check this
			//this.client.getItemRenderer().renderItem(new ItemStack(Items.CUT_SANDSTONE), ModelTransformation.Mode.NONE, 0, 0, matrices, null);
			matrices.pop();
			RenderSystem.setShaderTexture(2, EDITION_TITLE_TEXTURE);
			drawTexture(matrices, var6 + 88, 67, 0.0F, 0.0F, 98, 14, 128, 16);
			if (this.splashText != null) {
				matrices.push();
				matrices.translate((float) (this.width / 2 + 90), 70.0F, 0.0F);
				matrices.multiply(Vec3f.ZERO.getDegreesQuaternion(-20.0F));
				//GlStateManager.rotatef(-20.0F, 0.0F, 0.0F, 1.0F);
				float var13 = 1.8F - MathHelper.abs(MathHelper.sin((float) (Util.getMeasuringTimeMs() % 1000L) / 1000.0F * 6.2831855F) * 0.1F);
				var13 = var13 * 100.0F / (float) (this.textRenderer.getWidth(this.splashText) + 32);
				matrices.scale(var13, var13, var13);
				drawCenteredText(matrices, this.textRenderer, this.splashText, 0, -8, 16776960 | var9);
				matrices.pop();
			}

			String var14 = "Minecraft 3D Shareware v1.34";
			this.textRenderer.draw(matrices, var14, 2, this.height - 10, 16777215 | var9);
			this.textRenderer.draw(matrices, "Copyright Mojang AB. Do not distribute!", this.copyrightTextX, this.height - 10, 16777215 | var9);
			if (mouseX > this.copyrightTextX && mouseX < this.copyrightTextX + this.copyrightTextWidth && mouseY > this.height - 10 && mouseY < this.height) {
				fill(matrices, this.copyrightTextX, this.height - 1, this.copyrightTextX + this.copyrightTextWidth, this.height, 16777215 | var9);
			}

			if (this.warningTitle != null && !this.warningTitle.isEmpty()) {
				fill(matrices, this.warningAlignLeft - 2, this.warningAlignTop - 2, this.warningAlignRight + 2, this.warningAlignBottom - 1, 1428160512);
				this.textRenderer.draw(matrices, this.warningTitle, this.warningAlignLeft, this.warningAlignTop, 16777215 | var9);
				this.textRenderer.draw(matrices, this.warningText, (this.width - this.warningTextWidth) / 2, this.warningAlignTop + 12, 16777215 | var9);
			}

			for (Element element : this.children()) {
				if (element instanceof ClickableWidget clickableWidget) {
					clickableWidget.setAlpha(var8);
				}
			}

			super.render(matrices, mouseX, mouseY, delta);
		}
	}

	@Override
	public boolean mouseClicked (double var1, double var3, int var5) {
		if (super.mouseClicked(var1, var3, var5)) {
			return true;
		} else {
			synchronized (this.mutex) {
				if (!this.warningTitle.isEmpty() && !ChatUtil.isEmpty(this.warningLink) && var1 >= (double) this.warningAlignLeft && var1 <= (double) this.warningAlignRight && var3 >= (double) this.warningAlignTop && var3 <= (double) this.warningAlignBottom) {
					ConfirmChatLinkScreen var7 = new ConfirmChatLinkScreen(null, this.warningLink, true);
					this.client.setScreen(var7);
					return true;
				}
			}

			if (var1 > (double) this.copyrightTextX && var1 < (double) (this.copyrightTextX + this.copyrightTextWidth) && var3 > (double) (this.height - 10) && var3 < (double) this.height) {
				this.client.setScreen(new CreditsScreen(false, Runnables.doNothing()));
			}

			return false;
		}
	}

	@Override
	public void removed () {
		if (this.realmsNotificationGui != null) {
			this.realmsNotificationGui.removed();
		}

	}
}
