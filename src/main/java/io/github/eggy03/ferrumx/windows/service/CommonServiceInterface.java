/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service;

import com.profesorfalken.jpowershell.PowerShell;
import io.github.eggy03.ferrumx.windows.mapping.CommonMappingInterface;
import io.github.eggy03.ferrumx.windows.utility.TerminalUtility;

import java.util.List;

/**
 * Common service interface whose method implementations provide a way to fetch WMI data from PowerShell
 * in the form of a {@link List}
 * <p>
 *     Useful for implementing services of classes which return more than one instance
 *     such as the {@code Win32_NetworkAdapter} WMI class
 * </p>
 * @param <S> the entity type returned by the service implementation
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 * @since 2.2.0
 * @see OptionalCommonServiceInterface
 */
public interface CommonServiceInterface<S> {

    /**
     * Implementations of this method are expected to query the PowerShell using methods
     * that automatically handle the PowerShell process lifecycle
     * and then map the results to the expected entity types using a custom implementation or
     * the default methods of {@link CommonMappingInterface}
     * @return a {@link List} of entities of type {@code <S>} defined by the caller
     * @since 2.2.0
     */
    List<S> get();

    /**
     * Implementations of this method are expected to query the PowerShell using methods
     * that delegate the responsibility of managing the PowerShell session to the caller
     * and then map the results to the expected entity types using a custom implementation
     * or the default methods of {@link CommonMappingInterface}
     * @param powerShell the caller-managed PowerShell session passed to the method
     * @return a {@link List} of entities of type {@code <S>} defined by the caller
     * @since 2.2.0
     */
    List<S> get(PowerShell powerShell);

    /**
     * Implementations of this method are expected to skip {@link PowerShell} entirely and rely on
     * {@link TerminalUtility} instead for PowerShell session management
     * and then map the results to the expected entity types using a custom implementation
     * or the default methods of {@link CommonMappingInterface}
     * @param timeout the maximum time (in seconds) to wait for the PowerShell command to complete before terminating the process
     * @return a {@link List} of entities of type {@code <S>} defined by the caller
     * @since 3.1.0
     */
    List<S> get(long timeout);
}