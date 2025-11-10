/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.system;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.system.Win32ComputerSystem;
import io.github.eggy03.ferrumx.windows.mapping.system.Win32ComputerSystemMapper;
import io.github.eggy03.ferrumx.windows.service.OptionalCommonServiceInterface;

import java.util.Optional;

/**
 * Service class for fetching the computer system information running Windows.
 * <p>
 * This class executes the {@link Cimv2Namespace#WIN32_COMPUTER_SYSTEM_QUERY} PowerShell command
 * and maps the resulting JSON into an {@link Optional} {@link Win32ComputerSystem} object.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32ComputerSystemService systemService = new Win32ComputerSystemService();
 * Optional<Win32ComputerSystem> system = systemService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32ComputerSystemService systemService = new Win32ComputerSystemService();
 *     Optional<Win32ComputerSystem> system = systemService.get(session);
 * }
 * }</pre>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */

public class Win32ComputerSystemService implements OptionalCommonServiceInterface<Win32ComputerSystem> {

    /**
     * Retrieves an {@link Optional} containing the computer system information.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return an {@link Optional} of {@link Win32ComputerSystem} representing
     *         the computer system. Returns {@link Optional#empty()} if no system information is detected.
     *
     * @since 3.0.0
     */
    @Override
    public Optional<Win32ComputerSystem> get() {

        PowerShellResponse response = PowerShell.executeSingleCommand(Cimv2Namespace.WIN32_COMPUTER_SYSTEM_QUERY.getQuery());
        return new Win32ComputerSystemMapper().mapToObject(response.getCommandOutput(), Win32ComputerSystem.class);
    }

    /**
     * Retrieves an {@link Optional} containing the computer system information
     * using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return an {@link Optional} of {@link Win32ComputerSystem} representing
     *         the computer system. Returns {@link Optional#empty()} if no  information is detected.
     *
     * @since 3.0.0
     */
    @Override
    public Optional<Win32ComputerSystem> get(PowerShell powerShell) {

        PowerShellResponse response = powerShell.executeCommand(Cimv2Namespace.WIN32_COMPUTER_SYSTEM_QUERY.getQuery());
        return new Win32ComputerSystemMapper().mapToObject(response.getCommandOutput(), Win32ComputerSystem.class);
    }
}
