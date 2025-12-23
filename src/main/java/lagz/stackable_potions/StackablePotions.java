package lagz.stackable_potions;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.UseCooldown;

import java.util.List;
import java.util.Optional;

public class StackablePotions implements ModInitializer {
	public static final String MOD_ID = "stackable_potions";
//	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        DefaultItemComponentEvents.MODIFY.register(context -> context.modify(Items.POTION, builder -> builder.set(DataComponents.MAX_STACK_SIZE, 16)));
        DefaultItemComponentEvents.MODIFY.register(context -> context.modify(List.of(Items.SPLASH_POTION, Items.LINGERING_POTION), (builder, item) -> builder.set(DataComponents.MAX_STACK_SIZE, 16).set(DataComponents.USE_COOLDOWN, new UseCooldown(1.0F, Optional.of(Identifier.fromNamespaceAndPath(StackablePotions.MOD_ID, "throwable_potions"))))));
	}
}