package mnm.plugins.rpg.ability.data

import mnm.plugins.rpg.api.ability.data.AbilityProfileData
import mnm.plugins.rpg.api.ability.data.ImmutableAbilityProfileData
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataHolder
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder
import org.spongepowered.api.data.persistence.AbstractDataBuilder
import java.util.*

object AbilityManipulatorBuilder : AbstractDataBuilder<AbilityProfileData>(AbilityProfileData::class.java, 1),
        DataManipulatorBuilder<AbilityProfileData, ImmutableAbilityProfileData> {

    override fun buildContent(container: DataView): Optional<AbilityProfileData> {
        return create().from(container as? DataContainer ?: container.copy())
    }

    override fun createFrom(dataHolder: DataHolder): Optional<AbilityProfileData> {
        return create().fill(dataHolder)
    }

    override fun create() = AbilityUserData()

}
