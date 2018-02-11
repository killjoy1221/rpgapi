package mnm.plugins.rpg.ability

import mnm.plugins.rpg.api.ability.Ability
import mnm.plugins.rpg.api.ability.AbilityType
import org.spongepowered.api.item.inventory.ItemStackSnapshot

class AbilityBuilder : Ability.Builder {

    private var type: AbilityType? = null
    private var level: Int = 1
    private var itemIcon: ItemStackSnapshot? = null


    override fun type(type: AbilityType) = apply {
        this.type = type
    }

    override fun level(level: Int) = apply {
        check(level >= 0) {
            "level must be natural number"
        }
        this.level = level
    }

    override fun itemIcon(item: ItemStackSnapshot) = apply {
        this.itemIcon = item
    }

    override fun reset() = apply {
        type = null
        level = 1
        itemIcon = null
    }

    override fun from(value: Ability) = apply {
        type = value.type
        level = value.level
        itemIcon = value.itemIcon
    }

    override fun build() = AbilityImpl(checkNotNull(type), level, checkNotNull(itemIcon))
}
