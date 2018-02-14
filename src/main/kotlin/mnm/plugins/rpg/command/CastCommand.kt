package mnm.plugins.rpg.command

import mnm.plugins.rpg.RPGPlugin
import mnm.plugins.rpg.api.ability.Ability
import mnm.plugins.rpg.api.ability.AbilityType
import mnm.plugins.rpg.api.ability.PassiveAbilityType
import mnm.plugins.rpg.api.ability.data.AbilityProfileData
import mnm.plugins.rpg.sponge
import mnm.plugins.rpg.unwrap
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.args.GenericArguments
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

class CastCommand() : CommandExecutor {

    override fun execute(src: CommandSource, args: CommandContext): CommandResult {
        if (src !is Player) throw CommandException(Text.of("Must be player!"))

        val data = src.get(AbilityProfileData::class.java).unwrap
                ?: throw CommandException(Text.of("You don't have any class profiles"))
        val uuid = data.activeProfile().get().unwrap
                ?: throw CommandException(Text.of("You don't have an active profile"))
        val profile = data.profiles().get()[uuid]
                ?: throw CommandException(Text.of("You do not have an active profile"))

        val abil = args.getOne<String>(ABILITY).get()

        val type = sponge.registry.getType(AbilityType::class.java, abil).unwrap
                ?: throw CommandException(Text.of("Unknown ability: $abil"))

        val ability: Ability = profile.abilities.find { it.type == type }
                ?: throw CommandException(Text.of("You do not have that ability"))

        if (ability is PassiveAbilityType) {
            throw CommandException(Text.of("$abil is a passive ability. You cannot activate it manually."))
        }

        RPGPlugin.INSTANCE.abilityManager.activateAbility(src, profile, ability)

        return CommandResult.success()
    }

    companion object {

        private val ABILITY: Text = Text.of("ability")

        val spec = CommandSpec.builder()
                .arguments(GenericArguments.string(ABILITY))
                .executor(CastCommand())
                .build()
    }

}
