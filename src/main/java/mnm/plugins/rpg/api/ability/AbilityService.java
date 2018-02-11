package mnm.plugins.rpg.api.ability;

import org.spongepowered.api.entity.living.player.User;

public interface AbilityService {

    AbilityUser get(User user);

    ClassProfile newProfile(ClassType classType);

}
