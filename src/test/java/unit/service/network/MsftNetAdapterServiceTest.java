package unit.service.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetAdapter;
import io.github.eggy03.ferrumx.windows.service.network.MsftNetAdapterService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class MsftNetAdapterServiceTest {

    private MsftNetAdapterService msftAdapterService;

    private static MsftNetAdapter expectedEthernet;
    private static MsftNetAdapter expectedWifi;

    private static String json;

    @BeforeAll
    static void setAdapters() {
        expectedEthernet = MsftNetAdapter.builder()
                .deviceId("NET1")
                .pnpDeviceId("PCI\\VEN_8086&DEV_15BB&SUBSYS_07B01028&REV_10\\3&11583659&0&FE")
                .interfaceIndex(1L)
                .interfaceName("Ethernet")
                .interfaceType(6L) // Ethernet
                .interfaceDescription("Intel(R) Ethernet Connection I219-V")
                .interfaceAlias("Ethernet")
                .interfaceOperationalStatus(1L)
                .virtual(false)
                .fullDuplex(true)
                .hidden(false)
                .status("Up")
                .linkLayerAddress("00:1A:2B:3C:4D:5E")
                .linkSpeed("1 Gbps")
                .receiveLinkSpeedRaw(1000000000L)
                .transmitLinkSpeedRaw(1000000000L)
                .driverName("e1d68x64.sys")
                .driverVersion("12.19.1.37")
                .driverDate("2023-10-12")
                .mtuSize(1500L)
                .mediaConnectionState(1L)
                .mediaType("802.3")
                .physicalMediaType("Unspecified")
                .build();

        expectedWifi = MsftNetAdapter.builder()
                .deviceId("NET2")
                .pnpDeviceId("PCI\\VEN_14E4&DEV_43A0&SUBSYS_061114E4&REV_03\\4&2AAB3B17&0&00E1")
                .interfaceIndex(2L)
                .interfaceName("Wi-Fi")
                .interfaceType(71L) // Wireless
                .interfaceDescription("Broadcom 802.11ac Network Adapter")
                .interfaceAlias("Wi-Fi")
                .interfaceOperationalStatus(1L)
                .virtual(false)
                .fullDuplex(true)
                .hidden(false)
                .status("Up")
                .linkLayerAddress("44:1C:A8:9D:3E:7F")
                .linkSpeed("866 Mbps")
                .receiveLinkSpeedRaw(866000000L)
                .transmitLinkSpeedRaw(866000000L)
                .driverName("bcmwl63a.sys")
                .driverVersion("7.35.333.0")
                .driverDate("2022-05-01")
                .mtuSize(1500L)
                .mediaConnectionState(1L)
                .mediaType("802.11")
                .physicalMediaType("Wireless LAN")
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonArray adapters = new JsonArray();

        JsonObject eth = new JsonObject();
        eth.addProperty("DeviceID", "NET1");
        eth.addProperty("PnPDeviceID", "PCI\\VEN_8086&DEV_15BB&SUBSYS_07B01028&REV_10\\3&11583659&0&FE");
        eth.addProperty("InterfaceIndex", 1L);
        eth.addProperty("InterfaceName", "Ethernet");
        eth.addProperty("InterfaceType", 6L);
        eth.addProperty("InterfaceDescription", "Intel(R) Ethernet Connection I219-V");
        eth.addProperty("InterfaceAlias", "Ethernet");
        eth.addProperty("InterfaceOperationalStatus", 1L);
        eth.addProperty("Virtual", false);
        eth.addProperty("FullDuplex", true);
        eth.addProperty("Hidden", false);
        eth.addProperty("Status", "Up");
        eth.addProperty("LinkLayerAddress", "00:1A:2B:3C:4D:5E");
        eth.addProperty("LinkSpeed", "1 Gbps");
        eth.addProperty("ReceiveLinkSpeed", 1000000000L);
        eth.addProperty("TransmitLinkSpeed", 1000000000L);
        eth.addProperty("DriverName", "e1d68x64.sys");
        eth.addProperty("DriverVersion", "12.19.1.37");
        eth.addProperty("DriverDate", "2023-10-12");
        eth.addProperty("MtuSize", 1500L);
        eth.addProperty("MediaConnectionState", 1L);
        eth.addProperty("MediaType", "802.3");
        eth.addProperty("PhysicalMediaType", "Unspecified");

        JsonObject wifi = new JsonObject();
        wifi.addProperty("DeviceID", "NET2");
        wifi.addProperty("PnPDeviceID", "PCI\\VEN_14E4&DEV_43A0&SUBSYS_061114E4&REV_03\\4&2AAB3B17&0&00E1");
        wifi.addProperty("InterfaceIndex", 2L);
        wifi.addProperty("InterfaceName", "Wi-Fi");
        wifi.addProperty("InterfaceType", 71L);
        wifi.addProperty("InterfaceDescription", "Broadcom 802.11ac Network Adapter");
        wifi.addProperty("InterfaceAlias", "Wi-Fi");
        wifi.addProperty("InterfaceOperationalStatus", 1L);
        wifi.addProperty("Virtual", false);
        wifi.addProperty("FullDuplex", true);
        wifi.addProperty("Hidden", false);
        wifi.addProperty("Status", "Up");
        wifi.addProperty("LinkLayerAddress", "44:1C:A8:9D:3E:7F");
        wifi.addProperty("LinkSpeed", "866 Mbps");
        wifi.addProperty("ReceiveLinkSpeed", 866000000L);
        wifi.addProperty("TransmitLinkSpeed", 866000000L);
        wifi.addProperty("DriverName", "bcmwl63a.sys");
        wifi.addProperty("DriverVersion", "7.35.333.0");
        wifi.addProperty("DriverDate", "2022-05-01");
        wifi.addProperty("MtuSize", 1500L);
        wifi.addProperty("MediaConnectionState", 1L);
        wifi.addProperty("MediaType", "802.11");
        wifi.addProperty("PhysicalMediaType", "Wireless LAN");

        adapters.add(eth);
        adapters.add(wifi);

        json = new GsonBuilder().serializeNulls().create().toJson(adapters);
    }

    @BeforeEach
    void setUp() {
        msftAdapterService = new MsftNetAdapterService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<MsftNetAdapter> adapters = msftAdapterService.get();
            assertEquals(2, adapters.size());

            assertThat(adapters.get(0)).usingRecursiveComparison().isEqualTo(expectedEthernet);
            assertThat(adapters.get(1)).usingRecursiveComparison().isEqualTo(expectedWifi);
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<MsftNetAdapter> adapters = msftAdapterService.get();
            assertTrue(adapters.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> msftAdapterService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<MsftNetAdapter> adapters = msftAdapterService.get(mockShell);
            assertEquals(2, adapters.size());

            assertThat(adapters.get(0)).usingRecursiveComparison().isEqualTo(expectedEthernet);
            assertThat(adapters.get(1)).usingRecursiveComparison().isEqualTo(expectedWifi);
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<MsftNetAdapter> adapters = msftAdapterService.get(mockShell);
            assertTrue(adapters.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> msftAdapterService.get(mockShell));
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
        Field[] declaredClassFields = MsftNetAdapter.class.getDeclaredFields();
        Set<String> serializedNames = new HashSet<>();

        for(Field field: declaredClassFields){
            SerializedName s = field.getAnnotation(SerializedName.class);
            serializedNames.add(s!=null ? s.value() : field.getName());
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
