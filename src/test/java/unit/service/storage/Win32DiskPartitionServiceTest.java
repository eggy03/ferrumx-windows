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
import io.github.eggy03.ferrumx.windows.entity.storage.Win32DiskPartition;
import io.github.eggy03.ferrumx.windows.service.storage.Win32DiskPartitionService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class Win32DiskPartitionServiceTest {

    private Win32DiskPartitionService diskPartitionService;

    private static Win32DiskPartition expectedSystemPartition;
    private static Win32DiskPartition expectedDataPartition;
    private static String json;

    @BeforeAll
    static void setDiskPartitions() {
        expectedSystemPartition = Win32DiskPartition.builder()
                .deviceId("Disk0\\Partition1")
                .name("System Reserved")
                .description("EFI System Partition")
                .blockSize(BigInteger.valueOf(512L))
                .numberOfBlocks(BigInteger.valueOf(131072L))
                .bootable(true)
                .primaryPartition(true)
                .bootPartition(true)
                .diskIndex(0L)
                .size(BigInteger.valueOf(67108864L))
                .type("EFI")
                .build();

        expectedDataPartition = Win32DiskPartition.builder()
                .deviceId("Disk0\\Partition2")
                .name("Local Disk (C:)")
                .description("Primary OS Partition")
                .blockSize(BigInteger.valueOf(4096L))
                .numberOfBlocks(BigInteger.valueOf(244190000L))
                .bootable(false)
                .primaryPartition(true)
                .bootPartition(false)
                .diskIndex(0L)
                .size(BigInteger.valueOf(1000204886016L))
                .type("NTFS")
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonObject part1 = new JsonObject();
        part1.addProperty("DeviceID", "Disk0\\Partition1");
        part1.addProperty("Name", "System Reserved");
        part1.addProperty("Description", "EFI System Partition");
        part1.addProperty("BlockSize", 512L);
        part1.addProperty("NumberOfBlocks", 131072L);
        part1.addProperty("Bootable", true);
        part1.addProperty("PrimaryPartition", true);
        part1.addProperty("BootPartition", true);
        part1.addProperty("DiskIndex", 0L);
        part1.addProperty("Size", 67108864L);
        part1.addProperty("Type", "EFI");

        JsonObject part2 = new JsonObject();
        part2.addProperty("DeviceID", "Disk0\\Partition2");
        part2.addProperty("Name", "Local Disk (C:)");
        part2.addProperty("Description", "Primary OS Partition");
        part2.addProperty("BlockSize", 4096L);
        part2.addProperty("NumberOfBlocks", 244190000L);
        part2.addProperty("Bootable", false);
        part2.addProperty("PrimaryPartition", true);
        part2.addProperty("BootPartition", false);
        part2.addProperty("DiskIndex", 0L);
        part2.addProperty("Size", 1000204886016L);
        part2.addProperty("Type", "NTFS");

        JsonArray array = new JsonArray();
        array.add(part1);
        array.add(part2);

        json = new GsonBuilder().serializeNulls().create().toJson(array);
    }

    @BeforeEach
    void setUp() {
        diskPartitionService = new Win32DiskPartitionService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32DiskPartition> partitions = diskPartitionService.get();
            assertEquals(2, partitions.size());

            assertThat(partitions.get(0)).usingRecursiveComparison().isEqualTo(expectedSystemPartition);
            assertThat(partitions.get(1)).usingRecursiveComparison().isEqualTo(expectedDataPartition);
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32DiskPartition> partitions = diskPartitionService.get();
            assertTrue(partitions.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> diskPartitionService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32DiskPartition> partitions = diskPartitionService.get(mockShell);
            assertEquals(2, partitions.size());

            assertThat(partitions.get(0)).usingRecursiveComparison().isEqualTo(expectedSystemPartition);
            assertThat(partitions.get(1)).usingRecursiveComparison().isEqualTo(expectedDataPartition);
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32DiskPartition> partitions = diskPartitionService.get(mockShell);
            assertTrue(partitions.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> diskPartitionService.get(mockShell));
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
        Field[] declaredClassFields = Win32DiskPartition.class.getDeclaredFields();
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
