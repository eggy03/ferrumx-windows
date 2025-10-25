package io.github.eggy03.ferrumx.windows.service.storage;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.CimQuery;
import io.github.eggy03.ferrumx.windows.entity.storage.DiskPartition;
import io.github.eggy03.ferrumx.windows.mapping.storage.DiskPartitionMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching information about disk partitions.
 * <p>
 * This class executes the {@link CimQuery#DISK_PARTITION_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link DiskPartition} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * DiskPartitionService partitionService = new DiskPartitionService();
 * List<DiskPartition> partitions = partitionService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     DiskPartitionService partitionService = new DiskPartitionService();
 *     List<DiskPartition> partitions = partitionService.get(session);
 * }
 * }</pre>
 * @since 2.0.0
 * @author Egg-03
 */

public class DiskPartitionService implements CommonServiceInterface<DiskPartition> {

    /**
     * Retrieves a non-null list of disk partitions present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link DiskPartition} objects representing the disk partitions.
     *         Returns an empty list if no partitions are detected.
     *
     * @since 2.2.0
     */
    @NotNull
    @Override
    public List<DiskPartition> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(CimQuery.DISK_PARTITION_QUERY.getQuery());
        return new DiskPartitionMapper().mapToList(response.getCommandOutput(), DiskPartition.class);
    }

    /**
     * Retrieves a non-null list of disk partitions using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link DiskPartition} objects representing the disk partitions.
     *         Returns an empty list if no partitions are detected.
     *
     * @since 2.2.0
     */
    @NotNull
    @Override
    public List<DiskPartition> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(CimQuery.DISK_PARTITION_QUERY.getQuery());
        return new DiskPartitionMapper().mapToList(response.getCommandOutput(), DiskPartition.class);
    }

}