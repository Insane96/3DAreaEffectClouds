package insane96mcp.areaeffectcloud3d;

import insane96mcp.areaeffectcloud3d.entity.Cloud3DEntity;
import insane96mcp.insanelib.base.Feature;
import insane96mcp.insanelib.base.LoadFeature;
import insane96mcp.insanelib.base.Module;
import insane96mcp.insanelib.base.config.Config;
import net.minecraft.server.TickTask;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

@LoadFeature(
        module = AreaEffectCloud3D.MOD_ID + ":base",
        name = "Area Effect Cloud 3D",
        description = "No more boring flat Area of Effect Clouds",
        canBeDisabled = false
)
public class AOE3DFeature extends Feature {
    @Config(description = "If true, vanilla Area of Effect Clouds will be replaced with 3D versions of them")
    public static Boolean replaceVanillaAreaEffectClouds = true;

    public AOE3DFeature(Module module, boolean enabledByDefault, boolean canBeDisabled) {
        super(module, enabledByDefault, canBeDisabled);
    }

    @SubscribeEvent
    public void onSpawn(EntityJoinLevelEvent event) {
        if (!this.isEnabled()
                || !replaceVanillaAreaEffectClouds
                || !event.getEntity().getType().equals(EntityType.AREA_EFFECT_CLOUD))
            return;

        AreaEffectCloud areaEffectCloud = (AreaEffectCloud) event.getEntity();
        if (areaEffectCloud.effects.isEmpty() && areaEffectCloud.potion.equals(Potions.EMPTY))
            return;
        event.setCanceled(true);
        Cloud3DEntity areaEffectCloud3D = new Cloud3DEntity(areaEffectCloud);

        BlockableEventLoop<? super TickTask> executor = LogicalSidedProvider.WORKQUEUE.get(event.getLevel().isClientSide ? LogicalSide.CLIENT : LogicalSide.SERVER);
        executor.tell(new TickTask(0, () -> areaEffectCloud3D.level().addFreshEntity(areaEffectCloud3D)));
    }
}
