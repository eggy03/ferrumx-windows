/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package unit.service.display;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.display.Win32DesktopMonitor;
import io.github.eggy03.ferrumx.windows.service.display.Win32DesktopMonitorService;
import io.github.eggy03.ferrumx.windows.utility.TerminalUtility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class Win32DesktopMonitorServiceTest {

    private static Win32DesktopMonitor expectedMonitor1;
    private static Win32DesktopMonitor expectedMonitor2;
    private static String json;
    private Win32DesktopMonitorService service;

    @BeforeAll
    static void setMonitors() {
        expectedMonitor1 = Win32DesktopMonitor.builder()
                .deviceId("MON1")
                .name("Dell U2720Q")
                .pnpDeviceId("DISPLAY\\\\DELA0B1\\\\5&12345&0&UID4352")
                .status("OK")
                .monitorManufacturer("Dell Inc.")
                .monitorType("LCD")
                .pixelsPerXLogicalInch(96)
                .pixelsPerYLogicalInch(96)
                .build();

        expectedMonitor2 = Win32DesktopMonitor.builder()
                .deviceId("MON2")
                .name("LG UltraGear 27GL850")
                .pnpDeviceId("DISPLAY\\\\LGD1234\\\\5&67890&0&UID9832")
                .status("OK")
                .monitorManufacturer("LG Electronics")
                .monitorType("LED")
                .pixelsPerXLogicalInch(110)
                .pixelsPerYLogicalInch(110)
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonArray monitors = new JsonArray();

        JsonObject mon1 = new JsonObject();
        mon1.addProperty("DeviceID", "MON1");
        mon1.addProperty("Name", "Dell U2720Q");
        mon1.addProperty("PNPDeviceID", "DISPLAY\\\\DELA0B1\\\\5&12345&0&UID4352");
        mon1.addProperty("Status", "OK");
        mon1.addProperty("MonitorManufacturer", "Dell Inc.");
        mon1.addProperty("MonitorType", "LCD");
        mon1.addProperty("PixelsPerXLogicalInch", 96);
        mon1.addProperty("PixelsPerYLogicalInch", 96);

        JsonObject mon2 = new JsonObject();
        mon2.addProperty("DeviceID", "MON2");
        mon2.addProperty("Name", "LG UltraGear 27GL850");
        mon2.addProperty("PNPDeviceID", "DISPLAY\\\\LGD1234\\\\5&67890&0&UID9832");
        mon2.addProperty("Status", "OK");
        mon2.addProperty("MonitorManufacturer", "LG Electronics");
        mon2.addProperty("MonitorType", "LED");
        mon2.addProperty("PixelsPerXLogicalInch", 110);
        mon2.addProperty("PixelsPerYLogicalInch", 110);

        monitors.add(mon1);
        monitors.add(mon2);

        json = new GsonBuilder().serializeNulls().create().toJson(monitors);
    }

    @BeforeEach
    void setUp() {
        service = new Win32DesktopMonitorService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockedResponse = mock(PowerShellResponse.class);
        when(mockedResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMockedStatic = mockStatic(PowerShell.class)) {
            powerShellMockedStatic.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockedResponse);

            List<Win32DesktopMonitor> monitors = service.get();
            assertEquals(2, monitors.size());

            assertThat(monitors.get(0)).usingRecursiveComparison().isEqualTo(expectedMonitor1);
            assertThat(monitors.get(1)).usingRecursiveComparison().isEqualTo(expectedMonitor2);
        }
    }

    @Test
    void test_get_empty() {

        PowerShellResponse mockedResponse = mock(PowerShellResponse.class);
        when(mockedResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMockedStatic = mockStatic(PowerShell.class)) {
            powerShellMockedStatic.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockedResponse);

            List<Win32DesktopMonitor> monitors = service.get();
            assertTrue(monitors.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {

        PowerShellResponse mockedResponse = mock(PowerShellResponse.class);
        when(mockedResponse.getCommandOutput()).thenReturn("not a valid json");

        try (MockedStatic<PowerShell> powerShellMockedStatic = mockStatic(PowerShell.class)) {
            powerShellMockedStatic.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockedResponse);

            assertThrows(JsonSyntaxException.class, () -> service.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockedResponse = mock(PowerShellResponse.class);
        when(mockedResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeCommand(anyString())).thenReturn(mockedResponse);

            List<Win32DesktopMonitor> monitors = service.get(mockSession);
            assertEquals(2, monitors.size());

            assertThat(monitors.get(0)).usingRecursiveComparison().isEqualTo(expectedMonitor1);
            assertThat(monitors.get(1)).usingRecursiveComparison().isEqualTo(expectedMonitor2);
        }
    }

    @Test
    void test_getWithSession_empty() {

        PowerShellResponse mockedResponse = mock(PowerShellResponse.class);
        when(mockedResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeCommand(anyString())).thenReturn(mockedResponse);

            List<Win32DesktopMonitor> monitors = service.get(mockSession);
            assertTrue(monitors.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {

        PowerShellResponse mockedResponse = mock(PowerShellResponse.class);
        when(mockedResponse.getCommandOutput()).thenReturn("not a valid json");

        try (PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeCommand(anyString())).thenReturn(mockedResponse);
            assertThrows(JsonSyntaxException.class, () -> service.get(mockSession));
        }
    }

    @Test
    void test_getWithTimeout_success() {

        try (MockedStatic<TerminalUtility> mockedTerminal = mockStatic(TerminalUtility.class)) {
            mockedTerminal
                    .when(() -> TerminalUtility.executeCommand(anyString(), anyLong()))
                    .thenReturn(json);

            List<Win32DesktopMonitor> monitors = service.get(5L);
            assertEquals(2, monitors.size());

            assertThat(monitors.get(0)).usingRecursiveComparison().isEqualTo(expectedMonitor1);
            assertThat(monitors.get(1)).usingRecursiveComparison().isEqualTo(expectedMonitor2);
        }
    }

    @Test
    void test_getWithTimeout_invalidJson_throwsException() {

        try (MockedStatic<TerminalUtility> mockedTerminal = mockStatic(TerminalUtility.class)) {
            mockedTerminal
                    .when(() -> TerminalUtility.executeCommand(anyString(), anyLong()))
                    .thenReturn("invalid json");

            assertThrows(JsonSyntaxException.class, () -> service.get(5L));
        }
    }

    /*
     * This test ensures that the test JSON has keys matching all @SerializedName
     * (or raw field names if not annotated) declared in the entity class.
     *
     * The test fails if:
     * - any field is added or removed in the entity without updating the test JSON
     * - any @SerializedName value changes without updating the test JSON
     */
    @Test
    void test_entityFieldParity_withTestJson() {

        // get the serialized name for each field, in a set
        // store the field name in case no serialized names are found
        Field[] declaredClassFields = Win32DesktopMonitor.class.getDeclaredFields();
        Set<String> serializedNames = new HashSet<>();

        for (Field field : declaredClassFields) {
            SerializedName s = field.getAnnotation(SerializedName.class);
            serializedNames.add(s != null ? s.value() : field.getName());
        }

        // Extract JSON keys from the static test JSON
        Set<String> jsonKeys = new Gson().fromJson(json, JsonArray.class)
                .get(0).getAsJsonObject().keySet();

        // Validate equality of keys vs serialized names
        assertThat(serializedNames)
                .as("Entity fields and JSON keys must match exactly")
                .containsExactlyInAnyOrderElementsOf(jsonKeys);
    }
}
