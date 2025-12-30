/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package unit.service.compounded;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.compounded.MsftNetAdapterToIpAndDnsAndProfile;
import io.github.eggy03.ferrumx.windows.service.compounded.MsftNetAdapterToIpAndDnsAndProfileService;
import io.github.eggy03.ferrumx.windows.utility.TerminalUtility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class MsftNetAdapterToIpAndDnsAndProfileServiceTest {

    private static MsftNetAdapterToIpAndDnsAndProfile expectedObject;
    private static String json;
    private MsftNetAdapterToIpAndDnsAndProfileService service;

    @BeforeAll
    static void setJson() {

        JsonArray array = new JsonArray();

        JsonObject object = new JsonObject();
        object.addProperty("InterfaceIndex", 1L);
        object.add("NetworkAdapter", null);
        object.add("IPAddresses", null);
        object.add("DNSServers", null);
        object.add("Profile", null);

        array.add(object);

        json = new GsonBuilder().serializeNulls().create().toJson(array);

        expectedObject = MsftNetAdapterToIpAndDnsAndProfile.builder()
                .interfaceIndex(1L)
                .build();
    }

    @BeforeEach
    void setService() {
        service = new MsftNetAdapterToIpAndDnsAndProfileService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> mockPS = mockStatic(PowerShell.class);
             PowerShell mockShell = mock(PowerShell.class)) {

            mockPS.when(PowerShell::openSession).thenReturn(mockShell);
            when(mockShell.executeScript(any(BufferedReader.class))).thenReturn(mockResponse);

            List<MsftNetAdapterToIpAndDnsAndProfile> objectList = service.get();
            assertThat(objectList).hasSize(1);
            assertThat(objectList.get(0)).usingRecursiveComparison().isEqualTo(expectedObject);

        }
    }

    @Test
    void test_get_empty() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> mockPS = mockStatic(PowerShell.class);
             PowerShell mockShell = mock(PowerShell.class)) {

            mockPS.when(PowerShell::openSession).thenReturn(mockShell);
            when(mockShell.executeScript(any(BufferedReader.class))).thenReturn(mockResponse);

            List<MsftNetAdapterToIpAndDnsAndProfile> objectList = service.get();
            assertThat(objectList).isEmpty();
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a valid json");

        try (MockedStatic<PowerShell> mockPS = mockStatic(PowerShell.class);
             PowerShell mockShell = mock(PowerShell.class)) {

            mockPS.when(PowerShell::openSession).thenReturn(mockShell);
            when(mockShell.executeScript(any(BufferedReader.class))).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> service.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockedResponse = mock(PowerShellResponse.class);
        when(mockedResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeScript(any(BufferedReader.class))).thenReturn(mockedResponse);

            List<MsftNetAdapterToIpAndDnsAndProfile> objectList = service.get(mockSession);
            assertThat(objectList).hasSize(1);
            assertThat(objectList.get(0)).usingRecursiveComparison().isEqualTo(expectedObject);
        }
    }

    @Test
    void test_getWithSession_empty() {

        PowerShellResponse mockedResponse = mock(PowerShellResponse.class);
        when(mockedResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeScript(any(BufferedReader.class))).thenReturn(mockedResponse);

            List<MsftNetAdapterToIpAndDnsAndProfile> objectList = service.get(mockSession);
            assertThat(objectList).isEmpty();
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {

        PowerShellResponse mockedResponse = mock(PowerShellResponse.class);
        when(mockedResponse.getCommandOutput()).thenReturn("not a valid json");

        try (PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeScript(any(BufferedReader.class))).thenReturn(mockedResponse);
            assertThrows(JsonSyntaxException.class, () -> service.get(mockSession));
        }
    }

    @Test
    void test_getWithTimeout_success() {

        try(MockedStatic<TerminalUtility> mockedTerminal = mockStatic(TerminalUtility.class)){
            mockedTerminal
                    .when(()-> TerminalUtility.executeCommand(anyString(), anyLong()))
                    .thenReturn(json);

            List<MsftNetAdapterToIpAndDnsAndProfile> objectList = service.get(5L);
            assertThat(objectList).hasSize(1);
            assertThat(objectList.get(0)).usingRecursiveComparison().isEqualTo(expectedObject);
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
        Field[] declaredClassFields = MsftNetAdapterToIpAndDnsAndProfile.class.getDeclaredFields();
        Set<String> serializedNames = new HashSet<>();

        for (Field field : declaredClassFields) {
            SerializedName s = field.getAnnotation(SerializedName.class);
            serializedNames.add(s != null ? s.value() : field.getName());
        }

        // Extract JSON keys from the static test JSON
        Set<String> jsonKeys = new Gson().fromJson(json, JsonArray.class).get(0).getAsJsonObject().keySet();

        // Validate equality of keys vs serialized names
        assertThat(serializedNames)
                .as("Entity fields and JSON keys must match exactly")
                .containsExactlyInAnyOrderElementsOf(jsonKeys);
    }
}
