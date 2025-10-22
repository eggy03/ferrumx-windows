package io.github.eggy03.ferrumx.windows.service.os;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.CimQuery;
import io.github.eggy03.ferrumx.windows.entity.os.OperatingSystem;
import io.github.eggy03.ferrumx.windows.mapping.os.OperatingSystemMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching operating system information from the system.
 * <p>
 * This class executes the {@link CimQuery#OPERATING_SYSTEM_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link OperatingSystem} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * OperatingSystemService osService = new OperatingSystemService();
 * List<OperatingSystem> operatingSystems = osService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     OperatingSystemService osService = new OperatingSystemService();
 *     List<OperatingSystem> operatingSystems = osService.get(session);
 * }
 * }</pre>
 * @since 2.0.0
 * @author Egg-03
 */

public class OperatingSystemService implements CommonServiceInterface<OperatingSystem> {

    /**
     * Retrieves a list of operating systems present on the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link OperatingSystem} objects representing the system's operating systems.
     *         Returns an empty list if none are detected.
     */
    @NotNull
    @Override
    public List<OperatingSystem> get() {

        PowerShellResponse response = PowerShell.executeSingleCommand(CimQuery.OPERATING_SYSTEM_QUERY.getQuery());
        return new OperatingSystemMapper().mapToList(response.getCommandOutput(), OperatingSystem.class);
    }

    /**
     * Retrieves a list of operating systems using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link OperatingSystem} objects representing the system's operating systems.
     *         Returns an empty list if none are detected.
     */
    @NotNull
    @Override
    public List<OperatingSystem> get(PowerShell powerShell) {

        PowerShellResponse response = powerShell.executeCommand(CimQuery.OPERATING_SYSTEM_QUERY.getQuery());
        return new OperatingSystemMapper().mapToList(response.getCommandOutput(), OperatingSystem.class);
    }
}
