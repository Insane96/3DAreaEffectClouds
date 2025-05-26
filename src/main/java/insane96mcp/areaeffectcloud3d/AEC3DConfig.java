package insane96mcp.areaeffectcloud3d;

import insane96mcp.insanelib.base.Module;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = AreaEffectCloud3D.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AEC3DConfig {
	public static final ForgeConfigSpec COMMON_SPEC;
	public static final CommonConfig COMMON;

	public static final ForgeConfigSpec.Builder builder;

	static {
		builder = new ForgeConfigSpec.Builder();
		final Pair<CommonConfig, ForgeConfigSpec> specPair = builder.configure(CommonConfig::new);
		COMMON = specPair.getLeft();
		COMMON_SPEC = specPair.getRight();
	}

	public static class CommonConfig {
		public CommonConfig(final ForgeConfigSpec.Builder builder) {
			AEC3DModules.init();
			Module.loadFeatures(ModConfig.Type.COMMON, AreaEffectCloud3D.MOD_ID, this.getClass().getClassLoader());
		}
	}
}