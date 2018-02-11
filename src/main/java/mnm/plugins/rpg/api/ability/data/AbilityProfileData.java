package mnm.plugins.rpg.api.ability.data;

import mnm.plugins.rpg.api.ability.ClassProfile;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.value.mutable.MapValue;
import org.spongepowered.api.data.value.mutable.OptionalValue;

import java.util.UUID;

public interface AbilityProfileData
        extends DataManipulator<AbilityProfileData, ImmutableAbilityProfileData> {

    MapValue<UUID, ClassProfile> profiles();

    OptionalValue<UUID> activeProfile();

}
