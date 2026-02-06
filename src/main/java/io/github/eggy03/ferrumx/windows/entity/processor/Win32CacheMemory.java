/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.processor;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a processor cache (e.g., L1, L2, L3) on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_CacheMemory} WMI class.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new instance
 * Win32CacheMemory l2Cache = Win32CacheMemory.builder()
 *     .deviceId("CPU0_L2")
 *     .purpose("Instruction")
 *     .installedSize(512)
 *     .associativity(8)
 *     .build();
 *
 * // Create a modified copy using the builder
 * Win32CacheMemory resized = l2Cache.toBuilder()
 *     .installedSize(1024)
 *     .build();
 *
 * }</pre>
 * <p>
 * See {@link Win32Processor} for related CPU information.
 *
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-cachememory">Win32_CacheMemory Documentation</a>
 * @since 3.0.0
 */

@Value
@Builder(toBuilder = true)
public class Win32CacheMemory {

    /**
     * Unique identifier of the cache instance.
     * <p>
     * Example: {@code "Cache Memory 1"}
     * </p>
     */
    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    /**
     * Free-form description of the cache purpose or level designation.
     * <p>
     * Example: {@code "L2 Cache"}
     * </p>
     */
    @SerializedName("Purpose")
    @Nullable
    String purpose;

    /**
     * Type of cache.
     * <p>
     * Possible values:
     * <ul>
     *   <li>1 – Other</li>
     *   <li>2 – Unknown</li>
     *   <li>3 – Instruction</li>
     *   <li>4 – Data</li>
     *   <li>5 – Unified</li>
     * </ul>
     */
    @SerializedName("CacheType")
    @Nullable
    Integer cacheType;

    /**
     * Cache hierarchy level.
     * <p>
     * Possible values:
     * <ul>
     *   <li>1 – Other</li>
     *   <li>2 – Unknown</li>
     *   <li>3 – Primary (L1)</li>
     *   <li>4 – Secondary (L2)</li>
     *   <li>5 – Tertiary (L3)</li>
     *   <li>6 – Not Applicable</li>
     * </ul>
     */
    @SerializedName("Level")
    @Nullable
    Integer level;

    /**
     * Installed cache size in kilobytes.
     * <p>
     * Example: {@code 512} for 512 KB.
     * </p>
     */
    @SerializedName("InstalledSize")
    @Nullable
    Long installedSize;

    /**
     * Cache associativity.
     * <p>
     * Possible values:
     * <ul>
     *   <li>1 – Other</li>
     *   <li>2 – Unknown</li>
     *   <li>3 – Direct Mapped</li>
     *   <li>4 – 2-way Set-Associative</li>
     *   <li>5 – 4-way Set-Associative</li>
     *   <li>6 – Fully Associative</li>
     *   <li>7 – 8-way Set-Associative</li>
     *   <li>8 – 16-way Set-Associative</li>
     * </ul>
     */
    @SerializedName("Associativity")
    @Nullable
    Integer associativity;

    /**
     * Physical cache location relative to the processor.
     * <p>
     * Possible values:
     * <ul>
     *   <li>0 – Internal</li>
     *   <li>1 – External</li>
     *   <li>2 – Reserved</li>
     *   <li>3 – Unknown</li>
     * </ul>
     */
    @SerializedName("Location")
    @Nullable
    Integer location;

    /**
     * Error-correction method used by the cache.
     * <p>
     * Possible values:
     * <ul>
     *   <li>0 – Reserved</li>
     *   <li>1 – Other</li>
     *   <li>2 – Unknown</li>
     *   <li>3 – None</li>
     *   <li>4 – Parity</li>
     *   <li>5 – Single-bit ECC</li>
     *   <li>6 – Multi-bit ECC</li>
     * </ul>
     */
    @SerializedName("ErrorCorrectType")
    @Nullable
    Integer errorCorrectType;

    /**
     * Current availability and operational state.
     * <p>
     * Possible Values:
     * <ul>
     *   <li>1 – Other</li>
     *   <li>2 – Unknown</li>
     *   <li>3 – Running/Full Power</li>
     *   <li>4 – Warning</li>
     *   <li>5 – In Test</li>
     *   <li>6 – Not Applicable</li>
     *   <li>7 – Power Off</li>
     *   <li>8 – Offline</li>
     *   <li>9 – Off-duty</li>
     *   <li>10 – Degraded</li>
     *   <li>11 – Not Installed</li>
     *   <li>12 – Install Error</li>
     *   <li>13 – Power Save - Unknown</li>
     *   <li>14 – Power Save - Low Power Mode</li>
     *   <li>15 – Power Save - Standby</li>
     *   <li>16 – Power Cycle</li>
     *   <li>17 – Power Save - Warning</li>
     *   <li>18 – Paused</li>
     *   <li>19 – Not Ready</li>
     *   <li>20 – Not Configured</li>
     *   <li>21 – Quiesced</li>
     * </ul>
     */
    @SerializedName("Availability")
    @Nullable
    Integer availability;

    /**
     * Current operational status of the cache device.
     * <p>Possible OPERATIONAL values:</p>
     * <ul>
     *   <li>"OK"</li>
     *   <li>"Degraded"</li>
     *   <li>"Pred Fail"</li>
     * </ul>
     * <p>Possible NON-OPERATIONAL values:</p>
     * <ul>
     *   <li>"Unknown"</li>
     *   <li>"Error"</li>
     *   <li>"Starting"</li>
     *   <li>"Stopping"</li>
     *   <li>"Service"</li>
     * </ul>
     * <p>Possible OTHER values:</p>
     * <ul>
     *   <li>"Stressed"</li>
     *   <li>"NonRecover"</li>
     *   <li>"No Contact"</li>
     *   <li>"Lost Comm"</li>
     * </ul>
     */
    @SerializedName("Status")
    @Nullable
    String status;

    /**
     * Logical state of the device.
     * <p>
     * Possible values:
     * <ul>
     *   <li>1 – Other</li>
     *   <li>2 – Unknown</li>
     *   <li>3 – Enabled</li>
     *   <li>4 – Disabled</li>
     *   <li>5 – Not Applicable</li>
     * </ul>
     */
    @SerializedName("StatusInfo")
    @Nullable
    Integer statusInfo;

    /**
     * Retrieves the entity in a JSON pretty-print formatted string
     *
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