/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.user;

import com.google.gson.GsonBuilder;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a user on a system. The service which retrieves this info is platform-agnostic.
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
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 * @see Win32UserAccount
 */

@Value
@Builder(toBuilder = true)
public class User {

    /**
     * Current username of the user behind this instance
     */
    @Nullable
    String userName;

    /**
     * Fetches the user's home directory. For more information on what the home directory
     * could mean, check out the definitions provided by your OS.
     */
    @Nullable
    String userHome;

    /**
     * Current working directory of the user. This usually points to the directory where this library code exists.
     */
    @Nullable
    String userDirectory;

    /**
     * Retrieves the entity in a JSON pretty-print formatted string
     * @return the {@link String} value of the object in JSON pretty-print format
     */
    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}