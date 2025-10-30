package io.github.eggy03.ferrumx.windows.entity.processor;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.ferrumx.windows.constant.CimQuery;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a {@link Win32Processor} association with {@link Win32CacheMemory}.
 * <p>
 * Fields <b>indirectly</b> correspond to properties retrieved from the {@code Win32_AssociatedProcessorMemory} WMI class
 * and represent an association between {@code Win32_Processor} and {@code Win32_CacheMemory}.
 * </p>
 * <p>Links {@link Win32Processor} with {@link Win32CacheMemory} via their device IDs</p>
 * <p>This class has the following two fields:</p>
 * <ul>
 *     <li>{@code cacheMemoryDeviceId} - contains the {@code deviceId} field of {@link Win32CacheMemory}</li>
 *     <li>{@code processorDeviceId} - contains the {@code deviceId} field of {@link Win32Processor}</li>
 * </ul>
 *
 * <p>
 *     <b>Extra Notes:</b> The {@code Win32_AssociatedProcessorMemory} WMI class itself does not directly expose
 *     the {@code DeviceID} (from {@code Win32_CacheMemory}) or the {@code DeviceID}
 *     (from {@code Win32_Processor}) as standalone properties.
 *     Instead, these values are nested within its references: {@code Antecedent} and {@code Dependent}.

 * </p>
 * <p>
 *     To simplify data mapping, the PowerShell query defined in
 *     {@link CimQuery#ASSOCIATED_PROCESSOR_MEMORY_QUERY} constructs a custom {@code PSObject}
 *     that maps {@code Antecedent.DeviceID} to {@code cacheMemoryDeviceId} and {@code Dependent.DeviceID} to {@code processorDeviceId}
 *     and the resulting JSON returned is deserialized into this entity class.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new instance
 * Win32AssociatedProcessorMemory apm = Win32AssociatedProcessorMemory.builder()
 *     .cacheMemoryDeviceId("Cache Memory 1")
 *     .processorDeviceId("CPU0")
 *     .build();
 * // Create a modified copy using the builder
 * Win32AssociatedProcessorMemory updated = apm.toBuilder()
 *     .cacheMemoryDeviceId("Cache Memory 2")
 *     .build();
 * }</pre>
 *
 * <p>See {@link Win32Processor} for related CPU information.</p>
 * <p>See {@link Win32CacheMemory} for related CPU Cache information.</p>
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-associatedprocessormemory">Win32_AssociatedProcessorMemory Documentation</a>
 * @since 2.3.0
 * @author Egg-03
 */
@Value
@Builder (toBuilder = true)
public class Win32AssociatedProcessorMemory {

    @SerializedName("CacheMemoryDeviceID")
    @Nullable
    String cacheMemoryDeviceId;

    @SerializedName("ProcessorDeviceID")
    @Nullable
    String processorDeviceId;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
