package thelm.connectormixinpatches.adjuster.spectrum;

import java.util.List;

import org.objectweb.asm.tree.MethodNode;

import com.bawnorton.mixinsquared.adjuster.tools.AdjustableAnnotationNode;
import com.bawnorton.mixinsquared.api.MixinAnnotationAdjuster;

public class SpectrumMixinAdjuster4_2 implements MixinAnnotationAdjuster {

	@Override
	public AdjustableAnnotationNode adjust(List<String> targetClassNames, String mixinClassName, MethodNode handlerNode, AdjustableAnnotationNode annotationNode) {
		if("de.dafuqs.spectrum.mixin.compat.connectormod.present.PlayerEntityMixin".equals(mixinClassName) &&
				"spectrum$increaseSweepMaxDistance".equals(handlerNode.name)) {
			return null;
		}
		return annotationNode;
	}
}
