package mnm.plugins.rpg.api.event;

import mnm.plugins.rpg.api.event.impl.AbstractLevelEvent;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.util.annotation.eventgen.ImplementedBy;
import org.spongepowered.api.util.annotation.eventgen.PropertySettings;

@ImplementedBy(AbstractLevelEvent.class)
public interface LevelEvent extends Event, Cancellable {

    int getPreviousLevel();

    @PropertySettings(requiredParameter = false)
    int getOriginalLevel();

    int getLevel();

    void setLevel(int level);

    interface Ability extends LevelEvent, AbilityEvent {}

    interface Class extends LevelEvent, ClassProfileEvent {}
}
