/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package unit.service.peripheral;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.peripheral.Win32SoundDevice;
import io.github.eggy03.ferrumx.windows.service.peripheral.Win32SoundDeviceService;
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

class Win32SoundDeviceServiceTest {

    private Win32SoundDeviceService service;

    private static Win32SoundDevice expectedDevice1;
    private static Win32SoundDevice expectedDevice2;

    private static String json;

    @BeforeAll
    static void setPrinters() {
        expectedDevice1 = Win32SoundDevice.builder()
                .deviceId("AUDIO\\0001")
                .name("Realtek High Definition Audio")
                .pnpDeviceId("HDAUDIO\\FUNC_01&VEN_10EC&DEV_0256&SUBSYS_10431A00&REV_1000")
                .manufacturer("Realtek Semiconductor Corp.")
                .status("OK")
                .statusInfo(3)
                .build();

        expectedDevice2 = Win32SoundDevice.builder()
                .deviceId("AUDIO\\0002")
                .name("NVIDIA High Definition Audio")
                .pnpDeviceId("HDAUDIO\\FUNC_01&VEN_10DE&DEV_0080&SUBSYS_10DE1467&REV_1001")
                .manufacturer("NVIDIA Corporation")
                .status("OK")
                .statusInfo(3)
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonArray audioDevices = new JsonArray();

        JsonObject snd1 = new JsonObject();
        snd1.addProperty("DeviceID", "AUDIO\\0001");
        snd1.addProperty("Name", "Realtek High Definition Audio");
        snd1.addProperty("PNPDeviceID", "HDAUDIO\\FUNC_01&VEN_10EC&DEV_0256&SUBSYS_10431A00&REV_1000");
        snd1.addProperty("Manufacturer", "Realtek Semiconductor Corp.");
        snd1.addProperty("Status", "OK");
        snd1.addProperty("StatusInfo", 3);

        JsonObject snd2 = new JsonObject();
        snd2.addProperty("DeviceID", "AUDIO\\0002");
        snd2.addProperty("Name", "NVIDIA High Definition Audio");
        snd2.addProperty("PNPDeviceID", "HDAUDIO\\FUNC_01&VEN_10DE&DEV_0080&SUBSYS_10DE1467&REV_1001");
        snd2.addProperty("Manufacturer", "NVIDIA Corporation");
        snd2.addProperty("Status", "OK");
        snd2.addProperty("StatusInfo", 3);

        audioDevices.add(snd1);
        audioDevices.add(snd2);

        json = new GsonBuilder().serializeNulls().create().toJson(audioDevices);
    }


    @BeforeEach
    void setUp() {
        service = new Win32SoundDeviceService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32SoundDevice> printers = service.get();
            assertEquals(2, printers.size());

            assertThat(printers.get(0)).usingRecursiveComparison().isEqualTo(expectedDevice1);
            assertThat(printers.get(1)).usingRecursiveComparison().isEqualTo(expectedDevice2);
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32SoundDevice> printers = service.get();
            assertTrue(printers.isEmpty());
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

        try (PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32SoundDevice> printers = service.get(mockSession);
            assertEquals(2, printers.size());

            assertThat(printers.get(0)).usingRecursiveComparison().isEqualTo(expectedDevice1);
            assertThat(printers.get(1)).usingRecursiveComparison().isEqualTo(expectedDevice2);
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32SoundDevice> printers = service.get(mockSession);
            assertTrue(printers.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> service.get(mockSession));
        }
    }

    @Test
    void test_getWithTimeout_success() {

        try(MockedStatic<TerminalUtility> mockedTerminal = mockStatic(TerminalUtility.class)){
            mockedTerminal
                    .when(()-> TerminalUtility.executeCommand(anyString(), anyLong()))
                    .thenReturn(json);

            List<Win32SoundDevice> printers = service.get(5L);
            assertEquals(2, printers.size());

            assertThat(printers.get(0)).usingRecursiveComparison().isEqualTo(expectedDevice1);
            assertThat(printers.get(1)).usingRecursiveComparison().isEqualTo(expectedDevice2);
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
        Field[] declaredClassFields = Win32SoundDevice.class.getDeclaredFields();
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
