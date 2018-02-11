package mnm.plugins.rpg.ability.data

import mnm.plugins.rpg.api.ability.ClassProfile
import mnm.plugins.rpg.api.ability.data.AbilityProfileData
import mnm.plugins.rpg.api.ability.data.ImmutableAbilityProfileData
import mnm.plugins.rpg.invoke
import mnm.plugins.rpg.optional
import mnm.plugins.rpg.sponge
import mnm.plugins.rpg.unwrap
import mnm.plugins.rpg.util.RPGKeys
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataHolder
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData
import org.spongepowered.api.data.merge.MergeFunction
import org.spongepowered.api.data.value.mutable.MapValue
import org.spongepowered.api.data.value.mutable.OptionalValue
import java.util.*

class AbilityUserData(
        private var profiles: Map<UUID, ClassProfile> = mapOf(),
        private var activeProfile: UUID? = null
) : AbstractData<AbilityProfileData, ImmutableAbilityProfileData>(), AbilityProfileData {

    override fun activeProfile(): OptionalValue<UUID> = sponge.registry.valueFactory.createOptionalValue(RPGKeys.ACTIVE_ABILITY_PROFILE, activeProfile)
    override fun profiles(): MapValue<UUID, ClassProfile> = sponge.registry.valueFactory.createMapValue(RPGKeys.ABILITY_PROFILES, profiles, emptyMap())

    override fun registerGettersAndSetters() {
        registerFieldGetter(RPGKeys.ACTIVE_ABILITY_PROFILE) { activeProfile }
        registerFieldSetter(RPGKeys.ACTIVE_ABILITY_PROFILE) { activeProfile = it.unwrap }

        registerFieldGetter(RPGKeys.ABILITY_PROFILES) { profiles }
        registerFieldSetter(RPGKeys.ABILITY_PROFILES) { profiles = it }

        registerKeyValue(RPGKeys.ACTIVE_ABILITY_PROFILE) { activeProfile() }
        registerKeyValue(RPGKeys.ABILITY_PROFILES) { profiles() }
    }

    override fun from(container: DataContainer): Optional<AbilityProfileData> {
        val view = container.getView(RPGKeys.ABILITY_PROFILES.query).unwrap ?: return Optional.empty()

        profiles = mapOf(*view.getKeys(false).mapNotNull {
            val uuid = UUID.fromString(it.last().toString())
            val data = view.getSerializable(it, ClassProfile::class.java).unwrap
            data?.let { uuid to it }
        }.toTypedArray())

        activeProfile = container.getObject(RPGKeys.ACTIVE_ABILITY_PROFILE.query, UUID::class.java).unwrap

        return this.optional
    }

    override fun fill(dataHolder: DataHolder, overlap: MergeFunction): Optional<AbilityProfileData> {
        return overlap.merge(this, dataHolder<AbilityProfileData>()).also {
            profiles = it.profiles().get()
            activeProfile = it.activeProfile().get().unwrap
        }.optional
    }

    override fun copy() = AbilityUserData(profiles, activeProfile)

    override fun asImmutable() = ImmutableAbilityUserData(profiles, activeProfile)

    override fun getContentVersion() = 1

    override fun toContainer(): DataContainer = super.toContainer().also {
        if (!profiles.isEmpty()) {
            it.set(RPGKeys.ABILITY_PROFILES, profiles)
        }
        if (activeProfile != null) {
            it.set(RPGKeys.ACTIVE_ABILITY_PROFILE.query, activeProfile!!)
        }
    }
}
