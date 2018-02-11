package mnm.plugins.rpg.api.party;

import com.google.common.collect.Streams;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.util.Identifiable;

import java.util.Collection;
import java.util.stream.Collectors;

public interface Party extends Identifiable {

    Collection<User> getUsers();

    DataTransactionResult add(User member);

    DataTransactionResult remove(User member);

    default MessageChannel getChannel() {
        return () -> getUsers().stream()
                .map(User::getPlayer)
                .flatMap(Streams::stream)
                .collect(Collectors.toSet());
    }

}
