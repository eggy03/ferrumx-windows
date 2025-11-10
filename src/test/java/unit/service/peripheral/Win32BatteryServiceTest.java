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
import io.github.eggy03.ferrumx.windows.entity.peripheral.Win32Battery;
import io.github.eggy03.ferrumx.windows.service.peripheral.Win32BatteryService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.Arrays;
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

class Win32BatteryServiceTest {

    private Win32BatteryService batteryService;

    private static Win32Battery expectedPrimaryBattery;
    private static Win32Battery expectedSecondaryBattery;

    private static String json;

    @BeforeAll
    static void setBatteries() {
        expectedPrimaryBattery = Win32Battery.builder()
                .deviceId("BAT0")
                .caption("Primary Battery")
                .description("Internal Lithium-Ion Battery")
                .name("Battery #1")
                .status("OK")
                .powerManagementCapabilities(Arrays.asList(1, 2, 3))
                .powerManagementSupported(true)
                .batteryStatus(2)
                .chemistry(6)
                .designCapacity(50000)
                .designVoltage(11000)
                .estimatedChargeRemaining(87L)
                .estimatedRunTime(120L)
                .build();

        expectedSecondaryBattery = Win32Battery.builder()
                .deviceId("BAT1")
                .caption("Backup Battery")
                .description("External Lithium-Polymer Battery")
                .name("Battery #2")
                .status("Charging")
                .powerManagementCapabilities(Arrays.asList(1, 2))
                .powerManagementSupported(true)
                .batteryStatus(6)
                .chemistry(7)
                .designCapacity(30000)
                .designVoltage(7400)
                .estimatedChargeRemaining(45L)
                .estimatedRunTime(60L)
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonArray batteries = new JsonArray();

        JsonObject bat0 = new JsonObject();
        bat0.addProperty("DeviceID", "BAT0");
        bat0.addProperty("Caption", "Primary Battery");
        bat0.addProperty("Description", "Internal Lithium-Ion Battery");
        bat0.addProperty("Name", "Battery #1");
        bat0.addProperty("Status", "OK");
        bat0.add("PowerManagementCapabilities", new Gson().toJsonTree(Arrays.asList(1, 2, 3)));
        bat0.addProperty("PowerManagementSupported", true);
        bat0.addProperty("BatteryStatus", 2);
        bat0.addProperty("Chemistry", 6);
        bat0.addProperty("DesignCapacity", 50000);
        bat0.addProperty("DesignVoltage", 11000);
        bat0.addProperty("EstimatedChargeRemaining", 87L);
        bat0.addProperty("EstimatedRunTime", 120L);

        JsonObject bat1 = new JsonObject();
        bat1.addProperty("DeviceID", "BAT1");
        bat1.addProperty("Caption", "Backup Battery");
        bat1.addProperty("Description", "External Lithium-Polymer Battery");
        bat1.addProperty("Name", "Battery #2");
        bat1.addProperty("Status", "Charging");
        bat1.add("PowerManagementCapabilities", new Gson().toJsonTree(Arrays.asList(1, 2)));
        bat1.addProperty("PowerManagementSupported", true);
        bat1.addProperty("BatteryStatus", 6);
        bat1.addProperty("Chemistry", 7);
        bat1.addProperty("DesignCapacity", 30000);
        bat1.addProperty("DesignVoltage", 7400);
        bat1.addProperty("EstimatedChargeRemaining", 45L);
        bat1.addProperty("EstimatedRunTime", 60L);

        batteries.add(bat0);
        batteries.add(bat1);

        json = new GsonBuilder().serializeNulls().create().toJson(batteries);
    }


    @BeforeEach
    void setUp() {
        batteryService = new Win32BatteryService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32Battery> batteries = batteryService.get();
            assertEquals(2, batteries.size());

            assertThat(batteries.get(0)).usingRecursiveComparison().isEqualTo(expectedPrimaryBattery);
            assertThat(batteries.get(1)).usingRecursiveComparison().isEqualTo(expectedSecondaryBattery);
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32Battery> batteries = batteryService.get();
            assertTrue(batteries.isEmpty());
        }
    }

    @Test
    void test_get_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> batteryService.get());
        }
    }

    @Test
    void test_getWithSession_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32Battery> batteries = batteryService.get(mockSession);
            assertEquals(2, batteries.size());

            assertThat(batteries.get(0)).usingRecursiveComparison().isEqualTo(expectedPrimaryBattery);
            assertThat(batteries.get(1)).usingRecursiveComparison().isEqualTo(expectedSecondaryBattery);
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32Battery> batteries = batteryService.get(mockSession);
            assertTrue(batteries.isEmpty());
        }
    }

    @Test
    void test_getWithSession_malformedJson_throwsException() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("not a json");

        try (PowerShell mockSession = mock(PowerShell.class)) {
            when(mockSession.executeCommand(anyString())).thenReturn(mockResponse);

            assertThrows(JsonSyntaxException.class, () -> batteryService.get(mockSession));
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
        Field[] declaredClassFields = Win32Battery.class.getDeclaredFields();
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