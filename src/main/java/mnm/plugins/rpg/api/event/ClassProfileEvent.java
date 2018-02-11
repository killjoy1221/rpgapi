package mnm.plugins.rpg.api.event;

import mnm.plugins.rpg.api.ability.ClassProfile;
import org.spongepowered.api.event.Event;

public interface ClassProfileEvent extends Event {

    ClassProfile getProfile();
}
