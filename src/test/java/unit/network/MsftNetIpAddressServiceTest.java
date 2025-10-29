package unit.network;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetIpAddress;
import io.github.eggy03.ferrumx.windows.service.network.MsftNetIpAddressService;
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

class MsftNetIpAddressServiceTest {

    private MsftNetIpAddressService msftNetIpAddressService;

    private static String json;

    @BeforeAll
    static void setupJson() {
        JsonArray ip = new JsonArray();

        JsonObject ethernet = new JsonObject();
        ethernet.addProperty("InterfaceIndex", 1);
        ethernet.addProperty("InterfaceAlias", "Ethernet");
        ethernet.addProperty("IPv4Address", "192.168.1.254");
        ethernet.addProperty("IPv6Address", "");

        JsonObject wifi = new JsonObject();
        wifi.addProperty("InterfaceIndex", 2);
        wifi.addProperty("InterfaceAlias", "WiFi");
        wifi.addProperty("IPv4Address", "");
        wifi.addProperty("IPv6Address", "fe80::abed:1234:5678:9abc");

        ip.add(ethernet);
        ip.add(wifi);

        json = new Gson().toJson(ip);
    }

    @BeforeEach
    void setUp() {
        msftNetIpAddressService = new MsftNetIpAddressService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<MsftNetIpAddress> ip = msftNetIpAddressService.get();
            assertFalse(ip.isEmpty());
            assertEquals(1, ip.get(0).getInterfaceIndex());
            assertEquals("Ethernet", ip.get(0).getInterfaceAlias());
            assertEquals("192.168.1.254", ip.get(0).getIpv4Address());
            assertEquals("", ip.get(0).getIpv6Address());

            assertEquals(2, ip.get(1).getInterfaceIndex());
            assertEquals("WiFi", ip.get(1).getInterfaceAlias());
            assertEquals("", ip.get(1).getIpv4Address());
            assertEquals("fe80::abed:1234:5678:9abc", ip.get(1).getIpv6Address());
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<MsftNetIpAddress> ip = msftNetIpAddressService.get();
            assertTrue(ip.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> msftNetIpAddressService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<MsftNetIpAddress> ip = msftNetIpAddressService.get(mockShell);
            assertFalse(ip.isEmpty());
            assertEquals(1, ip.get(0).getInterfaceIndex());
            assertEquals("Ethernet", ip.get(0).getInterfaceAlias());
            assertEquals("192.168.1.254", ip.get(0).getIpv4Address());
            assertEquals("", ip.get(0).getIpv6Address());

            assertEquals(2, ip.get(1).getInterfaceIndex());
            assertEquals("WiFi", ip.get(1).getInterfaceAlias());
            assertEquals("", ip.get(1).getIpv4Address());
            assertEquals("fe80::abed:1234:5678:9abc", ip.get(1).getIpv6Address());
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<MsftNetIpAddress> ip = msftNetIpAddressService.get(mockShell);
            assertTrue(ip.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> msftNetIpAddressService.get(mockShell));
        }
    }
}
