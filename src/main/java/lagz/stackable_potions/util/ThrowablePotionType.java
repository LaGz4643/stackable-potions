package lagz.stackable_potions.util;

import lagz.stackable_potions.StackablePotions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ThrowablePotionItem;
import net.minecraft.world.item.alchemy.Potion;

import java.util.concurrent.ConcurrentHashMap;

public record ThrowablePotionType(ThrowablePotionItem item, String potionId) {
    private static final ConcurrentHashMap<ThrowablePotionType, Identifier> POTION_TYPE_COOLDOWN_GROUP_CACHE = new ConcurrentHashMap<>();
    
    public static Identifier getCooldownGroup(ThrowablePotionItem potionItem, Potion potion) {
        return getCooldownGroup(new ThrowablePotionType(potionItem, potion.name()));
    }
    
    private static Identifier getCooldownGroup(ThrowablePotionType potionType) {
        return POTION_TYPE_COOLDOWN_GROUP_CACHE.computeIfAbsent(potionType, potionTypeKey -> {
            String potionPath = potionTypeKey.potionId();
            String potionItemPath = BuiltInRegistries.ITEM.getKey(potionTypeKey.item()).getPath();
            String cooldownGroupPath = potionItemPath.contains("_potion") ? potionItemPath.replace("_potion", "_" + potionPath + "_potion") : potionItemPath + "_" + potionPath;
            return Identifier.fromNamespaceAndPath(StackablePotions.MOD_ID, cooldownGroupPath);
        });
    }
}
