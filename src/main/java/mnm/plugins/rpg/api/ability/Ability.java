package mnm.plugins.rpg.api.ability;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.util.ResettableBuilder;

public interface Ability extends DataSerializable {

    AbilityType getType();

    int getLevel();

    int getCooldown();

    int getManaCost();

    int getStaminaCost();

    static Ability of(AbilityType type, int level) {
        return builder()
                .type(type)
                .level(level)
                .build();
    }

    static Ability levelUp(Ability ability) {
        return builder()
                .from(ability)
                .level(ability.getLevel() + 1)
                .build();
    }

    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    interface Builder extends ResettableBuilder<Ability, Builder> {

        Builder type(AbilityType type);

        Builder level(int level);

        Builder cooldown(int cooldown);

        Builder mana(int manaCost);

        Builder stamina(int staminaCost);

        Ability build();
    }
}
