package mnm.plugins.rpg.ability

import mnm.plugins.rpg.api.ability.Ability
import mnm.plugins.rpg.api.ability.AbilityType

class AbilityBuilder : Ability.Builder {

    private var type: AbilityType? = null
    private var level: Int = 1
    private var cooldown: Int = 0
    private var manaCost: Int = 0
    private var staminaCost: Int = 0

    override fun type(type: AbilityType) = apply {
        this.type = type
    }

    override fun level(level: Int) = apply {
        check(level >= 0) { "level cannot be negative" }
        this.level = level
    }

    override fun cooldown(cooldown: Int) = apply {
        check(level >= 0) { "coolcown cannot be negative" }
        this.cooldown = cooldown
    }

    override fun mana(manaCost: Int) = apply {
        check(manaCost >= 0) { "mana cost cannot be negative" }
        this.manaCost = manaCost
    }

    override fun stamina(staminaCost: Int) = apply {
        check(staminaCost >= 0) { "stamina cost cannot be negative" }
    }

    override fun reset() = apply {
        type = null
        level = 1
        cooldown = 0
        manaCost = 0
        staminaCost = 0
    }

    override fun from(value: Ability) = apply {
        type = value.type
        level = value.level
        cooldown = value.cooldown
        staminaCost = value.staminaCost
        manaCost = value.manaCost
    }

    override fun build() = AbilityImpl(checkNotNull(type), level, cooldown, staminaCost, manaCost)
}
