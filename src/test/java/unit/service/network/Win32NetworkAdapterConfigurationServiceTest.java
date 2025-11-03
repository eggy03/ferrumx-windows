package unit.service.network;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.network.Win32NetworkAdapterConfiguration;
import io.github.eggy03.ferrumx.windows.service.network.Win32NetworkAdapterConfigurationService;
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

class Win32NetworkAdapterConfigurationServiceTest {

    private Win32NetworkAdapterConfigurationService networkAdapterConfigurationService;

    private static String json;

    @BeforeAll
    static void setupJson() {
        JsonArray adapters = new JsonArray();

        // ethernet
        JsonObject ethernet = new JsonObject();
        ethernet.addProperty("Index", 0);
        ethernet.addProperty("Description", "Ethernet Adapter");
        ethernet.addProperty("IPEnabled", true);

        JsonArray ipAddresses = new JsonArray();
        ipAddresses.add("192.168.0.10");
        ethernet.add("IPAddress", ipAddresses);

        JsonArray subnets = new JsonArray();
        subnets.add("255.255.255.0");
        ethernet.add("IPSubnet", subnets);

        JsonArray gateways = new JsonArray();
        gateways.add("192.168.0.1");
        ethernet.add("DefaultIPGateway", gateways);

        // wifi
        JsonObject wifi = new JsonObject();
        wifi.addProperty("Index", 1);
        wifi.addProperty("Description", "Wi-Fi Adapter");
        wifi.addProperty("IPEnabled", false);
        wifi.add("IPAddress", new JsonArray());
        wifi.add("IPSubnet", new JsonArray());
        wifi.add("DefaultIPGateway", new JsonArray());

        adapters.add(ethernet);
        adapters.add(wifi);

        json = new Gson().toJson(adapters);
    }

    @BeforeEach
    void setUp() {
        networkAdapterConfigurationService = new Win32NetworkAdapterConfigurationService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32NetworkAdapterConfiguration> configs = networkAdapterConfigurationService.get();
            assertFalse(configs.isEmpty());
            assertEquals(0, configs.get(0).getIndex());
            assertEquals(Boolean.TRUE, configs.get(0).isIPEnabled());
            assertEquals(1, configs.get(1).getIndex());
            assertEquals(Boolean.FALSE, configs.get(1).isIPEnabled());
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32NetworkAdapterConfiguration> configs = networkAdapterConfigurationService.get();
            assertTrue(configs.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> networkAdapterConfigurationService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32NetworkAdapterConfiguration> configs = networkAdapterConfigurationService.get(mockShell);
            assertFalse(configs.isEmpty());
            assertEquals(0, configs.get(0).getIndex());
            assertEquals(Boolean.TRUE, configs.get(0).isIPEnabled());
            assertEquals(1, configs.get(1).getIndex());
            assertEquals(Boolean.FALSE, configs.get(1).isIPEnabled());
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32NetworkAdapterConfiguration> configs = networkAdapterConfigurationService.get(mockShell);
            assertTrue(configs.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> networkAdapterConfigurationService.get(mockShell));
        }
    }
}