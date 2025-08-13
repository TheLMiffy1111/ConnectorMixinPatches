package thelm.connectormixinpatches.mixin.spectrum;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

@Mixin(Player.class)
public abstract class PlayerMixin4_2 extends LivingEntity {

	private PlayerMixin4_2(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Unique
	protected int getChanneling(ItemStack stack) {
		return EnchantmentHelper.getTagEnchantmentLevel(Enchantments.CHANNELING, stack);
	}

	@ModifyVariable(method = "attack", name = "reach", at = @At("STORE"))
	protected double spectrum$increaseSweepMaxDistance(double original) {
		ItemStack stack = getItemInHand(InteractionHand.MAIN_HAND);
		if(stack.getItem() == BuiltInRegistries.ITEM.get(new ResourceLocation("spectrum:draconic_twinsword"))) {
			return original * Math.sqrt(4.5 * (getChanneling(stack) + 1));
		}
		return original;
	}
}
