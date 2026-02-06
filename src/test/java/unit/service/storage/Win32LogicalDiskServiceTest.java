/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package unit.service.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32LogicalDisk;
import io.github.eggy03.ferrumx.windows.service.storage.Win32LogicalDiskService;
import io.github.eggy03.ferrumx.windows.utility.TerminalUtility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.math.BigInteger;
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

class Win32LogicalDiskServiceTest {

    private static Win32LogicalDisk expectedSystemVolume;
    private static Win32LogicalDisk expectedDataVolume;
    private static String json;
    private Win32LogicalDiskService service;

    @BeforeAll
    static void setLogicalDisks() {
        expectedSystemVolume = Win32LogicalDisk.builder()
                .deviceId("C:")
                .description("System Volume")
                .driveType(3L)
                .mediaType(12L)
                .fileSystem("NTFS")
                .size(BigInteger.valueOf(1000204886016L))
                .freeSpace(BigInteger.valueOf(532147200000L))
                .compressed(false)
                .supportsFileBasedCompression(true)
                .supportsDiskQuotas(false)
                .volumeName("Windows")
                .volumeSerialNumber("1A2B-3C4D")
                .build();

        expectedDataVolume = Win32LogicalDisk.builder()
                .deviceId("D:")
                .description("Data Volume")
                .driveType(3L)
                .mediaType(12L)
                .fileSystem("NTFS")
                .size(BigInteger.valueOf(2000409772032L))
                .freeSpace(BigInteger.valueOf(1240152000000L))
                .compressed(false)
                .supportsFileBasedCompression(true)
                .supportsDiskQuotas(false)
                .volumeName("Data")
                .volumeSerialNumber("5E6F-7G8H")
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonObject sysVol = new JsonObject();
        sysVol.addProperty("DeviceID", "C:");
        sysVol.addProperty("Description", "System Volume");
        sysVol.addProperty("DriveType", 3L);
        sysVol.addProperty("MediaType", 12L);
        sysVol.addProperty("FileSystem", "NTFS");
        sysVol.addProperty("Size", 1000204886016L);
        sysVol.addProperty("FreeSpace", 532147200000L);
        sysVol.addProperty("Compressed", false);
        sysVol.addProperty("SupportsFileBasedCompression", true);
        sysVol.addProperty("SupportsDiskQuotas", false);
        sysVol.addProperty("VolumeName", "Windows");
        sysVol.addProperty("VolumeSerialNumber", "1A2B-3C4D");

        JsonObject dataVol = new JsonObject();
        dataVol.addProperty("DeviceID", "D:");
        dataVol.addProperty("Description", "Data Volume");
        dataVol.addProperty("DriveType", 3L);
        dataVol.addProperty("MediaType", 12L);
        dataVol.addProperty("FileSystem", "NTFS");
        dataVol.addProperty("Size", 2000409772032L);
        dataVol.addProperty("FreeSpace", 1240152000000L);
        dataVol.addProperty("Compressed", false);
        dataVol.addProperty("SupportsFileBasedCompression", true);
        dataVol.addProperty("SupportsDiskQuotas", false);
        dataVol.addProperty("VolumeName", "Data");
        dataVol.addProperty("VolumeSerialNumber", "5E6F-7G8H");

        JsonArray array = new JsonArray();
        array.add(sysVol);
        array.add(dataVol);

        json = new GsonBuilder().serializeNulls().create().toJson(array);
    }


    @BeforeEach
    void setUp() {
        service = new Win32LogicalDiskService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32LogicalDisk> disks = service.get();
            assertEquals(2, disks.size());

            assertThat(disks.get(0)).usingRecursiveComparison().isEqualTo(expectedSystemVolume);
            assertThat(disks.get(1)).usingRecursiveComparison().isEqualTo(expectedDataVolume);
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32LogicalDisk> disks = service.get();
            assertTrue(disks.isEmpty());
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

            List<Win32LogicalDisk> disks = service.get(mockShell);
            assertEquals(2, disks.size());

            assertThat(disks.get(0)).usingRecursiveComparison().isEqualTo(expectedSystemVolume);
            assertThat(disks.get(1)).usingRecursiveComparison().isEqualTo(expectedDataVolume);
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32LogicalDisk> disks = service.get(mockShell);
            assertTrue(disks.isEmpty());
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

            List<Win32LogicalDisk> disks = service.get(5L);
            assertEquals(2, disks.size());

            assertThat(disks.get(0)).usingRecursiveComparison().isEqualTo(expectedSystemVolume);
            assertThat(disks.get(1)).usingRecursiveComparison().isEqualTo(expectedDataVolume);
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
        Field[] declaredClassFields = Win32LogicalDisk.class.getDeclaredFields();
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
