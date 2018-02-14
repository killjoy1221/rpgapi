package mnm.plugins.rpg.api.experience;

public interface Experience<T> {

    ExperienceType getType();

    int getAmount();
}
