/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package unit.service.system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.system.Win32Process;
import io.github.eggy03.ferrumx.windows.service.system.Win32ProcessService;
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

class Win32ProcessServiceTest {

    private static Win32Process expectedProcess1;
    private static Win32Process expectedProcess2;
    private static String json;
    private Win32ProcessService service;

    @BeforeAll
    static void setupProcesses() {
        expectedProcess1 = Win32Process.builder()
                .processId(1234L)
                .sessionId(1L)
                .name("explorer.exe")
                .caption("Windows Explorer")
                .description("Manages the Windows shell")
                .executablePath("C:\\Windows\\explorer.exe")
                .executionState(1)
                .handle("0xABC")
                .handleCount(500L)
                .priority(8L)
                .threadCount(35L)
                .kernelModeTime(new BigInteger("120000000"))
                .userModeTime(new BigInteger("80000000"))
                .workingSetSize(new BigInteger("52428800"))
                .peakWorkingSetSize(new BigInteger("67108864"))
                .privatePageCount(new BigInteger("33554432"))
                .pageFileUsage(30000L)
                .peakPageFileUsage(40000L)
                .virtualSize(new BigInteger("268435456"))
                .peakVirtualSize(new BigInteger("536870912"))
                .creationDate("20251103101530.000000+330")
                .terminationDate(null)
                .build();

        expectedProcess2 = Win32Process.builder()
                .processId(5678L)
                .sessionId(1L)
                .name("svchost.exe")
                .caption("Service Host")
                .description("Hosts Windows services")
                .executablePath("C:\\Windows\\System32\\svchost.exe")
                .executionState(1)
                .handle("0xDEF")
                .handleCount(200L)
                .priority(8L)
                .threadCount(10L)
                .kernelModeTime(new BigInteger("60000000"))
                .userModeTime(new BigInteger("40000000"))
                .workingSetSize(new BigInteger("26214400"))
                .peakWorkingSetSize(new BigInteger("31457280"))
                .privatePageCount(new BigInteger("16777216"))
                .pageFileUsage(15000L)
                .peakPageFileUsage(20000L)
                .virtualSize(new BigInteger("134217728"))
                .peakVirtualSize(new BigInteger("268435456"))
                .creationDate("20251103102000.000000+330")
                .terminationDate(null)
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonArray processes = new JsonArray();

        JsonObject p1 = new JsonObject();
        p1.addProperty("ProcessId", 1234L);
        p1.addProperty("SessionId", 1L);
        p1.addProperty("Name", "explorer.exe");
        p1.addProperty("Caption", "Windows Explorer");
        p1.addProperty("Description", "Manages the Windows shell");
        p1.addProperty("ExecutablePath", "C:\\Windows\\explorer.exe");
        p1.addProperty("ExecutionState", 1);
        p1.addProperty("Handle", "0xABC");
        p1.addProperty("HandleCount", 500L);
        p1.addProperty("Priority", 8L);
        p1.addProperty("ThreadCount", 35L);
        p1.addProperty("KernelModeTime", "120000000");
        p1.addProperty("UserModeTime", "80000000");
        p1.addProperty("WorkingSetSize", "52428800");
        p1.addProperty("PeakWorkingSetSize", "67108864");
        p1.addProperty("PrivatePageCount", "33554432");
        p1.addProperty("PageFileUsage", 30000L);
        p1.addProperty("PeakPageFileUsage", 40000L);
        p1.addProperty("VirtualSize", "268435456");
        p1.addProperty("PeakVirtualSize", "536870912");
        p1.addProperty("CreationDate", "20251103101530.000000+330");
        p1.add("TerminationDate", JsonNull.INSTANCE);

        JsonObject p2 = new JsonObject();
        p2.addProperty("ProcessId", 5678L);
        p2.addProperty("SessionId", 1L);
        p2.addProperty("Name", "svchost.exe");
        p2.addProperty("Caption", "Service Host");
        p2.addProperty("Description", "Hosts Windows services");
        p2.addProperty("ExecutablePath", "C:\\Windows\\System32\\svchost.exe");
        p2.addProperty("ExecutionState", 1);
        p2.addProperty("Handle", "0xDEF");
        p2.addProperty("HandleCount", 200L);
        p2.addProperty("Priority", 8L);
        p2.addProperty("ThreadCount", 10L);
        p2.addProperty("KernelModeTime", "60000000");
        p2.addProperty("UserModeTime", "40000000");
        p2.addProperty("WorkingSetSize", "26214400");
        p2.addProperty("PeakWorkingSetSize", "31457280");
        p2.addProperty("PrivatePageCount", "16777216");
        p2.addProperty("PageFileUsage", 15000L);
        p2.addProperty("PeakPageFileUsage", 20000L);
        p2.addProperty("VirtualSize", "134217728");
        p2.addProperty("PeakVirtualSize", "268435456");
        p2.addProperty("CreationDate", "20251103102000.000000+330");
        p2.add("TerminationDate", JsonNull.INSTANCE);

        processes.add(p1);
        processes.add(p2);

        json = new GsonBuilder().serializeNulls().create().toJson(processes);
    }

    @BeforeEach
    void setService() {
        service = new Win32ProcessService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32Process> os = service.get();
            assertEquals(2, os.size());
            assertThat(os.get(0)).usingRecursiveComparison().isEqualTo(expectedProcess1);
            assertThat(os.get(1)).usingRecursiveComparison().isEqualTo(expectedProcess2);
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32Process> os = service.get();
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

            List<Win32Process> os = service.get(mockShell);
            assertEquals(2, os.size());
            assertThat(os.get(0)).usingRecursiveComparison().isEqualTo(expectedProcess1);
            assertThat(os.get(1)).usingRecursiveComparison().isEqualTo(expectedProcess2);
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32Process> os = service.get(mockShell);
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

            List<Win32Process> os = service.get(5L);
            assertEquals(2, os.size());
            assertThat(os.get(0)).usingRecursiveComparison().isEqualTo(expectedProcess1);
            assertThat(os.get(1)).usingRecursiveComparison().isEqualTo(expectedProcess2);
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
        Field[] declaredClassFields = Win32Process.class.getDeclaredFields();
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
