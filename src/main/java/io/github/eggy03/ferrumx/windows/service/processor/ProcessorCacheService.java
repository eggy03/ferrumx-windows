package io.github.eggy03.ferrumx.windows.service.processor;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.CimQuery;
import io.github.eggy03.ferrumx.windows.entity.processor.ProcessorCache;
import io.github.eggy03.ferrumx.windows.mapping.processor.ProcessorCacheMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching processor cache information from the system.
 * <p>
 * This class executes the {@link CimQuery#PROCESSOR_CACHE_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link ProcessorCache} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * ProcessorCacheService cacheService = new ProcessorCacheService();
 * List<ProcessorCache> caches = cacheService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     ProcessorCacheService cacheService = new ProcessorCacheService();
 *     List<ProcessorCache> caches = cacheService.get(session);
 * }
 * }</pre>
 * @since 2.0.0
 * @author Egg-03
 */

public class ProcessorCacheService implements CommonServiceInterface<ProcessorCache> {

    /**
     * Retrieves a list of processor cache entries present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link ProcessorCache} objects representing the CPU caches.
     *         Returns an empty list if none are detected.
     *
     * @since 2.2.0
     */
    @NotNull
    @Override
    public List<ProcessorCache> get() {

        PowerShellResponse response = PowerShell.executeSingleCommand(CimQuery.PROCESSOR_CACHE_QUERY.getQuery());
        return new ProcessorCacheMapper().mapToList(response.getCommandOutput(), ProcessorCache.class);
    }

    /**
     * Retrieves a list of processor cache entries using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link ProcessorCache} objects representing the CPU caches.
     *         Returns an empty list if none are detected.
     *
     * @since 2.2.0
     */
    @NotNull
    @Override
    public List<ProcessorCache> get(PowerShell powerShell) {

        PowerShellResponse response = powerShell.executeCommand(CimQuery.PROCESSOR_CACHE_QUERY.getQuery());
        return new ProcessorCacheMapper().mapToList(response.getCommandOutput(), ProcessorCache.class);
    }
}
