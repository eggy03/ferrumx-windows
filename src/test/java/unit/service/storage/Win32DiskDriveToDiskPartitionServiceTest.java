package unit.service.storage;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32DiskDriveToDiskPartition;
import io.github.eggy03.ferrumx.windows.service.storage.Win32DiskDriveToDiskPartitionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class Win32DiskDriveToDiskPartitionServiceTest {

    private Win32DiskDriveToDiskPartitionService service;

    private static Win32DiskDriveToDiskPartition expectedMapping1;
    private static Win32DiskDriveToDiskPartition expectedMapping2;
    private static String json;

    @BeforeAll
    static void setDiskDriveToPartitionMappings() {
        expectedMapping1 = Win32DiskDriveToDiskPartition.builder()
                .diskDriveDeviceId("PHYSICALDRIVE0")
                .diskPartitionDeviceId("Disk #0 Partition #1")
                .build();

        expectedMapping2 = Win32DiskDriveToDiskPartition.builder()
                .diskDriveDeviceId("PHYSICALDRIVE1")
                .diskPartitionDeviceId("Disk #1 Partition #1")
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonObject mapping1 = new JsonObject();
        mapping1.addProperty("DiskDriveDeviceID", "PHYSICALDRIVE0");
        mapping1.addProperty("DiskPartitionDeviceID", "Disk #0 Partition #1");

        JsonObject mapping2 = new JsonObject();
        mapping2.addProperty("DiskDriveDeviceID", "PHYSICALDRIVE1");
        mapping2.addProperty("DiskPartitionDeviceID", "Disk #1 Partition #1");

        JsonArray array = new JsonArray();
        array.add(mapping1);
        array.add(mapping2);

        json = new Gson().toJson(array);
    }


    @BeforeEach
    void setService() {
        service = new Win32DiskDriveToDiskPartitionService();
    }

    @Test
    void test_get_success() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try(MockedStatic<PowerShell> mockShell = mockStatic(PowerShell.class)){

            mockShell.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32DiskDriveToDiskPartition> associationList = service.get();
            assertEquals(2, associationList.size());

            assertThat(associationList.get(0)).usingRecursiveComparison().isEqualTo(expectedMapping1);
            assertThat(associationList.get(1)).usingRecursiveComparison().isEqualTo(expectedMapping2);
        }
    }

    @Test
    void test_get_emptyJson_emptyResult() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try(MockedStatic<PowerShell> mockShell = mockStatic(PowerShell.class)){

            mockShell.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);
            List<Win32DiskDriveToDiskPartition> associationList = service.get();

            assertTrue(associationList.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try(MockedStatic<PowerShell> mockShell = mockStatic(PowerShell.class)){

            mockShell.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);
            assertThrows(JsonSyntaxException.class, ()-> service.get());
        }
    }

    @Test
    void test_getWithSession_success() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try(PowerShell mockShell = mock(PowerShell.class)){

            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32DiskDriveToDiskPartition> associationList = service.get(mockShell);
            assertEquals(2, associationList.size());

            assertThat(associationList.get(0)).usingRecursiveComparison().isEqualTo(expectedMapping1);
            assertThat(associationList.get(1)).usingRecursiveComparison().isEqualTo(expectedMapping2);
        }
    }

    @Test
    void test_getWithSession_emptyJson_emptyResult() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try(PowerShell mockShell = mock(PowerShell.class)){

            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);
            List<Win32DiskDriveToDiskPartition> associationList = service.get(mockShell);

            assertTrue(associationList.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try(PowerShell mockShell = mock(PowerShell.class)){

            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);
            assertThrows(JsonSyntaxException.class, ()-> service.get(mockShell));
        }
    }
}
