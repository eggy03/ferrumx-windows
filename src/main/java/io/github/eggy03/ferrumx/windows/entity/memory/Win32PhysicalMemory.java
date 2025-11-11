/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.memory;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

/**
 * Immutable representation of a RAM module on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_PhysicalMemory} WMI class.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * Win32PhysicalMemory ram = Win32PhysicalMemory.builder()
 *     .capacity(16L * 1024 * 1024 * 1024)
 *     .speed(3200)
 *     .build();
 *
 * // Create a modified copy
 * Win32PhysicalMemory upgraded = ram.toBuilder()
 *     .speed(3600)
 *     .build();
 * }</pre>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-physicalmemory">Win32_PhysicalMemory Documentation</a>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */

@Value
@Builder(toBuilder = true)
public class Win32PhysicalMemory {

    /**
     * Unique identifier for the physical memory device represented by an instance of this class.
     * <p>
     * Example: "Physical Memory 1"
     */
    @SerializedName("Tag")
    @Nullable
    String tag;

    /**
     * Label for the Physical Memory.
     */
    @SerializedName("Name")
    @Nullable
    String name;

    /**
     * Name of the organization responsible for producing the physical memory.
     */
    @SerializedName("Manufacturer")
    @Nullable
    String manufacturer;

    /**
     * Model name for the physical element.
     */
    @SerializedName("Model")
    @Nullable
    String model;

    /**
     * Additional data, beyond asset tag information, that can be used to identify a physical element.
     * For example, bar code data associated with an element that also has an asset tag.
     */
    @SerializedName("OtherIdentifyingInfo")
    @Nullable
    String otherIdentifyingInfo;

    /**
     * Part number assigned by the organization responsible for producing or manufacturing the physical element.
     */
    @SerializedName("PartNumber")
    @Nullable
    String partNumber;

    /**
     * Implementation form factor for the chip.
     * <ul>
     *     <li>0 - Unknown</li>
     *     <li>1 - Other</li>
     *     <li>2 - SIP</li>
     *     <li>3 - DIP</li>
     *     <li>4 - ZIP</li>
     *     <li>5 - SOJ</li>
     *     <li>6 - Proprietary</li>
     *     <li>7 - SIMM</li>
     *     <li>8 - DIMM</li>
     *     <li>9 - TSOP</li>
     *     <li>10 - PGA</li>
     *     <li>11 - RIMM</li>
     *     <li>12 - SODIMM</li>
     *     <li>13 - SRIMM</li>
     *     <li>14 - SMD</li>
     *     <li>15 - SSMP</li>
     *     <li>16 - QFP</li>
     *     <li>17 - TQFP</li>
     *     <li>18 - SOIC</li>
     *     <li>19 - LCC</li>
     *     <li>20 - PLCC</li>
     *     <li>21 - BGA</li>
     *     <li>22 - FPBGA</li>
     *     <li>23 - LGA</li>
     * </ul>
     */
    @SerializedName("FormFactor")
    @Nullable
    Integer formFactor;

    /**
     * Physically labeled bank where the memory is located.
     * <p>
     * Examples: "Bank 0", "Bank A"
     */
    @SerializedName("BankLabel")
    @Nullable
    String bankLabel;

    /**
     * Total capacity of the physical memory—in bytes.
     */
    @SerializedName("Capacity")
    @Nullable
    BigInteger capacity;

    /**
     * Data width of the physical memory—in bits.
     * A data width of 0 (zero) and a total width of 8 (eight) indicates that the memory is used solely to provide error correction bits.
     */
    @SerializedName("DataWidth")
    @Nullable
    Integer dataWidth;

    /**
     * Speed of the physical memory—in MHz.
     */
    @SerializedName("Speed")
    @Nullable
    Long speed;

    /**
     * The configured clock speed of the memory device, in MHz, or 0, if the speed is unknown.
     */
    @SerializedName("ConfiguredClockSpeed")
    @Nullable
    Long configuredClockSpeed;

    /**
     * Label of the socket or circuit board that holds the memory.
     * <p>
     * Example: "SIMM 3"
     */
    @SerializedName("DeviceLocator")
    @Nullable
    String deviceLocator;

    /**
     * Manufacturer-allocated number to identify the physical element.
     */
    @SerializedName("SerialNumber")
    @Nullable
    String serialNumber;

    /**
     * Retrieves the entity in a JSON pretty-print formatted string
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
