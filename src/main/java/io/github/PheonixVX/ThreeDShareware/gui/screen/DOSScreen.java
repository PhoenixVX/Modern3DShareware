package io.github.PheonixVX.ThreeDShareware.gui.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.DefaultResourcePack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DOSScreen extends Screen {
	private static final Identifier field_19193 = new Identifier("sound.sys");

	protected DOSScreen (Text title) {
		super(new LiteralText("Credits"));
	}

	@Override
	public void render (MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.client.getTextureManager().bindTexture(field_19193);
		Tessellator var4 = Tessellator.getInstance();
		BufferBuilder var5 = var4.getBuffer();
		var5.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
		var5.vertex(0.0D, this.height, this.getZOffset()).texture(0.0F, 1.0F).next();
		var5.vertex(this.width, this.height, this.getZOffset()).texture(1.0F, 1.0F).next();
		var5.vertex(this.width, 0.0D, this.getZOffset()).texture(1.0F, 0.0F).next();
		var5.vertex(0.0D, 0.0D, this.getZOffset()).texture(0.0F, 0.0F).next();
		var4.draw();
	}


	public static class class_4282 extends ResourceTexture {
		private final Identifier field_19194;

		public class_4282 (Identifier var1) {
			super(var1);
			this.field_19194 = var1;
		}

		protected ResourceTexture.TextureData loadTextureData (ResourceManager var1) {
			MinecraftClient var2 = MinecraftClient.getInstance();
			DefaultResourcePack var3 = var2.getResourcePackDownloader().getPack();
			byte[] var4 = new byte[4];

			try {
				InputStream var5 = var3.open(ResourceType.CLIENT_RESOURCES, this.field_19194);
				Throwable var6 = null;

				Object var9;
				try {
					var5.read(var4);
					InputStream var7 = new FilterInputStream(var5) {
						public int read () throws IOException {
							return super.read() ^ 113;
						}

						public int read (byte[] var1, int var2, int var3) throws IOException {
							int var4 = super.read(var1, var2, var3);

							for (int var5 = 0; var5 < var4; ++var5) {
								var1[var5 + var2] = (byte) (var1[var5 + var2] ^ 113);
							}

							return var4;
						}
					};
					Throwable var8 = null;

					try {
						var9 = new ResourceTexture.TextureData(null, NativeImage.read(var7));
					} catch (Throwable var34) {
						throw var34;
					} finally {
						if (var7 != null) {
							if (var8 != null) {
								try {
									var7.close();
								} catch (Throwable var33) {
									var8.addSuppressed(var33);
								}
							} else {
								var7.close();
							}
						}

					}
				} catch (Throwable var36) {
					throw var36;
				} finally {
					if (var5 != null) {
						if (var6 != null) {
							try {
								var5.close();
							} catch (Throwable var32) {
								var6.addSuppressed(var32);
							}
						} else {
							var5.close();
						}
					}

				}

				return (ResourceTexture.TextureData) var9;
			} catch (IOException var38) {
				return new ResourceTexture.TextureData(var38);
			}
		}
	}
}
