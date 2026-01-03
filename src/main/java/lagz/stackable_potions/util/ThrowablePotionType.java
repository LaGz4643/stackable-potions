package lagz.stackable_potions.util;

import lagz.stackable_potions.StackablePotions;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ThrowablePotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public record ThrowablePotionType(ThrowablePotionItem item, Optional<String> potionId) {
    private static final ConcurrentHashMap<ThrowablePotionType, Identifier> POTION_TYPE_COOLDOWN_GROUP_CACHE = new ConcurrentHashMap<>();
    
    public static Identifier getCooldownGroup(ThrowablePotionItem potionItem, ItemStack potionItemStack) {
        Optional<String> potionId = potionItemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                .potion()
                .map(Holder::value)
                .map(Potion::name);
        return getCooldownGroup(new ThrowablePotionType(potionItem, potionId));
    }
    
    private static Identifier getCooldownGroup(ThrowablePotionType potionType) {
        return POTION_TYPE_COOLDOWN_GROUP_CACHE.computeIfAbsent(potionType, potionTypeKey -> {
            String potionPath = potionTypeKey
                    .potionId()
                    .orElse("empty");
            String potionItemPath = BuiltInRegistries.ITEM.getKey(potionTypeKey.item()).getPath();
            String cooldownGroupPath = potionItemPath.contains("_potion") ? potionItemPath.replace("_potion", "_" + potionPath + "_potion") : potionItemPath + "_" + potionPath;
            return Identifier.fromNamespaceAndPath(StackablePotions.MOD_ID, cooldownGroupPath);
        });
    }
}
