package io.github.eggy03.ferrumx.windows.service.storage;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.CimQuery;
import io.github.eggy03.ferrumx.windows.entity.storage.DiskPartition;
import io.github.eggy03.ferrumx.windows.entity.storage.LogicalDisk;
import io.github.eggy03.ferrumx.windows.entity.storage.LogicalDiskToPartition;
import io.github.eggy03.ferrumx.windows.mapping.storage.LogicalDiskToPartitionMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching the association between a {@link DiskPartition}, and {@link LogicalDisk} from the system.
 * <p>
 * This class executes the {@link CimQuery#LOGICAL_DISK_TO_PARTITION_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link LogicalDiskToPartition} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of this class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * LogicalDiskToPartitionService ldtService = new LogicalDiskToPartitionService();
 * List<LogicalDiskToPartition> ldt = ddtService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     LogicalDiskToPartitionService ldtService = new LogicalDiskToPartitionService();
 *     List<LogicalDiskToPartition> ldt = ldtService.get(session);
 * }
 * }</pre>
 * @since 2.3.0
 * @author Egg-03
 */
public class LogicalDiskToPartitionService implements CommonServiceInterface<LogicalDiskToPartition> {

    /**
     * Retrieves a list of {@link LogicalDiskToPartition} entities present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link LogicalDiskToPartition} objects representing the association between
     * a {@link DiskPartition} and a {@link LogicalDisk}. Returns an empty list if none are detected.
     *
     * @since 2.3.0
     */
    @NotNull
    @Override
    public List<LogicalDiskToPartition> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(CimQuery.LOGICAL_DISK_TO_PARTITION_QUERY.getQuery());
        return new LogicalDiskToPartitionMapper().mapToList(response.getCommandOutput(), LogicalDiskToPartition.class);
    }

    /**
     * Retrieves a list of {@link LogicalDiskToPartition} entities using the caller's {@link PowerShell} session.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link LogicalDiskToPartition} objects representing the association between
     * a {@link DiskPartition} and a {@link LogicalDisk}. Returns an empty list if none are detected.
     *
     * @since 2.3.0
     */
    @NotNull
    @Override
    public List<LogicalDiskToPartition> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(CimQuery.LOGICAL_DISK_TO_PARTITION_QUERY.getQuery());
        return new LogicalDiskToPartitionMapper().mapToList(response.getCommandOutput(), LogicalDiskToPartition.class);
    }
}
