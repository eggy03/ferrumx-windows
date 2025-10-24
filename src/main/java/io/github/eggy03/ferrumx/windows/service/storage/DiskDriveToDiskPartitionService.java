package io.github.eggy03.ferrumx.windows.service.storage;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.CimQuery;
import io.github.eggy03.ferrumx.windows.entity.storage.DiskDrive;
import io.github.eggy03.ferrumx.windows.entity.storage.DiskDriveToDiskPartition;
import io.github.eggy03.ferrumx.windows.entity.storage.DiskPartition;
import io.github.eggy03.ferrumx.windows.mapping.storage.DiskDriveToDiskPartitionMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching the association between a {@link DiskDrive}, and {@link DiskPartition} from the system.
 * <p>
 * This class executes the {@link CimQuery#DISK_DRIVE_TO_DISK_PARTITION_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link DiskDriveToDiskPartition} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * DiskDriveToDiskPartitionService ddtService = new DiskDriveToDiskPartitionService();
 * List<DiskDriveToDiskPartition> ddt = ddtService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     DiskDriveToDiskPartitionService ddtService = new DiskDriveToDiskPartitionService();
 *     List<DiskDriveToDiskPartitionService> ddt = ddtService.get(session);
 * }
 * }</pre>
 * @since 2.3.0
 * @author Egg-03
 */
public class DiskDriveToDiskPartitionService implements CommonServiceInterface<DiskDriveToDiskPartition> {

    /**
     * Retrieves a list of {@link DiskDriveToDiskPartition} entities present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link DiskDriveToDiskPartition} objects representing the association between
     * a {@link DiskDrive} and it's {@link DiskPartition}. Returns an empty list if none are detected.
     */
    @NotNull
    @Override
    public List<DiskDriveToDiskPartition> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(CimQuery.DISK_DRIVE_TO_DISK_PARTITION_QUERY.getQuery());
        return new DiskDriveToDiskPartitionMapper().mapToList(response.getCommandOutput(), DiskDriveToDiskPartition.class);
    }

    /**
     * Retrieves a list of {@link DiskDriveToDiskPartition} entities using the caller's {@link PowerShell} session.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link DiskDriveToDiskPartition} objects representing the association between
     * a {@link DiskDrive} and it's {@link DiskPartition}. Returns an empty list if none are detected.
     */
    @NotNull
    @Override
    public List<DiskDriveToDiskPartition> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(CimQuery.DISK_DRIVE_TO_DISK_PARTITION_QUERY.getQuery());
        return new DiskDriveToDiskPartitionMapper().mapToList(response.getCommandOutput(), DiskDriveToDiskPartition.class);
    }
}
