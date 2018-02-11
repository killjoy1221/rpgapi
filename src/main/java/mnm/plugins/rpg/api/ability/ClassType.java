package mnm.plugins.rpg.api.ability;

import mnm.plugins.rpg.api.ItemRepresentable;
import org.spongepowered.api.CatalogType;

import java.util.Set;

public interface ClassType extends ItemRepresentable, CatalogType {

    Set<AbilityType> getSupportedSkills();

    int getMaxLevel();
}
