package unit.service.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetConnectionProfile;
import io.github.eggy03.ferrumx.windows.service.network.MsftNetConnectionProfileService;
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

class MsftNetConnectionProfileServiceTest {

    private MsftNetConnectionProfileService msftNetConnectionProfileService;

    private static MsftNetConnectionProfile expectedEthernetProfile;
    private static MsftNetConnectionProfile expectedWifiProfile;

    private static String json;

    @BeforeAll
    static void setProfiles() {
        expectedEthernetProfile = MsftNetConnectionProfile.builder()
                .interfaceIndex(1L)
                .interfaceAlias("Ethernet")
                .networkCategory(1L) // Private
                .domainAuthenticationKind(0L) // None
                .ipv4Connectivity(4L) // Internet
                .ipv6Connectivity(1L) // NoTraffic
                .build();

        expectedWifiProfile = MsftNetConnectionProfile.builder()
                .interfaceIndex(2L)
                .interfaceAlias("Wi-Fi")
                .networkCategory(0L) // Public
                .domainAuthenticationKind(0L) // None
                .ipv4Connectivity(4L) // Internet
                .ipv6Connectivity(4L) // Internet
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonArray profiles = new JsonArray();

        JsonObject eth = new JsonObject();
        eth.addProperty("InterfaceIndex", 1L);
        eth.addProperty("InterfaceAlias", "Ethernet");
        eth.addProperty("NetworkCategory", 1L);
        eth.addProperty("DomainAuthenticationKind", 0L);
        eth.addProperty("IPv4Connectivity", 4L);
        eth.addProperty("IPv6Connectivity", 1L);

        JsonObject wifi = new JsonObject();
        wifi.addProperty("InterfaceIndex", 2L);
        wifi.addProperty("InterfaceAlias", "Wi-Fi");
        wifi.addProperty("NetworkCategory", 0L);
        wifi.addProperty("DomainAuthenticationKind", 0L);
        wifi.addProperty("IPv4Connectivity", 4L);
        wifi.addProperty("IPv6Connectivity", 4L);

        profiles.add(eth);
        profiles.add(wifi);

        json = new GsonBuilder().serializeNulls().create().toJson(profiles);
    }


    @BeforeEach
    void setUp() {
        msftNetConnectionProfileService = new MsftNetConnectionProfileService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<MsftNetConnectionProfile> profiles = msftNetConnectionProfileService.get();
            assertEquals(2, profiles.size());

            assertThat(profiles.get(0)).usingRecursiveComparison().isEqualTo(expectedEthernetProfile);
            assertThat(profiles.get(1)).usingRecursiveComparison().isEqualTo(expectedWifiProfile);
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<MsftNetConnectionProfile> profiles = msftNetConnectionProfileService.get();
            assertTrue(profiles.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> msftNetConnectionProfileService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<MsftNetConnectionProfile> profiles = msftNetConnectionProfileService.get(mockShell);
            assertEquals(2, profiles.size());

            assertThat(profiles.get(0)).usingRecursiveComparison().isEqualTo(expectedEthernetProfile);
            assertThat(profiles.get(1)).usingRecursiveComparison().isEqualTo(expectedWifiProfile);
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<MsftNetConnectionProfile> profiles = msftNetConnectionProfileService.get(mockShell);
            assertTrue(profiles.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> msftNetConnectionProfileService.get(mockShell));
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
        Field[] declaredClassFields = MsftNetConnectionProfile.class.getDeclaredFields();
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
