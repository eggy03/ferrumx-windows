/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package unit.service.mainboard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.mainboard.Win32Baseboard;
import io.github.eggy03.ferrumx.windows.service.mainboard.Win32BaseboardService;
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

class Win32BaseboardServiceTest {

    private Win32BaseboardService service;

    private static Win32Baseboard expectedBoard1;
    private static Win32Baseboard expectedBoard2;

    private static String json;

    @BeforeAll
    static void setBaseboards() {
        expectedBoard1 = Win32Baseboard.builder()
                .manufacturer("ASUS")
                .model("ROG STRIX Z790-E GAMING WIFI")
                .product("Z790-E")
                .serialNumber("ABC123456789")
                .version("Rev 1.xx")
                .build();

        expectedBoard2 = Win32Baseboard.builder()
                .manufacturer("MSI")
                .model("MAG B650 TOMAHAWK WIFI")
                .product("B650 TOMAHAWK")
                .serialNumber("XYZ987654321")
                .version("Rev 2.00")
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonArray boards = new JsonArray();

        JsonObject board1 = new JsonObject();
        board1.addProperty("Manufacturer", "ASUS");
        board1.addProperty("Model", "ROG STRIX Z790-E GAMING WIFI");
        board1.addProperty("Product", "Z790-E");
        board1.addProperty("SerialNumber", "ABC123456789");
        board1.addProperty("Version", "Rev 1.xx");

        JsonObject board2 = new JsonObject();
        board2.addProperty("Manufacturer", "MSI");
        board2.addProperty("Model", "MAG B650 TOMAHAWK WIFI");
        board2.addProperty("Product", "B650 TOMAHAWK");
        board2.addProperty("SerialNumber", "XYZ987654321");
        board2.addProperty("Version", "Rev 2.00");

        boards.add(board1);
        boards.add(board2);

        json = new GsonBuilder().serializeNulls().create().toJson(boards);
    }


    @BeforeEach
    void setUp() {
        service = new Win32BaseboardService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32Baseboard> baseboardList = service.get();
            assertEquals(2, baseboardList.size());

            assertThat(baseboardList.get(0)).usingRecursiveComparison().isEqualTo(expectedBoard1);
            assertThat(baseboardList.get(1)).usingRecursiveComparison().isEqualTo(expectedBoard2);
        }
    }

    @Test
    void test_get_emptyJson_returnsEmpty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32Baseboard> baseboardListList = service.get();
            assertTrue(baseboardListList.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> service.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32Baseboard> baseboardList = service.get(mockShell);
            assertEquals(2, baseboardList.size());

            assertThat(baseboardList.get(0)).usingRecursiveComparison().isEqualTo(expectedBoard1);
            assertThat(baseboardList.get(1)).usingRecursiveComparison().isEqualTo(expectedBoard2);
        }
    }

    @Test
    void test_getWithSession_emptyJson_returnsEmpty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32Baseboard> baseboardList = service.get(mockShell);
            assertTrue(baseboardList.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

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

            List<Win32Baseboard> baseboardList = service.get(5L);
            assertEquals(2, baseboardList.size());

            assertThat(baseboardList.get(0)).usingRecursiveComparison().isEqualTo(expectedBoard1);
            assertThat(baseboardList.get(1)).usingRecursiveComparison().isEqualTo(expectedBoard2);
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
        Field[] declaredClassFields = Win32Baseboard.class.getDeclaredFields();
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

