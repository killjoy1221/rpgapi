package mnm.plugins.rpg.party.data

import mnm.plugins.rpg.*
import mnm.plugins.rpg.api.party.data.ImmutablePartyMemberData
import mnm.plugins.rpg.api.party.data.PartyMemberData
import mnm.plugins.rpg.util.RPGKeys
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataHolder
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData
import org.spongepowered.api.data.merge.MergeFunction
import org.spongepowered.api.data.value.mutable.OptionalValue
import java.util.*

class RPGPartyMemberData(
        private var party: UUID? = null,
        private var invite: UUID? = null
) : AbstractData<PartyMemberData, ImmutablePartyMemberData>(), PartyMemberData {

    override fun party(): OptionalValue<UUID> = sponge.registry.valueFactory.createOptionalValue(RPGKeys.PARTY, party)
    override fun invite(): OptionalValue<UUID> = sponge.registry.valueFactory.createOptionalValue(RPGKeys.PARTY_INVITE, invite)

    override fun registerGettersAndSetters() {
        registerFieldSetter(RPGKeys.PARTY) { party = it.unwrap }
        registerFieldSetter(RPGKeys.PARTY_INVITE) { invite = it.unwrap }

        registerFieldGetter(RPGKeys.PARTY) { party.optional }
        registerFieldGetter(RPGKeys.PARTY_INVITE) { invite.optional }

        registerKeyValue(RPGKeys.PARTY) { party() }
        registerKeyValue(RPGKeys.PARTY_INVITE) { invite() }
    }

    override fun copy() = RPGPartyMemberData(party, invite)

    override fun from(container: DataContainer): Optional<PartyMemberData> = this.also {
        this.party = container.getObject<UUID>(RPGKeys.PARTY.query)
        this.invite = container.getObject<UUID>(RPGKeys.PARTY_INVITE.query)
    }.optional

    override fun fill(dataHolder: DataHolder, overlap: MergeFunction) =
            overlap.merge(this, dataHolder<PartyMemberData>()).also {
                this.party = it.party().get().unwrap
                this.invite = it.invite().get().unwrap
            }.optional

    override fun getContentVersion() = 1

    override fun asImmutable() = RPGImmutablePartyMemberData(party, invite)

    override fun toContainer(): DataContainer {
        return super.toContainer().also {
            if (party != null) {
                it.set(RPGKeys.PARTY.query, party!!)
            }
            if (invite != null) {
                it.set(RPGKeys.PARTY_INVITE.query, invite!!)
            }
        }
    }
}
