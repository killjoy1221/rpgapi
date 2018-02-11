package mnm.plugins.rpg.party.data

import mnm.plugins.rpg.api.party.data.ImmutablePartyMemberData
import mnm.plugins.rpg.api.party.data.PartyMemberData
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataHolder
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder
import org.spongepowered.api.data.persistence.AbstractDataBuilder
import java.util.*

object PartyManipulatorBuilder : AbstractDataBuilder<PartyMemberData>(PartyMemberData::class.java, 1),
        DataManipulatorBuilder<PartyMemberData, ImmutablePartyMemberData> {

    override fun buildContent(container: DataView): Optional<PartyMemberData> {
        return create().from(container as? DataContainer ?: container.copy())
    }

    override fun createFrom(dataHolder: DataHolder): Optional<PartyMemberData> {
        return create().fill(dataHolder)
    }

    override fun create() = RPGPartyMemberData()

}
