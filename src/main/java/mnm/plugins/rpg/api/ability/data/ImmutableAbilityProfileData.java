package mnm.plugins.rpg.api.ability.data;

import mnm.plugins.rpg.api.ability.ClassProfile;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableMapValue;
import org.spongepowered.api.data.value.immutable.ImmutableOptionalValue;

import java.util.UUID;

public interface ImmutableAbilityProfileData
        extends ImmutableDataManipulator<ImmutableAbilityProfileData, AbilityProfileData> {

    ImmutableMapValue<UUID, ClassProfile> profiles();

    ImmutableOptionalValue<UUID> activeProfile();


}
