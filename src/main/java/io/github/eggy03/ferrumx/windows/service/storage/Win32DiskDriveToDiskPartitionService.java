/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.storage;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32DiskDrive;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32DiskDriveToDiskPartition;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32DiskPartition;
import io.github.eggy03.ferrumx.windows.mapping.storage.Win32DiskDriveToDiskPartitionMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching the association between a {@link Win32DiskDrive}, and {@link Win32DiskPartition} from the system.
 * <p>
 * This class executes the {@link Cimv2Namespace#WIN32_DISK_DRIVE_TO_DISK_PARTITION_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Win32DiskDriveToDiskPartition} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32DiskDriveToDiskPartitionService ddtService = new Win32DiskDriveToDiskPartitionService();
 * List<Win32DiskDriveToDiskPartition> ddt = ddtService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32DiskDriveToDiskPartitionService ddtService = new Win32DiskDriveToDiskPartitionService();
 *     List<Win32DiskDriveToDiskPartition> ddt = ddtService.get(session);
 * }
 * }</pre>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
public class Win32DiskDriveToDiskPartitionService implements CommonServiceInterface<Win32DiskDriveToDiskPartition> {

    /**
     * Retrieves a list of {@link Win32DiskDriveToDiskPartition} entities present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32DiskDriveToDiskPartition} objects representing the association between
     * a {@link Win32DiskDrive} and it's {@link Win32DiskPartition}. Returns an empty list if none are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32DiskDriveToDiskPartition> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(Cimv2Namespace.WIN32_DISK_DRIVE_TO_DISK_PARTITION_QUERY.getQuery());
        return new Win32DiskDriveToDiskPartitionMapper().mapToList(response.getCommandOutput(), Win32DiskDriveToDiskPartition.class);
    }

    /**
     * Retrieves a list of {@link Win32DiskDriveToDiskPartition} entities using the caller's {@link PowerShell} session.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32DiskDriveToDiskPartition} objects representing the association between
     * a {@link Win32DiskDrive} and it's {@link Win32DiskPartition}. Returns an empty list if none are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32DiskDriveToDiskPartition> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(Cimv2Namespace.WIN32_DISK_DRIVE_TO_DISK_PARTITION_QUERY.getQuery());
        return new Win32DiskDriveToDiskPartitionMapper().mapToList(response.getCommandOutput(), Win32DiskDriveToDiskPartition.class);
    }
}
