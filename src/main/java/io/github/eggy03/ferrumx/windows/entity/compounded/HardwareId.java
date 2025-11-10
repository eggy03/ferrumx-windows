/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.compounded;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a hardware identity (HWID) in a Windows system.
 * <p>
 * This entity corresponds to the deserialized output of a combination of the following class fields:
 * </p>
 * <ul>
 *     <li>{@code Win32_ComputerSystemProduct.UUID} - subject to change with OS re-installation</li>
 *     <li>{@code Win32_DiskDrive.SerialNumber} - serial numbers of physical disk(s) in the system (excluding USB drives)</li>
 *     <li>{@code Win32_Processor.ProcessorID} - the ID of your physical processor(s)</li>
 * </ul>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 * <h2>ID generation</h2>
 * <p>
 * The associated PowerShell script performs the following operations:
 * </p>
 * <ol>
 *   <li>Retrieves the combined IDs from the aforementioned classes</li>
 *   <li>Trims whitespaces and filters out <code>null</code> or empty values.</li>
 *   <li>Sorts and uppercases all identifiers.</li>
 *   <li>Joins them using a <code>|</code> delimiter to form {@link #rawHWID}.</li>
 *   <li>Computes the SHA-256 hash of this concatenated string.</li>
 *   <li>Formats the hash into a <code>8-4-4-4-12-16-16</code> grouped representation stored in {@link #hashHWID}.</li>
 * </ol>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * HardwareId hwid = HardwareId.builder()
 *     .rawHWID("UUID|DISK1|CPU1")
 *     .hashHWID("03560274-043C-05B7-4C06-C80700080009")
 *     .build();
 * }</pre>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-computersystemproduct">Win32_ComputerSystemProduct</a>
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-diskdrive">Win32_DiskDrive</a>
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-processor">Win32_Processor</a>
 *
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@Value
@Builder(toBuilder = true)
public class HardwareId {

    @SerializedName("HWIDRaw")
    @Nullable
    String rawHWID;

    @SerializedName("HWIDHash")
    @Nullable
    String hashHWID;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
