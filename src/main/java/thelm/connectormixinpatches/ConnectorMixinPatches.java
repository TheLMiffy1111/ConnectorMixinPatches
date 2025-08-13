package thelm.connectormixinpatches;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.VersionRange;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import com.bawnorton.mixinsquared.adjuster.MixinAnnotationAdjusterRegistrar;
import com.bawnorton.mixinsquared.api.MixinAnnotationAdjuster;

import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.IModInfo.DependencySide;
import net.minecraftforge.versions.forge.ForgeVersion;

public class ConnectorMixinPatches implements IMixinConfigPlugin {

	private static final Logger LOGGER = LogManager.getLogger();

	public static final String ADJUSTER_PACKAGE = "thelm.connectormixinpatches.adjuster.";

	public static final PatchEntry[] ADJUSTERS = new PatchEntry[] {
			PatchEntry.of(DependencySide.BOTH, "bewitchment", "[47.1.7,)", "bewitchment.BewitchmentMixinAdjuster1_7"),
			PatchEntry.of(DependencySide.BOTH, "spectrum", "[47.4.2,)", "spectrum.SpectrumMixinAdjuster4_2"),
	};

	public static final PatchEntry[] MIXINS = new PatchEntry[] {
			PatchEntry.of(DependencySide.BOTH, "bewitchment", "[47.1.7,)", "bewitchment.ForgeHooksMixin1_7"),
			PatchEntry.of(DependencySide.BOTH, "spectrum", "[47.4.2,)", "spectrum.PlayerMixin4_2"),
	};

	public static final ArtifactVersion FORGE_VERSION = new DefaultArtifactVersion(ForgeVersion.getVersion());

	@Override
	public void onLoad(String mixinPackage) {
		LOGGER.info("Adding adjusters");
		for(PatchEntry entry : ADJUSTERS) {
			if(!entry.side().isCorrectSide()) {
				continue;
			}
			LOGGER.info("Checking {}", entry.dependencies());
			if(dependenciesSatisfied(entry.dependencies())) {
				int adjusterCount = 0;
				for(String patchName : entry.patchNames()) {
					try {
						MixinAnnotationAdjusterRegistrar.register((MixinAnnotationAdjuster)Class.forName(ADJUSTER_PACKAGE + patchName).getConstructor().newInstance());
						adjusterCount++;
					}
					catch(Exception e) {
						LOGGER.warn("Error loading adjuster {}", patchName, e);
					}
				}
				LOGGER.info("Added {} adjusters", adjusterCount);
			}
			else {
				LOGGER.info("Added 0 adjusters");
			}
		}
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

	@Override
	public List<String> getMixins() {
		LOGGER.info("Adding mixins");
		List<String> mixins = new ArrayList<>();
		for(PatchEntry entry : MIXINS) {
			if(!entry.side().isCorrectSide()) {
				continue;
			}
			LOGGER.info("Checking {}", entry.dependencies());
			if(dependenciesSatisfied(entry.dependencies())) {
				Collections.addAll(mixins, entry.patchNames());
				LOGGER.info("Added {} mixins", entry.patchNames().length);
			}
			else {
				LOGGER.info("Added 0 mixins");
			}
		}
		return mixins;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

	public static boolean dependenciesSatisfied(List<DependencyEntry> dependencies) {
		return dependencies.stream().allMatch(dependency -> {
			ArtifactVersion version;
			if("forge".equals(dependency.modId())) {
				version = FORGE_VERSION;
			}
			else {
				IModInfo modInfo = LoadingModList.get().getMods().stream().
						filter(mod -> dependency.modId().equals(mod.getModId())).
						findFirst().orElse(null);
				if(modInfo == null) {
					boolean matched = dependency.negate();
					LOGGER.info("{}: {}", dependency, matched);
					return dependency.negate();
				}
				version = modInfo.getVersion();
			}
			VersionRange versionRange = getVersionRange(dependency.versionRange());
			if(versionRange == null) {
				LOGGER.warn("Invalid version range {}", dependency.versionRange());
				return false;
			}
			boolean matched = versionRange.containsVersion(version) != dependency.negate();
			LOGGER.info("{}: {}", dependency, matched);
			return matched;
		});
	}

	public static VersionRange getVersionRange(String versionRange) {
		try {
			return VersionRange.createFromVersionSpec(versionRange);
		}
		catch(Exception e) {
			return null;
		}
	}

	public static Class<?> getClass(String className) {
		try {
			return Class.forName(className);
		}
		catch(Exception e) {
			return null;
		}
	}
}
