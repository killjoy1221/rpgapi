package mnm.plugins.rpg.ability

import mnm.plugins.rpg.api.ability.Ability
import mnm.plugins.rpg.api.ability.AbilityType
import mnm.plugins.rpg.getCatalogType
import mnm.plugins.rpg.getSerializable
import mnm.plugins.rpg.optional
import mnm.plugins.rpg.unwrap
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataQuery
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.Queries
import org.spongepowered.api.data.persistence.AbstractDataBuilder
import org.spongepowered.api.item.inventory.ItemStackSnapshot
import java.util.*

class AbilityImpl(
        private val type: AbilityType,
        private var level: Int,
        private val itemIcon: ItemStackSnapshot
) : Ability {

    override fun getType() = type
    override fun getLevel() = level
    override fun getItemIcon() = itemIcon

    override fun setLevel(level: Int) {
        this.level = level
    }

    override fun getContentVersion() = 1

    override fun toContainer(): DataContainer = DataContainer.createNew()
            .set(Queries.CONTENT_VERSION, contentVersion)
            .set(TYPE, type)
            .set(ITEM, itemIcon)
            .set(LEVEL, level)

    companion object : AbstractDataBuilder<Ability>(Ability::class.java, 1) {

        val TYPE: DataQuery = DataQuery.of("Type")
        val ITEM: DataQuery = DataQuery.of("Item")
        val LEVEL: DataQuery = DataQuery.of("Level")

        override fun buildContent(container: DataView): Optional<Ability> {
            val type = container.getCatalogType<AbilityType>(TYPE) ?: return null.optional
            val item = container.getSerializable<ItemStackSnapshot>(ITEM) ?: return null.optional
            val level = container.getInt(LEVEL).unwrap ?: return null.optional

            return Ability.builder()
                    .type(type)
                    .itemIcon(item)
                    .level(level)
                    .build().optional
        }
    }
}
