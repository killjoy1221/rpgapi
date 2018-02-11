package mnm.plugins.rpg.api.event.impl;

import mnm.plugins.rpg.api.event.LevelEvent;
import org.spongepowered.api.event.impl.AbstractEvent;
import org.spongepowered.api.util.annotation.eventgen.UseField;

public abstract class AbstractLevelEvent extends AbstractEvent implements LevelEvent {

    @UseField
    protected int originalLevel;
    @UseField
    protected int level;

    @Override
    protected void init() {
        this.originalLevel = level;
    }
}
