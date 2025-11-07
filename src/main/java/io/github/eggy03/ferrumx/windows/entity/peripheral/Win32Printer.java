package io.github.eggy03.ferrumx.windows.entity.peripheral;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Immutable representation of a Printing device on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_Printer} WMI class.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * // Build a new instance
 * Win32Printer printer = Win32Printer.builder()
 *     .deviceId("PR1")
 *     .name("Primary Printer")
 *     .isShared(true)
 *     .shareName("Shared Primary Printer")
 *     .build();
 *
 * // Modify using toBuilder()
 * Win32Printer updated = printer.toBuilder()
 *     .isShared(false)
 *     .shareName(null)
 *     .build();
 * }</pre>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-printer">Win32_Printer Documentation</a>
 * @since 3.0.0
 * @author Egg-03
 */
@Value
@Builder(toBuilder = true)
public class Win32Printer {

    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    @SerializedName("Name")
    @Nullable
    String name;

    @SerializedName("PNPDeviceID")
    @Nullable
    String pnpDeviceId;

    @SerializedName("Capabilities")
    @Nullable
    List<Integer> capabilities;

    @SerializedName("CapabilityDescriptions")
    @Nullable
    List<String> capabilityDescriptions;

    @SerializedName("HorizontalResolution")
    @Nullable
    Long horizontalResolution;

    @SerializedName("VerticalResolution")
    @Nullable
    Long verticalResolution;

    @SerializedName("PaperSizesSupported")
    @Nullable
    List<Integer> paperSizesSupported;

    @SerializedName("PrinterPaperNames")
    @Nullable
    List<String> printerPaperNames;

    @SerializedName("PrinterState")
    @Nullable
    Integer printerState;

    @SerializedName("PrintJobDataType")
    @Nullable
    String printJobDataType;

    @SerializedName("PrintProcessor")
    @Nullable
    String printProcessor;

    @SerializedName("DriverName")
    @Nullable
    String driverName;

    @Getter(AccessLevel.NONE)
    @SerializedName("Shared")
    @Nullable
    Boolean shared;
    public @Nullable Boolean isShared() {return shared;}

    @SerializedName("ShareName")
    @Nullable
    String shareName;

    @Getter(AccessLevel.NONE)
    @SerializedName("SpoolEnabled")
    @Nullable
    Boolean spoolEnabled;
    public @Nullable Boolean hasSpoolEnabled() {return spoolEnabled;}

    @Getter(AccessLevel.NONE)
    @SerializedName("Hidden")
    @Nullable
    Boolean hidden;
    public @Nullable Boolean isHidden() {return hidden;}

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}