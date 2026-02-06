/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.network;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.ferrumx.windows.constant.namespace.Cimv2Namespace;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of the association between a {@link Win32NetworkAdapter} and it's {@link Win32NetworkAdapterConfiguration}.
 * <p>
 * Fields <b>indirectly</b> correspond to properties retrieved from the {@code Win32_NetworkAdapterSetting} WMI class
 * and represent an association between {@code Win32_NetworkAdapter} and {@code Win32_NetworkAdapterConfiguration}.
 * </p>
 * <p>Links {@link Win32NetworkAdapter} with {@link Win32NetworkAdapterConfiguration} via their device IDs and indexes respectively</p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 * <p>This class has the following two fields:</p>
 * <ul>
 *     <li>{@code networkAdapterDeviceId} - contains the value of the {@code deviceId} field of {@link Win32NetworkAdapter}</li>
 *     <li>{@code networkAdapterConfigurationIndex} - contains the value of the {@code index} field of {@link Win32NetworkAdapterConfiguration}</li>
 * </ul>
 *
 * <p>
 *     <b>Extra Notes:</b> The {@code Win32_NetworkAdapterSetting} WMI class itself does not directly expose
 *     the {@code DeviceID} (from {@code Win32_NetworkAdapter}) or the {@code Index}
 *     (from {@code Win32_NetworkAdapterConfiguration}) as standalone properties.
 *     Instead, these values are nested within its references: {@code Element} and {@code Setting}, respectively.
 * </p>
 * <p>
 *     To simplify data mapping, the PowerShell query defined in
 *     {@link Cimv2Namespace#WIN32_NETWORK_ADAPTER_SETTING_QUERY} constructs a custom {@code PSObject}
 *     that maps {@code Element.DeviceID} to {@code networkAdapterDeviceId} and {@code Setting.Index} to {@code networkAdapterConfigurationIndex}
 *     and the resulting JSON returned is deserialized into this entity class.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new NetworkAdapterSetting instance
 * Win32NetworkAdapterSetting nas = Win32NetworkAdapterSetting.builder()
 *     .networkAdapterDeviceId("1")
 *     .networkAdapterConfigurationIndex(1)
 *     .build();
 *
 * // Create a modified copy using the builder
 * Win32NetworkAdapterSetting updated = nas.toBuilder()
 *     .networkAdapterConfigurationIndex(2)
 *     .build();
 *
 * }</pre>
 *
 * <p>See {@link Win32NetworkAdapter} for adapter info.</p>
 * <p>See {@link Win32NetworkAdapterConfiguration} for related adapter config info.</p>
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-networkadaptersetting">Win32_NetworkAdapterSetting Documentation</a>
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@Value
@Builder(toBuilder = true)
public class Win32NetworkAdapterSetting {

    /**
     * The {@code deviceId} field value of {@link Win32NetworkAdapter}
     */
    @SerializedName("NetworkAdapterDeviceID")
    @Nullable
    String networkAdapterDeviceId;

    /**
     * The {@code index} field value of {@link Win32NetworkAdapterConfiguration}
     */
    @SerializedName("NetworkAdapterConfigurationIndex")
    @Nullable
    Integer networkAdapterConfigurationIndex;

    /**
     * Retrieves the entity in a JSON pretty-print formatted string
     * @return the {@link String} value of the object in JSON pretty-print format
     */
    @Override
    @NotNull
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
