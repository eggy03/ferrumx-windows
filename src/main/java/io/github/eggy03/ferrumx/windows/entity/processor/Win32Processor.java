package io.github.eggy03.ferrumx.windows.entity.processor;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a CPU device on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_Processor} WMI class.
 * </p>
 * <p>
 * Instances are thread-safe and may be safely cached or shared across threads.
 * </p>
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new Processor instance
 * Win32Processor cpu = Win32Processor.builder()
 *     .name("Intel Core i9-13900K")
 *     .numberOfCores(24)
 *     .threadCount(32)
 *     .maxClockSpeed(5300)
 *     .build();
 *
 * // Create a modified copy using the builder
 * Win32Processor updated = cpu.toBuilder()
 *     .threadCount(64)
 *     .build();
 * }</pre>
 *
 * {@link Win32CacheMemory} for related cache information.
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-processor">Win32_Processor Documentation</a>
 * @since 2.0.0
 * @author Egg-03
 */

@Value
@Builder(toBuilder = true)
public class Win32Processor {

    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    @SerializedName("Name")
    @Nullable
    String name;

    @SerializedName("NumberOfCores")
    @Nullable
    Integer numberOfCores;

    @SerializedName("NumberOfEnabledCore")
    @Nullable
    Integer numberOfEnabledCores;

    @SerializedName("ThreadCount")
    @Nullable
    Integer threadCount;

    @SerializedName("NumberOfLogicalProcessors")
    @Nullable
    Integer numberOfLogicalProcessors;

    @SerializedName("Manufacturer")
    @Nullable
    String manufacturer;

    @SerializedName("AddressWidth")
    @Nullable
    Integer addressWidth;

    @SerializedName("L2CacheSize")
    @Nullable
    Integer l2CacheSize;

    @SerializedName("L3CacheSize")
    @Nullable
    Integer l3CacheSize;

    @SerializedName("MaxClockSpeed")
    @Nullable
    Integer maxClockSpeed;

    @SerializedName("ExtClock")
    @Nullable
    Integer extClock;

    @SerializedName("SocketDesignation")
    @Nullable
    String socketDesignation;

    @SerializedName("Version")
    @Nullable
    String version;

    @SerializedName("Caption")
    @Nullable
    String caption;

    @SerializedName("Family")
    @Nullable
    Integer family;

    @SerializedName("Stepping")
    @Nullable
    Integer stepping;

    @SerializedName("VirtualizationFirmwareEnabled")
    @Nullable
    Boolean virtualizationFirmwareEnabled;

    @SerializedName("ProcessorId")
    @Nullable
    String processorId;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}