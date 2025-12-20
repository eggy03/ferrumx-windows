/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.network;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.namespace.StandardCimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetAdapter;
import io.github.eggy03.ferrumx.windows.mapping.network.MsftNetAdapterMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import io.github.eggy03.ferrumx.windows.utility.TerminalUtility;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching network adapter information from the system.
 * <p>
 * This class executes the {@link StandardCimv2Namespace#MSFT_NET_ADAPTER_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link MsftNetAdapter} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * MsftNetAdapterService service = new MsftNetAdapterService();
 * List<MsftNetAdapter> adapters = service.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     MsftNetAdapterService service = new MsftNetAdapterService();
 *     List<MsftNetAdapter> adapters = service.get(session);
 * }
 *
 * // API with execution timeout (auto-created session is terminated if the timeout is exceeded)
 * MsftNetAdapterService service = new MsftNetAdapterService();
 * List<MsftNetAdapter> adapters = service.get(10);
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
public class MsftNetAdapterService implements CommonServiceInterface<MsftNetAdapter> {

    /**
     * Retrieves a list of network adapters present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link MsftNetAdapter} objects representing the system's network adapters.
     *         Returns an empty list if no adapters are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<MsftNetAdapter> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(StandardCimv2Namespace.MSFT_NET_ADAPTER_QUERY.getQuery());
        log.trace("PowerShell response for auto-managed session :\n{}", response.getCommandOutput());
        return new MsftNetAdapterMapper().mapToList(response.getCommandOutput(), MsftNetAdapter.class);
    }

    /**
     * Retrieves a list of network adapters using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link MsftNetAdapter} objects representing the system's network adapters.
     *         Returns an empty list if no adapters are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<MsftNetAdapter> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(StandardCimv2Namespace.MSFT_NET_ADAPTER_QUERY.getQuery());
        log.trace("PowerShell response for self-managed session :\n{}", response.getCommandOutput());
        return new MsftNetAdapterMapper().mapToList(response.getCommandOutput(), MsftNetAdapter.class);
    }

    /**
     * Retrieves a list of network adapters connected to the system
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return a list of {@link MsftNetAdapter} objects representing the system's network adapters.
     *         Returns an empty list if no adapters are detected.
     *
     * @since 3.1.0
     */
    @NotNull
    @Override
    public List<MsftNetAdapter> get(long timeout) {
        String command = StandardCimv2Namespace.MSFT_NET_ADAPTER_QUERY.getQuery();
        String response = TerminalUtility.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new MsftNetAdapterMapper().mapToList(response, MsftNetAdapter.class);
    }
}
