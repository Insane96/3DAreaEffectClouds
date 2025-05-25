package insane96mcp.areaffectclouds3d;

import insane96mcp.insanelib.base.Module;
import net.minecraftforge.fml.config.ModConfig;

public class AOE3DModules {
    static Module BaseModule;

    public static void init() {
        BaseModule = Module.Builder.create(AreaEffectClouds3D.MOD_ID, "base", "Base", ModConfig.Type.COMMON, AOE3DConfig.builder)
                .canBeDisabled(false)
                .build();
    }
}
