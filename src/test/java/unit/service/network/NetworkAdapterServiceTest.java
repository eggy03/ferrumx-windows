package unit.service.network;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.network.NetworkAdapter;
import io.github.eggy03.ferrumx.windows.service.network.NetworkAdapterService;
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

class NetworkAdapterServiceTest {

    private NetworkAdapterService networkAdapterService;

    private static String json;

    @BeforeAll
    static void setupJson() {
        JsonArray adapters = new JsonArray();

        JsonObject ethernet = new JsonObject();
        ethernet.addProperty("DeviceID", "1");
        ethernet.addProperty("Index", 0);
        ethernet.addProperty("Name", "Ethernet Adapter");
        ethernet.addProperty("MACAddress", "00-14-22-01-23-45");
        ethernet.addProperty("NetEnabled", true);

        JsonObject wifi = new JsonObject();
        wifi.addProperty("DeviceID", "2");
        wifi.addProperty("Index", 1);
        wifi.addProperty("Name", "Wi-Fi Adapter");
        wifi.addProperty("MACAddress", "00-16-36-FF-EE-11");
        wifi.addProperty("NetEnabled", false);

        adapters.add(ethernet);
        adapters.add(wifi);

        json = new Gson().toJson(adapters);
    }

    @BeforeEach
    void setUp() {
        networkAdapterService = new NetworkAdapterService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<NetworkAdapter> adapters = networkAdapterService.get();
            assertFalse(adapters.isEmpty());
            assertEquals("1", adapters.get(0).getDeviceId());
            assertEquals("Ethernet Adapter", adapters.get(0).getName());
            assertEquals("2", adapters.get(1).getDeviceId());
            assertEquals("Wi-Fi Adapter", adapters.get(1).getName());
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<NetworkAdapter> adapters = networkAdapterService.get();
            assertTrue(adapters.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> networkAdapterService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<NetworkAdapter> adapters = networkAdapterService.get(mockShell);
            assertFalse(adapters.isEmpty());
            assertEquals("1", adapters.get(0).getDeviceId());
            assertEquals("Ethernet Adapter", adapters.get(0).getName());
            assertEquals("2", adapters.get(1).getDeviceId());
            assertEquals("Wi-Fi Adapter", adapters.get(1).getName());
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<NetworkAdapter> adapters = networkAdapterService.get(mockShell);
            assertTrue(adapters.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> networkAdapterService.get(mockShell));
        }
    }
}