package io.github.eggy03.ferrumx.windows.entity.processor;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a CPU device on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_Processor} WMI class.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
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
 * See {@link Win32CacheMemory} for related cache information.
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-processor">Win32_Processor Documentation</a>
 * @since 3.0.0
 * @author Egg-03
 */

@Value
@Builder(toBuilder = true)
public class Win32Processor {

    /**
     * Unique identifier of the processor on the system.
     */
    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    /**
     * Processor name: Typically includes manufacturer, brand, and model information.
     */
    @SerializedName("Name")
    @Nullable
    String name;

    /**
     * Number of physical cores on the processor.
     */
    @SerializedName("NumberOfCores")
    @Nullable
    Integer numberOfCores;

    /**
     * Number of enabled processor cores.
     */
    @SerializedName("NumberOfEnabledCore")
    @Nullable
    Integer numberOfEnabledCores;

    /**
     * Number of hardware threads across all cores.
     */
    @SerializedName("ThreadCount")
    @Nullable
    Integer threadCount;

    /**
     * Number of logical processors on the system.
     */
    @SerializedName("NumberOfLogicalProcessors")
    @Nullable
    Integer numberOfLogicalProcessors;

    /**
     * Name of the processor manufacturer.
     */
    @SerializedName("Manufacturer")
    @Nullable
    String manufacturer;

    /**
     * Width of the processor address bus in bits.
     * For a 32-bit CPU the value is 32 and for a 64-bit CPU, the value is 64
     */
    @SerializedName("AddressWidth")
    @Nullable
    Integer addressWidth;

    /**
     * Size of the Level 2 cache in kilobytes.
     */
    @SerializedName("L2CacheSize")
    @Nullable
    Integer l2CacheSize;

    /**
     * Size of the Level 3 cache in kilobytes.
     */
    @SerializedName("L3CacheSize")
    @Nullable
    Integer l3CacheSize;

    /**
     * Maximum speed of the processor in megahertz under normal operating conditions.
     */
    @SerializedName("MaxClockSpeed")
    @Nullable
    Integer maxClockSpeed;

    /**
     * External clock frequency of the processor in megahertz.
     */
    @SerializedName("ExtClock")
    @Nullable
    Integer extClock;

    /**
     * Type of socket or slot used by the processor.
     */
    @SerializedName("SocketDesignation")
    @Nullable
    String socketDesignation;

    /**
     * Version of the processor as reported by the manufacturer.
     */
    @SerializedName("Version")
    @Nullable
    String version;

    /**
     * Short textual description of the processor.
     */
    @SerializedName("Caption")
    @Nullable
    String caption;

    /**
     * Processor family type. Indicates the manufacturer and generation of the processor.
     */
    @SerializedName("Family")
    @Nullable
    Integer family;

    /**
     * Stepping information for the processor revision.
     */
    @SerializedName("Stepping")
    @Nullable
    String stepping;

    /**
     * Indicates whether virtualization technology is enabled in firmware.
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("VirtualizationFirmwareEnabled")
    @Nullable
    Boolean virtualizationFirmwareEnabled;
    public @Nullable Boolean isVirtualizationEnabled() {return virtualizationFirmwareEnabled;}

    /**
     * Processor identifier string, which may include family, model, and stepping information.
     */
    @SerializedName("ProcessorId")
    @Nullable
    String processorId;

    /**
     * Prints the entity in a JSON pretty-print format
     * @return the {@link String} value of the object in JSON pretty-print format
     */
    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}