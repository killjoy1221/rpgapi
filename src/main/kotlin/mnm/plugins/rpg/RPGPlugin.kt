package mnm.plugins.rpg

import com.google.inject.Inject
import mnm.plugins.rpg.ability.*
import mnm.plugins.rpg.ability.data.AbilityManipulatorBuilder
import mnm.plugins.rpg.ability.data.AbilityUserData
import mnm.plugins.rpg.api.ability.Ability
import mnm.plugins.rpg.api.ability.ClassProfile
import mnm.plugins.rpg.api.ability.ClassType
import mnm.plugins.rpg.api.ability.data.AbilityProfileData
import mnm.plugins.rpg.api.ability.data.ImmutableAbilityProfileData
import mnm.plugins.rpg.api.party.PartyService
import mnm.plugins.rpg.api.party.data.ImmutablePartyMemberData
import mnm.plugins.rpg.api.party.data.PartyMemberData
import mnm.plugins.rpg.party.RPGPartyService
import mnm.plugins.rpg.party.data.PartyManipulatorBuilder
import mnm.plugins.rpg.party.data.RPGImmutablePartyMemberData
import mnm.plugins.rpg.party.data.RPGPartyMemberData
import mnm.plugins.rpg.util.RPGKeys
import org.slf4j.Logger
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.args.GenericArguments
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.data.DataRegistration
import org.spongepowered.api.data.key.Key
import org.spongepowered.api.data.value.BaseValue
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.filter.Getter
import org.spongepowered.api.event.game.GameRegistryEvent
import org.spongepowered.api.event.game.state.GameInitializationEvent
import org.spongepowered.api.event.game.state.GamePostInitializationEvent
import org.spongepowered.api.event.game.state.GamePreInitializationEvent
import org.spongepowered.api.event.network.ClientConnectionEvent
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.action.TextActions
import org.spongepowered.api.text.format.TextColors

@Plugin(id = "rpgapi",
        name = "Role Playing Game API",
        description = "API for RPG plugins",
        authors = ["killjoy1221"])
class RPGPlugin
@Inject constructor(
        private val container: PluginContainer,
        private val logger: Logger) {

    companion object {
        val PARTY_PREFIX = TextColors.LIGHT_PURPLE + "[Party] "

        lateinit var INSTANCE: RPGPlugin
            private set
    }

    private val partyService get() = sponge.serviceManager<PartyService>()!!

    lateinit var abilityManager: AbilityManager

    init {
        INSTANCE = this
    }

    @Listener
    fun registerBuilders(event: GamePreInitializationEvent) {

        sponge.dataManager.registerBuilder(ClassProfile::class.java, RPGClassProfile)
        sponge.dataManager.registerBuilder(Ability::class.java, AbilityImpl)

        DataRegistration.builder()
                .dataClass(PartyMemberData::class.java)
                .immutableClass(ImmutablePartyMemberData::class.java)
                .dataImplementation(RPGPartyMemberData::class.java)
                .immutableImplementation(RPGImmutablePartyMemberData::class.java)
                .builder(PartyManipulatorBuilder)
                .manipulatorId("rpg-party")
                .dataName("RPG Party")
                .buildAndRegister(container)

        DataRegistration.builder()
                .dataClass(AbilityProfileData::class.java)
                .immutableClass(ImmutableAbilityProfileData::class.java)
                .dataImplementation(AbilityUserData::class.java)
                .immutableImplementation(ImmutableAbilityProfileData::class.java)
                .builder(AbilityManipulatorBuilder)
                .manipulatorId("rpg-ability")
                .dataName("RPG Ability")
                .buildAndRegister(container)

        sponge.registry.registerBuilderSupplier(Ability.Builder::class.java, ::AbilityBuilder)
        sponge.registry.registerBuilderSupplier(ClassProfile.Builder::class.java, ::ClassProfileBuilder)

        sponge.registry.registerModule(ClassType::class.java, ClassTypeModule())

        sponge.serviceManager.setProvider(this, PartyService::class.java, RPGPartyService())
    }

    @Listener
    fun init(event: GameInitializationEvent) {
        sponge.eventManager.registerListeners(this, EffectAbilityType)

        abilityManager = AbilityManager(this)
    }

    // TODO this goes in separate plugin
    @Listener
    fun registerCommands(event: GamePostInitializationEvent) {
        sponge.commandManager.register(this, CommandSpec.builder()
                .child(CommandSpec.builder()
                        .executor { src, _ ->
                            if (src !is Player) {
                                throw CommandException(Text.of("You must be a player"))
                            }
                            val p = partyService[src].unwrap
                            if (p != null) {
                                throw CommandException(Text.of("You are already in a party. You must leave it before creating a new one."))
                            }
                            partyService.create().also {
                                it.add(src)
                                logger.info("{} created a new party ({})", src.name, it.uniqueId)
                            }
                            src.sendMessage(PARTY_PREFIX + "Created new party")
                            CommandResult.success()
                        }
                        .build(), "create")
                .child(CommandSpec.builder()
                        .executor { src, _ ->
                            if (src !is Player) {
                                throw CommandException(Text.of("You must be a player"))
                            }
                            val data = src[PartyMemberData::class.java].unwrap!!
                            val invite = data.invite().get().unwrap
                                    ?: throw CommandException(Text.of("You have not been invite to a party"))

                            val party = partyService[invite].unwrap

                            if (party == null) {
                                logger.warn("{} tried to join an invalid party {}", src.name, invite)
                                throw CommandException(Text.of("ERROR: Party not found!"))
                            }

                            party.add(src)
                            src.sendMessage(PARTY_PREFIX + "You have joined a party with (${party.users.size}): " + party.users.joinToString(", ") { it.name })

                            src.offer(data.invite().setTo(null))

                            CommandResult.success()

                        }
                        .build(), "accept")
                .child(CommandSpec.builder()
                        .executor { src, _ ->
                            if (src !is Player) {
                                throw CommandException(Text.of("You must be a player"))
                            }
                            val data = src[PartyMemberData::class.java].unwrap!!
                            val invite = data.invite().get().unwrap
                                    ?: throw CommandException(Text.of("You have not been invite to a party"))

                            partyService[invite].unwrap?.channel?.send(PARTY_PREFIX + src.name + " has denied the party invite.")

                            src.offer(data.invite().setTo(null))
                            src.sendMessage(PARTY_PREFIX + "The party invite was denied")

                            CommandResult.success()
                        }
                        .build(), "deny")
                .child(CommandSpec.builder()
                        .arguments(GenericArguments.flags()
                                .flag("c", "-cancel")
                                .buildWith(GenericArguments.user(Text.of("user"))))
                        .executor { src, args ->
                            if (src !is Player) {
                                throw CommandException(Text.of("You must be a player"))
                            }
                            val user = args.getOne<User>(Text.of("user")).unwrap!!
                            val cancelled = args.getOne<Boolean>("c").unwrap == true
                            val un = cancelled.takeIf { it } ?: "un"
                            if (src == user)
                                throw CommandException(Text.of("You cannot ${un}invite yourself"))
                            val party = partyService[src].unwrap
                                    ?: throw CommandException(Text.of("You are not in a party"))

                            if (user in party.users)
                                throw CommandException(Text.of("That user is already in your party"))

                            val partyData = user[PartyMemberData::class.java].unwrap!!

                            val invited = partyData.invite().get().unwrap
                            if (invited != null) {
                                if (cancelled && invited != party.uniqueId)
                                    throw CommandException(Text.of("That user has not been invite to the party"))

                                if (!cancelled && invited == party.uniqueId)
                                    throw CommandException(Text.of("That user has already been invite to the party"))
                            }

                            user.offer(partyData.invite().setTo(party.uniqueId.takeIf { cancelled }))
                            user.player.unwrap?.sendMessage(PARTY_PREFIX + "You have been ${un}invited to a party by ${src.name}." +
                                    // if not cancelled, display how to accept
                                    "To join, type /party accept".takeIf { cancelled }.orEmpty()
                            )
                            party.channel.send(PARTY_PREFIX + "${src.name} has ${un}invited ${user.name} to the party")
                            CommandResult.success()

                        }
                        .build(), "invite")
                .child(CommandSpec.builder()
                        .executor { src, _ ->
                            if (src !is Player) {
                                throw CommandException(Text.of("You must be a player"))
                            }
                            val p = sponge.serviceManager<PartyService>()!![src].unwrap
                                    ?: throw CommandException(Text.of("You are not in a party"))
                            p.remove(src)
                            src.sendMessage(PARTY_PREFIX + "You have left the party.")
                            CommandResult.success()


                        }
                        .build(), "leave")
                .child(CommandSpec.builder()
                        .arguments(GenericArguments.flags().flag("a", "-all").buildWith(GenericArguments.none()))
                        .executor { src, ctx ->
                            if (src !is Player) {
                                throw CommandException(Text.of("You must be a player"))
                            }
                            val p = sponge.serviceManager<PartyService>()!![src].unwrap
                                    ?: throw CommandException(Text.of("You are not in a party"))

                            val all = ctx.getOne<Boolean>("a").unwrap == true
                            val users = Text.joinWith(Text.of(", "), p.users
                                    .mapNotNull { if (all) it else it.player.unwrap }
                                    .map {
                                        Text.builder(it.name).apply {
                                            if (!it.isOnline) this.color(TextColors.GRAY)
                                        }.build()
                                    })

                            src.sendMessage(PARTY_PREFIX + (TextColors.YELLOW + "Party Members: ") + users)

                            CommandResult.success()
                        }
                        .build(), "list")
                .build(), "party")
    }

    @Listener
    fun registerKeys(event: GameRegistryEvent.Register<Key<out BaseValue<*>>>) {
        event.register(RPGKeys.ABILITY_PROFILES)
        event.register(RPGKeys.ACTIVE_ABILITY_PROFILE)
        event.register(RPGKeys.PARTY)
    }

    @Listener
    fun onJoin(event: ClientConnectionEvent.Join, @Getter("getTargetEntity") player: Player) {
        player.offer(listOf(
                player.getOrCreate(PartyMemberData::class.java).unwrap!!,
                player.getOrCreate(AbilityProfileData::class.java).unwrap!!
        ))

        // notify of pending invites
        // TODO serialize invites
        player[PartyMemberData::class.java].unwrap?.also {
            val uuid = it.invite().get().unwrap
            if (uuid != null) {
                val party = sponge.serviceManager<PartyService>()?.get(uuid).unwrap
                if (party == null) {
                    logger.warn("Player {} was invite to unknown party {}", player.name, uuid)
                    player.offer(it.invite().setTo(null))
                } else {
                    val members = Text.joinWith(Text.of(", "), party.users.map {
                        Text.builder(it.name).onHover(TextActions.showText(Text.of(it.uniqueId))).build()
                    })
                    player.sendMessage(TextColors.YELLOW + "You have a pending party invite. To join, type /party accept"
                            + Text.NEW_LINE
                            + TextColors.YELLOW + "Members (${party.users.size}"
                            + Text.NEW_LINE
                            + members
                    )
                }
            }
        }
    }
}
