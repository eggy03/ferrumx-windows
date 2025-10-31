package io.github.eggy03.ferrumx.windows.service.processor;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.processor.Win32CacheMemory;
import io.github.eggy03.ferrumx.windows.mapping.processor.Win32CacheMemoryMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching processor cache information from the system.
 * <p>
 * This class executes the {@link Cimv2Namespace#WIN32_CACHE_MEMORY_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Win32CacheMemory} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32CacheMemoryService cacheService = new Win32CacheMemoryService();
 * List<Win32CacheMemory> caches = cacheService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32CacheMemoryService cacheService = new Win32CacheMemoryService();
 *     List<Win32CacheMemory> caches = cacheService.get(session);
 * }
 * }</pre>
 * @since 3.0.0
 * @author Egg-03
 */

public class Win32CacheMemoryService implements CommonServiceInterface<Win32CacheMemory> {

    /**
     * Retrieves a list of processor cache entries present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32CacheMemory} objects representing the CPU caches.
     *         Returns an empty list if none are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32CacheMemory> get() {

        PowerShellResponse response = PowerShell.executeSingleCommand(Cimv2Namespace.WIN32_CACHE_MEMORY_QUERY.getQuery());
        return new Win32CacheMemoryMapper().mapToList(response.getCommandOutput(), Win32CacheMemory.class);
    }

    /**
     * Retrieves a list of processor cache entries using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Win32CacheMemory} objects representing the CPU caches.
     *         Returns an empty list if none are detected.
     *
     * @since 3.0.0
     */
    @NotNull
    @Override
    public List<Win32CacheMemory> get(PowerShell powerShell) {

        PowerShellResponse response = powerShell.executeCommand(Cimv2Namespace.WIN32_CACHE_MEMORY_QUERY.getQuery());
        return new Win32CacheMemoryMapper().mapToList(response.getCommandOutput(), Win32CacheMemory.class);
    }
}
