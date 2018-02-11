package mnm.plugins.rpg.api.ability;

import mnm.plugins.rpg.api.ItemRepresentable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.util.ResettableBuilder;

public interface Ability extends ItemRepresentable, DataSerializable {

    AbilityType getType();

    int getLevel();

    void setLevel(int level);

    static Ability of(AbilityType type, ItemStackSnapshot item) {
        return builder()
                .type(type)
                .itemIcon(item)
                .build();
    }

    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    interface Builder extends ResettableBuilder<Ability, Builder> {

        Builder type(AbilityType type);

        Builder level(int level);

        Builder itemIcon(ItemStackSnapshot item);

        Ability build();
    }
}
