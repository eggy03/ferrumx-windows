package io.github.eggy03.ferrumx.windows.entity.network;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.ferrumx.windows.constant.CimQuery;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of the association between a {@link NetworkAdapter} and it's {@link NetworkAdapterConfiguration}.
 * <p>
 * Fields <b>indirectly</b> correspond to properties retrieved from the {@code Win32_NetworkAdapterSetting} WMI class
 * and represent an association between {@code Win32_NetworkAdapter} and {@code Win32_NetworkAdapterConfiguration}.
 * </p>
 * <p>Links {@link NetworkAdapter} with {@link NetworkAdapterConfiguration} via their device IDs and indexes respectively</p>
 * <p>This class has the following two fields:</p>
 * <ul>
 *     <li>{@code networkAdapterDeviceId} - contains the value of the {@code deviceId} field of {@link NetworkAdapter}</li>
 *     <li>{@code networkAdapterConfigurationIndex} - contains the value of the {@code index} field of {@link NetworkAdapterConfiguration}</li>
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
 *     {@link CimQuery#NETWORK_ADAPTER_SETTING_QUERY} constructs a custom {@code PSObject}
 *     that maps {@code Element.DeviceID} to {@code networkAdapterDeviceId} and {@code Setting.Index} to {@code networkAdapterConfigurationIndex}
 *     and the resulting JSON returned is deserialized into this entity class.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new NetworkAdapterSetting instance
 * NetworkAdapterSetting nas = NetworkAdapterSetting.builder()
 *     .networkAdapterDeviceId("1")
 *     .networkAdapterConfigurationIndex(1)
 *     .build();
 *
 * // Create a modified copy using the builder
 * NetworkAdapterSetting updated = nas.toBuilder()
 *     .networkAdapterConfigurationIndex(2)
 *     .build();
 *
 * }</pre>
 *
 * <p>See {@link NetworkAdapter} for adapter info.</p>
 * <p>See {@link NetworkAdapterConfiguration} for related adapter config info.</p>
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-networkadaptersetting">Win32_NetworkAdapterSetting</a>
 * @since 2.3.0
 * @author Egg-03
 */
@Value
@Builder(toBuilder = true)
public class NetworkAdapterSetting {

    @SerializedName("NetworkAdapterDeviceID")
    @Nullable
    String networkAdapterDeviceId;

    @SerializedName("NetworkAdapterConfigurationIndex")
    @Nullable
    Integer networkAdapterConfigurationIndex;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
