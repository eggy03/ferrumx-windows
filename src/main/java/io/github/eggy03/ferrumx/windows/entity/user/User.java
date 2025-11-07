package io.github.eggy03.ferrumx.windows.entity.user;

import com.google.gson.GsonBuilder;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a user on a Windows system.
 * <p>
 * Fields capture basic user information such as username, home directory, and user directory.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new User
 * User user = User.builder()
 *     .userName("john_doe")
 *     .userHome("C:\\Users\\john_doe")
 *     .userDirectory("C:\\Users\\john_doe\\Documents")
 *     .build();
 *
 * // Create a modified copy using the builder
 * User updatedUser = user.toBuilder()
 *     .userName("jane_doe")
 *     .build();
 *
 * }</pre>
 * @since 2.0.0
 * @author Egg-03
 */

@Value
@Builder(toBuilder = true)
public class User {

    @Nullable
    String userName;

    @Nullable
    String userHome;

    @Nullable
    String userDirectory;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}