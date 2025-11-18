/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.storage;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.namespace.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32DiskDrive;
import io.github.eggy03.ferrumx.windows.mapping.storage.Win32DiskDriveMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching information about disk drives.
 * <p>
 * This class executes the {@link Cimv2Namespace#WIN32_DISK_DRIVE_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Win32DiskDrive} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32DiskDriveService diskService = new Win32DiskDriveService();
 * List<Win32DiskDrive> drives = diskService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32DiskDriveService diskService = new Win32DiskDriveService();
 *     List<Win32DiskDrive> drives = diskService.get(session);
 * }
 * }</pre>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@Slf4j
public class Win32DiskDriveService implements CommonServiceInterface<Win32DiskDrive> {

    /**
     * Retrieves a non-null list of disk drives present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32DiskDrive} objects representing the disk drives.
     *         Returns an empty list if no disk drives are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32DiskDrive> get() {

        PowerShellResponse response = PowerShell.executeSingleCommand(Cimv2Namespace.WIN32_DISK_DRIVE_QUERY.getQuery());
        log.trace("Powershell response for auto-managed session :\n{}", response.getCommandOutput());
        return new Win32DiskDriveMapper().mapToList(response.getCommandOutput(), Win32DiskDrive.class);
    }

    /**
     * Retrieves a non-null list of disk drives using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Win32DiskDrive} objects representing the disk drives.
     *         Returns an empty list if no disk drives are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32DiskDrive> get(PowerShell powerShell) {

        PowerShellResponse response = powerShell.executeCommand(Cimv2Namespace.WIN32_DISK_DRIVE_QUERY.getQuery());
        log.trace("Powershell response for self-managed session :\n{}", response.getCommandOutput());
        return new Win32DiskDriveMapper().mapToList(response.getCommandOutput(), Win32DiskDrive.class);
    }
}
