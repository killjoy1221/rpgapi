package mnm.plugins.rpg.api.ability;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.util.ResettableBuilder;

import java.util.Set;
import java.util.UUID;

public interface ClassProfile extends Identifiable, DataSerializable {

    static ClassProfile of(String name, ClassType type, User user) {
        return builder().name(name).type(type).build(user);
    }

    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    /**
     * User defined name
     *
     * @return
     */
    String getName();

    ClassType getClassType();

    Set<Ability> getAbilities();

    void add(Ability ability);

    void remove(AbilityType ability);

    int getLevel();

    void setLevel(int level);

    int getPoints();

    void setPoints(int points);

    interface Builder extends ResettableBuilder<ClassProfile, Builder> {

        Builder name(String name);

        Builder uniqueId(UUID uniqueId);

        Builder type(ClassType type);

        Builder abilities(Ability... abilities);

        Builder level(int level);

        Builder points(int points);

        ClassProfile build();
    }
}
