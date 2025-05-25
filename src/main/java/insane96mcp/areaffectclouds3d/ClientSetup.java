package insane96mcp.areaffectclouds3d;

import insane96mcp.areaffectclouds3d.entity.Cloud3DRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class ClientSetup {
    public static void init(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AreaEffectClouds3D.CLOUD.get(), Cloud3DRenderer::new);
    }
}
