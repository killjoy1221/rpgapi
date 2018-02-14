package mnm.plugins.rpg.ability

import mnm.plugins.rpg.api.ability.Ability
import mnm.plugins.rpg.api.ability.AbilityType
import mnm.plugins.rpg.getCatalogType
import mnm.plugins.rpg.optional
import mnm.plugins.rpg.unwrap
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataQuery
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.Queries
import org.spongepowered.api.data.persistence.AbstractDataBuilder
import java.util.*

class AbilityImpl(
        private val type: AbilityType,
        private val level: Int,
        private val cooldown: Int,
        private val manaCost: Int,
        private val staminaCost: Int
) : Ability {

    override fun getType() = type
    override fun getLevel() = level
    override fun getCooldown() = cooldown
    override fun getManaCost() = manaCost
    override fun getStaminaCost() = staminaCost

    override fun getContentVersion() = 1

    override fun toContainer(): DataContainer = DataContainer.createNew()
            .set(Queries.CONTENT_VERSION, contentVersion)
            .set(TYPE, type)
            .set(MANA, manaCost)
            .set(STAMINA, staminaCost)
            .set(LEVEL, level)

    companion object : AbstractDataBuilder<Ability>(Ability::class.java, 1) {

        val TYPE: DataQuery = DataQuery.of("Type")
        val LEVEL: DataQuery = DataQuery.of("Level")
        val MANA: DataQuery = DataQuery.of("ManaCost")
        val STAMINA: DataQuery = DataQuery.of("StaminaCost")

        override fun buildContent(container: DataView): Optional<Ability> {
            val type = container.getCatalogType<AbilityType>(TYPE) ?: return null.optional
            val level = container.getInt(LEVEL).unwrap ?: return null.optional
            val mana = container.getInt(MANA).unwrap ?: 0
            val stamina = container.getInt(STAMINA).unwrap ?: 0

            return Ability.builder()
                    .type(type)
                    .level(level)
                    .mana(mana)
                    .stamina(stamina)
                    .build().optional
        }
    }
}
