package io.github.eggy03.ferrumx.windows.entity.processor;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a processor cache (e.g., L1, L2, L3) on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_CacheMemory} WMI class.
 * </p>
 * <p>
 * Instances are thread-safe and may be safely cached or shared across threads.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new ProcessorCache instance
 * ProcessorCache l2Cache = ProcessorCache.builder()
 *     .deviceId("CPU0_L2")
 *     .purpose("Instruction")
 *     .installedSize(512)
 *     .associativity(8)
 *     .build();
 *
 * // Create a modified copy using the builder
 * ProcessorCache resized = l2Cache.toBuilder()
 *     .installedSize(1024)
 *     .build();
 *
 * }</pre>
 *
 * {@link Processor} for related CPU information.
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-cachememory">Win32_CacheMemory</a>
 * @since 2.0.0
 * @author Egg-03
 */

@Value
@Builder(toBuilder = true)
public class ProcessorCache {

    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    @SerializedName("Purpose")
    @Nullable
    String purpose;

    @SerializedName("InstalledSize")
    @Nullable
    Integer installedSize;

    @SerializedName("Associativity")
    @Nullable
    Integer associativity;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}