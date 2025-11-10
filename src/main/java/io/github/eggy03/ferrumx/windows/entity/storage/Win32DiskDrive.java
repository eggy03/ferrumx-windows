/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.storage;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.List;

/**
 * Immutable representation of a <b>Physical</b> disk on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_DiskDrive} WMI class.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new instance
 * Win32DiskDrive drive = Win32DiskDrive.builder()
 *     .deviceId("\\\\.\\PHYSICALDRIVE0")
 *     .caption("Samsung SSD 970 EVO")
 *     .model("MZ-V7E1T0")
 *     .size(1000204886016L)
 *     .firmwareRevision("2B2QEXM7")
 *     .serialNumber("S4EVNX0M123456")
 *     .partitions(3)
 *     .status("OK")
 *     .interfaceType("NVMe")
 *     .pnpDeviceId("PCI\\VEN_144D&DEV_A808&SUBSYS_0A0E144D&REV_01\\4&1A2B3C4D&0&000000")
 *     .build();
 *
 * // Create a modified copy
 * Win32DiskDrive updatedDrive = drive.toBuilder()
 *     .size(2000409772032L)
 *     .build();
 *
 * }</pre>
 *
 * <p>See {@link Win32DiskPartition} for information about partitions on this disk.</p>
 * <p>See {@link Win32LogicalDisk} for information about the logical volumes on this disk.</p>
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-diskdrive">Win32_DiskDrive Documentation</a>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */

@Value
@Builder(toBuilder = true)
public class Win32DiskDrive {

    /**
     * Unique identifier of the disk drive with other devices on the system.
     */
    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    /**
     * Short description of the object.
     */
    @SerializedName("Caption")
    @Nullable
    String caption;

    /**
     * Manufacturer’s model number of the disk drive.
     */
    @SerializedName("Model")
    @Nullable
    String model;

    /**
     * Size of the disk drive, calculated by multiplying the total number of cylinders,
     * tracks in each cylinder, sectors in each track, and bytes in each sector.
     */
    @SerializedName("Size")
    @Nullable
    BigInteger size;

    /**
     * Revision of the disk drive firmware assigned by the manufacturer.
     */
    @SerializedName("FirmwareRevision")
    @Nullable
    String firmwareRevision;

    /**
     * Number allocated by the manufacturer to identify the physical media.
     */
    @SerializedName("SerialNumber")
    @Nullable
    String serialNumber;

    /**
     * Number of partitions on this physical disk drive recognized by the operating system.
     */
    @SerializedName("Partitions")
    @Nullable
    Long partitions;

    /**
     * Current operational status of the physical disk.
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
     * Interface type of the physical disk drive (e.g., SCSI, IDE, USB, NVMe).
     */
    @SerializedName("InterfaceType")
    @Nullable
    String interfaceType;

    /**
     * Windows Plug and Play device identifier of the logical device.
     */
    @SerializedName("PNPDeviceID")
    @Nullable
    String pnpDeviceId;

    /**
     * Array of capabilities of the media access device.
     * <p>Possible Values: </p>
     * <ul>
     *     <li>1 - Other</li>
     *     <li>2 - Sequential Access</li>
     *     <li>3 - Random Access</li>
     *     <li>4 - Supports Writing</li>
     *     <li>5 - Encryption</li>
     *     <li>6 - Compression</li>
     *     <li>7 - Supports Removable Media</li>
     *     <li>8 - Manual Cleaning</li>
     *     <li>9 - Automatic Cleaning</li>
     *     <li>10 - S.M.A.R.T Notification</li>
     *     <li>11 - Supports Dual Sided Media</li>
     *     <li>12 - Pre-dismount Eject Not Required</li>
     * </ul>
     */
    @SerializedName("Capabilities")
    @Nullable
    List<Integer> capabilities;

    /**
     * List of more detailed explanations for any of the access device features indicated in the {@link #capabilities} array.
     * Note, each entry of this array is related to the entry in the {@link #capabilities} array that is located at the same index.
     */
    @SerializedName("CapabilityDescriptions")
    @Nullable
    List<String> capabilityDescriptions;

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