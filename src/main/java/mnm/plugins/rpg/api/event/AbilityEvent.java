package mnm.plugins.rpg.api.event;

import mnm.plugins.rpg.api.ability.Ability;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.entity.living.humanoid.player.TargetPlayerEvent;

public interface AbilityEvent extends TargetPlayerEvent {

    Ability getAbility();

    interface Start extends Tick {}

    interface Tick extends AbilityEvent, Cancellable {
        int getRemainingTicks();

        void setRemainingTicks(int ticks);
    }

    interface Stop extends AbilityEvent {}

}
