package insane96mcp.areaffectclouds3d;

import com.mojang.logging.LogUtils;
import insane96mcp.areaffectclouds3d.entity.Cloud3DEntity;
import insane96mcp.insanelib.InsaneLib;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(AreaEffectClouds3D.MOD_ID)
public class AreaEffectClouds3D
{
    public static final String MOD_ID = "areaffectclouds3d";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, InsaneLib.MOD_ID);

    public static final RegistryObject<EntityType<Cloud3DEntity>> CLOUD = ENTITIES.register("cloud", () -> EntityType.Builder.<Cloud3DEntity>of(Cloud3DEntity::new, MobCategory.MISC).fireImmune().sized(6.0F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE).build("cloud"));

    public AreaEffectClouds3D(FMLJavaModLoadingContext context)
    {
        context.registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, AOE3DConfig.COMMON_SPEC, MOD_ID + ".toml");
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(ClientSetup::init);

        ENTITIES.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }
}
