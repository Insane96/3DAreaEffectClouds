package insane96mcp.areaeffectcloud3d;

import insane96mcp.areaeffectcloud3d.entity.Cloud3DRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class ClientSetup {
    public static void init(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AreaEffectCloud3D.CLOUD.get(), Cloud3DRenderer::new);
    }
}
