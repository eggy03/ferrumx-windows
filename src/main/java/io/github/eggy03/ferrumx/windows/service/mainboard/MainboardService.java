package io.github.eggy03.ferrumx.windows.service.mainboard;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.CimQuery;
import io.github.eggy03.ferrumx.windows.entity.mainboard.Mainboard;
import io.github.eggy03.ferrumx.windows.mapping.mainboard.MainboardMapper;
import io.github.eggy03.ferrumx.windows.service.OptionalCommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Service class for fetching mainboard information from the system.
 * <p>
 * This class executes the {@link CimQuery#MAINBOARD_QUERY} PowerShell command
 * and maps the resulting JSON into a {@link Mainboard} object.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * MainboardService mainboardService = new MainboardService();
 * Optional<Mainboard> mainboard = mainboardService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     MainboardService mainboardService = new MainboardService();
 *     Optional<Mainboard> mainboard = mainboardService.get(session);
 * }
 * }</pre>
 * @since 2.0.0
 * @author Egg-03
 */

public class MainboardService implements OptionalCommonServiceInterface<Mainboard> {

    /**
     * Retrieves an {@link Optional} containing the system's mainboard information.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return an {@link Optional} of {@link Mainboard} representing the system's mainboard.
     *
     * @throws com.google.gson.JsonSyntaxException if there is an error executing the PowerShell command
     *                          or parsing the output.
     */
    @NotNull
    @Override
    public Optional<Mainboard> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(CimQuery.MAINBOARD_QUERY.getQuery());
        return new MainboardMapper().mapToObject(response.getCommandOutput(), Mainboard.class);
    }

    /**
     * Retrieves an {@link Optional} containing the system's mainboard information
     * using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return an {@link Optional} of {@link Mainboard} representing the system's mainboard.
     *
     * @throws com.google.gson.JsonSyntaxException if there is an error executing the PowerShell command
     *                          or parsing the output.
     */
    @NotNull
    @Override
    public Optional<Mainboard> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(CimQuery.MAINBOARD_QUERY.getQuery());
        return new MainboardMapper().mapToObject(response.getCommandOutput(), Mainboard.class);
    }
}
