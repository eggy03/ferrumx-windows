/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.system;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Immutable representation of the Windows Operating System.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_OperatingSystem} WMI class.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new OperatingSystem instance
 * Win32OperatingSystem os = Win32OperatingSystem.builder()
 *     .name("Windows 11 Pro")
 *     .version("22H2")
 *     .numberOfUsers(1)
 *     .osArchitecture("64-bit")
 *     .build();
 *
 * // Create a modified copy using the builder
 * Win32OperatingSystem updated = os.toBuilder()
 *     .numberOfUsers(5)
 *     .build();
 *
 * }</pre>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-operatingsystem">Win32_OperatingSystem Documentation</a>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */

@Value
@Builder(toBuilder = true)
public class Win32OperatingSystem {

    /**
     * Name of the operating system.
     */
    @SerializedName("Name")
    @Nullable
    String name;

    /**
     * Short textual description (friendly name) of the operating system.
     */
    @SerializedName("Caption")
    @Nullable
    String caption;

    /**
     * Date and time when the operating system was installed.
     */
    @SerializedName("InstallDate")
    @Nullable
    String installDate;

    /**
     * Name of the computer system running the operating system.
     */
    @SerializedName("CSName")
    @Nullable
    String csName;

    /**
     * Date and time when the computer was last booted.
     */
    @SerializedName("LastBootUpTime")
    @Nullable
    String lastBootUpTime;

    /**
     * Current local date and time of the operating system.
     */
    @SerializedName("LocalDateTime")
    @Nullable
    String localDateTime;

    /**
     * Indicates whether this operating system is part of a distributed system.
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("Distributed")
    @Nullable
    Boolean distributed;
    public @Nullable Boolean isDistributed() {return distributed;}

    /**
     * Number of user sessions currently active.
     */
    @SerializedName("NumberOfUsers")
    @Nullable
    Integer numberOfUsers;

    /**
     * Version number of the operating system (for example, "10.0.22621").
     */
    @SerializedName("Version")
    @Nullable
    String version;

    /**
     * Path to the boot device that the operating system uses to start the computer.
     */
    @SerializedName("BootDevice")
    @Nullable
    String bootDevice;

    /**
     * Internal build number of the operating system.
     */
    @SerializedName("BuildNumber")
    @Nullable
    String buildNumber;

    /**
     * Type of build (e.g., "Multiprocessor Free" or "Checked").
     */
    @SerializedName("BuildType")
    @Nullable
    String buildType;

    /**
     * Manufacturer of the operating system (typically "Microsoft Corporation").
     */
    @SerializedName("Manufacturer")
    @Nullable
    String manufacturer;

    /**
     * Architecture of the operating system, such as "32-bit" or "64-bit".
     */
    @SerializedName("OSArchitecture")
    @Nullable
    String osArchitecture;

    /**
     * List of installed user interface languages (MUI language codes).
     */
    @SerializedName("MUILanguages")
    @Nullable
    List<String> muiLanguages;

    /**
     * Indicates whether the operating system is installed on a portable device.
     * <ul>
     *   <li><b>true</b> — The operating system is installed on a portable device (e.g., Windows To Go).</li>
     *   <li><b>false</b> — The operating system is installed on a fixed computer.</li>
     * </ul>
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("PortableOperatingSystem")
    @Nullable
    Boolean portableOperatingSystem;
    public @Nullable Boolean isPortable() {return portableOperatingSystem;}

    /**
     * Indicates whether this is the primary operating system on the computer.
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("Primary")
    @Nullable
    Boolean primary;
    public @Nullable Boolean isPrimary() {return primary;}

    /**
     * Name of the registered user of the operating system.
     */
    @SerializedName("RegisteredUser")
    @Nullable
    String registeredUser;

    /**
     * Operating system serial number or product key identifier.
     */
    @SerializedName("SerialNumber")
    @Nullable
    String serialNumber;

    /**
     * Major version number of the most recent service pack installed.
     */
    @SerializedName("ServicePackMajorVersion")
    @Nullable
    Integer servicePackMajorVersion;

    /**
     * Minor version number of the most recent service pack installed.
     */
    @SerializedName("ServicePackMinorVersion")
    @Nullable
    Integer servicePackMinorVersion;

    /**
     * Full path to the system directory (typically "C:\WINDOWS\system32").
     */
    @SerializedName("SystemDirectory")
    @Nullable
    String systemDirectory;

    /**
     * Drive letter where the operating system is installed (e.g., "C:").
     */
    @SerializedName("SystemDrive")
    @Nullable
    String systemDrive;

    /**
     * Full path to the Windows installation directory (typically "C:\WINDOWS").
     */
    @SerializedName("WindowsDirectory")
    @Nullable
    String windowsDirectory;

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