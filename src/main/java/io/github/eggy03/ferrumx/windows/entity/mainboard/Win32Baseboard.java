package io.github.eggy03.ferrumx.windows.entity.mainboard;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a motherboard device on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_Baseboard} WMI class.
 * </p>
 * <p>
 * Instances are inherently thread-safe and may be safely cached or shared across threads.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * Win32Baseboard board = Win32Baseboard.builder()
 *     .manufacturer("ASUS")
 *     .serialNumber("ABC123456")
 *     .build();
 *
 * // Create a modified copy
 * Win32Baseboard updated = board.toBuilder()
 *     .serialNumber("XYZ987654")
 *     .build();
 * }</pre>
 *
 * {@link Win32PortConnector} contains details about ports on this mainboard.
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-baseboard">Win32_Baseboard Documentation</a>
 * @since 3.0.0
 * @author Egg-03
 */

@Value
@Builder(toBuilder = true)
public class Win32Baseboard {

    @SerializedName("Manufacturer")
    @Nullable
    String manufacturer;

    @SerializedName("Model")
    @Nullable
    String model;

    @SerializedName("Product")
    @Nullable
    String product;

    @SerializedName("SerialNumber")
    @Nullable
    String serialNumber;

    @SerializedName("Version")
    @Nullable
    String version;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}