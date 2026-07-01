package insane96mcp.areaeffectcloud3d;

import com.mojang.logging.LogUtils;
import insane96mcp.areaeffectcloud3d.entity.Cloud3DEntity;
import insane96mcp.insanelib.setup.ILModConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(AreaEffectCloud3D.MOD_ID)
public class AreaEffectCloud3D
{
    public static final String MOD_ID = "areaeffectcloud3d";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static ILModConfig CONFIG;

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<Cloud3DEntity>> CLOUD = ENTITIES.register("cloud", () -> EntityType.Builder.<Cloud3DEntity>of(Cloud3DEntity::new, MobCategory.MISC).fireImmune().sized(6.0F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE).build("cloud"));

    public AreaEffectCloud3D(IEventBus modEventBus, ModContainer modContainer)
    {
        CONFIG = new ILModConfig(location("base"), "Base", ModConfig.Type.COMMON, modEventBus, AreaEffectCloud3D.class.getClassLoader());
        modContainer.registerConfig(ModConfig.Type.COMMON, CONFIG.spec);

        modEventBus.addListener(ClientSetup::init);

        ENTITIES.register(modEventBus);
    }

    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
