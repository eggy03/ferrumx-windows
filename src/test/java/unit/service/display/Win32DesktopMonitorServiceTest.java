package unit.service.display;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.display.Win32DesktopMonitor;
import io.github.eggy03.ferrumx.windows.service.display.Win32DesktopMonitorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class Win32DesktopMonitorServiceTest {

    private Win32DesktopMonitorService monitorService;

    private static Win32DesktopMonitor expectedMonitor1;
    private static Win32DesktopMonitor expectedMonitor2;

    private static String json;

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

        json = new Gson().toJson(monitors);
    }

    @BeforeEach
    void setUp() {
        monitorService = new Win32DesktopMonitorService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockedResponse = mock(PowerShellResponse.class);
        when(mockedResponse.getCommandOutput()).thenReturn(json);

        try(MockedStatic<PowerShell> powerShellMockedStatic = mockStatic(PowerShell.class)) {
            powerShellMockedStatic.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockedResponse);

            List<Win32DesktopMonitor> monitors = monitorService.get();
            assertEquals(2, monitors.size());

            assertThat(monitors.get(0)).usingRecursiveComparison().isEqualTo(expectedMonitor1);
            assertThat(monitors.get(1)).usingRecursiveComparison().isEqualTo(expectedMonitor2);
        }
    }

    @Test
    void test_get_empty() {

        PowerShellResponse mockedResponse = mock(PowerShellResponse.class);
        when(mockedResponse.getCommandOutput()).thenReturn("");

        try(MockedStatic<PowerShell> powerShellMockedStatic = mockStatic(PowerShell.class)) {
            powerShellMockedStatic.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockedResponse);

            List<Win32DesktopMonitor> monitors = monitorService.get();
            assertTrue(monitors.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {

        PowerShellResponse mockedResponse = mock(PowerShellResponse.class);
        when(mockedResponse.getCommandOutput()).thenReturn("not a valid json");

        try (MockedStatic<PowerShell> powerShellMockedStatic = mockStatic(PowerShell.class)) {
            powerShellMockedStatic.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockedResponse);

            assertThrows(JsonSyntaxException.class, () -> monitorService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockedResponse = mock(PowerShellResponse.class);
        when(mockedResponse.getCommandOutput()).thenReturn(json);

        try(PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeCommand(anyString())).thenReturn(mockedResponse);

            List<Win32DesktopMonitor> monitors = monitorService.get(mockSession);
            assertEquals(2, monitors.size());

            assertThat(monitors.get(0)).usingRecursiveComparison().isEqualTo(expectedMonitor1);
            assertThat(monitors.get(1)).usingRecursiveComparison().isEqualTo(expectedMonitor2);
        }
    }

    @Test
    void test_getWithSession_empty() {

        PowerShellResponse mockedResponse = mock(PowerShellResponse.class);
        when(mockedResponse.getCommandOutput()).thenReturn("");

        try(PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeCommand(anyString())).thenReturn(mockedResponse);

            List<Win32DesktopMonitor> monitors = monitorService.get(mockSession);
            assertTrue(monitors.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {

        PowerShellResponse mockedResponse = mock(PowerShellResponse.class);
        when(mockedResponse.getCommandOutput()).thenReturn("not a valid json");

        try(PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeCommand(anyString())).thenReturn(mockedResponse);
            assertThrows(JsonSyntaxException.class, () -> monitorService.get(mockSession));
        }
    }
}
