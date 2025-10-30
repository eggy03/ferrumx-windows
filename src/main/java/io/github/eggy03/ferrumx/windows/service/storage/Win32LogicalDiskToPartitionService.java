package io.github.eggy03.ferrumx.windows.service.storage;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32DiskPartition;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32LogicalDisk;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32LogicalDiskToPartition;
import io.github.eggy03.ferrumx.windows.mapping.storage.Win32LogicalDiskToPartitionMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching the association between a {@link Win32DiskPartition}, and {@link Win32LogicalDisk} from the system.
 * <p>
 * This class executes the {@link Cimv2Namespace#LOGICAL_DISK_TO_PARTITION_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Win32LogicalDiskToPartition} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of this class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32LogicalDiskToPartitionService ldtService = new Win32LogicalDiskToPartitionService();
 * List<Win32LogicalDiskToPartition> ldt = ddtService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32LogicalDiskToPartitionService ldtService = new Win32LogicalDiskToPartitionService();
 *     List<Win32LogicalDiskToPartition> ldt = ldtService.get(session);
 * }
 * }</pre>
 * @since 3.0.0
 * @author Egg-03
 */
public class Win32LogicalDiskToPartitionService implements CommonServiceInterface<Win32LogicalDiskToPartition> {

    /**
     * Retrieves a list of {@link Win32LogicalDiskToPartition} entities present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32LogicalDiskToPartition} objects representing the association between
     * a {@link Win32DiskPartition} and a {@link Win32LogicalDisk}. Returns an empty list if none are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32LogicalDiskToPartition> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(Cimv2Namespace.LOGICAL_DISK_TO_PARTITION_QUERY.getQuery());
        return new Win32LogicalDiskToPartitionMapper().mapToList(response.getCommandOutput(), Win32LogicalDiskToPartition.class);
    }

    /**
     * Retrieves a list of {@link Win32LogicalDiskToPartition} entities using the caller's {@link PowerShell} session.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32LogicalDiskToPartition} objects representing the association between
     * a {@link Win32DiskPartition} and a {@link Win32LogicalDisk}. Returns an empty list if none are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32LogicalDiskToPartition> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(Cimv2Namespace.LOGICAL_DISK_TO_PARTITION_QUERY.getQuery());
        return new Win32LogicalDiskToPartitionMapper().mapToList(response.getCommandOutput(), Win32LogicalDiskToPartition.class);
    }
}
