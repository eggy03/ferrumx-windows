package io.github.eggy03.ferrumx.windows.service.memory;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.CimQuery;
import io.github.eggy03.ferrumx.windows.entity.memory.PhysicalMemory;
import io.github.eggy03.ferrumx.windows.mapping.memory.PhysicalMemoryMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching information about physical memory modules (RAM) in the system.
 * <p>
 * This class executes the {@link CimQuery#PHYSICAL_MEMORY_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link PhysicalMemory} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * PhysicalMemoryService memoryService = new PhysicalMemoryService();
 * List<PhysicalMemory> memories = memoryService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     PhysicalMemoryService memoryService = new PhysicalMemoryService();
 *     List<PhysicalMemory> memories = memoryService.get(session);
 * }
 * }</pre>
 * @since 2.0.0
 * @author Egg-03
 */

public class PhysicalMemoryService implements CommonServiceInterface<PhysicalMemory> {

    /**
     * Retrieves a list of physical memory modules present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link PhysicalMemory} objects representing the system's RAM.
     *         Returns an empty list if no memory modules are detected.
     * @throws com.google.gson.JsonSyntaxException if there is an error executing the PowerShell command
     *                          or parsing the output.
     */
    @NotNull
    @Override
    public List<PhysicalMemory> get() {

        PowerShellResponse response = PowerShell.executeSingleCommand(CimQuery.PHYSICAL_MEMORY_QUERY.getQuery());
        return new PhysicalMemoryMapper().mapToList(response.getCommandOutput(), PhysicalMemory.class);
    }

    /**
     * Retrieves a list of physical memory modules using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link PhysicalMemory} objects representing the system's RAM.
     *         Returns an empty list if no memory modules are detected.
     * @throws com.google.gson.JsonSyntaxException if there is an error executing the PowerShell command
     *                          or parsing the output.
     */
    @NotNull
    @Override
    public List<PhysicalMemory> get(PowerShell powerShell) {

        PowerShellResponse response = powerShell.executeCommand(CimQuery.PHYSICAL_MEMORY_QUERY.getQuery());
        return new PhysicalMemoryMapper().mapToList(response.getCommandOutput(), PhysicalMemory.class);
    }

}