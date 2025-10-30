package io.github.eggy03.ferrumx.windows.service.processor;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.constant.Cimv2Namespace;
import io.github.eggy03.ferrumx.windows.entity.processor.Win32Processor;
import io.github.eggy03.ferrumx.windows.mapping.processor.Win32ProcessorMapper;
import io.github.eggy03.ferrumx.windows.service.CommonServiceInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service class for fetching CPU information from the system.
 * <p>
 * This class executes the {@link Cimv2Namespace#PROCESSOR_QUERY} PowerShell command
 * and maps the resulting JSON into {@link Win32Processor} objects.
 * </p>
 *
 * <h2>Thread safety</h2>
 * Methods of class are not thread safe.
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Convenience API (creates its own short-lived session)
 * Win32ProcessorService processorService = new Win32ProcessorService();
 * List<Win32Processor> processors = processorService.getManaged();
 *
 * // API with re-usable session (caller manages session lifecycle)
 * try (PowerShell session = PowerShell.openSession()) {
 *     Win32ProcessorService processorService = new Win32ProcessorService();
 *     List<Win32Processor> processors = processorService.get(session);
 * }
 * }</pre>
 * @since 2.0.0
 * @author Egg-03
 */

public class Win32ProcessorService implements CommonServiceInterface<Win32Processor> {

    /**
     * Retrieves a non-null list of processor entries present in the system.
     * <p>
     * Each invocation creates and uses a short-lived PowerShell session internally.
     * </p>
     *
     * @return a list of {@link Win32Processor} objects representing the CPU(s).
     *         Returns an empty list if no processors are detected.
     *
     * @since 2.2.0
     */
    @NotNull
    @Override
    public List<Win32Processor> get() {
        PowerShellResponse response = PowerShell.executeSingleCommand(Cimv2Namespace.PROCESSOR_QUERY.getQuery());
        return new Win32ProcessorMapper().mapToList(response.getCommandOutput(), Win32Processor.class);
    }

    /**
     * Retrieves a non-null list of processor entries using the caller's {@link PowerShell} session.
     *
     * @param powerShell an existing PowerShell session managed by the caller
     * @return a list of {@link Win32Processor} objects representing the CPU(s).
     *         Returns an empty list if no processors are detected.
     *
     * @since 2.2.0
     */
    @NotNull
    @Override
    public List<Win32Processor> get(PowerShell powerShell) {
        PowerShellResponse response = powerShell.executeCommand(Cimv2Namespace.PROCESSOR_QUERY.getQuery());
        return new Win32ProcessorMapper().mapToList(response.getCommandOutput(), Win32Processor.class);
    }
}
