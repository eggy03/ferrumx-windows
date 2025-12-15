/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.system;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.namespace.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.system.Win32Environment;
import io.github.eggy03.ferrumx.windows.mapping.system.Win32EnvironmentMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import io.github.eggy03.ferrumx.windows.utility.TerminalUtility;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching information about environment variables in a Windows System.
 * <p>
 * This class executes the {@link Cimv2Namespace#WIN32_ENVIRONMENT_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Win32Environment} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32EnvironmentService envService = new Win32EnvironmentService();
 * List<Win32Environment> env = envService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32EnvironmentService envService = new Win32EnvironmentService();
 *     List<Win32Environment> env = envService.get(session);
 * }
 * }</pre>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@Slf4j
public class Win32EnvironmentService implements CommonServiceInterface<Win32Environment> {

    /**
     * Retrieves a non-null list of env variables present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32Environment} objects representing the env variables.
     *         Returns an empty list if no env variables are detected.
     *
     * @since 3.0.0
     */
    @Override
    @NotNull
    public List<Win32Environment> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(Cimv2Namespace.WIN32_ENVIRONMENT_QUERY.getQuery());
        log.trace("PowerShell response for auto-managed session :\n{}", response.getCommandOutput());
        return new Win32EnvironmentMapper().mapToList(response.getCommandOutput(), Win32Environment.class);
    }

    /**
     * Retrieves a non-null list of env variables using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Win32Environment} objects representing the env variables.
     *         Returns an empty list if no env variables are detected.
     *
     * @since 3.0.0
     */
    @Override
    @NotNull
    public List<Win32Environment> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(Cimv2Namespace.WIN32_ENVIRONMENT_QUERY.getQuery());
        log.trace("PowerShell response for self-managed session :\n{}", response.getCommandOutput());
        return new Win32EnvironmentMapper().mapToList(response.getCommandOutput(), Win32Environment.class);
    }

    @Override
    public List<Win32Environment> get(long timeout) {
        String command = Cimv2Namespace.WIN32_ENVIRONMENT_QUERY.getQuery();
        String response = TerminalUtility.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32EnvironmentMapper().mapToList(response, Win32Environment.class);
    }
}
