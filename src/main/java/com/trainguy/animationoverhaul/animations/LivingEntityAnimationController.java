package com.trainguy.animationoverhaul.animations;

import com.trainguy.animationoverhaul.access.LivingEntityAccess;
import com.trainguy.animationoverhaul.util.animation.LivingEntityAnimParams;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;

public class LivingEntityAnimationController<T extends LivingEntity, L extends LivingEntityPartAnimator<T, M>, M extends EntityModel<T>> {

    private final L partAnimator;
    private final T livingEntity;

    @SuppressWarnings("unchecked")
    public LivingEntityAnimationController(T livingEntity, M model, LivingEntityAnimParams livingEntityAnimParams){
        partAnimator = switch (livingEntity.getType().toString().split("\\.")[2]){
            case "player" -> (L) new PlayerPartAnimator<T, M>(livingEntity, model, livingEntityAnimParams);
            default -> null;
        };
        this.livingEntity = livingEntity;
    }

    public void animate(){
        // Only animate the parts if the part animator exists
        if(partAnimator != null){
            partAnimator.initModel();
            if(((LivingEntityAccess)livingEntity).getUseInventoryRenderer()){
                partAnimator.adjustTimersInventory();
                partAnimator.animatePartsInventory();
                ((LivingEntityAccess)livingEntity).setUseInventoryRenderer(false);
            } else {
                partAnimator.adjustGeneralMovementTimers();
                partAnimator.adjustTimers();
                partAnimator.animateParts();
            }
            partAnimator.bakeLocatorRig();
            partAnimator.finalizeModel();
        }
    }
}
