package insane96mcp.areaeffectcloud3d;

import insane96mcp.insanelib.base.Module;
import net.minecraftforge.fml.config.ModConfig;

public class AEC3DModules {
    static Module BaseModule;

    public static void init() {
        BaseModule = Module.Builder.create(AreaEffectCloud3D.MOD_ID, "base", "Base", ModConfig.Type.COMMON, AEC3DConfig.builder)
                .canBeDisabled(false)
                .build();
    }
}
