/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.system;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
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
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 */
@Value
@Builder(toBuilder = true)
public class Win32Process {

    /**
     * Unique identifier of the process.
     */
    @SerializedName("ProcessId")
    @Nullable
    Long processId;

    /**
     * Identifier of the session under which this process is running.
     */
    @SerializedName("SessionId")
    @Nullable
    Long sessionId;

    /**
     * Name of the executable file responsible for this process.
     */
    @SerializedName("Name")
    @Nullable
    String name;

    /**
     * Short one-line description of the process.
     */
    @SerializedName("Caption")
    @Nullable
    String caption;

    /**
     * Full description of the process.
     */
    @SerializedName("Description")
    @Nullable
    String description;

    /**
     *  Full path to the executable file of the process.
     */
    @SerializedName("ExecutablePath")
    @Nullable
    String executablePath;

    /**
     * Current execution state of the process.
     * <ul>
     *   <li>0 — Unknown</li>
     *   <li>1 — Other</li>
     *   <li>2 — Ready</li>
     *   <li>3 — Running</li>
     *   <li>4 — Blocked</li>
     *   <li>5 — Suspended Blocked</li>
     *   <li>6 — Suspended Ready</li>
     *   <li>7 — Terminated</li>
     *   <li>8 — Stopped</li>
     *   <li>9 — Growing</li>
     * </ul>
     */
    @SerializedName("ExecutionState")
    @Nullable
    Integer executionState;

    /**
     * Handle of the process (string representation of ProcessId).
     */
    @SerializedName("Handle")
    @Nullable
    String handle;

    /**
     * Number of handles currently open by the process.
     */
    @SerializedName("HandleCount")
    @Nullable
    Long handleCount;

    /**
     * Scheduling priority of the process.
     * <ul>
     *   <li>0 (lowest) to 31 (highest)</li>
     * </ul>
     */
    @SerializedName("Priority")
    @Nullable
    Long priority;

    /**
     * Number of active threads in the process.
     */
    @SerializedName("ThreadCount")
    @Nullable
    Long threadCount;

    /**
     * Time spent by the process in kernel mode (in ms).
     */
    @SerializedName("KernelModeTime")
    @Nullable
    BigInteger kernelModeTime;

    /**
     * Time spent by the process in user mode (in ms).
     */
    @SerializedName("UserModeTime")
    @Nullable
    BigInteger userModeTime;

    /**
     * Current working set size (in bytes) used by the process.
     */
    @SerializedName("WorkingSetSize")
    @Nullable
    BigInteger workingSetSize;

    /**
     * Peak working set size (in KB) of the process.
     */
    @SerializedName("PeakWorkingSetSize")
    @Nullable
    BigInteger peakWorkingSetSize;

    /**
     * Current number of private memory pages used by the process.
     */
    @SerializedName("PrivatePageCount")
    @Nullable
    BigInteger privatePageCount;

    /**
     * Current amount of page file usage (kilobytes).
     */
    @SerializedName("PageFileUsage")
    @Nullable
    Long pageFileUsage;

    /**
     * Peak page file usage (kilobytes).
     */
    @SerializedName("PeakPageFileUsage")
    @Nullable
    Long peakPageFileUsage;

    /**
     * Current virtual address space used by the process (bytes).
     */
    @SerializedName("VirtualSize")
    @Nullable
    BigInteger virtualSize;

    /**
     * Peak virtual address space used by the process (bytes).
     */
    @SerializedName("PeakVirtualSize")
    @Nullable
    BigInteger peakVirtualSize;

    /**
     * Date/time when the process was created.
     */
    @SerializedName("CreationDate")
    @Nullable
    String creationDate;

    /**
     * Date/time when the process was terminated (if available).
     */
    @SerializedName("TerminationDate")
    @Nullable
    String terminationDate;

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
