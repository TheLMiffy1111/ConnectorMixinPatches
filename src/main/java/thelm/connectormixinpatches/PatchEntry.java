package thelm.connectormixinpatches;

import java.util.List;
import java.util.stream.Stream;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.forgespi.language.IModInfo.DependencySide;

public record PatchEntry(DependencySide side, List<DependencyEntry> dependencies, String... patchNames) {

	public static PatchEntry of(DependencySide side, List<DependencyEntry> dependencies, String... patchNames) {
		return new PatchEntry(side, dependencies, patchNames);
	}

	public static PatchEntry of(DependencySide side, List<DependencyEntry> dependencies, String forgeVersionRange, String... patchNames) {
		dependencies = Stream.concat(dependencies.stream(), Stream.of(DependencyEntry.forge(forgeVersionRange))).toList();
		return new PatchEntry(side, dependencies, patchNames);
	}

	public static PatchEntry of(DependencySide side, DependencyEntry dependency, String forgeVersionRange, String... patchNames) {
		List<DependencyEntry> dependencies = List.of(dependency, DependencyEntry.forge(forgeVersionRange));
		return new PatchEntry(side, dependencies, patchNames);
	}

	public static PatchEntry of(DependencySide side, String modId, String forgeVersionRange, String... patchNames) {
		List<DependencyEntry> dependencies = List.of(DependencyEntry.of(modId), DependencyEntry.forge(forgeVersionRange));
		return new PatchEntry(side, dependencies, patchNames);
	}
}
