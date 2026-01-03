package lagz.stackable_potions.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import lagz.stackable_potions.StackablePotions;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ThrowablePotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.UseCooldown;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @WrapOperation(method = "applyAfterUseComponentSideEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/UseCooldown;apply(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V"))
    private void removeCreativeThrowablePotionCooldown(UseCooldown useCooldown, ItemStack itemstack, LivingEntity entity, Operation<Void> original) {
        if (itemstack.getItem() instanceof ThrowablePotionItem
                && useCooldown.cooldownGroup()
                    .filter(StackablePotions.THROWABLE_POTION_COOLDOWN_GROUP::equals)
                    .isPresent()) {
            if ((entity instanceof Player player && player.getAbilities().instabuild)) {
                return;
            }
            if (itemstack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                    .potion()
                    .map(Holder::value)
                    .filter(Potion::hasInstantEffects)
                    .isEmpty()) {
                return;
            }
        }
        original.call(useCooldown, itemstack, entity);
    }
}
