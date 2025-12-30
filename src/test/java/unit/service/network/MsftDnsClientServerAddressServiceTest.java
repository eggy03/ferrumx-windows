/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package unit.service.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.network.MsftDnsClientServerAddress;
import io.github.eggy03.ferrumx.windows.service.network.MsftDnsClientServerAddressService;
import io.github.eggy03.ferrumx.windows.utility.TerminalUtility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.Arrays;
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

class MsftDnsClientServerAddressServiceTest {

    private MsftDnsClientServerAddressService service;

    private static MsftDnsClientServerAddress expectedDns1;
    private static MsftDnsClientServerAddress expectedDns2;

    private static String json;

    @BeforeAll
    static void setDnsConfigs() {
        expectedDns1 = MsftDnsClientServerAddress.builder()
                .interfaceIndex(1L)
                .interfaceAlias("Ethernet")
                .addressFamily(2) // IPv4
                .dnsServerAddresses(Arrays.asList("8.8.8.8", "4.4.4.4"))
                .build();

        expectedDns2 = MsftDnsClientServerAddress.builder()
                .interfaceIndex(2L)
                .interfaceAlias("Wi-Fi")
                .addressFamily(23) // IPv6
                .dnsServerAddresses(Arrays.asList("2001:4860:4860::8888", "2001:4860:4860::8844"))
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonArray dnsConfigs = new JsonArray();

        JsonObject dns1 = new JsonObject();
        dns1.addProperty("InterfaceIndex", 1L);
        dns1.addProperty("InterfaceAlias", "Ethernet");
        dns1.addProperty("AddressFamily", 2);
        dns1.add("ServerAddresses", new Gson().toJsonTree(Arrays.asList("8.8.8.8", "4.4.4.4")));

        JsonObject dns2 = new JsonObject();
        dns2.addProperty("InterfaceIndex", 2L);
        dns2.addProperty("InterfaceAlias", "Wi-Fi");
        dns2.addProperty("AddressFamily", 23);
        dns2.add("ServerAddresses", new Gson().toJsonTree(Arrays.asList("2001:4860:4860::8888", "2001:4860:4860::8844")));

        dnsConfigs.add(dns1);
        dnsConfigs.add(dns2);

        json = new GsonBuilder().serializeNulls().create().toJson(dnsConfigs);
    }

    @BeforeEach
    void setUp() {
        service = new MsftDnsClientServerAddressService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<MsftDnsClientServerAddress> dns = service.get();
            assertEquals(2, dns.size());

            assertThat(dns.get(0)).usingRecursiveComparison().isEqualTo(expectedDns1);
            assertThat(dns.get(1)).usingRecursiveComparison().isEqualTo(expectedDns2);
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<MsftDnsClientServerAddress> dns = service.get();
            assertTrue(dns.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> service.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<MsftDnsClientServerAddress> dns = service.get(mockShell);
            assertEquals(2, dns.size());

            assertThat(dns.get(0)).usingRecursiveComparison().isEqualTo(expectedDns1);
            assertThat(dns.get(1)).usingRecursiveComparison().isEqualTo(expectedDns2);
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<MsftDnsClientServerAddress> dns = service.get(mockShell);
            assertTrue(dns.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> service.get(mockShell));
        }
    }

    @Test
    void test_getWithTimeout_success() {

        try(MockedStatic<TerminalUtility> mockedTerminal = mockStatic(TerminalUtility.class)){
            mockedTerminal
                    .when(()-> TerminalUtility.executeCommand(anyString(), anyLong()))
                    .thenReturn(json);

            List<MsftDnsClientServerAddress> dns = service.get(5L);
            assertEquals(2, dns.size());

            assertThat(dns.get(0)).usingRecursiveComparison().isEqualTo(expectedDns1);
            assertThat(dns.get(1)).usingRecursiveComparison().isEqualTo(expectedDns2);
        }
    }

    @Test
    void test_getWithTimeout_invalidJson_throwsException() {

        try(MockedStatic<TerminalUtility> mockedTerminal = mockStatic(TerminalUtility.class)){
            mockedTerminal
                    .when(()-> TerminalUtility.executeCommand(anyString(), anyLong()))
                    .thenReturn("invalid json");

            assertThrows(JsonSyntaxException.class, ()-> service.get(5L));
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
        Field[] declaredClassFields = MsftDnsClientServerAddress.class.getDeclaredFields();
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
