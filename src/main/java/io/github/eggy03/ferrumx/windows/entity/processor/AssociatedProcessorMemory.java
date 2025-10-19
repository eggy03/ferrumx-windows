package io.github.eggy03.ferrumx.windows.entity.processor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of a {@link Processor} association with {@link ProcessorCache}.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_AssociatedProcessorMemory} WMI class
 * and represent an association between {@code Win32_Processor} and {@code Win32_CacheMemory}.
 * </p>
 * <p>Links {@link Processor} with {@link ProcessorCache} via their device IDs</p>
 * <p>This class has the following two fields:</p>
 * <ul>
 *     <li>{@code Antecedent} - contains the {@code deviceId} field of {@link ProcessorCache}</li>
 *     <li>{@code Dependent} - contains the {@code deviceId} field of {@link Processor}</li>
 * </ul>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new AssociatedProcessorMemory instance
 * AssociatedProcessorMemory apm = AssociatedProcessorMemory.builder()
 *     .antecedent("Cache Memory 1")
 *     .dependent("CPU0")
 *     .build();
 *
 * // Create a modified copy using the builder
 * AssociatedProcessorMemory updated = apm.toBuilder()
 *     .antecedent("Cache Memory 2")
 *     .build();
 *
 * }</pre>
 *
 * {@link Processor} for related CPU information.
 * {@link ProcessorCache} for related CPU Cache information.
 * @see <a href="https://powershell.one/wmi/root/cimv2/win32_associatedprocessormemory">Win32_AssociatedProcessorMemory</a>
 * @since 2.3.0
 */
@Value
@Builder (toBuilder = true)
public class AssociatedProcessorMemory {

    @SerializedName("Antecedent")
    @Nullable
    String antecedent;

    @SerializedName("Dependent")
    @Nullable
    String dependent;

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
