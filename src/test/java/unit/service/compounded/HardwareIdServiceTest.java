/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package unit.service.compounded;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.compounded.HardwareId;
import io.github.eggy03.ferrumx.windows.service.compounded.HardwareIdService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class HardwareIdServiceTest {

    private static HardwareId expectedHwid;
    private static String json;
    private HardwareIdService service;

    @BeforeAll
    static void setJson() {

        JsonObject hwidObject = new JsonObject();
        hwidObject.addProperty("HWIDRaw", "ABC123");
        hwidObject.addProperty("HWIDHash", "123XYZ");

        json = new GsonBuilder().serializeNulls().create().toJson(hwidObject);

        expectedHwid = HardwareId.builder()
                .rawHWID("ABC123")
                .hashHWID("123XYZ")
                .build();
    }

    @BeforeEach
    void setService() {
        service = new HardwareIdService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> mockPS = mockStatic(PowerShell.class);
             PowerShell mockShell = mock(PowerShell.class)) {

            mockPS.when(PowerShell::openSession).thenReturn(mockShell);
            when(mockShell.executeScript(anyString())).thenReturn(mockResponse);

            Optional<HardwareId> hwid = service.get();
            assertThat(hwid).isPresent();
            assertThat(hwid.get()).usingRecursiveComparison().isEqualTo(expectedHwid);

        }
    }

    @Test
    void test_get_empty() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> mockPS = mockStatic(PowerShell.class);
             PowerShell mockShell = mock(PowerShell.class)) {

            mockPS.when(PowerShell::openSession).thenReturn(mockShell);
            when(mockShell.executeScript(anyString())).thenReturn(mockResponse);

            Optional<HardwareId> hwid = service.get();
            assertThat(hwid).isEmpty();
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a valid json");

        try (MockedStatic<PowerShell> mockPS = mockStatic(PowerShell.class);
             PowerShell mockShell = mock(PowerShell.class)) {

            mockPS.when(PowerShell::openSession).thenReturn(mockShell);
            when(mockShell.executeScript(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> service.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockedResponse = mock(PowerShellResponse.class);
        when(mockedResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeScript(anyString())).thenReturn(mockedResponse);

            Optional<HardwareId> hwid = service.get(mockSession);
            assertThat(hwid).isPresent();
            assertThat(hwid.get()).usingRecursiveComparison().isEqualTo(expectedHwid);
        }
    }

    @Test
    void test_getWithSession_empty() {

        PowerShellResponse mockedResponse = mock(PowerShellResponse.class);
        when(mockedResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeScript(anyString())).thenReturn(mockedResponse);

            Optional<HardwareId> hwid = service.get(mockSession);
            assertThat(hwid).isEmpty();
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {

        PowerShellResponse mockedResponse = mock(PowerShellResponse.class);
        when(mockedResponse.getCommandOutput()).thenReturn("not a valid json");

        try (PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeScript(anyString())).thenReturn(mockedResponse);
            assertThrows(JsonSyntaxException.class, () -> service.get(mockSession));
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
        Field[] declaredClassFields = HardwareId.class.getDeclaredFields();
        Set<String> serializedNames = new HashSet<>();

        for (Field field : declaredClassFields) {
            SerializedName s = field.getAnnotation(SerializedName.class);
            serializedNames.add(s != null ? s.value() : field.getName());
        }

        // Extract JSON keys from the static test JSON
        Set<String> jsonKeys = new Gson().fromJson(json, JsonObject.class).keySet();

        // Validate equality of keys vs serialized names
        assertThat(serializedNames)
                .as("Entity fields and JSON keys must match exactly")
                .containsExactlyInAnyOrderElementsOf(jsonKeys);
    }
}
