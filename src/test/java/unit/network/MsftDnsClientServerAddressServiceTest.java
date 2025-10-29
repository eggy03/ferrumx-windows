package unit.network;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.network.MsftDnsClientServerAddress;
import io.github.eggy03.ferrumx.windows.service.network.MsftDnsClientServerAddressService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class MsftDnsClientServerAddressServiceTest {

    private MsftDnsClientServerAddressService msftDnsService;

    private static String json;

    @BeforeAll
    static void setupJson() {
        JsonArray dnsArray = new JsonArray();

        JsonObject ethernet = new JsonObject();
        ethernet.addProperty("InterfaceIndex", 1);
        JsonArray ethernetAddressArray = new JsonArray();
        ethernetAddressArray.add("8.8.8.8");
        ethernetAddressArray.add("9.9.9.9");
        ethernet.add("ServerAddresses", ethernetAddressArray);

        JsonObject wifi = new JsonObject();
        wifi.addProperty("InterfaceIndex", 2);
        JsonArray wifiAddressArray = new JsonArray();
        wifiAddressArray.add("8.8.4.4");
        wifiAddressArray.add("1.1.1.1");
        wifi.add("ServerAddresses", wifiAddressArray);

        dnsArray.add(ethernet);
        dnsArray.add(wifi);

        json = new Gson().toJson(dnsArray);
    }

    @BeforeEach
    void setUp() {
        msftDnsService = new MsftDnsClientServerAddressService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<MsftDnsClientServerAddress> dns = msftDnsService.get();
            assertFalse(dns.isEmpty());
            assertEquals(1, dns.get(0).getInterfaceIndex());
            assertEquals(2, dns.get(1).getInterfaceIndex());
            
            List<String> ethernetDnsAddress = dns.get(0).getDnsServerAddresses();
            List<String> wifiDnsAddress = dns.get(1).getDnsServerAddresses();

            assertNotNull(ethernetDnsAddress);
            assertNotNull(wifiDnsAddress);

            assertEquals("8.8.8.8", ethernetDnsAddress.get(0));
            assertEquals("9.9.9.9", ethernetDnsAddress.get(1));

            assertEquals("8.8.4.4", wifiDnsAddress.get(0));
            assertEquals("1.1.1.1", wifiDnsAddress.get(1));
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<MsftDnsClientServerAddress> dns = msftDnsService.get();
            assertTrue(dns.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> msftDnsService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<MsftDnsClientServerAddress> dns = msftDnsService.get(mockShell);
            assertFalse(dns.isEmpty());
            assertEquals(1, dns.get(0).getInterfaceIndex());
            assertEquals(2, dns.get(1).getInterfaceIndex());

            List<String> ethernetDnsAddress = dns.get(0).getDnsServerAddresses();
            List<String> wifiDnsAddress = dns.get(1).getDnsServerAddresses();

            assertNotNull(ethernetDnsAddress);
            assertNotNull(wifiDnsAddress);

            assertEquals("8.8.8.8", ethernetDnsAddress.get(0));
            assertEquals("9.9.9.9", ethernetDnsAddress.get(1));

            assertEquals("8.8.4.4", wifiDnsAddress.get(0));
            assertEquals("1.1.1.1", wifiDnsAddress.get(1));
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<MsftDnsClientServerAddress> dns = msftDnsService.get(mockShell);
            assertTrue(dns.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> msftDnsService.get(mockShell));
        }
    }
}
