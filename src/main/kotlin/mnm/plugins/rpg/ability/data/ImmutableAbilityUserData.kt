package mnm.plugins.rpg.ability.data

import mnm.plugins.rpg.api.ability.ClassProfile
import mnm.plugins.rpg.api.ability.data.AbilityProfileData
import mnm.plugins.rpg.api.ability.data.ImmutableAbilityProfileData
import mnm.plugins.rpg.optional
import mnm.plugins.rpg.sponge
import mnm.plugins.rpg.util.RPGKeys
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData
import org.spongepowered.api.data.value.immutable.ImmutableMapValue
import org.spongepowered.api.data.value.immutable.ImmutableOptionalValue
import java.util.*

class ImmutableAbilityUserData(
        private val profiles: Map<UUID, ClassProfile>,
        private val activeProfile: UUID?
) : AbstractImmutableData<ImmutableAbilityProfileData, AbilityProfileData>(), ImmutableAbilityProfileData {

    override fun activeProfile(): ImmutableOptionalValue<UUID> = sponge.registry.valueFactory.createOptionalValue(RPGKeys.ACTIVE_ABILITY_PROFILE, activeProfile).asImmutable() as ImmutableOptionalValue<UUID>

    override fun profiles(): ImmutableMapValue<UUID, ClassProfile> = sponge.registry.valueFactory.createMapValue(RPGKeys.ABILITY_PROFILES, profiles).asImmutable()

    override fun registerGetters() {
        registerFieldGetter(RPGKeys.ACTIVE_ABILITY_PROFILE) { activeProfile }
        registerFieldGetter(RPGKeys.ABILITY_PROFILES) { profiles }

        registerKeyValue(RPGKeys.ACTIVE_ABILITY_PROFILE) { activeProfile() }
        registerKeyValue(RPGKeys.ABILITY_PROFILES) { profiles() }
    }

    override fun getContentVersion() = 1

    override fun asMutable(): AbilityProfileData {
        return AbilityUserData(profiles, activeProfile)
    }

    override fun toContainer(): DataContainer = super.toContainer()
            .set(RPGKeys.ACTIVE_ABILITY_PROFILE, activeProfile.optional)
            .set(RPGKeys.ABILITY_PROFILES, profiles)
}
