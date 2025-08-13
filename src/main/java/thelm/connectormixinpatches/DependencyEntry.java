package thelm.connectormixinpatches;


public record DependencyEntry(String modId, String versionRange, boolean negate) {

	public static DependencyEntry of(String modId, String versionRange, boolean negate) {
		return new DependencyEntry(modId, versionRange, negate);
	}

	public static DependencyEntry of(String modId, String versionRange) {
		return new DependencyEntry(modId, versionRange, false);
	}

	public static DependencyEntry of(String modId, boolean negate) {
		return new DependencyEntry(modId, "*", negate);
	}

	public static DependencyEntry of(String modId) {
		return new DependencyEntry(modId, "*", false);
	}

	public static DependencyEntry forge(String versionRange) {
		return new DependencyEntry("forge", versionRange, false);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(negate) {
			sb.append('!');
		}
		sb.append(modId);
		if(!"*".equals(versionRange)) {
			sb.append('@');
			sb.append(versionRange);
		}
		return sb.toString();
	}
}
