package mnm.plugins.rpg.util

import mnm.plugins.rpg.api.ability.ClassProfile
import mnm.plugins.rpg.typeToken
import org.spongepowered.api.data.DataQuery
import org.spongepowered.api.data.key.Key
import org.spongepowered.api.data.value.mutable.MapValue
import org.spongepowered.api.data.value.mutable.OptionalValue
import java.util.*

object RPGKeys {

    val ABILITY_PROFILES: Key<MapValue<UUID, ClassProfile>> = Key.builder()
            .type(typeToken<MapValue<UUID, ClassProfile>>())
            .id("ability_profiles")
            .name("Class Profiles")
            .query(DataQuery.of("RPG", "Profiles"))
            .build()

    val ACTIVE_ABILITY_PROFILE: Key<OptionalValue<UUID>> = Key.builder()
            .type(typeToken<OptionalValue<UUID>>())
            .id("active_ability_profile")
            .name("Active Class Profile")
            .query(DataQuery.of("RPG", "ActiveProfile"))
            .build()

    val PARTY: Key<OptionalValue<UUID>> = Key.builder()
            .type(typeToken<OptionalValue<UUID>>())
            .id("party_member")
            .name("Joined Party")
            .query(DataQuery.of("Party"))
            .build()

    val PARTY_INVITE: Key<OptionalValue<UUID>> = Key.builder()
            .type(typeToken<OptionalValue<UUID>>())
            .id("party_invite")
            .name("Invited Party")
            .query((DataQuery.of("PartyInvite")))
            .build()
}
