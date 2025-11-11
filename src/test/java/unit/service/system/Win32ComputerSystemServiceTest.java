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
import io.github.eggy03.ferrumx.windows.entity.system.Win32ComputerSystem;
import io.github.eggy03.ferrumx.windows.service.system.Win32ComputerSystemService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class Win32ComputerSystemServiceTest {

    private Win32ComputerSystemService systemService;

    private static Win32ComputerSystem expectedComputerSystem;
    private static String json;

    @BeforeAll
    static void setupComputerSystem() {
        expectedComputerSystem = Win32ComputerSystem.builder()
                .adminPasswordStatus(3)
                .keyboardPasswordStatus(3)
                .powerOnPasswordStatus(3)
                .bootupState("Normal boot")
                .bootStatus(Arrays.asList(0, 1))
                .automaticResetBootOption(true)
                .powerState(1)
                .powerSupplyState(3)
                .powerManagementCapabilities(Arrays.asList(1, 2, 3))
                .powerManagementSupported(true)
                .resetCapability(4)
                .resetCount(1)
                .resetLimit(5)
                .frontPanelResetStatus(2)
                .automaticResetCapability(true)
                .name("DESKTOP-12345")
                .caption("Workstation PC")
                .description("High-end office workstation")
                .manufacturer("ASUSTeK COMPUTER INC.")
                .model("ProArt B760-Creator D4")
                .primaryOwnerName("User")
                .primaryOwnerContact("user@example.com")
                .roles(Arrays.asList("LM_Workstation", "LM_Server"))
                .chassisSKUNumber("PROD-1234")
                .systemSKUNumber("Default String")
                .systemFamily("Desktop")
                .systemType("x64-based PC")
                .userName("DESKTOP-12345\\User")
                .workgroup("WORKGROUP")
                .oemStringArray(Collections.singletonList("Default String"))
                .numberOfProcessors(1L)
                .numberOfLogicalProcessors(20L)
                .totalPhysicalMemory(BigInteger.valueOf(17122615296L))
                .automaticManagedPagefile(true)
                .infraredSupported(false)
                .networkServerModeEnabled(true)
                .hypervisorPresent(false)
                .thermalState(3)
                .currentTimeZone(330)
                .daylightInEffect(true)
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("AdminPasswordStatus", 3);
        obj.addProperty("KeyboardPasswordStatus", 3);
        obj.addProperty("PowerOnPasswordStatus", 3);
        obj.addProperty("BootupState", "Normal boot");
        obj.add("BootStatus", new Gson().toJsonTree(Arrays.asList(0, 1)));
        obj.addProperty("AutomaticResetBootOption", true);
        obj.addProperty("PowerState", 1);
        obj.addProperty("PowerSupplyState", 3);
        obj.add("PowerManagementCapabilities", new Gson().toJsonTree(Arrays.asList(1, 2, 3)));
        obj.addProperty("PowerManagementSupported", true);
        obj.addProperty("ResetCapability", 4);
        obj.addProperty("ResetCount", 1);
        obj.addProperty("ResetLimit", 5);
        obj.addProperty("FrontPanelResetStatus", 2);
        obj.addProperty("AutomaticResetCapability", true);
        obj.addProperty("Name", "DESKTOP-12345");
        obj.addProperty("Caption", "Workstation PC");
        obj.addProperty("Description", "High-end office workstation");
        obj.addProperty("Manufacturer", "ASUSTeK COMPUTER INC.");
        obj.addProperty("Model", "ProArt B760-Creator D4");
        obj.addProperty("PrimaryOwnerName", "User");
        obj.addProperty("PrimaryOwnerContact", "user@example.com");
        obj.add("Roles", new Gson().toJsonTree(Arrays.asList("LM_Workstation", "LM_Server")));
        obj.addProperty("ChassisSKUNumber", "PROD-1234");
        obj.addProperty("SystemSKUNumber", "Default String");
        obj.addProperty("SystemFamily", "Desktop");
        obj.addProperty("SystemType", "x64-based PC");
        obj.addProperty("UserName", "DESKTOP-12345\\User");
        obj.addProperty("Workgroup", "WORKGROUP");
        obj.add("OEMStringArray", new Gson().toJsonTree(Collections.singletonList("Default String")));
        obj.addProperty("NumberOfProcessors", 1);
        obj.addProperty("NumberOfLogicalProcessors", 20);
        obj.addProperty("TotalPhysicalMemory", 17122615296L);
        obj.addProperty("AutomaticManagedPagefile", true);
        obj.addProperty("InfraredSupported", false);
        obj.addProperty("NetworkServerModeEnabled", true);
        obj.addProperty("HypervisorPresent", false);
        obj.addProperty("ThermalState", 3);
        obj.addProperty("CurrentTimeZone", 330);
        obj.addProperty("DaylightInEffect", true);

        json = new GsonBuilder().serializeNulls().create().toJson(obj);
    }


    @BeforeEach
    void setUp() {
        systemService = new Win32ComputerSystemService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            Optional<Win32ComputerSystem> system = systemService.get();
            assertTrue(system.isPresent());
            assertThat(system.get()).usingRecursiveComparison().isEqualTo(expectedComputerSystem);
        }
    }

    @Test
    void test_get_emptyJson_returnsEmpty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            Optional<Win32ComputerSystem> system = systemService.get();
            assertFalse(system.isPresent());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (MockedStatic<PowerShell> mockedPowershell = mockStatic(PowerShell.class)) {
            mockedPowershell.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> systemService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            Optional<Win32ComputerSystem> system = systemService.get(mockShell);
            assertTrue(system.isPresent());
            assertThat(system.get()).usingRecursiveComparison().isEqualTo(expectedComputerSystem);
        }
    }

    @Test
    void test_getWithSession_emptyJson_returnsEmpty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            Optional<Win32ComputerSystem> system = systemService.get(mockShell);
            assertFalse(system.isPresent());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("invalid json");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> systemService.get(mockShell));
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
        Field[] declaredClassFields = Win32ComputerSystem.class.getDeclaredFields();
        Set<String> serializedNames = new HashSet<>();

        for(Field field: declaredClassFields){
            SerializedName s = field.getAnnotation(SerializedName.class);
            serializedNames.add(s!=null ? s.value() : field.getName());
        }

        // Extract JSON keys from the static test JSON
        Set<String> jsonKeys = new Gson().fromJson(json, JsonObject.class).keySet();

        // Validate equality of keys vs serialized names
        assertThat(serializedNames)
                .as("Entity fields and JSON keys must match exactly")
                .containsExactlyInAnyOrderElementsOf(jsonKeys);
    }
}
