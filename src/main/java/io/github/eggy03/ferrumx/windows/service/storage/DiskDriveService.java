package io.github.eggy03.ferrumx.windows.service.storage;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.CimQuery;
import io.github.eggy03.ferrumx.windows.entity.storage.DiskDrive;
import io.github.eggy03.ferrumx.windows.mapping.storage.DiskDriveMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching information about disk drives.
 * <p>
 * This class executes the {@link CimQuery#DISK_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link DiskDrive} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * DiskDriveService diskService = new DiskDriveService();
 * List<DiskDrive> drives = diskService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     DiskDriveService diskService = new DiskDriveService();
 *     List<DiskDrive> drives = diskService.get(session);
 * }
 * }</pre>
 * @since 2.0.0
 * @author Egg-03
 */

public class DiskDriveService implements CommonServiceInterface<DiskDrive> {

    /**
     * Retrieves a non-null list of disk drives present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link DiskDrive} objects representing the disk drives.
     *         Returns an empty list if no disk drives are detected.
     */
    @NotNull
    @Override
    public List<DiskDrive> get() {

        PowerShellResponse response = PowerShell.executeSingleCommand(CimQuery.DISK_QUERY.getQuery());
        return new DiskDriveMapper().mapToList(response.getCommandOutput(), DiskDrive.class);
    }

    /**
     * Retrieves a non-null list of disk drives using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link DiskDrive} objects representing the disk drives.
     *         Returns an empty list if no disk drives are detected.
     */
    @NotNull
    @Override
    public List<DiskDrive> get(PowerShell powerShell) {

        PowerShellResponse response = powerShell.executeCommand(CimQuery.DISK_QUERY.getQuery());
        return new DiskDriveMapper().mapToList(response.getCommandOutput(), DiskDrive.class);
    }
}
