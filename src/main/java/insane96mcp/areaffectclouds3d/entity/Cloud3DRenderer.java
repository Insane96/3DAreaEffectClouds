package insane96mcp.areaffectclouds3d.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class Cloud3DRenderer extends EntityRenderer<Cloud3DEntity> {
	public Cloud3DRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull Cloud3DEntity entity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}
}
