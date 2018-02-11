package mnm.plugins.rpg.party.data

import mnm.plugins.rpg.api.party.data.ImmutablePartyMemberData
import mnm.plugins.rpg.api.party.data.PartyMemberData
import mnm.plugins.rpg.optional
import mnm.plugins.rpg.sponge
import mnm.plugins.rpg.util.RPGKeys
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData
import org.spongepowered.api.data.value.immutable.ImmutableOptionalValue
import java.util.*

class RPGImmutablePartyMemberData(
        private val party: UUID?,
        private val invite: UUID?
) : AbstractImmutableData<ImmutablePartyMemberData, PartyMemberData>(), ImmutablePartyMemberData {

    override fun party(): ImmutableOptionalValue<UUID> = sponge.registry.valueFactory.createOptionalValue(RPGKeys.PARTY, party).asImmutable() as ImmutableOptionalValue<UUID>
    override fun invite(): ImmutableOptionalValue<UUID> = sponge.registry.valueFactory.createOptionalValue(RPGKeys.PARTY_INVITE, invite).asImmutable() as ImmutableOptionalValue<UUID>

    override fun registerGetters() {
        registerFieldGetter(RPGKeys.PARTY) { party.optional }
        registerFieldGetter(RPGKeys.PARTY_INVITE) { invite.optional }

        registerKeyValue(RPGKeys.PARTY) { party() }
        registerKeyValue(RPGKeys.PARTY_INVITE) { invite() }
    }

    override fun getContentVersion() = 1

    override fun asMutable() = RPGPartyMemberData(party, invite)

    override fun toContainer(): DataContainer {
        return super.toContainer().also {
            if (party != null) {
                it.set(RPGKeys.PARTY.query, party)
            }
            if (invite != null) {
                it.set(RPGKeys.PARTY_INVITE.query, invite)
            }
        }
    }
}
