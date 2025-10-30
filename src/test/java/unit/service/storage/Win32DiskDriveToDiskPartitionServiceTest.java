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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class Win32DiskDriveToDiskPartitionServiceTest {

    private Win32DiskDriveToDiskPartitionService service;

    private static String json;

    @BeforeAll
    static void setJson() {
        JsonArray jsonArray = new JsonArray();

        JsonObject drivePartOne = new JsonObject();
        JsonObject drivePartTwo = new JsonObject();

        drivePartOne.addProperty("DiskDriveDeviceID", "PHYSICAL_DRIVE_1");
        drivePartOne.addProperty("DiskPartitionDeviceID", "LOGICAL_PARTITION_1");

        drivePartTwo.addProperty("DiskDriveDeviceID", "PHYSICAL_DRIVE_2");
        drivePartTwo.addProperty("DiskPartitionDeviceID", "LOGICAL_PARTITION_2");

        jsonArray.add(drivePartOne);
        jsonArray.add(drivePartTwo);

        json = new Gson().toJson(jsonArray);
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

            assertFalse(associationList.isEmpty());
            assertEquals("PHYSICAL_DRIVE_1", associationList.get(0).getDiskDriveDeviceId());
            assertEquals("PHYSICAL_DRIVE_2", associationList.get(1).getDiskDriveDeviceId());
            assertEquals("LOGICAL_PARTITION_1", associationList.get(0).getDiskPartitionDeviceId());
            assertEquals("LOGICAL_PARTITION_2", associationList.get(1).getDiskPartitionDeviceId());
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

            assertFalse(associationList.isEmpty());
            assertEquals("PHYSICAL_DRIVE_1", associationList.get(0).getDiskDriveDeviceId());
            assertEquals("PHYSICAL_DRIVE_2", associationList.get(1).getDiskDriveDeviceId());
            assertEquals("LOGICAL_PARTITION_1", associationList.get(0).getDiskPartitionDeviceId());
            assertEquals("LOGICAL_PARTITION_2", associationList.get(1).getDiskPartitionDeviceId());
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
