package io.github.eggy03.ferrumx.windows.entity.memory;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a RAM module on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_PhysicalMemory} WMI class.
 * </p>
 * <p>
 * Instances are inherently thread-safe and may be safely cached or shared across threads.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * PhysicalMemory ram = PhysicalMemory.builder()
 *     .capacity(16L * 1024 * 1024 * 1024)
 *     .speed(3200)
 *     .build();
 *
 * // Create a modified copy
 * PhysicalMemory upgraded = ram.toBuilder()
 *     .speed(3600)
 *     .build();
 * }</pre>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-physicalmemory">Win32_PhysicalMemory</a>
 * @since 2.0.0
 * @author Egg-03
 */

@Value
@Builder(toBuilder = true)
public class PhysicalMemory {

    @SerializedName("Tag")
    @Nullable
    String tag;

    @SerializedName("Name")
    @Nullable
    String name;

    @SerializedName("Manufacturer")
    @Nullable
    String manufacturer;

    @SerializedName("Model")
    @Nullable
    String model;

    @SerializedName("OtherIdentifyingInfo")
    @Nullable
    String otherIdentifyingInfo;

    @SerializedName("PartNumber")
    @Nullable
    String partNumber;

    @SerializedName("FormFactor")
    @Nullable
    Integer formFactor;

    @SerializedName("BankLabel")
    @Nullable
    String bankLabel;

    @SerializedName("Capacity")
    @Nullable
    Long capacity;

    @SerializedName("DataWidth")
    @Nullable
    Integer dataWidth;

    @SerializedName("Speed")
    @Nullable
    Integer speed;

    @SerializedName("ConfiguredClockSpeed")
    @Nullable
    Integer configuredClockSpeed;

    @SerializedName("DeviceLocator")
    @Nullable
    String deviceLocator;

    @SerializedName("SerialNumber")
    @Nullable
    String serialNumber;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
