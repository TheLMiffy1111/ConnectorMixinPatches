package thelm.connectormixinpatches.mixin.bewitchment;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.FluidType;

@Mixin(ForgeHooks.class)
public abstract class ForgeHooksMixin1_7 {

	@ModifyExpressionValue(method = "onLivingBreathe", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fluids/FluidType;isAir()Z", remap = false), remap = false)
	private static boolean bewitchment$voodooDrownEffect(boolean original, LivingEntity entity, int consumeAirAmount, int refillAirAmount) {
		return BWComponents.ADDITIONAL_WATER_DATA_COMPONENT.get(entity).isSubmerged() || original;
	}
}
