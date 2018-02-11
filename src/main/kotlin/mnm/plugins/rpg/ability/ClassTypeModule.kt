package mnm.plugins.rpg.ability

import mnm.plugins.rpg.api.ability.ClassType
import org.spongepowered.api.registry.CatalogRegistryModule
import java.util.*

class ClassTypeModule : CatalogRegistryModule<ClassType> {

    override fun getById(id: String?): Optional<ClassType> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAll(): MutableCollection<ClassType> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun registerDefaults() {
        super.registerDefaults()
    }

}
