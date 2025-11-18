/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service.system;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.namespace.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.system.Win32PnPEntity;
import io.github.eggy03.ferrumx.windows.mapping.system.Win32PnPEntityMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Service class for fetching operating system information from the system.
 * <p>
 * This class executes the {@link Cimv2Namespace#WIN32_OPERATING_SYSTEM_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Win32PnPEntity} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32PnPEntityService pnpService = new Win32PnPEntityService();
 * List<Win32PnPEntity> pnpEntityList = pnpService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32PnPEntityService pnpService = new Win32PnPEntityService();
 *     List<Win32PnPEntity> pnpEntityList = pnpService.get(session);
 * }
 * }</pre>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@Slf4j
public class Win32PnPEntityService implements CommonServiceInterface<Win32PnPEntity> {

    /**
     * Retrieves a list of pnp entities present on the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32PnPEntity} objects representing the system's pnp entities.
     *         Returns an empty list if none are detected.
     *
     * @since 3.0.0
     */
    @Override
    public List<Win32PnPEntity> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(Cimv2Namespace.WIN32_PNP_ENTITY_QUERY.getQuery());
        log.trace("Powershell response for auto-managed session :\n{}", response.getCommandOutput());
        return new Win32PnPEntityMapper().mapToList(response.getCommandOutput(), Win32PnPEntity.class);
    }

    /**
     * Retrieves a list of pnp entities using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Win32PnPEntity} objects representing the system's pnp entities.
     *         Returns an empty list if none are detected.
     *
     * @since 3.0.0
     */
    @Override
    public List<Win32PnPEntity> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(Cimv2Namespace.WIN32_PNP_ENTITY_QUERY.getQuery());
        log.trace("Powershell response for self-managed session :\n{}", response.getCommandOutput());
        return new Win32PnPEntityMapper().mapToList(response.getCommandOutput(), Win32PnPEntity.class);
    }
}
