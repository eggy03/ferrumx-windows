/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.entity.mainboard;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Immutable representation of a motherboard port on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_PortConnector} WMI class.
 * </p>
 * <p>
 * Instances of this class are thread-safe.
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
 * <p>
 * {@link Win32Baseboard} contains the details of the motherboard this port belongs to.
 *
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-portconnector">Win32_PortConnector Documentation</a>
 * @since 3.0.0
 */

@Value
@Builder(toBuilder = true)
public class Win32PortConnector {

    /**
     * Unique identifier of a port connection on the computer system.
     * <p>
     * Example: "Port Connector 1"
     */
    @SerializedName("Tag")
    @Nullable
    String tag;

    /**
     * External reference designator of the port. External reference designators are identifiers
     * that determine the type and use of the port.
     * Example: "COM1"
     */
    @SerializedName("ExternalReferenceDesignator")
    @Nullable
    String externalReferenceDesignator;

    /**
     * Internal reference designator of the port. Internal reference designators are specific
     * to the manufacturer, and identify the circuit board location or use of the port.
     * Example: "J101"
     */
    @SerializedName("InternalReferenceDesignator")
    @Nullable
    String internalReferenceDesignator;

    /**
     * Function of the port.
     * <p>
     * Possible values include:
     * <ul>
     *   <li>None (0)</li>
     *   <li>Parallel Port XT/AT Compatible (1)</li>
     *   <li>Parallel Port PS/2 (2)</li>
     *   <li>Parallel Port ECP (3)</li>
     *   <li>Parallel Port EPP (4)</li>
     *   <li>Parallel Port ECP/EPP (5)</li>
     *   <li>Serial Port XT/AT Compatible (6)</li>
     *   <li>Serial Port 16450 Compatible (7)</li>
     *   <li>Serial Port 16550 Compatible (8)</li>
     *   <li>Serial Port 16550A Compatible (9)</li>
     *   <li>SCSI Port (10)</li>
     *   <li>MIDI Port (11)</li>
     *   <li>Joy Stick Port (12)</li>
     *   <li>Keyboard Port (13)</li>
     *   <li>Mouse Port (14)</li>
     *   <li>SSA SCSI (15)</li>
     *   <li>USB (16)</li>
     *   <li>FireWire (IEEE P1394) (17)</li>
     *   <li>PCMCIA Type I (18)</li>
     *   <li>PCMCIA Type II (19)</li>
     *   <li>PCMCIA Type III (20)</li>
     *   <li>Cardbus (21)</li>
     *   <li>Access Bus Port (22)</li>
     *   <li>SCSI II (23)</li>
     *   <li>SCSI Wide (24)</li>
     *   <li>PC-98 (25)</li>
     *   <li>PC-98-Hireso (26)</li>
     *   <li>PC-H98 (27)</li>
     *   <li>Video Port (28)</li>
     *   <li>Audio Port (29)</li>
     *   <li>Modem Port (30)</li>
     *   <li>Network Port (31)</li>
     *   <li>8251 Compatible (32)</li>
     *   <li>8251 FIFO Compatible (33)</li>
     * </ul>
     */
    @SerializedName("PortType")
    @Nullable
    Integer portType;

    /**
     * Array of physical attributes of the connector used by this port.
     * <p>
     * Refer to the microsoft documentation provided at the class level for a list of possible values
     */
    @SerializedName("ConnectorType")
    @Nullable
    List<Integer> connectorType;

    /**
     * Retrieves the entity in a JSON pretty-print formatted string
     *
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