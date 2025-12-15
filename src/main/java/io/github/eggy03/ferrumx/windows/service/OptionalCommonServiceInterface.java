/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.service;

import com.profesorfalken.jpowershell.PowerShell;
import io.github.eggy03.ferrumx.windows.mapping.CommonMappingInterface;
import io.github.eggy03.ferrumx.windows.utility.TerminalUtility;

import java.util.Optional;

/**
 * Common service interface whose method implementations provide a way to fetch WMI data from Powershell
 * in the form of {@link Optional}
 * <p>
 *     Useful for implementing services of classes which return exactly one instance
 *     such as the {@code Win32_ComputerSystem} WMI class
 * </p>
 * @param <S> the entity type returned by the service implementation
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 * @since 2.2.0
 * @see CommonServiceInterface
 */
public interface OptionalCommonServiceInterface<S> {

    /**
     * Implementations of this method are expected to query the Powershell using methods
     * that automatically handle the Powershell process lifecycle and then
     * map the results to the expected entity types using an implementation of {@link CommonMappingInterface}
     * @return an {@link Optional} entity of type {@code <S>} defined by the caller
     * @since 2.2.0
     */
    Optional<S> get();

    /**
     * Implementations of this method are expected to query the Powershell using methods
     * that delegate the responsibility of managing the Powershell session to the caller
     * and then map the results to the expected entity types using an implementation of {@link CommonMappingInterface}
     * @param powerShell the caller-managed powershell session passed to the method
     * @return an {@link Optional} entity of type {@code <S>} defined by the caller
     * @since 2.2.0
     */
    Optional<S> get(PowerShell powerShell);

    /**
     * Implementations of this method are expected to skip {@link PowerShell} entirely and rely on
     * {@link TerminalUtility} instead for powershell session management
     * @param timeout the timeout in seconds after which the process will be force stopped
     * @return an {@link Optional} entity of type {@code <S>} defined by the caller
     * @since 3.1.0
     */
    Optional<S> get(long timeout);
}