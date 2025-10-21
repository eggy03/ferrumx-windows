package io.github.eggy03.ferrumx.windows.service.storage;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.CimQuery;
import io.github.eggy03.ferrumx.windows.entity.storage.LogicalDisk;
import io.github.eggy03.ferrumx.windows.mapping.MapperUtil;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching information about <b>logical</b> disks.
 * <p>
 * This class executes the {@link CimQuery#LOGICAL_DISK_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link LogicalDisk} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * LogicalDiskService logicalDiskService = new LogicalDiskService();
 * List<LogicalDisk> logicalDisks = logicalDiskService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     LogicalDiskService logicalDiskService = new LogicalDiskService();
 *     List<LogicalDisk> logicalDisks = logicalDiskService.get(session);
 * }
 * }</pre>
 * @since 2.3.0
 * @author Egg-03
 */
public class LogicalDiskService implements CommonServiceInterface<LogicalDisk> {

    /**
     * Retrieves a non-null list of logical disks present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link LogicalDisk} objects representing the logical volumes.
     *         Returns an empty list if no volumes are detected.
     */
    @NotNull
    @Override
    public List<LogicalDisk> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(CimQuery.LOGICAL_DISK_QUERY.getQuery());
        return MapperUtil.mapToList(response.getCommandOutput(), LogicalDisk.class);
    }

    /**
     * Retrieves a non-null list of logical disk volumes using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link LogicalDisk} objects representing the logical volumes.
     *         Returns an empty list if no volumes are detected.
     */
    @NotNull
    @Override
    public List<LogicalDisk> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(CimQuery.LOGICAL_DISK_QUERY.getQuery());
        return MapperUtil.mapToList(response.getCommandOutput(), LogicalDisk.class);
    }
}
