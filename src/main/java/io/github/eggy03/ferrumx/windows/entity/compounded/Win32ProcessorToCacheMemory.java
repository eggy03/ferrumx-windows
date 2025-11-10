/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.compounded;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.ferrumx.windows.entity.processor.Win32AssociatedProcessorMemory;
import io.github.eggy03.ferrumx.windows.entity.processor.Win32CacheMemory;
import io.github.eggy03.ferrumx.windows.entity.processor.Win32Processor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Immutable representation of a {@link Win32Processor} and its associated
 * {@code 1:N} relationship with {@link Win32CacheMemory} in a Windows system.
 * <p>
 * Each instance represents a single processor identified by {@link #deviceId},
 * and maintains a one-to-many mapping with its corresponding cache memory objects
 * (such as L1, L2, and L3 caches).
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 * <p>
 * This class is purely a convenience class designed to eliminate the
 * need for using {@link Win32AssociatedProcessorMemory} when fetching a
 * relation between {@link Win32Processor} and {@link Win32CacheMemory} as it stores
 * all instances of cache memories for a particular processor.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * Win32ProcessorToCacheMemory cpuInfo = Win32ProcessorToCacheMemory.builder()
 *     .deviceId("CPU0")
 *     .processor(processor)
 *     .cacheMemoryList(cacheList)
 *     .build();
 * }</pre>
 *
 * @see Win32Processor
 * @see Win32CacheMemory
 *
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */

@Value
@Builder(toBuilder = true)
public class Win32ProcessorToCacheMemory {

    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    @SerializedName("Processor")
    @Nullable
    Win32Processor processor;

    @SerializedName("CacheMemory")
    @Nullable
    List<Win32CacheMemory> cacheMemoryList;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}