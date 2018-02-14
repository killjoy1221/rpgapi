package mnm.plugins.rpg.ability

import mnm.plugins.rpg.api.ability.Ability
import mnm.plugins.rpg.api.ability.AbilityType
import mnm.plugins.rpg.api.ability.ClassProfile
import mnm.plugins.rpg.api.ability.ClassType
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataQuery
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.Queries
import org.spongepowered.api.data.persistence.AbstractDataBuilder
import mnm.plugins.rpg.*
import java.util.*

data class RPGClassProfile(
        private val classType: ClassType,
        private val name: String,
        private val uniqueId: UUID
) : ClassProfile {

    private val abilities = mutableMapOf<AbilityType, Ability>()

    private var level: Int = 1
    private var points: Int = 0
    private var stamina: Int = 0
    private var mana: Int = 0

    override fun getName() = name
    override fun getUniqueId() = uniqueId
    override fun getClassType() = classType
    override fun getAbilities() = abilities.values.toSet()
    override fun getLevel() = level
    override fun getPoints() = points
    override fun getStamina() = stamina
    override fun getMana() = mana

    override fun remove(ability: AbilityType) {
        abilities -= ability
    }

    override fun add(ability: Ability) {
        abilities += ability.type to ability
    }

    override fun setLevel(level: Int) {
        this.level = level
    }

    override fun setPoints(points: Int) {
        this.points = points
    }

    override fun setStamina(stamina: Int) {
        this.stamina = stamina
    }

    override fun setMana(mana: Int) {
        this.mana = mana
    }

    override fun getContentVersion() = 1

    override fun toContainer(): DataContainer = DataContainer.createNew()
            .set(Queries.CONTENT_VERSION, contentVersion)
            .set(UNIQUE_ID, uniqueId)
            .set(CLASS, classType)
            .set(ABILITIES, abilities.values)
            .set(LEVEL, level)
            .set(POINTS, points)
            .set(NAME, name)

    companion object : AbstractDataBuilder<ClassProfile>(ClassProfile::class.java, 1) {
        val PROFILE: DataQuery = DataQuery.of("GameProfile")
        val UNIQUE_ID: DataQuery = DataQuery.of("UniqueID")
        val CLASS: DataQuery = DataQuery.of("Class")
        val ABILITIES: DataQuery = DataQuery.of("Abilities")
        val LEVEL: DataQuery = DataQuery.of("Level")
        val POINTS: DataQuery = DataQuery.of("Points")
        val NAME: DataQuery = DataQuery.of("Name")

        override fun buildContent(container: DataView): Optional<ClassProfile> {
            val uuid = container.getObject<UUID>(UNIQUE_ID)
            val cl = container.getCatalogType<ClassType>(CLASS)
            val abilities = container.getSerializableList<Ability>(ABILITIES) ?: listOf()
            val level = container.getInt(LEVEL).unwrap ?: 1
            val points = container.getInt(POINTS).unwrap ?: 0
            val name = container.getString(NAME).unwrap

            if (uuid != null && cl != null && name != null) {
                // need uuid and class

                return RPGClassProfile(cl, name, uuid).apply {
                    this.abilities += abilities.map { it.type to it }
                    this.points = points
                    this.level = level
                }.optional
            }
            return null.optional
        }
    }
}

class ClassProfileBuilder : ClassProfile.Builder {

    private var uniqueId: UUID? = null
    private var name: String? = null
    private var type: ClassType? = null
    private val abilities = mutableMapOf<AbilityType, Ability>()

    private var level = 1
    private var points = 0

    override fun uniqueId(uniqueId: UUID) = this.apply {
        this.uniqueId = uniqueId
    }

    override fun name(name: String) = this.apply {
        this.name = name
    }

    override fun type(type: ClassType) = this.apply {
        this.type = type
    }

    override fun abilities(vararg abilities: Ability) = this.apply {
        // should only exist one ability per type.
        require(abilities.distinctBy { it.type }.size == abilities.size) { "abilities cannot contain duplicate ability types" }
        this.abilities += abilities.map { it.type to it }
    }

    override fun level(level: Int) = this.apply {
        // level starts at 1, cannot be any lower
        require(level <= 0) { "level cannot be less than 1" }
        this.level = level
    }

    override fun points(points: Int) = this.apply {
        // negative points are impossible
        require(points < 0) { "points cannot be negative" }
        this.points = points
    }

    override fun reset() = this.apply {
        uniqueId = null
        this.name = null
        this.type = null
        this.abilities.clear()
        level = 1
        points = 0
    }

    override fun from(value: ClassProfile) = this.apply {
        this.name = value.name
        this.type = value.classType
        this.abilities.clear()
        this.abilities += value.abilities.map { it.type to it }
    }

    override fun build() =
            RPGClassProfile(
                    classType = checkNotNull(type) { "type was null" },
                    name = checkNotNull(name) { "name was null" },
                    uniqueId = uniqueId ?: UUID.randomUUID()) // uuid is optional
                    .also { cl ->
                        this.abilities.values.forEach { cl.add(it) }
                        cl.level = level
                        cl.points = points
                    }

}
