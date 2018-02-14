package mnm.plugins.rpg.api.experience;

import org.spongepowered.api.util.generator.dummy.DummyObjectProvider;

public final class ExperienceTypes {

    public static final ExperienceType CRAFT = DummyObjectProvider.createFor(ExperienceType.class, "CRAFT");

    public static final ExperienceType KILL = DummyObjectProvider.createFor(ExperienceType.class, "KILL");

    public static final ExperienceType MINE = DummyObjectProvider.createFor(ExperienceType.class, "MINE");

    private ExperienceTypes() {
        throw new UnsupportedOperationException("You should not instantiate this class");
    }
}
