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
 *     <li>{@code deviceId} - contains the value of the {@code deviceId} field of {@link NetworkAdapter}</li>
 *     <li>{@code index} - contains the value of the {@code index} field of {@link NetworkAdapterConfiguration}</li>
 * </ul>
 *
 * <p>
 *     <b>Extra Notes:</b> The {@code Win32_NetworkAdapterSetting} WMI class itself does not directly expose
 *     the {@code DeviceID} (from {@code Win32_NetworkAdapter}) or the {@code Index}
 *     (from {@code Win32_NetworkAdapterConfiguration}) as standalone properties.
 *     Instead, these values are nested within its references: {@code Element} and {@code Setting}.
 * </p>
 * <p>
 *     To simplify data mapping, the PowerShell query defined in
 *     {@link CimQuery#NETWORK_ADAPTER_SETTING_QUERY} constructs a custom {@code PSObject}
 *     that maps {@code Element.DeviceID} to {@code DeviceID} and {@code Setting.Index} to {@code Index}
 *     and the resulting JSON returned is deserialized into this entity class.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new NetworkAdapterSetting instance
 * NetworkAdapterSetting nas = NetworkAdapterSetting.builder()
 *     .deviceId("1")
 *     .index(1)
 *     .build();
 *
 * // Create a modified copy using the builder
 * NetworkAdapterSetting updated = nas.toBuilder()
 *     .index(2)
 *     .build();
 *
 * }</pre>
 *
 * <p>See {@link NetworkAdapter} for adapter info.</p>
 * <p>See {@link NetworkAdapterConfiguration} for related adapter config info.</p>
 * @see <a href="https://powershell.one/wmi/root/cimv2/win32_networkadaptersetting">Win32_NetworkAdapterSetting</a>
 * @since 2.3.0
 * @author Egg-03
 */
@Value
@Builder(toBuilder = true)
public class NetworkAdapterSetting {

    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    @SerializedName("Index")
    @Nullable
    Integer index;

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
