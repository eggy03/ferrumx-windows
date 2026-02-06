/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package unit.service.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetIpAddress;
import io.github.eggy03.ferrumx.windows.service.network.MsftNetIpAddressService;
import io.github.eggy03.ferrumx.windows.utility.TerminalUtility;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class MsftNetIpAddressServiceTest {

    private static MsftNetIpAddress expectedIPv4Address;
    private static MsftNetIpAddress expectedIPv6Address;
    private static String json;
    private MsftNetIpAddressService service;

    @BeforeAll
    static void setAddresses() {
        MsftNetIpAddress.Datetime lifetime = MsftNetIpAddress.Datetime.builder()
                .days(9999L)
                .hours(0L)
                .minutes(0L)
                .seconds(0L)
                .build();

        expectedIPv4Address = MsftNetIpAddress.builder()
                .interfaceIndex(1L)
                .interfaceAlias("Ethernet")
                .addressFamily(2L) // IPv4
                .ipAddress("192.168.1.10")
                .ipv4Address("192.168.1.10")
                .ipv6Address(null)
                .type(1)
                .prefixOrigin(1L)
                .suffixOrigin(2L)
                .prefixLength(24L)
                .preferredLifetime(lifetime)
                .validLifeTime(lifetime)
                .build();

        expectedIPv6Address = MsftNetIpAddress.builder()
                .interfaceIndex(2L)
                .interfaceAlias("Wi-Fi")
                .addressFamily(23L) // IPv6
                .ipAddress("fe80::1a2b:3c4d:5e6f:7a8b")
                .ipv4Address(null)
                .ipv6Address("fe80::1a2b:3c4d:5e6f:7a8b")
                .type(2)
                .prefixOrigin(3L)
                .suffixOrigin(3L)
                .prefixLength(64L)
                .preferredLifetime(lifetime)
                .validLifeTime(lifetime)
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonArray addresses = new JsonArray();

        JsonObject ipv4 = new JsonObject();
        ipv4.addProperty("InterfaceIndex", 1L);
        ipv4.addProperty("InterfaceAlias", "Ethernet");
        ipv4.addProperty("AddressFamily", 2L);
        ipv4.addProperty("IPAddress", "192.168.1.10");
        ipv4.addProperty("IPv4Address", "192.168.1.10");
        ipv4.add("IPv6Address", JsonNull.INSTANCE);
        ipv4.addProperty("Type", 1);
        ipv4.addProperty("PrefixOrigin", 1L);
        ipv4.addProperty("SuffixOrigin", 2L);
        ipv4.addProperty("PrefixLength", 24L);

        JsonObject lifetime = new JsonObject();
        lifetime.addProperty("Days", 9999L);
        lifetime.addProperty("Hours", 0L);
        lifetime.addProperty("Minutes", 0L);
        lifetime.addProperty("Seconds", 0L);
        ipv4.add("PreferredLifetime", lifetime);
        ipv4.add("ValidLifetime", lifetime);

        JsonObject ipv6 = new JsonObject();
        ipv6.addProperty("InterfaceIndex", 2L);
        ipv6.addProperty("InterfaceAlias", "Wi-Fi");
        ipv6.addProperty("AddressFamily", 23L);
        ipv6.addProperty("IPAddress", "fe80::1a2b:3c4d:5e6f:7a8b");
        ipv6.add("IPv4Address", JsonNull.INSTANCE);
        ipv6.addProperty("IPv6Address", "fe80::1a2b:3c4d:5e6f:7a8b");
        ipv6.addProperty("Type", 2);
        ipv6.addProperty("PrefixOrigin", 3L);
        ipv6.addProperty("SuffixOrigin", 3L);
        ipv6.addProperty("PrefixLength", 64L);
        ipv6.add("PreferredLifetime", lifetime);
        ipv6.add("ValidLifetime", lifetime);

        addresses.add(ipv4);
        addresses.add(ipv6);

        json = new GsonBuilder().serializeNulls().create().toJson(addresses);
    }


    @BeforeEach
    void setUp() {
        service = new MsftNetIpAddressService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<MsftNetIpAddress> ip = service.get();
            assertEquals(2, ip.size());

            assertThat(ip.get(0)).usingRecursiveComparison().isEqualTo(expectedIPv4Address);
            assertThat(ip.get(1)).usingRecursiveComparison().isEqualTo(expectedIPv6Address);
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<MsftNetIpAddress> ip = service.get();
            assertTrue(ip.isEmpty());
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

            List<MsftNetIpAddress> ip = service.get(mockShell);
            assertEquals(2, ip.size());

            assertThat(ip.get(0)).usingRecursiveComparison().isEqualTo(expectedIPv4Address);
            assertThat(ip.get(1)).usingRecursiveComparison().isEqualTo(expectedIPv6Address);
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<MsftNetIpAddress> ip = service.get(mockShell);
            assertTrue(ip.isEmpty());
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

        try (MockedStatic<TerminalUtility> mockedTerminal = mockStatic(TerminalUtility.class)) {
            mockedTerminal
                    .when(() -> TerminalUtility.executeCommand(anyString(), anyLong()))
                    .thenReturn(json);

            List<MsftNetIpAddress> ip = service.get(5L);
            assertEquals(2, ip.size());

            assertThat(ip.get(0)).usingRecursiveComparison().isEqualTo(expectedIPv4Address);
            assertThat(ip.get(1)).usingRecursiveComparison().isEqualTo(expectedIPv6Address);
        }
    }

    @Test
    void test_getWithTimeout_invalidJson_throwsException() {

        try (MockedStatic<TerminalUtility> mockedTerminal = mockStatic(TerminalUtility.class)) {
            mockedTerminal
                    .when(() -> TerminalUtility.executeCommand(anyString(), anyLong()))
                    .thenReturn("invalid json");

            assertThrows(JsonSyntaxException.class, () -> service.get(5L));
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
        Field[] declaredClassFields = MsftNetIpAddress.class.getDeclaredFields();
        Set<String> serializedNames = new HashSet<>();

        for (Field field : declaredClassFields) {
            SerializedName s = field.getAnnotation(SerializedName.class);
            serializedNames.add(s != null ? s.value() : field.getName());
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
