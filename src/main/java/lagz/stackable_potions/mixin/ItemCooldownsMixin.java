package lagz.stackable_potions.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import lagz.stackable_potions.StackablePotions;
import lagz.stackable_potions.util.ThrowablePotionType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ThrowablePotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(ItemCooldowns.class)
public class ItemCooldownsMixin {
    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType"})
    @WrapOperation(method = "getCooldownGroup", at = @At(value = "INVOKE", target = "Ljava/util/Optional;orElse(Ljava/lang/Object;)Ljava/lang/Object;"))
    private Object getEffectDependentGroup(Optional<Identifier> instance, Object otherIdentifier, Operation<Object> original, @Local(argsOnly = true) ItemStack itemstack) {
        if (itemstack.getItem() instanceof ThrowablePotionItem throwablePotionItem
                && instance
                .filter(StackablePotions.THROWABLE_POTION_COOLDOWN_GROUP::equals)
                .isPresent()) {
            return ThrowablePotionType.getCooldownGroup(throwablePotionItem, itemstack);
        }
        return original.call(instance, otherIdentifier);
    }
}
