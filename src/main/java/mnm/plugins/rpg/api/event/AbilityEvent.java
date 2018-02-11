package mnm.plugins.rpg.api.event;

import mnm.plugins.rpg.api.ability.Ability;
import org.spongepowered.api.event.Event;

public interface AbilityEvent extends Event {

    Ability getAbility();
}
