/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.storage;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

/**
 * Immutable representation of a <b>Logical</b> disk volume on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_LogicalDisk} WMI class.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new LogicalDisk instance
 * Win32LogicalDisk lDisk = Win32LogicalDisk.builder()
 *     .deviceId("C:")
 *     .driveType(3)
 *     .fileSystem("NTFS")
 *     .size(1000204886016L)
 *     .build();
 *
 * // Create a modified copy
 * Win32LogicalDisk updatedDisk = drive.toBuilder()
 *     .size(2000409772032L)
 *     .fileSystem("ReFS")
 *     .build();
 *
 * }</pre>
 *
 * <p>See {@link Win32DiskDrive} for information about physical disks in the system.</p>
 * <p>See {@link Win32DiskPartition} for information about partitions in a physical disk.</p>
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-logicaldisk">Win32_LogicalDisk Documentation</a>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@Value
@Builder(toBuilder = true)
public class Win32LogicalDisk {

    /**
     * Unique identifier of the logical disk from other devices on the system.
     * Appears as the drive letter assigned to the partition in the physical disk
     * Example: {@code "C:"}
     */
    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    /**
     * Description of the logical disk object.
     */
    @SerializedName("Description")
    @Nullable
    String description;

    /**
     * Numeric value that corresponds to the type of disk drive this logical disk represents.
     * <ul>
     *     <li>0 – Unknown</li>
     *     <li>1 – No Root Directory</li>
     *     <li>2 – Removable Disk</li>
     *     <li>3 – Local Disk</li>
     *     <li>4 – Network Drive</li>
     *     <li>5 – Compact Disc</li>
     *     <li>6 – RAM Disk</li>
     * </ul>
     * <p>Data type: uint32</p>
     */
    @SerializedName("DriveType")
    @Nullable
    Long driveType;

    /**
     * Type of media currently present in the logical drive.
     * Value corresponds to a member of the MEDIA_TYPE enumeration defined in {@code Winioctl.h.}
     * Visit the microsoft documentation stated at the class level to know about the possible values.
     */
    @SerializedName("MediaType")
    @Nullable
    Long mediaType;

    /**
     * File system on the logical disk.
     * Example: {@code "NTFS"}, {@code "FAT32"}, {@code "ReFS"}
     */
    @SerializedName("FileSystem")
    @Nullable
    String fileSystem;

    /**
     * Size of the disk drive in bytes.
     */
    @SerializedName("Size")
    @Nullable
    BigInteger size;

    /**
     * Free space, in bytes, available on the logical disk.
     */
    @SerializedName("FreeSpace")
    @Nullable
    BigInteger freeSpace;

    /**
     * Indicates if the logical volume exists as a single compressed entity (e.g., DoubleSpace).
     * If file-based compression is supported (e.g., NTFS), this value is {@code false}.
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("Compressed")
    @Nullable
    Boolean compressed;
    public @Nullable Boolean isCompressed() {return compressed;}

    /**
     * Indicates whether the logical disk supports file-based compression (e.g., NTFS).
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("SupportsFileBasedCompression")
    @Nullable
    Boolean supportsFileBasedCompression;
    public @Nullable Boolean supportsFileBasedCompression() {return supportsFileBasedCompression;}

    /**
     * Indicates whether this volume supports disk quotas.
     */
    @Getter(AccessLevel.NONE)
    @SerializedName("SupportsDiskQuotas")
    @Nullable
    Boolean supportsDiskQuotas;
    public @Nullable Boolean supportsDiskQuotas() {return supportsDiskQuotas;}

    /**
     * Volume name of the logical disk.
     * Example: {@code "Local Disk"}
     */
    @SerializedName("VolumeName")
    @Nullable
    String volumeName;

    /**
     * Volume serial number of the logical disk.
     */
    @SerializedName("VolumeSerialNumber")
    @Nullable
    String volumeSerialNumber;

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
