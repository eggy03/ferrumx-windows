package io.github.eggy03.ferrumx.windows.entity.mainboard;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a BIOS entity on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_BIOS} WMI class.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * Win32Bios bios = Win32Bios.builder()
 *     .name("BIOS Name")
 *     .version("1.2.3")
 *     .build();
 *
 * // Create a modified copy
 * Win32Bios updated = bios.toBuilder()
 *     .version("1.2.4")
 *     .build();
 * }</pre>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-bios">Win32_BIOS</a>
 * @since 3.0.0
 * @author Egg-03
 */

@Value
@Builder(toBuilder = true)
public class Win32Bios {

    /**
     * The BIOS name.
     */
    @SerializedName("Name")
    @Nullable
    String name;

    /**
     * Short description of the BIOS.
     */
    @SerializedName("Caption")
    @Nullable
    String caption;

    /**
     * Manufacturer of the BIOS.
     */
    @SerializedName("Manufacturer")
    @Nullable
    String manufacturer;

    /**
     * BIOS release date in UTC format (YYYYMMDDHHMMSS.MMMMMM±OOO).
     */
    @SerializedName("ReleaseDate")
    @Nullable
    String releaseDate;

    /**
     * If true, the SMBIOS is available on this computer system.
     * <p>
     * Qualifiers: MappingStrings ("SMBIOS|CSMBios|Init")
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("SMBIOSPresent")
    @Nullable
    Boolean smbiosPresent;
    public @Nullable Boolean isSMBIOSPresent(){return smbiosPresent;}

    /**
     * Current status of the object.
     * <p>
     * Possible OPERATIONAL values include:
     * <ul>
     *   <li>OK</li>
     *   <li>Degraded</li>
     *   <li>Pred Fail</li>
     * </ul>
     * Possible NON-OPERATIONAL values include:
     * <ul>
     *   <li>Error</li>
     *   <li>Unknown</li>
     *   <li>Starting</li>
     *   <li>Stopping</li>
     *   <li>Service</li>
     *   <li>Stressed</li>
     *   <li>NonRecover</li>
     *   <li>No Contact</li>
     *   <li>Lost Comm</li>
     * </ul>
     */
    @SerializedName("Status")
    @Nullable
    String status;

    /**
     * Version of the BIOS. This string is created by the BIOS manufacturer.
     */
    @SerializedName("Version")
    @Nullable
    String version;

    /**
     * Name of the current BIOS language.
     */
    @SerializedName("CurrentLanguage")
    @Nullable
    String currentLanguage;

    /**
     * BIOS version as reported by SMBIOS.
     */
    @SerializedName("SMBIOSBIOSVersion")
    @Nullable
    String smbiosBiosVersion;

    /**
     * If TRUE, this is the primary BIOS of the computer system.
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("PrimaryBIOS")
    @Nullable
    Boolean primaryBios;
    public @Nullable Boolean isPrimaryBios(){return primaryBios;}

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