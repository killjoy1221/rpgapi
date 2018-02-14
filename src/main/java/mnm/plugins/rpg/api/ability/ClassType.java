package mnm.plugins.rpg.api.ability;

import org.spongepowered.api.CatalogType;

import java.util.Set;

public interface ClassType extends CatalogType {

    Set<AbilityType> getSupportedSkills();

    int getMaxLevel();
}
