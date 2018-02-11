package mnm.plugins.rpg.api.party.data;

import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableOptionalValue;

import java.util.UUID;

public interface ImmutablePartyMemberData extends ImmutableDataManipulator<ImmutablePartyMemberData, PartyMemberData> {

    ImmutableOptionalValue<UUID> party();

    ImmutableOptionalValue<UUID> invite();
}
