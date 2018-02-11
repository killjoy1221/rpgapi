package mnm.plugins.rpg.api.party.data;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.value.mutable.OptionalValue;

import java.util.UUID;

public interface PartyMemberData extends DataManipulator<PartyMemberData, ImmutablePartyMemberData> {

    OptionalValue<UUID> party();

    OptionalValue<UUID> invite();
}
