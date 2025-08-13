package thelm.connectormixinpatches.adjuster.bewitchment;

import java.util.List;

import org.objectweb.asm.tree.MethodNode;

import com.bawnorton.mixinsquared.adjuster.tools.AdjustableAnnotationNode;
import com.bawnorton.mixinsquared.api.MixinAnnotationAdjuster;

public class BewitchmentMixinAdjuster1_7 implements MixinAnnotationAdjuster {

	@Override
	public AdjustableAnnotationNode adjust(List<String> targetClassNames, String mixinClassName, MethodNode handlerNode, AdjustableAnnotationNode annotationNode) {
		if("moriyashiine.bewitchment.mixin.poppet.LivingEntityMixin".equals(mixinClassName) &&
				"bewitchment$voodooDrownEffect".equals(handlerNode.name)) {
			return null;
		}
		return annotationNode;
	}
}
