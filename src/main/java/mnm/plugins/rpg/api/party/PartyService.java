package mnm.plugins.rpg.api.party;

import org.spongepowered.api.entity.living.player.User;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PartyService {

    Set<Party> getAll();

    Optional<Party> get(UUID partyId);

    Optional<Party> get(User member);

    // lets get this party started!
    Party create();

    Party remove(UUID partyId);
}
