package io.github.eggy03.ferrumx.windows.service.os;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.os.Win32OperatingSystem;
import io.github.eggy03.ferrumx.windows.mapping.os.Win32OperatingSystemMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching operating system information from the system.
 * <p>
 * This class executes the {@link Cimv2Namespace#OPERATING_SYSTEM_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Win32OperatingSystem} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32OperatingSystemService osService = new Win32OperatingSystemService();
 * List<Win32OperatingSystem> operatingSystems = osService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32OperatingSystemService osService = new Win32OperatingSystemService();
 *     List<Win32OperatingSystem> operatingSystems = osService.get(session);
 * }
 * }</pre>
 * @since 2.0.0
 * @author Egg-03
 */

public class Win32OperatingSystemService implements CommonServiceInterface<Win32OperatingSystem> {

    /**
     * Retrieves a list of operating systems present on the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32OperatingSystem} objects representing the system's operating systems.
     *         Returns an empty list if none are detected.
     *
     * @since 2.2.0
     */
    @NotNull
    @Override
    public List<Win32OperatingSystem> get() {

        PowerShellResponse response = PowerShell.executeSingleCommand(Cimv2Namespace.OPERATING_SYSTEM_QUERY.getQuery());
        return new Win32OperatingSystemMapper().mapToList(response.getCommandOutput(), Win32OperatingSystem.class);
    }

    /**
     * Retrieves a list of operating systems using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Win32OperatingSystem} objects representing the system's operating systems.
     *         Returns an empty list if none are detected.
     *
     * @since 2.2.0
     */
    @NotNull
    @Override
    public List<Win32OperatingSystem> get(PowerShell powerShell) {

        PowerShellResponse response = powerShell.executeCommand(Cimv2Namespace.OPERATING_SYSTEM_QUERY.getQuery());
        return new Win32OperatingSystemMapper().mapToList(response.getCommandOutput(), Win32OperatingSystem.class);
    }
}
