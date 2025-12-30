/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.system;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.namespace.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.system.Win32ComputerSystem;
import io.github.eggy03.ferrumx.windows.mapping.system.Win32ComputerSystemMapper;
import io.github.eggy03.ferrumx.windows.service.OptionalCommonServiceInterface;
import io.github.eggy03.ferrumx.windows.utility.TerminalUtility;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Service class for fetching the computer system information running Windows.
 * <p>
 * This class executes the {@link Cimv2Namespace#WIN32_COMPUTER_SYSTEM_QUERY} PowerShell command
 * and maps the resulting JSON into an {@link Optional} {@link Win32ComputerSystem} object.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32ComputerSystemService service = new Win32ComputerSystemService();
 * Optional<Win32ComputerSystem> system = service.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32ComputerSystemService service = new Win32ComputerSystemService();
 *     Optional<Win32ComputerSystem> system = service.get(session);
 * }
 *
 * // API with execution timeout (auto-created session is terminated if the timeout is exceeded)
 * Win32ComputerSystemService service = new Win32ComputerSystemService();
 * Optional<Win32ComputerSystem> system = service.get(10);
 * }</pre>
 *
 * <h2>Execution models and concurrency</h2>
 * <p>
 * This service supports multiple PowerShell execution strategies:
 * </p>
 *
 * <ul>
 *   <li>
 *     <b>jPowerShell-based execution</b> via {@link #get()} and
 *     {@link #get(PowerShell)}:
 *     <br>
 *     These methods rely on {@code jPowerShell} sessions. Due to internal
 *     global configuration of {@code jPowerShell}, the PowerShell sessions
 *     launched by it is <b>not safe to use concurrently across multiple
 *     threads or executors</b>. Running these methods in parallel may result
 *     in runtime exceptions.
 *   </li>
 *
 *   <li>
 *     <b>Isolated PowerShell execution</b> via {@link #get(long timeout)}:
 *     <br>
 *     This method doesn't rely on {@code jPowerShell} and instead, launches a
 *     standalone PowerShell process per invocation using
 *     {@link TerminalUtility}. Each call is fully isolated and
 *     <b>safe to use in multithreaded and executor-based environments</b>.
 *   </li>
 * </ul>
 *
 * <p>
 * For concurrent or executor-based workloads, prefer {@link #get(long timeout)}.
 * </p>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@Slf4j
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
        log.trace("PowerShell response for auto-managed session :\n{}", response.getCommandOutput());
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
        log.trace("PowerShell response for self-managed session :\n{}", response.getCommandOutput());
        return new Win32ComputerSystemMapper().mapToObject(response.getCommandOutput(), Win32ComputerSystem.class);
    }

    /**
     * Retrieves an {@link Optional} containing the Computer System information
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an {@link Optional} of {@link Win32ComputerSystem} representing
     *         the HWID. Returns {@link Optional#empty()} if no information
     *         is detected.
     *
     * @since 3.1.0
     */
    @NotNull
    @Override
    public Optional<Win32ComputerSystem> get(long timeout) {
        String command = Cimv2Namespace.WIN32_COMPUTER_SYSTEM_QUERY.getQuery();
        String response = TerminalUtility.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32ComputerSystemMapper().mapToObject(response, Win32ComputerSystem.class);
    }
}
