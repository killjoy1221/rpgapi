package mnm.plugins.rpg

import com.google.common.reflect.TypeToken
import org.spongepowered.api.CatalogType
import org.spongepowered.api.Game
import org.spongepowered.api.GameRegistry
import org.spongepowered.api.Sponge
import org.spongepowered.api.data.DataQuery
import org.spongepowered.api.data.DataSerializable
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.value.ValueContainer
import org.spongepowered.api.data.value.mutable.CompositeValueStore
import org.spongepowered.api.service.ServiceManager
import org.spongepowered.api.service.user.UserStorageService
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.TextElement
import java.util.*

val sponge: Game get() = Sponge.getGame()

val <T> T?.optional: Optional<T> get() = Optional.ofNullable(this)
val <T> Optional<T>?.unwrap: T? get() = this?.orElse(null)

val UUID.user get() = sponge.serviceManager<UserStorageService>()?.get(this).unwrap

// replace all Class<T> parameters with <reified T>

inline operator fun <reified T> ServiceManager.invoke() = this.provide(T::class.java).unwrap

//inline fun <reified T> ServiceManager.setProvider(plugin: Any, service: T) = this.setProvider(plugin, T::class.java, service)

inline fun <reified T : CatalogType> GameRegistry.getType(type: String) = this.getType(T::class.java, type).unwrap

inline fun <reified T> typeToken() = object : TypeToken<T>() {}

inline operator fun <reified T : ValueContainer<*>> CompositeValueStore<*, in T>.invoke() = this.get(T::class.java).unwrap

inline fun <reified T : DataSerializable> DataView.getSerializableList(query: DataQuery) = this.getSerializableList(query, T::class.java).unwrap?.toList()
inline fun <reified T : CatalogType> DataView.getCatalogType(query: DataQuery) = this.getCatalogType(query, T::class.java).unwrap
inline fun <reified T : DataSerializable> DataView.getSerializable(query: DataQuery) = this.getSerializable(query, T::class.java).unwrap
inline fun <reified T> DataView.getObject(query: DataQuery) = this.getObject(query, T::class.java).unwrap

operator fun TextElement.plus(args: Any): Text = Text.of(this, args)
