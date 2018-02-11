package mnm.plugins.rpg.party

import mnm.plugins.rpg.api.party.Party
import mnm.plugins.rpg.invoke
import mnm.plugins.rpg.optional
import mnm.plugins.rpg.sponge
import mnm.plugins.rpg.unwrap
import mnm.plugins.rpg.util.RPGKeys
import org.spongepowered.api.data.DataTransactionResult
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.service.user.UserStorageService
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors
import java.util.*

class RPGParty : Party {

    private val id: UUID = UUID.randomUUID()
    private val members = mutableSetOf<UUID>()

    override fun getUniqueId() = id

    override fun getUsers() = members
            .map { sponge.serviceManager<UserStorageService>()!![it] }
            .filter { it.isPresent }
            .map { it.get() }
            .toSet()

    override fun add(member: User): DataTransactionResult {
        return if (member.get(RPGKeys.PARTY).unwrap?.unwrap != null)
            DataTransactionResult.failNoData()
        else {
            channel.send(Text.of("Welcome ", Text.of(TextColors.YELLOW, member.name), " to the party!"))
            members += member.uniqueId
            member.offer(RPGKeys.PARTY, uniqueId.optional)
        }
    }

    override fun remove(member: User): DataTransactionResult {
        return if (member.uniqueId !in members || member.get(RPGKeys.PARTY).unwrap?.isPresent == false)
            DataTransactionResult.failNoData()
        else {
            members -= member.uniqueId
            member.offer(RPGKeys.PARTY, Optional.empty())
        }
    }

    /**
     * Adds to the members list without effecting the data. It is not persisted.
     */
    internal operator fun plusAssign(member: UUID) {
        members += member
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Party) {
            return false
        }
        return uniqueId == other.uniqueId
    }

    override fun hashCode() = uniqueId.hashCode()
}
