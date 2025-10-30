package io.github.eggy03.ferrumx.windows.service.processor;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.processor.Win32AssociatedProcessorMemory;
import io.github.eggy03.ferrumx.windows.entity.processor.Win32CacheMemory;
import io.github.eggy03.ferrumx.windows.entity.processor.Win32Processor;
import io.github.eggy03.ferrumx.windows.mapping.processor.Win32AssociatedProcessorMemoryMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;

import java.util.List;

/**
 * Service class for fetching the association between a Processor, and it's Cache information from the system.
 * <p>
 * This class executes the {@link Cimv2Namespace#ASSOCIATED_PROCESSOR_MEMORY_QUERY} PowerShell command
 * and maps the resulting JSON into a list of {@link Win32AssociatedProcessorMemory} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32AssociatedProcessorMemoryService apmService = new Win32AssociatedProcessorMemoryService();
 * List<Win32AssociatedProcessorMemory> apm = apmService.get();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32AssociatedProcessorMemoryService apmService = new Win32AssociatedProcessorMemoryService();
 *     List<Win32AssociatedProcessorMemory> apm = apmService.get(session);
 * }
 * }</pre>
 * @since 2.3.0
 * @author Egg-03
 */
public class Win32AssociatedProcessorMemoryService implements CommonServiceInterface<Win32AssociatedProcessorMemory> {

    /**
     * Retrieves a list of {@link Win32AssociatedProcessorMemory} entities present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32AssociatedProcessorMemory} objects representing the association between
     * a {@link Win32Processor} and it's {@link Win32CacheMemory}. Returns an empty list if none are detected.
     *
     * @since 2.3.0
     */
    @Override
    public List<Win32AssociatedProcessorMemory> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(Cimv2Namespace.ASSOCIATED_PROCESSOR_MEMORY_QUERY.getQuery());
        return new Win32AssociatedProcessorMemoryMapper().mapToList(response.getCommandOutput(), Win32AssociatedProcessorMemory.class);
    }

    /**
     * Retrieves a list of {@link Win32AssociatedProcessorMemory} entities using the caller's {@link PowerShell} session.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32AssociatedProcessorMemory} objects representing the association between
     * a {@link Win32Processor} and it's {@link Win32CacheMemory}. Returns an empty list if none are detected.
     *
     * @since 2.3.0
     */
    @Override
    public List<Win32AssociatedProcessorMemory> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(Cimv2Namespace.ASSOCIATED_PROCESSOR_MEMORY_QUERY.getQuery());
        return new Win32AssociatedProcessorMemoryMapper().mapToList(response.getCommandOutput(), Win32AssociatedProcessorMemory.class);
    }
}
