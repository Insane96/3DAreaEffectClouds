package insane96mcp.areaeffectcloud3d.entity;

import com.google.common.collect.Lists;
import insane96mcp.areaeffectcloud3d.AreaEffectCloud3D;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class Cloud3DEntity extends AreaEffectCloud {
	public Cloud3DEntity(EntityType<? extends Cloud3DEntity> cloud, Level world) {
		super(cloud, world);
	}

	public Cloud3DEntity(Level worldIn, double x, double y, double z) {
		this(AreaEffectCloud3D.CLOUD.get(), worldIn);
		this.setPos(x, y, z);
	}

	public Cloud3DEntity(AreaEffectCloud areaEffectCloudEntity) {
		this(AreaEffectCloud3D.CLOUD.get(), areaEffectCloudEntity.level());
		this.setPos(areaEffectCloudEntity.getX(), areaEffectCloudEntity.getY(), areaEffectCloudEntity.getZ());
		CompoundTag nbt = new CompoundTag();
		areaEffectCloudEntity.saveAsPassenger(nbt);
		this.readAdditionalSaveData(nbt);
	}

	@Override
	public void refreshDimensions() {
		super.refreshDimensions();
		double radius = (double)this.getDimensions(Pose.STANDING).width() / 2.0D;
		this.setBoundingBox(new AABB(this.getX() - radius, this.getY() - radius, this.getZ() - radius, this.getX() + radius, this.getY() + radius, this.getZ() + radius));
	}

	@Override
	public void tick() {
		boolean isWaiting = this.isWaiting();
		float radius = this.getRadius();
		if (this.level().isClientSide) {
			ParticleOptions particleOptions = this.getParticle();
			if (isWaiting) {
				if (this.random.nextBoolean()) {
					for (int i = 0; i < radius; ++i) {
						float f1 = this.random.nextFloat() * ((float)Math.PI * 2F);
						float f2 = Mth.sqrt(this.random.nextFloat()) * 0.2F;
						float x = Mth.cos(f1) * f2;
						float z = Mth.sin(f1) * f2;
						if (particleOptions.getType() == ParticleTypes.ENTITY_EFFECT) {
							if (this.random.nextBoolean())
								this.level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, -1), this.getX() + (double)x, this.getY(), this.getZ() + (double)z, 0.0D, 0.0D, 0.0D);
							else
								this.level().addParticle(particleOptions, this.getX() + (double)x, this.getY(), this.getZ() + (double)z, 0.0D, 0.0D, 0.0D);
						}
						else {
							this.level().addParticle(particleOptions, this.getX() + (double)x, this.getY(), this.getZ() + (double)z, 0.0D, 0.0D, 0.0D);
						}
					}
				}
			}
			else {
				int particleAmount = (int) (Math.PI * radius * radius);

				for (int k1 = 0; k1 < particleAmount; ++k1) {
					float x = Mth.nextFloat(this.random, -radius, radius);
					float y = Mth.nextFloat(this.random, -radius, radius);
					float z = Mth.nextFloat(this.random, -radius, radius);
					if ((x*x) + (y*y) + (z*z) > (radius*radius))
						continue;

					if (particleOptions.getType() == ParticleTypes.ENTITY_EFFECT) {
						this.level().addParticle(particleOptions, this.getX() + (double)x, this.getY() + (double)y, this.getZ() + (double)z, 0.0D, 0.0D, 0.0D);
					} else {
						this.level().addParticle(particleOptions, this.getX() + (double)x, this.getY() + (double)y, this.getZ() + (double)z, (0.5D - this.random.nextDouble()) * 0.15D, (double)0.01F, (0.5D - this.random.nextDouble()) * 0.15D);
					}
				}
			}
		}
		else {
			if (this.tickCount >= this.waitTime + this.duration) {
				this.discard();
				return;
			}

			boolean flag1 = this.tickCount < this.waitTime;
			if (isWaiting != flag1) {
				this.setWaiting(flag1);
			}

			if (flag1) {
				return;
			}

			if (this.radiusPerTick != 0.0F) {
				radius += this.radiusPerTick;
				if (radius < 0.5F) {
					this.discard();
					return;
				}

				this.setRadius(radius);
			}

			if (this.tickCount % 5 == 0) {
				this.victims.entrySet().removeIf(entry -> this.tickCount >= entry.getValue());

				if (!this.potionContents.hasEffects()) {
					this.victims.clear();
				} else {
					List<MobEffectInstance> list = Lists.newArrayList();
					if (this.potionContents.potion().isPresent()) {
						for (MobEffectInstance effectinstance1 : this.potionContents.potion().get().value().getEffects()) {
							list.add(new MobEffectInstance(effectinstance1.getEffect(), effectinstance1.mapDuration(duration -> duration / 4), effectinstance1.getAmplifier(), effectinstance1.isAmbient(), effectinstance1.isVisible()));
						}
					}

					list.addAll(this.potionContents.customEffects());

					List<LivingEntity> list1 = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
					if (!list1.isEmpty()) {
						for(LivingEntity livingentity : list1) {
							if (!this.victims.containsKey(livingentity) && livingentity.isAffectedByPotions()) {
								double x = livingentity.getX() - this.getX();
								double y = livingentity.getY() + (livingentity.getDimensions(livingentity.getPose()).height() / 2) - (this.getY());
								double z = livingentity.getZ() - this.getZ();
								double d2 = x * x + y * y + z * z;
								if (d2 <= (double)(radius * radius)) {
									this.victims.put(livingentity, this.tickCount + this.reapplicationDelay);
									for (MobEffectInstance effectinstance : list) {
										if (effectinstance.getEffect().value().isInstantenous()) {
											effectinstance.getEffect().value().applyInstantenousEffect(this, this.getOwner(), livingentity, effectinstance.getAmplifier(), 0.5D);
										}
										else {
											livingentity.addEffect(new MobEffectInstance(effectinstance), this);
										}
									}
									if (this.radiusOnUse != 0.0F) {
										radius += this.radiusOnUse;
										if (radius < 0.5F) {
											this.discard();
											return;
										}
										this.setRadius(radius);
									}
									if (this.durationOnUse != 0) {
										this.duration += this.durationOnUse;
										if (this.duration <= 0) {
											this.discard();
											return;
										}
									}
								}
							}
						}
					}
				}
			}
		}

	}

	@Override
	public EntityDimensions getDimensions(Pose poseIn) {
		return EntityDimensions.scalable(this.getRadius() * 2.0F, this.getRadius() * 2.0F);
	}
}
