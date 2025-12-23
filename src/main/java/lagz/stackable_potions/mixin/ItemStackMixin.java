package lagz.stackable_potions.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import lagz.stackable_potions.StackablePotions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.UseCooldown;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @WrapOperation(method = "applyAfterUseComponentSideEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/UseCooldown;apply(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V"))
    private void removeCreativeThrowablePotionCooldown(UseCooldown useCooldown, ItemStack itemstack, LivingEntity entity, Operation<Void> original) {
        if (entity instanceof Player player && player.getAbilities().instabuild &&
                useCooldown.cooldownGroup()
                        .filter(cooldownGroup ->
                                cooldownGroup.equals(StackablePotions.THROWABLE_POTION_COOLDOWN_GROUP))
                        .isPresent()) {
            return;
        }
        original.call(useCooldown, itemstack, entity);
    }
}
