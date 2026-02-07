/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.system;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.ferrumx.windows.entity.memory.Win32PhysicalMemory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.List;

/**
 * Immutable representation of a computer system running Windows.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_ComputerSystem} WMI class.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new instance
 * Win32ComputerSystem system = Win32ComputerSystem.builder()
 *     .caption("Workstation PC")
 *     .description("High-end office workstation")
 *     .name("User-PC")
 *     .chassisSKUNumber("PROD-1234")
 *     .systemSKUNumber("Default String")
 *     .build();
 *
 * // Create a modified copy
 * Win32ComputerSystem updated = system.toBuilder()
 *     .name("Admin-PC")
 *     .build();
 *
 * }</pre>
 *
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-computersystem">Win32_ComputerSystem Documentation</a>
 * @since 3.0.0
 */
@Value
@Builder(toBuilder = true)
public class Win32ComputerSystem {

    // Password Status Properties

    /**
     * System hardware security settings for administrator password status.
     * Possible values:
     * <ul>
     *   <li>0 — Disabled</li>
     *   <li>1 — Enabled</li>
     *   <li>2 — Not Implemented</li>
     *   <li>3 — Unknown</li>
     * </ul>
     */
    @SerializedName("AdminPasswordStatus")
    @Nullable
    Integer adminPasswordStatus;

    /**
     * System hardware security settings for keyboard password status.
     * <ul>
     *   <li>0 — Disabled</li>
     *   <li>1 — Enabled</li>
     *   <li>2 — Not Implemented</li>
     *   <li>3 — Unknown</li>
     * </ul>
     */
    @SerializedName("KeyboardPasswordStatus")
    @Nullable
    Integer keyboardPasswordStatus;

    /**
     * System hardware security settings for power-on password status.
     * <ul>
     *   <li>0 — Disabled</li>
     *   <li>1 — Enabled</li>
     *   <li>2 — Not Implemented</li>
     *   <li>3 — Unknown</li>
     * </ul>
     */
    @SerializedName("PowerOnPasswordStatus")
    @Nullable
    Integer powerOnPasswordStatus;


    // Boot

    /**
     * System start type. Possible values:
     * <ul>
     *   <li>"Normal boot" — Normal boot</li>
     *   <li>"Fail-safe boot" — Safe mode (no network)</li>
     *   <li>"Fail-safe with network boot" — Safe mode with networking</li>
     * </ul>
     */
    @SerializedName("BootupState")
    @Nullable
    String bootupState;

    /**
     * Status and additional data fields that identify the boot status.
     * This value comes from the Boot Status member of the System Boot Information structure in the SMBIOS information.
     * <p>
     * Note: BootStatus is platform/firmware dependent and is not supported before Windows 10 and Windows Server 2016
     */
    @SerializedName("BootStatus")
    @Nullable
    List<Integer> bootStatus;

    /**
     * If true, the automatic reset boot option is enabled.
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("AutomaticResetBootOption")
    @Nullable
    Boolean automaticResetBootOption;
    /**
     * Current power state of the computer system. Possible values:
     * <ul>
     *   <li>0 — Unknown</li>
     *   <li>1 — Full Power</li>
     *   <li>2 — Power Save - Low Power Mode</li>
     *   <li>3 — Power Save - Standby</li>
     *   <li>4 — Power Save - Unknown</li>
     *   <li>5 — Power Cycle</li>
     *   <li>6 — Power Off</li>
     *   <li>7 — Power Save - Warning</li>
     *   <li>8 — Power Save - Hibernate</li>
     *   <li>9 — Power Save - Soft Off</li>
     * </ul>
     */
    @SerializedName("PowerState")
    @Nullable
    Integer powerState;


    // Power
    /**
     * State of the power supply or supplies when last booted.
     * <p>Possible Values:</p>
     * <ul>
     *   <li>1 — Other</li>
     *   <li>2 — Unknown</li>
     *   <li>3 — Safe</li>
     *   <li>4 — Warning</li>
     *   <li>5 — Critical</li>
     *   <li>6 — Non-recoverable</li>
     * </ul>
     */
    @SerializedName("PowerSupplyState")
    @Nullable
    Integer powerSupplyState;
    /**
     * Array of specific power-related capabilities.
     * <p>Possible Values: </p>
     * <ul>
     *   <li>0 — Unknown</li>
     *   <li>1 — Not Supported</li>
     *   <li>2 — Disabled</li>
     *   <li>3 — Enabled</li>
     *   <li>4 — Power Saving Modes Entered Automatically</li>
     *   <li>5 — Power State Settable</li>
     *   <li>6 — Power Cycling Supported</li>
     *   <li>7 — Timed Power On Supported</li>
     * </ul>
     */
    @SerializedName("PowerManagementCapabilities")
    @Nullable
    List<Integer> powerManagementCapabilities;
    /**
     * If true, the device can be power-managed
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("PowerManagementSupported")
    @Nullable
    Boolean powerManagementSupported;
    /**
     * If enabled, indicates the unitary computer system can be reset using power and reset buttons.
     * Typical values:
     * <ul>
     *   <li>1 — Other</li>
     *   <li>2 — Unknown</li>
     *   <li>3 — Disabled</li>
     *   <li>4 — Enabled</li>
     *   <li>5 — Not Implemented</li>
     * </ul>
     */
    @SerializedName("ResetCapability")
    @Nullable
    Integer resetCapability;
    /**
     * Number of automatic resets since the last reset.
     * A value of -1 indicates the count is unknown.
     */
    @SerializedName("ResetCount")
    @Nullable
    Integer resetCount;


    // Reset
    /**
     * Number of consecutive times a system reset is attempted.
     * A value of -1 indicates the limit is unknown.
     */
    @SerializedName("ResetLimit")
    @Nullable
    Integer resetLimit;
    /**
     * Hardware security setting for the front-panel reset button.
     * Typical values:
     * <ul>
     *   <li>0 — Disabled</li>
     *   <li>1 — Enabled</li>
     *   <li>2 — Not Implemented</li>
     *   <li>3 — Unknown</li>
     * </ul>
     */
    @SerializedName("FrontPanelResetStatus")
    @Nullable
    Integer frontPanelResetStatus;
    /**
     * If true, automatic reset capability is available.
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("AutomaticResetCapability")
    @Nullable
    Boolean automaticResetCapability;
    /**
     * Key of a CIM_System instance. Name of the computer system.
     */
    @SerializedName("Name")
    @Nullable
    String name;
    /**
     * Short one-line description of the object.
     */
    @SerializedName("Caption")
    @Nullable
    String caption;
    /**
     * Longer description of the object.
     */
    @SerializedName("Description")
    @Nullable
    String description;


    // General identifying / owner info
    /**
     * Name of the computer manufacturer
     */
    @SerializedName("Manufacturer")
    @Nullable
    String manufacturer;
    /**
     * Product name assigned by the manufacturer.
     */
    @SerializedName("Model")
    @Nullable
    String model;
    /**
     * Name of the primary owner.
     */
    @SerializedName("PrimaryOwnerName")
    @Nullable
    String primaryOwnerName;
    /**
     * Contact information for the primary owner.
     */
    @SerializedName("PrimaryOwnerContact")
    @Nullable
    String primaryOwnerContact;
    /**
     * List of roles the system performs in the environment (editable).
     */
    @SerializedName("Roles")
    @Nullable
    List<String> roles;
    /**
     * Chassis or enclosure SKU number (from SMBIOS).
     */
    @SerializedName("ChassisSKUNumber")
    @Nullable
    String chassisSKUNumber;
    /**
     * SKU/Product ID for the system configuration.
     */
    @SerializedName("SystemSKUNumber")
    @Nullable
    String systemSKUNumber;
    /**
     * Family of the computer (SMBIOS Family field). May be unsupported on older OS versions.
     */
    @SerializedName("SystemFamily")
    @Nullable
    String systemFamily;
    /**
     * System architecture description
     * <p>Possible Values: </p>
     * <ul>
     *   <li>"x64-based PC"</li>
     *   <li>"X86-based PC"</li>
     *   <li>"MIPS-based PC"</li>
     *   <li>"Alpha-based PC"</li>
     *   <li>"Power PC"</li>
     *   <li>"SH-x PC"</li>
     *   <li>"StrongARM PC"</li>
     *   <li>"64-bit Intel PC"</li>
     *   <li>"64-bit Alpha PC"</li>
     *   <li>"Unknown"</li>
     *   <li>"X86-Nec98 PC"</li>
     * </ul>
     */
    @SerializedName("SystemType")
    @Nullable
    String systemType;
    /**
     * Currently logged-on user. In Terminal Services scenarios, this is the console user.
     */
    @SerializedName("UserName")
    @Nullable
    String userName;
    /**
     * Name of the workgroup or domain (if PartOfDomain==false this is a workgroup name).
     */
    @SerializedName("Workgroup")
    @Nullable
    String workgroup;
    /**
     * OEM-defined strings
     */
    @SerializedName("OEMStringArray")
    @Nullable
    List<String> oemStringArray;
    /**
     * Number of physical processors installed (enabled).
     */
    @SerializedName("NumberOfProcessors")
    @Nullable
    Long numberOfProcessors;
    /**
     * Number of logical processors available (includes hyperthreading logical CPUs).
     */
    @SerializedName("NumberOfLogicalProcessors")
    @Nullable
    Long numberOfLogicalProcessors;
    /**
     * Total size of physical memory in bytes.
     * Note: under some circumstances this may not be accurate (BIOS reservation). For accurate module-by-module capacity,
     * query the equivalent method(s) in {@link Win32PhysicalMemory}
     */
    @SerializedName("TotalPhysicalMemory")
    @Nullable
    BigInteger totalPhysicalMemory;
    /**
     * If true, the system manages the page file automatically.
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("AutomaticManagedPagefile")
    @Nullable
    Boolean automaticManagedPagefile;
    /**
     * If true, an infrared (IR) port exists on the computer system.
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("InfraredSupported")
    @Nullable
    Boolean infraredSupported;
    /**
     * If true, network server mode is enabled (system behaves as a server).
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("NetworkServerModeEnabled")
    @Nullable
    Boolean networkServerModeEnabled;


    // Uncategorized
    /**
     * If true, a hypervisor is present on the system.
     * Note: not supported before Windows 8 / Windows Server 2012 on older OSes.
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("HypervisorPresent")
    @Nullable
    Boolean hypervisorPresent;
    /**
     * Thermal state of the system when last booted.
     * <p>Possible values:</p>
     * <ul>
     *   <li>1 — Other</li>
     *   <li>2 — Unknown</li>
     *   <li>3 — Safe</li>
     *   <li>4 — Warning</li>
     *   <li>5 — Critical</li>
     *   <li>6 — Non-recoverable</li>
     * </ul>
     */
    @SerializedName("ThermalState")
    @Nullable
    Integer thermalState;
    /**
     * Amount of time the system is offset from UTC, in minutes.
     * Example: for UTC+5:30 (Asia/Kolkata) the value is 330.
     */
    @SerializedName("CurrentTimeZone")
    @Nullable
    Integer currentTimeZone;
    /**
     * If True, the daylight savings mode is ON.
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("DaylightInEffect")
    @Nullable
    Boolean daylightInEffect;

    public @Nullable Boolean hasAutomaticResetBootOption() {
        return automaticResetBootOption;
    }

    public @Nullable Boolean isPowerManagementSupported() {
        return powerManagementSupported;
    }

    public @Nullable Boolean hasAutomaticResetCapability() {
        return automaticResetCapability;
    }

    public @Nullable Boolean isAutomaticManagedPagefile() {
        return automaticManagedPagefile;
    }

    public @Nullable Boolean isInfraredSupported() {
        return infraredSupported;
    }

    public @Nullable Boolean isNetworkServerModeEnabled() {
        return networkServerModeEnabled;
    }

    public @Nullable Boolean isHypervisorPresent() {
        return hypervisorPresent;
    }

    public @Nullable Boolean isDaylightInEffect() {
        return daylightInEffect;
    }

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
