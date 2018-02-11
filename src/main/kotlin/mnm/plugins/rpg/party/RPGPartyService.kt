package mnm.plugins.rpg.party;

import mnm.plugins.rpg.api.party.Party
import mnm.plugins.rpg.api.party.PartyService
import mnm.plugins.rpg.invoke
import mnm.plugins.rpg.optional
import mnm.plugins.rpg.sponge
import mnm.plugins.rpg.unwrap
import mnm.plugins.rpg.util.RPGKeys
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.service.user.UserStorageService
import java.util.*

class RPGPartyService : PartyService {

    private val parties by lazy {
        // populate the party info
        val map = mutableMapOf<UUID, RPGParty>()
        val users = sponge.serviceManager<UserStorageService>()!!
        users.all
                // convert gameprofile to user
                .mapNotNull {
                    users[it].unwrap
                }
                // make sure it has a party
                .filter {
                    it.supports(RPGKeys.PARTY) && it.require(RPGKeys.PARTY).isPresent
                }
                // add to or create party
                .map {
                    val party = it.require(RPGKeys.PARTY).unwrap!!
                    map.getOrPut(party, ::RPGParty) += it.uniqueId

                    // complains without this
                    Unit
                }
        map
    }

    override fun getAll() = parties.values.toSet()

    override fun get(member: User): Optional<Party> =
            member.get(RPGKeys.PARTY).unwrap?.let {
                it.unwrap?.let { parties[it] }
            }.optional

    override fun create() = RPGParty().also {
        parties[it.uniqueId] = it
    }

    override fun get(partyId: UUID): Optional<Party> = parties[partyId].optional

    override fun remove(partyId: UUID) = parties.remove(partyId)?.apply {
        users.forEach { user ->
            remove(user)
        }
    }
}
