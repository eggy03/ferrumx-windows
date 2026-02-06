/*
 * © 2024–2025 The ferrumx-windows contributors
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
import org.jetbrains.annotations.NotNull;
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

    /**
     * The unique identifier for the {@link Win32Processor} instance.
     * <p>
     * Corresponds to the processor’s {@code DeviceID}, such as {@code "CPU0"} or {@code "CPU1"}.
     * </p>
     */
    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    /**
     * The {@link Win32Processor} entity associated with the {@link #deviceId}.
     * <p>
     * Represents the physical or logical processor containing one or more cache memory units.
     * </p>
     */
    @SerializedName("Processor")
    @Nullable
    Win32Processor processor;

    /**
     * A list of {@link Win32CacheMemory} entities associated with the {@link #deviceId}.
     * <p>
     * Represents the processor’s cache hierarchy (such as L1, L2, and L3 cache levels)
     * linked to the specified {@link #processor}.
     * </p>
     */
    @SerializedName("CacheMemory")
    @Nullable
    List<Win32CacheMemory> cacheMemoryList;

    /**
     * Retrieves the entity in a JSON pretty-print formatted string
     * @return the {@link String} value of the object in JSON pretty-print format
     */
    @Override
    @NotNull
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}