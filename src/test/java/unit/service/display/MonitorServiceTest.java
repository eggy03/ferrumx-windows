package unit.service.display;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.display.Monitor;
import io.github.eggy03.ferrumx.windows.service.display.MonitorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class MonitorServiceTest {

    private MonitorService monitorService;

    private static String json;

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
        monitorService = new MonitorService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockedResponse = mock(PowerShellResponse.class);
        when(mockedResponse.getCommandOutput()).thenReturn(json);

        try(MockedStatic<PowerShell> powerShellMockedStatic = mockStatic(PowerShell.class)) {
            powerShellMockedStatic.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockedResponse);

            List<Monitor> monitors = monitorService.get();
            assertFalse(monitors.isEmpty());
            assertEquals("MON1", monitors.get(0).getDeviceId());
            assertEquals("Dell U2720Q", monitors.get(0).getName());
            assertEquals("MON2", monitors.get(1).getDeviceId());
            assertEquals("LG UltraGear 27GL850", monitors.get(1).getName());
        }
    }

    @Test
    void test_get_empty() {

        PowerShellResponse mockedResponse = mock(PowerShellResponse.class);
        when(mockedResponse.getCommandOutput()).thenReturn("");

        try(MockedStatic<PowerShell> powerShellMockedStatic = mockStatic(PowerShell.class)) {
            powerShellMockedStatic.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockedResponse);

            List<Monitor> monitors = monitorService.get();
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

            List<Monitor> monitors = monitorService.get(mockSession);
            assertFalse(monitors.isEmpty());
            assertEquals("MON1", monitors.get(0).getDeviceId());
            assertEquals("Dell U2720Q", monitors.get(0).getName());
            assertEquals("MON2", monitors.get(1).getDeviceId());
            assertEquals("LG UltraGear 27GL850", monitors.get(1).getName());
        }
    }

    @Test
    void test_getWithSession_empty() {

        PowerShellResponse mockedResponse = mock(PowerShellResponse.class);
        when(mockedResponse.getCommandOutput()).thenReturn("");

        try(PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeCommand(anyString())).thenReturn(mockedResponse);

            List<Monitor> monitors = monitorService.get(mockSession);
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
