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
 * @author Egg-03
 */

@Value
@Builder(toBuilder = true)
public class Win32OperatingSystem {

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

    @Getter(AccessLevel.NONE)
    @SerializedName("Distributed")
    @Nullable
    Boolean distributed;
    public @Nullable Boolean isDistributed() {return distributed;}

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

    @Getter(AccessLevel.NONE)
    @SerializedName("PortableOperatingSystem")
    @Nullable
    Boolean portableOperatingSystem;
    public @Nullable Boolean isPortable() {return portableOperatingSystem;}

    @Getter(AccessLevel.NONE)
    @SerializedName("Primary")
    @Nullable
    Boolean primary;
    public @Nullable Boolean isPrimary() {return primary;}

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