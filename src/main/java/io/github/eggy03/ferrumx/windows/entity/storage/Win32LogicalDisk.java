package io.github.eggy03.ferrumx.windows.entity.storage;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a <b>Logical</b> disk volume on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_LogicalDisk} WMI class.
 * </p>
 * <p>
 * Instances are thread-safe and may be safely cached or shared across threads.
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
 * @author Egg-03
 */
@Value
@Builder(toBuilder = true)
public class Win32LogicalDisk {

    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    @SerializedName("Description")
    @Nullable
    String description;

    @SerializedName("DriveType")
    @Nullable
    Long driveType;

    @SerializedName("MediaType")
    @Nullable
    Long mediaType;

    @SerializedName("FileSystem")
    @Nullable
    String fileSystem;

    @SerializedName("Size")
    @Nullable
    Long size;

    @SerializedName("FreeSpace")
    @Nullable
    Long freeSpace;

    @SerializedName("Compressed")
    @Nullable
    Boolean isCompressed;

    @SerializedName("SupportsFileBasedCompression")
    @Nullable
    Boolean supportsFileBasedCompression;

    @SerializedName("SupportsDiskQuotas")
    @Nullable
    Boolean supportsDiskQuotas;

    @SerializedName("VolumeName")
    @Nullable
    String volumeName;

    @SerializedName("VolumeSerialNumber")
    @Nullable
    String volumeSerialNumber;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }

}
