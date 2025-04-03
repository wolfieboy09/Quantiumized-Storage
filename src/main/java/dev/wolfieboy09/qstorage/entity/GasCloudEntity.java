package dev.wolfieboy09.qstorage.entity;

import dev.wolfieboy09.qstorage.api.registry.QSRegistries;
import dev.wolfieboy09.qstorage.api.registry.gas.Gas;
import dev.wolfieboy09.qstorage.api.registry.gas.GasStack;
import dev.wolfieboy09.qstorage.particles.GasParticleOptions;
import dev.wolfieboy09.qstorage.registries.QSParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class GasCloudEntity extends AreaEffectCloud {
    private Gas gas;
    private int lifetime = 400;

    public GasCloudEntity(EntityType<? extends AreaEffectCloud> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
        this.setNoGravity(true);
    }


    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            AABB area = this.getBoundingBox().inflate(this.gas.getGasData().gasRange());

            serverLevel.getEntitiesOfClass(LivingEntity.class, area).forEach((entity) ->
                    this.gas.getGasData().effects().forEach((effect) ->
                            entity.addEffect(new MobEffectInstance(effect))));

            serverLevel.sendParticles(new GasParticleOptions(QSParticleTypes.GAS_PARTICLE.get(), new GasStack(this.gas)), this.getX(), this.getY(), this.getZ(), 1500, this.gas.getGasData().gasRange(), this.gas.getGasData().gasRange(), this.gas.getGasData().gasRange(), this.gas.getGasData().particleSpeed());

            if (--this.lifetime <= 0) {
                this.discard();
            }
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        this.lifetime = tag.getInt("Lifetime");
        this.gas = QSRegistries.GAS.get(ResourceLocation.parse(tag.getString("Gas")));
        super.readAdditionalSaveData(tag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Lifetime", this.lifetime);
        tag.putString("Gas", this.gas.getResourceLocation().toString());
        super.addAdditionalSaveData(tag);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0);
    }
}

