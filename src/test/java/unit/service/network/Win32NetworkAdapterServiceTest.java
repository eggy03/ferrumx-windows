package unit.service.network;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.network.Win32NetworkAdapter;
import io.github.eggy03.ferrumx.windows.service.network.Win32NetworkAdapterService;
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

class Win32NetworkAdapterServiceTest {

    private Win32NetworkAdapterService networkAdapterService;

    private static Win32NetworkAdapter expectedEthernetAdapter;
    private static Win32NetworkAdapter expectedWifiAdapter;

    private static String json;

    @BeforeAll
    static void setAdapters() {
        expectedEthernetAdapter = Win32NetworkAdapter.builder()
                .deviceId("1")
                .index(1)
                .name("Ethernet")
                .description("Intel(R) Ethernet Connection I219-V")
                .pnpDeviceId("PCI\\VEN_8086&DEV_15B8&SUBSYS_06A41028&REV_31\\3&11583659&0&FE")
                .macAddress("00:1A:2B:3C:4D:5E")
                .installed(true)
                .netEnabled(true)
                .netConnectionId("Ethernet")
                .physicalAdapter(true)
                .timeOfLastReset("2024-07-12T15:30:00Z")
                .build();

        expectedWifiAdapter = Win32NetworkAdapter.builder()
                .deviceId("2")
                .index(2)
                .name("Wi-Fi")
                .description("Intel(R) Wi-Fi 6 AX200 160MHz")
                .pnpDeviceId("PCI\\VEN_8086&DEV_2723&SUBSYS_00848086&REV_1A\\3&11583659&0&A3")
                .macAddress("A0:B1:C2:D3:E4:F5")
                .installed(true)
                .netEnabled(false)
                .netConnectionId("Wi-Fi")
                .physicalAdapter(true)
                .timeOfLastReset("2024-07-12T15:45:00Z")
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonArray adapters = new JsonArray();

        JsonObject eth = new JsonObject();
        eth.addProperty("DeviceID", "1");
        eth.addProperty("Index", 1);
        eth.addProperty("Name", "Ethernet");
        eth.addProperty("Description", "Intel(R) Ethernet Connection I219-V");
        eth.addProperty("PNPDeviceID", "PCI\\VEN_8086&DEV_15B8&SUBSYS_06A41028&REV_31\\3&11583659&0&FE");
        eth.addProperty("MACAddress", "00:1A:2B:3C:4D:5E");
        eth.addProperty("Installed", true);
        eth.addProperty("NetEnabled", true);
        eth.addProperty("NetConnectionID", "Ethernet");
        eth.addProperty("PhysicalAdapter", true);
        eth.addProperty("TimeOfLastReset", "2024-07-12T15:30:00Z");

        JsonObject wifi = new JsonObject();
        wifi.addProperty("DeviceID", "2");
        wifi.addProperty("Index", 2);
        wifi.addProperty("Name", "Wi-Fi");
        wifi.addProperty("Description", "Intel(R) Wi-Fi 6 AX200 160MHz");
        wifi.addProperty("PNPDeviceID", "PCI\\VEN_8086&DEV_2723&SUBSYS_00848086&REV_1A\\3&11583659&0&A3");
        wifi.addProperty("MACAddress", "A0:B1:C2:D3:E4:F5");
        wifi.addProperty("Installed", true);
        wifi.addProperty("NetEnabled", false);
        wifi.addProperty("NetConnectionID", "Wi-Fi");
        wifi.addProperty("PhysicalAdapter", true);
        wifi.addProperty("TimeOfLastReset", "2024-07-12T15:45:00Z");

        adapters.add(eth);
        adapters.add(wifi);

        json = new Gson().toJson(adapters);
    }


    @BeforeEach
    void setUp() {
        networkAdapterService = new Win32NetworkAdapterService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32NetworkAdapter> adapters = networkAdapterService.get();
            assertEquals(2, adapters.size());

            assertThat(adapters.get(0)).usingRecursiveComparison().isEqualTo(expectedEthernetAdapter);
            assertThat(adapters.get(1)).usingRecursiveComparison().isEqualTo(expectedWifiAdapter);
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32NetworkAdapter> adapters = networkAdapterService.get();
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

            List<Win32NetworkAdapter> adapters = networkAdapterService.get(mockShell);
            assertEquals(2, adapters.size());

            assertThat(adapters.get(0)).usingRecursiveComparison().isEqualTo(expectedEthernetAdapter);
            assertThat(adapters.get(1)).usingRecursiveComparison().isEqualTo(expectedWifiAdapter);
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32NetworkAdapter> adapters = networkAdapterService.get(mockShell);
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