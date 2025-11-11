/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.peripheral;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Immutable representation of a battery device on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_Battery} WMI class.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * // Build a new battery instance
 * Win32Battery battery = Win32Battery.builder()
 *     .deviceId("BAT0")
 *     .name("Primary Battery")
 *     .estimatedChargeRemaining(75)
 *     .build();
 *
 * // Modify using toBuilder (copy-on-write)
 * Win32Battery updated = battery.toBuilder()
 *     .estimatedChargeRemaining(50)
 *     .build();
 * }</pre>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-battery">Win32_Battery Documentation</a>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */

@Value
@Builder(toBuilder = true)
public class Win32Battery {

    /**
     * Identifier of the battery.
     */
    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    /**
     * Short, one-line description of the battery object.
     */
    @SerializedName("Caption")
    @Nullable
    String caption;

    /**
     * Description of the battery.
     */
    @SerializedName("Description")
    @Nullable
    String description;

    /**
     * Label by which the battery is known.
     */
    @SerializedName("Name")
    @Nullable
    String name;

    /**
     * Current operational status of the battery device.
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
     * Array of specific power-related capabilities supported by the battery.
     * <p>Possible values:</p>
     * <ul>
     *   <li>0 - Unknown</li>
     *   <li>1 - Not Supported</li>
     *   <li>2 - Disabled</li>
     *   <li>3 - Enabled</li>
     *   <li>4 - Power Saving Modes Entered Automatically</li>
     *   <li>5 - Power State Settable</li>
     *   <li>6 - Power Cycling Supported</li>
     *   <li>7 - Timed Power On Supported</li>
     * </ul>
     */
    @SerializedName("PowerManagementCapabilities")
    @Nullable
    List<Integer> powerManagementCapabilities;

    /**
     * Indicates whether the battery can be power-managed.
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("PowerManagementSupported")
    @Nullable
    Boolean powerManagementSupported;
    public @Nullable Boolean isPowerManagementSupported() {return powerManagementSupported;}

    /**
     * Status of the battery.
     * <p>Possible values:</p>
     * <ul>
     *   <li>1 - Discharging</li>
     *   <li>2 - AC present, not charging</li>
     *   <li>3 - Fully Charged</li>
     *   <li>4 - Low</li>
     *   <li>5 - Critical</li>
     *   <li>6 - Charging</li>
     *   <li>7 - Charging and High</li>
     *   <li>8 - Charging and Low</li>
     *   <li>9 - Charging and Critical</li>
     *   <li>10 - Undefined</li>
     *   <li>11 - Partially Charged</li>
     * </ul>
     */
    @SerializedName("BatteryStatus")
    @Nullable
    Integer batteryStatus;

    /**
     * Type of battery chemistry.
     * <p>Possible values:</p>
     * <ul>
     *   <li>1 - Other</li>
     *   <li>2 - Unknown</li>
     *   <li>3 - Lead Acid</li>
     *   <li>4 - Nickel Cadmium</li>
     *   <li>5 - Nickel Metal Hydride</li>
     *   <li>6 - Lithium-ion</li>
     *   <li>7 - Zinc Air</li>
     *   <li>8 - Lithium Polymer</li>
     * </ul>
     */
    @SerializedName("Chemistry")
    @Nullable
    Integer chemistry;

    /**
     * Design capacity of the battery in milliwatt-hours.
     */
    @SerializedName("DesignCapacity")
    @Nullable
    Integer designCapacity;

    /**
     * Design voltage of the battery in millivolts.
     */
    @SerializedName("DesignVoltage")
    @Nullable
    Integer designVoltage;

    /**
     * Estimated percentage of full charge remaining.
     */
    @SerializedName("EstimatedChargeRemaining")
    @Nullable
    Long estimatedChargeRemaining;

    /**
     * Estimated remaining runtime of the battery in minutes.
     */
    @SerializedName("EstimatedRunTime")
    @Nullable
    Long estimatedRunTime;

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