package unit.service.system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.system.Win32PnPEntity;
import io.github.eggy03.ferrumx.windows.service.system.Win32PnPEntityService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
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

class Win32PnPEntityServiceTest {

    private Win32PnPEntityService service;

    private static Win32PnPEntity expectedDevice1;
    private static Win32PnPEntity expectedDevice2;

    private static String json;

    @BeforeAll
    static void setupPnPEntities() {
        expectedDevice1 = Win32PnPEntity.builder()
                .deviceId("USB\\VID_045E&PID_07A5\\6&1A2C0F8&0&2")
                .pnpDeviceId("USB\\VID_045E&PID_07A5\\6&1A2C0F8&0&2")
                .hardwareId(Collections.singletonList("USB\\VID_045E&PID_07A5&REV_0100"))
                .compatibleId(Collections.singletonList("USB\\Class_03&SubClass_01&Prot_02"))
                .name("USB ComppnpEntityite Device")
                .description("Generic USB ComppnpEntityite Device")
                .manufacturer("MicrpnpEntityoft")
                .present(true)
                .status("OK")
                .build();

        expectedDevice2 = Win32PnPEntity.builder()
                .deviceId("PCI\\VEN_10DE&DEV_1C82&SUBSYS_85AE1043&REV_A1\\4&2D77E6E1&0&0008")
                .pnpDeviceId("PCI\\VEN_10DE&DEV_1C82&SUBSYS_85AE1043&REV_A1\\4&2D77E6E1&0&0008")
                .hardwareId(Arrays.asList("PCI\\VEN_10DE&DEV_1C82&SUBSYS_85AE1043", "PCI\\VEN_10DE&DEV_1C82"))
                .compatibleId(Collections.singletonList("PCI\\CC_030000"))
                .name("NVIDIA GeForce GTX 1050 Ti")
                .description("Display Adapter")
                .manufacturer("NVIDIA")
                .present(true)
                .status("OK")
                .build();
    }

    @BeforeAll
    static void setupJson() {
        Gson gson = new GsonBuilder().serializeNulls().create();

        JsonArray array = new JsonArray();

        JsonObject obj1 = new JsonObject();
        obj1.addProperty("DeviceID", "USB\\VID_045E&PID_07A5\\6&1A2C0F8&0&2");
        obj1.addProperty("PNPDeviceID", "USB\\VID_045E&PID_07A5\\6&1A2C0F8&0&2");
        obj1.add("HardwareID", gson.toJsonTree(Collections.singletonList("USB\\VID_045E&PID_07A5&REV_0100")));
        obj1.add("CompatibleID", gson.toJsonTree(Collections.singletonList("USB\\Class_03&SubClass_01&Prot_02")));
        obj1.addProperty("Name", "USB ComppnpEntityite Device");
        obj1.addProperty("Description", "Generic USB ComppnpEntityite Device");
        obj1.addProperty("Manufacturer", "MicrpnpEntityoft");
        obj1.addProperty("Present", true);
        obj1.addProperty("Status", "OK");

        JsonObject obj2 = new JsonObject();
        obj2.addProperty("DeviceID", "PCI\\VEN_10DE&DEV_1C82&SUBSYS_85AE1043&REV_A1\\4&2D77E6E1&0&0008");
        obj2.addProperty("PNPDeviceID", "PCI\\VEN_10DE&DEV_1C82&SUBSYS_85AE1043&REV_A1\\4&2D77E6E1&0&0008");
        obj2.add("HardwareID", gson.toJsonTree(Arrays.asList("PCI\\VEN_10DE&DEV_1C82&SUBSYS_85AE1043", "PCI\\VEN_10DE&DEV_1C82")));
        obj2.add("CompatibleID", gson.toJsonTree(Collections.singletonList("PCI\\CC_030000")));
        obj2.addProperty("Name", "NVIDIA GeForce GTX 1050 Ti");
        obj2.addProperty("Description", "Display Adapter");
        obj2.addProperty("Manufacturer", "NVIDIA");
        obj2.addProperty("Present", true);
        obj2.addProperty("Status", "OK");

        array.add(obj1);
        array.add(obj2);

        json = gson.toJson(array);
    }

    @BeforeEach
    void setUp() {
        service = new Win32PnPEntityService();
    }

    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32PnPEntity> pnpEntity = service.get();
            assertEquals(2, pnpEntity.size());
            assertThat(pnpEntity.get(0)).usingRecursiveComparison().isEqualTo(expectedDevice1);
            assertThat(pnpEntity.get(1)).usingRecursiveComparison().isEqualTo(expectedDevice2);
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32PnPEntity> pnpEntity = service.get();
            assertTrue(pnpEntity.isEmpty());
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

            List<Win32PnPEntity> pnpEntity = service.get(mockShell);
            assertEquals(2, pnpEntity.size());
            assertThat(pnpEntity.get(0)).usingRecursiveComparison().isEqualTo(expectedDevice1);
            assertThat(pnpEntity.get(1)).usingRecursiveComparison().isEqualTo(expectedDevice2);
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32PnPEntity> pnpEntity = service.get(mockShell);
            assertTrue(pnpEntity.isEmpty());
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
        Field[] declaredClassFields = Win32PnPEntity.class.getDeclaredFields();
        Set<String> serializedNames = new HashSet<>();

        for(Field field: declaredClassFields){
            SerializedName s = field.getAnnotation(SerializedName.class);
            serializedNames.add(s!=null ? s.value() : field.getName());
        }

        // Extract JSON keys from the static test JSON
        Set<String> jsonKeys = new Gson().fromJson(json, JsonArray.class).get(0).getAsJsonObject().keySet();

        // Validate equality of keys vs serialized names
        assertThat(serializedNames)
                .as("Entity fields and JSON keys must match exactly")
                .containsExactlyInAnyOrderElementsOf(jsonKeys);
    }
}