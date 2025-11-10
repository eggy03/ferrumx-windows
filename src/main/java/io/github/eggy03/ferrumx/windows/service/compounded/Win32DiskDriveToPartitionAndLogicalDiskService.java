/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.compounded;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.PowerShellScript;
import io.github.eggy03.ferrumx.windows.entity.compounded.Win32DiskDriveToPartitionAndLogicalDisk;
import io.github.eggy03.ferrumx.windows.mapping.compounded.Win32DiskDriveToPartitionAndLogicalDiskMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import io.github.eggy03.ferrumx.windows.service.storage.Win32DiskDriveService;
import io.github.eggy03.ferrumx.windows.service.storage.Win32DiskDriveToDiskPartitionService;
import io.github.eggy03.ferrumx.windows.service.storage.Win32DiskPartitionService;
import io.github.eggy03.ferrumx.windows.service.storage.Win32LogicalDiskService;
import io.github.eggy03.ferrumx.windows.service.storage.Win32LogicalDiskToPartitionService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching physical disk and related partition and logical disk data from the system.
 * <p>
 * This class executes the {@link PowerShellScript#WIN32_DISK_DRIVE_TO_PARTITION_AND_LOGICAL_DISK_SCRIPT} PowerShell command
 * and maps the resulting JSON into a list of {@link Win32DiskDriveToPartitionAndLogicalDisk} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32DiskDriveToPartitionAndLogicalDiskService diskService = new Win32DiskDriveToPartitionAndLogicalDiskService();
 * List<Win32DiskDriveToPartitionAndLogicalDisk> disks = diskService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     List<Win32DiskDriveToPartitionAndLogicalDisk> disks = diskService.get(session);
 * }
 * }</pre>
 * @see Win32DiskPartitionToLogicalDiskService
 * @see Win32DiskDriveService
 * @see Win32DiskPartitionService
 * @see Win32LogicalDiskService
 * @see Win32DiskDriveToDiskPartitionService
 * @see Win32LogicalDiskToPartitionService
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
public class Win32DiskDriveToPartitionAndLogicalDiskService implements CommonServiceInterface<Win32DiskDriveToPartitionAndLogicalDisk> {

    /**
     * Retrieves a list of physical disks with related partition and logical disk data connected to the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32DiskDriveToPartitionAndLogicalDisk} objects representing connected physical disks
     * with their partitions and logical disks. Returns an empty list if no data is found.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32DiskDriveToPartitionAndLogicalDisk> get() {
        try(PowerShell shell = PowerShell.openSession()){
            PowerShellResponse response = shell.executeScript(PowerShellScript.WIN32_DISK_DRIVE_TO_PARTITION_AND_LOGICAL_DISK_SCRIPT.getPath());
            return new Win32DiskDriveToPartitionAndLogicalDiskMapper().mapToList(response.getCommandOutput(), Win32DiskDriveToPartitionAndLogicalDisk.class);
        }
    }

    /**
     * Retrieves a list of physical disks with related partition and logical disk data connected to the system
     * using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Win32DiskDriveToPartitionAndLogicalDisk} objects representing connected physical disks
     * with their partitions and logical disks. Returns an empty list if no data is found.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32DiskDriveToPartitionAndLogicalDisk> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeScript(PowerShellScript.WIN32_DISK_DRIVE_TO_PARTITION_AND_LOGICAL_DISK_SCRIPT.getPath());
        return new Win32DiskDriveToPartitionAndLogicalDiskMapper().mapToList(response.getCommandOutput(), Win32DiskDriveToPartitionAndLogicalDisk.class);
    }
}
