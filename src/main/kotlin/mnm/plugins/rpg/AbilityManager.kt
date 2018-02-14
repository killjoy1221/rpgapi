package mnm.plugins.rpg

import mnm.plugins.rpg.api.ability.Ability
import mnm.plugins.rpg.api.ability.ClassProfile
import mnm.plugins.rpg.api.event.impl.RPGEventFactory
import mnm.plugins.rpg.command.CastCommand
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.scheduler.Task

class AbilityManager(plugin: RPGPlugin) {

    private val queue = mutableListOf<AbilityTask>()

    init {
        sponge.commandManager.register(plugin, CastCommand.spec, "cast")

        Task.builder()
                .intervalTicks(1)
                .execute(::run)
                .submit(plugin)
    }

    fun activateAbility(player: Player, profile: ClassProfile, ability: Ability) {
        sponge.causeStackManager.pushCauseFrame().use {
            val event = RPGEventFactory.createAbilityEventStart(it.currentCause, ability, player, 0)
            if (!sponge.eventManager.post(event)) {
                profile.mana -= ability.manaCost
                profile.stamina -= ability.staminaCost

                if (event.remainingTicks > 0) // add the task to the queue to be completed next tick.
                    queue += AbilityTask(player, profile, ability, event.remainingTicks)
                else // post the stop ability stop event
                    sponge.causeStackManager.pushCauseFrame().use {
                        it.pushCause(event)
                        sponge.eventManager.post(RPGEventFactory.createAbilityEventStop(it.currentCause, ability, player))
                    }
            }
        }
    }

    private fun run() {
        // remove any players which don't exist
        queue.removeAll { it.player.isRemoved }
        queue.removeAll { data ->
            sponge.causeStackManager.pushCauseFrame().use {
                val event = RPGEventFactory.createAbilityEventTick(it.currentCause, data.ability, data.player, data.ticks - 1)

                // remove any completed abilities from the queue
                if (sponge.eventManager.post(event) || event.remainingTicks <= 0) {
                    // send the stop event
                    sponge.causeStackManager.pushCauseFrame().use {
                        it.pushCause(event)
                        sponge.eventManager.post(RPGEventFactory.createAbilityEventStop(it.currentCause, data.ability, data.player))
                    }
                    return@removeAll true
                }
                // reassign the ticks to the task
                data.ticks = event.remainingTicks
            }
            false
        }

    }

    private data class AbilityTask(
            val player: Player,
            val profile: ClassProfile,
            val ability: Ability,
            var ticks: Int)

}
