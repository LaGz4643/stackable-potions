package lagz.stackable_potions.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import lagz.stackable_potions.StackablePotions;
import lagz.stackable_potions.util.ThrowablePotionType;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ThrowablePotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
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
            Optional<Potion> potion = itemstack
                    .getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                    .potion()
                    .map(Holder::value);
            if (potion.filter(Potion::hasInstantEffects).isPresent()) {
                return ThrowablePotionType.getCooldownGroup(throwablePotionItem, potion.get());
            }
        }
        return original.call(instance, otherIdentifier);
    }
}
