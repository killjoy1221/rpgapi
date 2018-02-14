package mnm.plugins.rpg.api.event;

import mnm.plugins.rpg.api.ability.ClassProfile;
import mnm.plugins.rpg.api.event.impl.AbstractLevelEvent;
import org.spongepowered.api.event.user.TargetUserEvent;
import org.spongepowered.api.util.annotation.eventgen.ImplementedBy;

public interface ClassProfileEvent extends TargetUserEvent {

    ClassProfile getProfile();

    interface Change extends ClassProfileEvent {

        ClassProfile getOldProfile();

        void setProfile(ClassProfile profile);
    }

    @ImplementedBy(AbstractLevelEvent.class)
    interface Level extends LevelEvent, ClassProfileEvent {}

    interface AddAbility extends ClassProfileEvent {

    }
}
