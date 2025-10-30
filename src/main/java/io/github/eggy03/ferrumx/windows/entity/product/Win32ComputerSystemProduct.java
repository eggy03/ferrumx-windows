package io.github.eggy03.ferrumx.windows.entity.product;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a computer product on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_ComputerSystemProduct} WMI class.
 * </p>
 * <p>
 * Instances are thread-safe and may be safely cached or shared across threads.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new instance
 * Win32ComputerSystemProduct product = Win32ComputerSystemProduct.builder()
 *     .caption("Workstation PC")
 *     .description("High-end office workstation")
 *     .identifyingNumber("ID-001")
 *     .name("User-PC")
 *     .skuNumber("PROD-1234")
 *     .vendor("Default Vendor")
 *     .version("1.0")
 *     .uuid("550e8400-e29b-41d4-a716-446655440000")
 *     .build();
 *
 * // Create a modified copy
 * Win32ComputerSystemProduct updated = product.toBuilder()
 *     .skuNumber("PROD-5678")
 *     .build();
 *
 * }</pre>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-computersystemproduct">Win32_ComputerSystemProduct Documentation</a>
 * @since 2.0.0
 * @author Egg-03
 */

@Value
@Builder(toBuilder = true)
public class Win32ComputerSystemProduct {

    @SerializedName("Caption")
    @Nullable
    String caption;

    @SerializedName("Description")
    @Nullable
    String description;

    @SerializedName("IdentifyingNumber")
    @Nullable
    String identifyingNumber;

    @SerializedName("Name")
    @Nullable
    String name;

    @SerializedName("SKUNumber")
    @Nullable
    String skuNumber;

    @SerializedName("Vendor")
    @Nullable
    String vendor;

    @SerializedName("Version")
    @Nullable
    String version;

    @SerializedName("UUID")
    @Nullable
    String uuid;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}