package io.github.eggy03.ferrumx.windows.service.compounded;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.PowerShellScript;
import io.github.eggy03.ferrumx.windows.entity.compounded.Win32ProcessorToCacheMemory;
import io.github.eggy03.ferrumx.windows.mapping.compounded.Win32ProcessorToCacheMemoryMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import io.github.eggy03.ferrumx.windows.service.processor.Win32AssociatedProcessorMemoryService;
import io.github.eggy03.ferrumx.windows.service.processor.Win32CacheMemoryService;
import io.github.eggy03.ferrumx.windows.service.processor.Win32ProcessorService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching processor and related cache information from the system.
 * <p>
 * This class executes the {@link PowerShellScript#WIN32_PROCESSOR_TO_CACHE_MEMORY_SCRIPT} script
 * and maps the resulting JSON into a list of {@link Win32ProcessorToCacheMemory} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32ProcessorToCacheMemoryService procService = new Win32ProcessorToCacheMemoryService();
 * List<Win32ProcessorToCacheMemory> procAndCacheList = procService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     List<Win32ProcessorToCacheMemory> procAndCacheList = procService.get(session);
 * }
 * }</pre>
 * @see Win32AssociatedProcessorMemoryService
 * @see Win32ProcessorService
 * @see Win32CacheMemoryService
 * @since 3.0.0
 * @author Egg-03
 */
public class Win32ProcessorToCacheMemoryService implements CommonServiceInterface<Win32ProcessorToCacheMemory> {

    /**
     * Retrieves a list of processors and related cache information connected to the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32ProcessorToCacheMemory} objects.
     * Returns an empty list if no processors and related cache information are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32ProcessorToCacheMemory> get() {
        try(PowerShell shell = PowerShell.openSession()){
            PowerShellResponse response = shell.executeScript(PowerShellScript.WIN32_PROCESSOR_TO_CACHE_MEMORY_SCRIPT.getPath());
            return new Win32ProcessorToCacheMemoryMapper().mapToList(response.getCommandOutput(), Win32ProcessorToCacheMemory.class);
        }
    }

    /**
     * Retrieves a list of processors and related cache information connected to the system using the caller's
     * {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Win32ProcessorToCacheMemory} objects
     * Returns an empty list if no processors and related cache information are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32ProcessorToCacheMemory> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeScript(PowerShellScript.WIN32_PROCESSOR_TO_CACHE_MEMORY_SCRIPT.getPath());
        return new Win32ProcessorToCacheMemoryMapper().mapToList(response.getCommandOutput(), Win32ProcessorToCacheMemory.class);
    }
}
