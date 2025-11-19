/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.peripheral;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.namespace.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.peripheral.Win32Printer;
import io.github.eggy03.ferrumx.windows.mapping.peripheral.Win32PrinterMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching printer information from the system.
 * <p>
 * This class executes the {@link Cimv2Namespace#WIN32_PRINTER_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Win32Printer} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32PrinterService printerService = new Win32PrinterService();
 * List<Win32Printer> printers = printerService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     List<Win32Printer> printers = printerService.get(session);
 * }
 * }</pre>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@Slf4j
public class Win32PrinterService implements CommonServiceInterface<Win32Printer> {

    /**
     * Retrieves a list of printers present on the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32Printer} objects representing the system's printers.
     *         If no printers are present, returns an empty list.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32Printer> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(Cimv2Namespace.WIN32_PRINTER_QUERY.getQuery());
        log.trace("Powershell response for auto-managed session :\n{}", response.getCommandOutput());
        return new Win32PrinterMapper().mapToList(response.getCommandOutput(), Win32Printer.class);
    }

    /**
     * Retrieves a list of printers present on the system using the caller's
     * {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Win32Printer} objects representing the system's printers.
     *         If no printers are present, returns an empty list.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32Printer> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(Cimv2Namespace.WIN32_PRINTER_QUERY.getQuery());
        log.trace("Powershell response for self-managed session :\n{}", response.getCommandOutput());
        return new Win32PrinterMapper().mapToList(response.getCommandOutput(), Win32Printer.class);
    }
}
