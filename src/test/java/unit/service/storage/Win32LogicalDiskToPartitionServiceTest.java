package unit.service.storage;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.storage.Win32LogicalDiskToPartition;
import io.github.eggy03.ferrumx.windows.service.storage.Win32LogicalDiskToPartitionService;
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

class Win32LogicalDiskToPartitionServiceTest {

    private Win32LogicalDiskToPartitionService service;

    private static Win32LogicalDiskToPartition expectedSystemLogicalDiskPartition;
    private static Win32LogicalDiskToPartition expectedDataLogicalDiskPartition;
    private static String json;

    @BeforeAll
    static void setLogicalDiskToPartition() {
        expectedSystemLogicalDiskPartition = Win32LogicalDiskToPartition.builder()
                .diskPartitionDeviceId("Disk #0 Partition #1")
                .logicalDiskDeviceId("C:")
                .build();

        expectedDataLogicalDiskPartition = Win32LogicalDiskToPartition.builder()
                .diskPartitionDeviceId("Disk #0 Partition #2")
                .logicalDiskDeviceId("D:")
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonObject sysPartJson = new JsonObject();
        sysPartJson.addProperty("DiskPartitionDeviceID", "Disk #0 Partition #1");
        sysPartJson.addProperty("LogicalDiskDeviceID", "C:");

        JsonObject dataPartJson = new JsonObject();
        dataPartJson.addProperty("DiskPartitionDeviceID", "Disk #0 Partition #2");
        dataPartJson.addProperty("LogicalDiskDeviceID", "D:");

        JsonArray array = new JsonArray();
        array.add(sysPartJson);
        array.add(dataPartJson);

        json = new Gson().toJson(array);
    }


    @BeforeEach
    void setService() {
        service = new Win32LogicalDiskToPartitionService();
    }

    @Test
    void test_get_success() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try(MockedStatic<PowerShell> mockShell = mockStatic(PowerShell.class)){

            mockShell.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32LogicalDiskToPartition> associationList = service.get();
            assertEquals(2, associationList.size());

            assertThat(associationList.get(0)).usingRecursiveComparison().isEqualTo(expectedSystemLogicalDiskPartition);
            assertThat(associationList.get(1)).usingRecursiveComparison().isEqualTo(expectedDataLogicalDiskPartition);
        }
    }

    @Test
    void test_get_emptyJson_emptyResult() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try(MockedStatic<PowerShell> mockShell = mockStatic(PowerShell.class)){

            mockShell.when(()-> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);
            List<Win32LogicalDiskToPartition> associationList = service.get();

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

            List<Win32LogicalDiskToPartition> associationList = service.get(mockShell);
            assertEquals(2, associationList.size());

            assertThat(associationList.get(0)).usingRecursiveComparison().isEqualTo(expectedSystemLogicalDiskPartition);
            assertThat(associationList.get(1)).usingRecursiveComparison().isEqualTo(expectedDataLogicalDiskPartition);
        }
    }

    @Test
    void test_getWithSession_emptyJson_emptyResult() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try(PowerShell mockShell = mock(PowerShell.class)){

            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);
            List<Win32LogicalDiskToPartition> associationList = service.get(mockShell);

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
