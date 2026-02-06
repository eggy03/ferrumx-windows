/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.storage;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.namespace.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32DiskPartition;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32LogicalDisk;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32LogicalDiskToPartition;
import io.github.eggy03.ferrumx.windows.mapping.storage.Win32LogicalDiskToPartitionMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import io.github.eggy03.ferrumx.windows.utility.TerminalUtility;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching the association between a {@link Win32DiskPartition}, and {@link Win32LogicalDisk} from the system.
 * <p>
 * This class executes the {@link Cimv2Namespace#WIN32_LOGICAL_DISK_TO_PARTITION_QUERY} PowerShell command
 * and maps the resulting JSON into an immutable list of {@link Win32LogicalDiskToPartition} objects.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32LogicalDiskToPartitionService service = new Win32LogicalDiskToPartitionService();
 * List<Win32LogicalDiskToPartition> ldt = ddtService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32LogicalDiskToPartitionService service = new Win32LogicalDiskToPartitionService();
 *     List<Win32LogicalDiskToPartition> ldt = service.get(session);
 * }
 *
 * // API with execution timeout (auto-created session is terminated if the timeout is exceeded)
 * Win32LogicalDiskToPartitionService service = new Win32LogicalDiskToPartitionService();
 * List<Win32LogicalDiskToPartition> ldt = ddtService.get(10);
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
public class Win32LogicalDiskToPartitionService implements CommonServiceInterface<Win32LogicalDiskToPartition> {

    /**
     * Retrieves an immutable list of {@link Win32LogicalDiskToPartition} entities present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return an immutable list of {@link Win32LogicalDiskToPartition} objects representing the association between
     * a {@link Win32DiskPartition} and a {@link Win32LogicalDisk}. Returns an empty list if none are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32LogicalDiskToPartition> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(Cimv2Namespace.WIN32_LOGICAL_DISK_TO_PARTITION_QUERY.getQuery());
        log.trace("Powershell response for auto-managed session :\n{}", response.getCommandOutput());
        return new Win32LogicalDiskToPartitionMapper().mapToList(response.getCommandOutput(), Win32LogicalDiskToPartition.class);
    }

    /**
     * Retrieves an immutable list of {@link Win32LogicalDiskToPartition} entities using the caller's {@link PowerShell} session.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return an immutable list of {@link Win32LogicalDiskToPartition} objects representing the association between
     * a {@link Win32DiskPartition} and a {@link Win32LogicalDisk}. Returns an empty list if none are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32LogicalDiskToPartition> get(@NonNull PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(Cimv2Namespace.WIN32_LOGICAL_DISK_TO_PARTITION_QUERY.getQuery());
        log.trace("PowerShell response for self-managed session :\n{}", response.getCommandOutput());
        return new Win32LogicalDiskToPartitionMapper().mapToList(response.getCommandOutput(), Win32LogicalDiskToPartition.class);
    }

    /**
     * Retrieves an immutable list of {@link Win32LogicalDiskToPartition} entities
     * using an isolated PowerShell process with a configurable timeout.
     * <p>
     * Each invocation creates an isolated PowerShell process, which is
     * pre-maturely terminated if execution exceeds the specified timeout.
     * </p>
     *
     * @param timeout the maximum time (in seconds) to wait for the PowerShell
     *                command to complete before terminating the process
     * @return an immutable list of {@link Win32LogicalDiskToPartition} objects representing the association between
     *         a {@link Win32DiskPartition} and a {@link Win32LogicalDisk}.
     *         Returns an empty list if none are detected.
     *
     * @since 3.1.0
     */
    @NotNull
    @Override
    public List<Win32LogicalDiskToPartition> get(long timeout) {
        String command = Cimv2Namespace.WIN32_LOGICAL_DISK_TO_PARTITION_QUERY.getQuery();
        String response = TerminalUtility.executeCommand(command, timeout);
        log.trace("PowerShell response for the apache terminal session: \n{}", response);
        return new Win32LogicalDiskToPartitionMapper().mapToList(response, Win32LogicalDiskToPartition.class);
    }
}
