package io.github.eggy03.ferrumx.windows.service.mainboard;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.mainboard.Win32Baseboard;
import io.github.eggy03.ferrumx.windows.mapping.mainboard.Win32BaseboardMapper;
import io.github.eggy03.ferrumx.windows.service.OptionalCommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Service class for fetching mainboard information from the system.
 * <p>
 * This class executes the {@link Cimv2Namespace#WIN32_BASEBOARD_QUERY} PowerShell command
 * and maps the resulting JSON into a {@link Win32Baseboard} object.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32BaseboardService mainboardService = new Win32BaseboardService();
 * Optional<Win32Baseboard> mainboard = mainboardService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32BaseboardService mainboardService = new Win32BaseboardService();
 *     Optional<Win32Baseboard> mainboard = mainboardService.get(session);
 * }
 * }</pre>
 * @since 3.0.0
 * @author Egg-03
 */

public class Win32BaseboardService implements OptionalCommonServiceInterface<Win32Baseboard> {

    /**
     * Retrieves an {@link Optional} containing the system's mainboard information.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return an {@link Optional} of {@link Win32Baseboard} representing the system's mainboard.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public Optional<Win32Baseboard> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(Cimv2Namespace.WIN32_BASEBOARD_QUERY.getQuery());
        return new Win32BaseboardMapper().mapToObject(response.getCommandOutput(), Win32Baseboard.class);
    }

    /**
     * Retrieves an {@link Optional} containing the system's mainboard information
     * using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return an {@link Optional} of {@link Win32Baseboard} representing the system's mainboard.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public Optional<Win32Baseboard> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(Cimv2Namespace.WIN32_BASEBOARD_QUERY.getQuery());
        return new Win32BaseboardMapper().mapToObject(response.getCommandOutput(), Win32Baseboard.class);
    }
}
