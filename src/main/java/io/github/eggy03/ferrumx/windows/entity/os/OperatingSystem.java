package io.github.eggy03.ferrumx.windows.entity.os;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Immutable representation of the Windows Operating System.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_OperatingSystem} WMI class.
 * </p>
 * <p>
 * Instances are thread-safe and may be safely cached or shared across threads.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new OperatingSystem instance
 * OperatingSystem os = OperatingSystem.builder()
 *     .name("Windows 11 Pro")
 *     .version("22H2")
 *     .numberOfUsers(1)
 *     .osArchitecture("64-bit")
 *     .build();
 *
 * // Create a modified copy using the builder
 * OperatingSystem updated = os.toBuilder()
 *     .numberOfUsers(5)
 *     .build();
 *
 * }</pre>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-operatingsystem">Win32_OperatingSystem</a>
 * @since 2.0.0
 * @author Egg-03
 */

@Value
@Builder(toBuilder = true)
public class OperatingSystem {

    @SerializedName("Name")
    @Nullable
    String name;

    @SerializedName("Caption")
    @Nullable
    String caption;

    @SerializedName("InstallDate")
    @Nullable
    String installDate;

    @SerializedName("CSName")
    @Nullable
    String csName;

    @SerializedName("LastBootUpTime")
    @Nullable
    String lastBootUpTime;

    @SerializedName("LocalDateTime")
    @Nullable
    String localDateTime;

    @SerializedName("Distributed")
    @Nullable
    Boolean distributed;

    @SerializedName("NumberOfUsers")
    @Nullable
    Integer numberOfUsers;

    @SerializedName("Version")
    @Nullable
    String version;

    @SerializedName("BootDevice")
    @Nullable
    String bootDevice;

    @SerializedName("BuildNumber")
    @Nullable
    String buildNumber;

    @SerializedName("BuildType")
    @Nullable
    String buildType;

    @SerializedName("Manufacturer")
    @Nullable
    String manufacturer;

    @SerializedName("OSArchitecture")
    @Nullable
    String osArchitecture;

    @SerializedName("MUILanguages")
    @Nullable
    List<String> muiLanguages;

    @SerializedName("PortableOperatingSystem")
    @Nullable
    Boolean portableOperatingSystem;

    @SerializedName("Primary")
    @Nullable
    Boolean primary;

    @SerializedName("RegisteredUser")
    @Nullable
    String registeredUser;

    @SerializedName("SerialNumber")
    @Nullable
    String serialNumber;

    @SerializedName("ServicePackMajorVersion")
    @Nullable
    Integer servicePackMajorVersion;

    @SerializedName("ServicePackMinorVersion")
    @Nullable
    Integer servicePackMinorVersion;

    @SerializedName("SystemDirectory")
    @Nullable
    String systemDirectory;

    @SerializedName("SystemDrive")
    @Nullable
    String systemDrive;

    @SerializedName("WindowsDirectory")
    @Nullable
    String windowsDirectory;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}