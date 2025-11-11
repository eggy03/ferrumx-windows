/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.compounded;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import io.github.eggy03.ferrumx.windows.entity.network.Win32NetworkAdapter;
import io.github.eggy03.ferrumx.windows.entity.network.Win32NetworkAdapterConfiguration;
import io.github.eggy03.ferrumx.windows.entity.network.Win32NetworkAdapterSetting;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Immutable representation of a {@link Win32NetworkAdapter} and its associated
 * {@code 1:N} relationship with {@link Win32NetworkAdapterConfiguration} in a Windows system.
 * <p>
 * Each instance represents a single network adapter identified by {@link #deviceId},
 * and maintains a one-to-many mapping with its corresponding network configuration objects.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 * <p>
 * This class is purely a convenience class designed to eliminate the need for using
 * {@link Win32NetworkAdapterSetting} when fetching a relation between {@link Win32NetworkAdapter}
 * and {@link Win32NetworkAdapterConfiguration} as it directly stores all instances of configuration
 * for a particular adapter
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * Win32NetworkAdapterToConfiguration adapterInfo = Win32NetworkAdapterToConfiguration.builder()
 *     .deviceId("1")
 *     .adapter(networkAdapter)
 *     .configurationList(configurations)
 *     .build();
 * }</pre>
 *
 * @see Win32NetworkAdapter
 * @see Win32NetworkAdapterConfiguration
 * @see Win32NetworkAdapterSetting
 *
 * @since 3.0.0
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */

@Value
@Builder(toBuilder = true)
public class Win32NetworkAdapterToConfiguration {

    /**
     * The unique identifier for the {@link Win32NetworkAdapter} instance.
     * <p>
     * Typically corresponds to the adapter’s {@code DeviceID} as defined by Windows Management Instrumentation (WMI).
     * </p>
     */
    @SerializedName("DeviceID")
    @Nullable
    String deviceId;

    /**
     * The {@link Win32NetworkAdapter} entity associated with the {@link #deviceId}.
     * <p>
     * Represents the physical or virtual network adapter that owns one or more configuration objects.
     * </p>
     */
    @SerializedName("Adapter")
    @Nullable
    Win32NetworkAdapter adapter;

    /**
     * A list of {@link Win32NetworkAdapterConfiguration} entities associated with the {@link #deviceId}.
     * <p>
     * Represents one or more configuration settings applied to the {@link #adapter},
     * </p>
     */
    @SerializedName("Configurations")
    @Nullable
    List<Win32NetworkAdapterConfiguration> configurationList;

    /**
     * Retrieves the entity in a JSON pretty-print formatted string
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
