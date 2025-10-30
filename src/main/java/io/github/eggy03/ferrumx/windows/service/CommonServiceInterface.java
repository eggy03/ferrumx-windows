package io.github.eggy03.ferrumx.windows.service;

import com.profesorfalken.jpowershell.PowerShell;
import io.github.eggy03.ferrumx.windows.mapping.CommonMappingInterface;

import java.util.List;

/**
 * Common service interface whose method implementations provide a way to fetch WMI data from Powershell
 * in the form of a {@link List}
 * <p>
 *     Useful for implementing services of classes which return more than one instance
 *     such as the {@code Win32_NetworkAdapter} WMI class
 * </p>
 * @param <S> the entity type returned by the service implementation
 * @author Egg-03
 * @since 2.2.0
 * @see OptionalCommonServiceInterface
 */
public interface CommonServiceInterface<S> {

    /**
     * Implementations of this method are expected to query the Powershell using methods
     * that automatically handle the Powershell process lifecycle
     * and then map the results to the expected entity types using an implementation of {@link CommonMappingInterface}
     * @return a {@link List} of entities of type {@code <S>} defined by the caller
     * @since 2.2.0
     */
    List<S> get();

    /**
     * Implementations of this method are expected to query the Powershell using methods
     * that delegate the responsibility of managing the Powershell session to the caller
     * and then map the results to the expected entity types using an implementation of {@link CommonMappingInterface}
     * @param powerShell the caller-managed powershell session passed to the method
     * @return a {@link List} of entities of type {@code <S>} defined by the caller
     * @since 2.2.0
     */
    List<S> get(PowerShell powerShell);
}