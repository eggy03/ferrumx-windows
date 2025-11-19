/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.mapping.memory;

import io.github.eggy03.ferrumx.windows.entity.memory.Win32PhysicalMemory;
import io.github.eggy03.ferrumx.windows.mapping.CommonMappingInterface;

/**
 * Provides a type-safe implementation of {@link CommonMappingInterface}
 * and maps JSON strings from Powershell to objects or lists of {@link Win32PhysicalMemory}
 *
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
public class Win32PhysicalMemoryMapper implements CommonMappingInterface<Win32PhysicalMemory> {
}
