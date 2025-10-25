package io.github.eggy03.ferrumx.windows.service.mainboard;

import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.CimQuery;
import io.github.eggy03.ferrumx.windows.entity.mainboard.Bios;
import io.github.eggy03.ferrumx.windows.mapping.mainboard.BiosMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching BIOS information from the system.
 * <p>
 * This class executes the {@link CimQuery#BIOS_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Bios} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * BiosService biosService = new BiosService();
 * List<Bios> biosList = biosService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     BiosService biosService = new BiosService();
 *     List<Bios> biosList = biosService.get(session);
 * }
 * }</pre>
 * @since 2.0.0
 * @author Egg-03
 */

public class BiosService implements CommonServiceInterface<Bios> {

    /**
     * Retrieves a list of BIOS entries present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     * @return a list of {@link Bios} objects representing the system BIOS.
     *         Returns an empty list if no BIOS entries are detected.
     *
     * @since 2.2.0
     */
    @NotNull
    @Override
    public List<Bios> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(CimQuery.BIOS_QUERY.getQuery());
        return new BiosMapper().mapToList(response.getCommandOutput(), Bios.class);
    }

    /**
     * Retrieves a list of BIOS entries present in the system using the caller's
     * {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Bios} objects representing the system BIOS.
     *         Returns an empty list if no BIOS entries are detected.
     *
     * @since 2.2.0
     */
    @NotNull
    @Override
    public List<Bios> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(CimQuery.BIOS_QUERY.getQuery());
        return new BiosMapper().mapToList(response.getCommandOutput(), Bios.class);
    }
}