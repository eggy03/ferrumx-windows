package io.github.eggy03.ferrumx.windows.entity.system;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

/**
 * Immutable representation of a process in a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_Process} WMI class.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
 * </p>
 *
 * <h2>Usage examples</h2>
 * <pre>{@code
 * // Build a new Process instance
 * Win32Process process = Win32Process.builder()
 *     .processId(19845)
 *     .name("svchost.exe")
 *     .priority(8)
 *     .build();
 *
 * // Create a modified copy using the builder
 * Win32Process updated = process.toBuilder()
 *     .priority(1)
 *     .build();
 *
 * }</pre>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-process">Win32_Process Documentation</a>
 * @since 3.0.0
 * @author Egg-03
 */
@Value
@Builder(toBuilder = true)
public class Win32Process {

    @SerializedName("ProcessId")
    @Nullable
    Long processId;

    @SerializedName("SessionId")
    @Nullable
    Long sessionId;

    @SerializedName("Name")
    @Nullable
    String name;

    @SerializedName("Caption")
    @Nullable
    String caption;

    @SerializedName("Description")
    @Nullable
    String description;

    @SerializedName("ExecutablePath")
    @Nullable
    String executablePath;

    @SerializedName("ExecutionState")
    @Nullable
    Integer executionState;

    @SerializedName("Handle")
    @Nullable
    String handle;

    @SerializedName("HandleCount")
    @Nullable
    Long handleCount;

    @SerializedName("Priority")
    @Nullable
    Long priority;

    @SerializedName("ThreadCount")
    @Nullable
    Long threadCount;

    @SerializedName("KernelModeTime")
    @Nullable
    BigInteger kernelModeTime;

    @SerializedName("UserModeTime")
    @Nullable
    BigInteger userModeTime;

    @SerializedName("WorkingSetSize")
    @Nullable
    BigInteger workingSetSize;

    @SerializedName("PeakWorkingSetSize")
    @Nullable
    BigInteger peakWorkingSetSize;

    @SerializedName("PrivatePageCount")
    @Nullable
    BigInteger privatePageCount;

    @SerializedName("PageFileUsage")
    @Nullable
    Long pageFileUsage;

    @SerializedName("PeakPageFileUsage")
    @Nullable
    Long peakPageFileUsage;

    @SerializedName("VirtualSize")
    @Nullable
    BigInteger virtualSize;

    @SerializedName("PeakVirtualSize")
    @Nullable
    BigInteger peakVirtualSize;

    @SerializedName("CreationDate")
    @Nullable
    String creationDate;

    @SerializedName("TerminationDate")
    @Nullable
    String terminationDate;

    @Override
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
