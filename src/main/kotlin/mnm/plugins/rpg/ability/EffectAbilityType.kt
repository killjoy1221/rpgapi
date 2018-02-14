package mnm.plugins.rpg.ability

import mnm.plugins.rpg.api.ability.PassiveAbilityType
import mnm.plugins.rpg.api.event.AbilityEvent
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData
import org.spongepowered.api.effect.potion.PotionEffect
import org.spongepowered.api.effect.potion.PotionEffectType
import org.spongepowered.api.event.Listener

sealed class EffectAbilityType(
        private val id: String,
        private val name: String,
        private val potion: PotionEffectType,
        private val maxLevel: Int) : PassiveAbilityType {

    override fun getId() = id
    override fun getName() = name

    override fun getMaxLevel() = maxLevel

    companion object {

        @Listener
        fun onActivate(event: AbilityEvent.Start) {
            val ability = event.ability.type as? EffectAbilityType ?: return

            val player = event.targetEntity
            val level = event.ability.level

            val effect = PotionEffect.of(ability.potion, level, 160)

            player.offer(player.require(PotionEffectData::class.java).effects()
                    .removeAll { it.type == ability.potion }
                    .add(effect))

        }
    }
}
