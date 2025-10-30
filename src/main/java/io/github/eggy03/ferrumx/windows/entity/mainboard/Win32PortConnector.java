package io.github.eggy03.ferrumx.windows.entity.mainboard;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a motherboard port on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_PortConnector} WMI class.
 * </p>
 * <p>
 * Instances are inherently thread-safe and may be safely cached or shared across threads.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * Win32PortConnector port = Win32PortConnector.builder()
 *     .externalReferenceDesignator("USB3_0")
 *     .build();
 *
 * // Create a modified copy
 * Win32PortConnector updated = port.toBuilder()
 *     .externalReferenceDesignator("USB3_1")
 *     .build();
 * }</pre>
 *
 * {@link Win32Baseboard} contains the details of the motherboard this port belongs to.
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-portconnector">Win32_PortConnector Documentation</a>
 * @since 3.0.0
 * @author Egg-03
 */

@Value
@Builder(toBuilder = true)
public class Win32PortConnector {

    @SerializedName("Tag")
    @Nullable
    String tag;

    @SerializedName("ExternalReferenceDesignator")
    @Nullable
    String externalReferenceDesignator;

    @SerializedName("InternalReferenceDesignator")
    @Nullable
    String internalReferenceDesignator;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}