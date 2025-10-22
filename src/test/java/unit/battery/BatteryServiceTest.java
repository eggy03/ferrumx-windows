package unit.battery;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.battery.Battery;
import io.github.eggy03.ferrumx.windows.service.battery.BatteryService;
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

class BatteryServiceTest {

    private BatteryService batteryService;

    private static String json;

    @BeforeAll
    static void loadJson() {
        JsonArray batteries = new JsonArray();

        JsonObject bat0 = new JsonObject();
        bat0.addProperty("DeviceID", "BAT0");
        bat0.addProperty("Caption", "Internal Battery");
        bat0.addProperty("Name", "Battery 0");
        bat0.addProperty("Status", "OK");
        bat0.addProperty("BatteryStatus", 2);
        bat0.addProperty("EstimatedChargeRemaining", 85);

        JsonObject bat1 = new JsonObject();
        bat1.addProperty("DeviceID", "BAT1");
        bat1.addProperty("Caption", "External Battery");
        bat1.addProperty("Name", "Battery 1");
        bat1.addProperty("Status", "OK");
        bat1.addProperty("BatteryStatus", 2);
        bat1.addProperty("EstimatedChargeRemaining", 100);

        batteries.add(bat0);
        batteries.add(bat1);

        json = new Gson().toJson(batteries);
    }

    @BeforeEach
    void setUp() {
        batteryService = new BatteryService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Battery> batteries = batteryService.get();
            assertFalse(batteries.isEmpty());
            assertEquals("BAT0", batteries.get(0).getDeviceId());
            assertEquals("Battery 0", batteries.get(0).getName());
            assertEquals("BAT1", batteries.get(1).getDeviceId());
            assertEquals("Battery 1", batteries.get(1).getName());
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Battery> batteries = batteryService.get();
            assertTrue(batteries.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> batteryService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeCommand(anyString())).thenReturn(mockResponse);

            List<Battery> batteries = batteryService.get(mockSession);
            assertFalse(batteries.isEmpty());
            assertEquals("BAT0", batteries.get(0).getDeviceId());
            assertEquals("Battery 0", batteries.get(0).getName());
            assertEquals("BAT1", batteries.get(1).getDeviceId());
            assertEquals("Battery 1", batteries.get(1).getName());
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeCommand(anyString())).thenReturn(mockResponse);

            List<Battery> batteries = batteryService.get(mockSession);
            assertTrue(batteries.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> batteryService.get(mockSession));
        }
    }
}