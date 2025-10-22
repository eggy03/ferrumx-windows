package io.github.eggy03.ferrumx.windows.service.processor;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.CimQuery;
import io.github.eggy03.ferrumx.windows.entity.processor.AssociatedProcessorMemory;
import io.github.eggy03.ferrumx.windows.entity.processor.Processor;
import io.github.eggy03.ferrumx.windows.entity.processor.ProcessorCache;
import io.github.eggy03.ferrumx.windows.mapping.processor.AssociatedProcessorMemoryMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;

import java.util.List;

/**
 * Service class for fetching the association between a Processor, and it's Cache information from the system.
 * <p>
 * This class executes the {@link CimQuery#ASSOCIATED_PROCESSOR_MEMORY_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link AssociatedProcessorMemory} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * AssociatedProcessorMemoryService apmService = new AssociatedProcessorMemoryService();
 * List<AssociatedProcessorMemory> apm = apmService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     AssociatedProcessorMemoryService apmService = new AssociatedProcessorMemoryService();
 *     List<AssociatedProcessorMemory> apm = apmService.get(session);
 * }
 * }</pre>
 * @since 2.3.0
 * @author Egg-03
 */
public class AssociatedProcessorMemoryService implements CommonServiceInterface<AssociatedProcessorMemory> {

    /**
     * Retrieves a list of {@link AssociatedProcessorMemory} entities present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link AssociatedProcessorMemory} objects representing the association between
     * a {@link Processor} and it's {@link ProcessorCache}. Returns an empty list if none are detected.
     */
    @Override
    public List<AssociatedProcessorMemory> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(CimQuery.ASSOCIATED_PROCESSOR_MEMORY_QUERY.getQuery());
        return new AssociatedProcessorMemoryMapper().mapToList(response.getCommandOutput(), AssociatedProcessorMemory.class);
    }

    /**
     * Retrieves a list of {@link AssociatedProcessorMemory} entities using the caller's {@link PowerShell} session.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link AssociatedProcessorMemory} objects representing the association between
     * a {@link Processor} and it's {@link ProcessorCache}. Returns an empty list if none are detected.
     */
    @Override
    public List<AssociatedProcessorMemory> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(CimQuery.ASSOCIATED_PROCESSOR_MEMORY_QUERY.getQuery());
        return new AssociatedProcessorMemoryMapper().mapToList(response.getCommandOutput(), AssociatedProcessorMemory.class);
    }
}
