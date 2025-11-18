/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.user;

import io.github.eggy03.ferrumx.windows.entity.user.User;
import org.jetbrains.annotations.NotNull;

/**
 * Service class for fetching the current system user information.
 * <p>
 * This class retrieves the current user's details such as username,
 * home directory, and working directory using standard Java system properties.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * UserService userService = new UserService();
 * User currentUser = userService.getUser();
 * System.out.println(currentUser);
 * }</pre>
 * @since 2.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */

public class UserService {

    /**
     * Retrieves the current system user information.
     *
     * @return a non-null {@link User} object containing the username,
     *         user home directory, and current working directory.
     */
    @NotNull
    public User getUser() {

        return User.builder()
                .userName(System.getProperty("user.name"))
                .userHome(System.getProperty("user.home"))
                .userDirectory(System.getProperty("user.dir"))
                .build();
    }
}
