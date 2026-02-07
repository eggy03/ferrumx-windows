/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package unit.service.system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.system.Win32OperatingSystem;
import io.github.eggy03.ferrumx.windows.service.system.Win32OperatingSystemService;
import io.github.eggy03.ferrumx.windows.utility.TerminalUtility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.Collections;
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

class Win32OperatingSystemServiceTest {

    private static Win32OperatingSystem expectedOs;
    private static String json;
    private Win32OperatingSystemService service;

    @BeforeAll
    static void setupOperatingSystem() {
        expectedOs = Win32OperatingSystem.builder()
                .name("Microsoft Windows 11 Pro|C:\\WINDOWS|\\Device\\Harddisk0\\Partition4")
                .caption("Microsoft Windows 11 Pro")
                .installDate("20240101000000.000000+330")
                .csName("DESKTOP-12345")
                .lastBootUpTime("20241102123000.000000+330")
                .localDateTime("20251103180000.000000+330")
                .distributed(false)
                .numberOfUsers(1)
                .version("10.0.22631")
                .bootDevice("\\Device\\HarddiskVolume3")
                .buildNumber("22631")
                .buildType("Multiprocessor Free")
                .manufacturer("Microsoft Corporation")
                .osArchitecture("64-bit")
                .muiLanguages(Collections.singletonList("en-US"))
                .portableOperatingSystem(false)
                .primary(true)
                .registeredUser("User")
                .serialNumber("00330-80000-00000-AA123")
                .servicePackMajorVersion(0)
                .servicePackMinorVersion(0)
                .systemDirectory("C:\\WINDOWS\\system32")
                .systemDrive("C:")
                .windowsDirectory("C:\\WINDOWS")
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("Name", "Microsoft Windows 11 Pro|C:\\WINDOWS|\\Device\\Harddisk0\\Partition4");
        obj.addProperty("Caption", "Microsoft Windows 11 Pro");
        obj.addProperty("InstallDate", "20240101000000.000000+330");
        obj.addProperty("CSName", "DESKTOP-12345");
        obj.addProperty("LastBootUpTime", "20241102123000.000000+330");
        obj.addProperty("LocalDateTime", "20251103180000.000000+330");
        obj.addProperty("Distributed", false);
        obj.addProperty("NumberOfUsers", 1);
        obj.addProperty("Version", "10.0.22631");
        obj.addProperty("BootDevice", "\\Device\\HarddiskVolume3");
        obj.addProperty("BuildNumber", "22631");
        obj.addProperty("BuildType", "Multiprocessor Free");
        obj.addProperty("Manufacturer", "Microsoft Corporation");
        obj.addProperty("OSArchitecture", "64-bit");
        obj.add("MUILanguages", new Gson().toJsonTree(Collections.singletonList("en-US")));
        obj.addProperty("PortableOperatingSystem", false);
        obj.addProperty("Primary", true);
        obj.addProperty("RegisteredUser", "User");
        obj.addProperty("SerialNumber", "00330-80000-00000-AA123");
        obj.addProperty("ServicePackMajorVersion", 0);
        obj.addProperty("ServicePackMinorVersion", 0);
        obj.addProperty("SystemDirectory", "C:\\WINDOWS\\system32");
        obj.addProperty("SystemDrive", "C:");
        obj.addProperty("WindowsDirectory", "C:\\WINDOWS");

        json = new GsonBuilder().serializeNulls().create().toJson(obj);
    }


    @BeforeEach
    void setUp() {
        service = new Win32OperatingSystemService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32OperatingSystem> os = service.get();
            assertEquals(1, os.size());
            assertThat(os.get(0)).usingRecursiveComparison().isEqualTo(expectedOs);
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32OperatingSystem> os = service.get();
            assertTrue(os.isEmpty());
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

            List<Win32OperatingSystem> os = service.get(mockShell);
            assertEquals(1, os.size());
            assertThat(os.get(0)).usingRecursiveComparison().isEqualTo(expectedOs);
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32OperatingSystem> os = service.get(mockShell);
            assertTrue(os.isEmpty());
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

            List<Win32OperatingSystem> os = service.get(5L);
            assertEquals(1, os.size());
            assertThat(os.get(0)).usingRecursiveComparison().isEqualTo(expectedOs);
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
        Field[] declaredClassFields = Win32OperatingSystem.class.getDeclaredFields();
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