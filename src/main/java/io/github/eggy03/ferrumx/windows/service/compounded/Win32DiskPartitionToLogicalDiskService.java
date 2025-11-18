/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.compounded;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.PowerShellScript;
import io.github.eggy03.ferrumx.windows.entity.compounded.Win32DiskDriveToPartitionAndLogicalDisk;
import io.github.eggy03.ferrumx.windows.entity.compounded.Win32DiskPartitionToLogicalDisk;
import io.github.eggy03.ferrumx.windows.mapping.compounded.Win32DiskPartitionToLogicalDiskMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import io.github.eggy03.ferrumx.windows.service.storage.Win32DiskDriveService;
import io.github.eggy03.ferrumx.windows.service.storage.Win32DiskDriveToDiskPartitionService;
import io.github.eggy03.ferrumx.windows.service.storage.Win32DiskPartitionService;
import io.github.eggy03.ferrumx.windows.service.storage.Win32LogicalDiskService;
import io.github.eggy03.ferrumx.windows.service.storage.Win32LogicalDiskToPartitionService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching physical disk and related logical disk information from the system.
 * <p>
 * This class executes the {@link PowerShellScript#WIN32_DISK_PARTITION_TO_LOGICAL_DISK_SCRIPT} script
 * and maps the resulting JSON into a list of {@link Win32DiskPartitionToLogicalDisk} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32DiskPartitionToLogicalDiskService diskService = new Win32DiskPartitionToLogicalDiskService();
 * List<Win32DiskPartitionToLogicalDisk> disks = diskService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     List<Win32DiskPartitionToLogicalDisk> disks = diskService.get(session);
 * }
 * }</pre>
 * @see Win32DiskDriveToPartitionAndLogicalDisk
 * @see Win32DiskDriveService
 * @see Win32DiskPartitionService
 * @see Win32LogicalDiskService
 * @see Win32DiskDriveToDiskPartitionService
 * @see Win32LogicalDiskToPartitionService
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@Slf4j
public class Win32DiskPartitionToLogicalDiskService implements CommonServiceInterface<Win32DiskPartitionToLogicalDisk> {

    /**
     * Retrieves a list of physical disk and related logical disks connected to the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32DiskPartitionToLogicalDisk} objects representing connected physical disk and related logical disks.
     * Returns an empty list if no data is found.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32DiskPartitionToLogicalDisk> get() {
        try(PowerShell shell = PowerShell.openSession()){
            PowerShellResponse response = shell.executeScript(PowerShellScript.WIN32_DISK_PARTITION_TO_LOGICAL_DISK_SCRIPT.getScript());
            log.trace("Powershell response for auto-managed session :\n{}", response.getCommandOutput());
            return new Win32DiskPartitionToLogicalDiskMapper().mapToList(response.getCommandOutput(), Win32DiskPartitionToLogicalDisk.class);
        }
    }

    /**
     * Retrieves a list of physical disk and related logical disks connected to the system using the caller's
     * {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Win32DiskPartitionToLogicalDisk} objects representing connected physical disk and related logical disks.
     * Returns an empty list if no data is found.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32DiskPartitionToLogicalDisk> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeScript(PowerShellScript.WIN32_DISK_PARTITION_TO_LOGICAL_DISK_SCRIPT.getScript());
        log.trace("Powershell response for self-managed session :\n{}", response.getCommandOutput());
        return new Win32DiskPartitionToLogicalDiskMapper().mapToList(response.getCommandOutput(), Win32DiskPartitionToLogicalDisk.class);
    }
}
