/*
 * © 2024–2025 Sayan Bhattacharjee (Alias: Egg-03/Eggy)
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package unit.service.user;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import io.github.eggy03.ferrumx.windows.entity.user.Win32UserAccount;
import io.github.eggy03.ferrumx.windows.service.user.Win32UserAccountService;
import io.github.eggy03.ferrumx.windows.utility.TerminalUtility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
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

class Win32UserAccountTest {

    private Win32UserAccountService service;
    
    private static Win32UserAccount expectedUser1;
    private static Win32UserAccount expectedUser2;
    private static String json;

    @BeforeAll
    static void setupUsers() {
        expectedUser1 = Win32UserAccount.builder()
                .sid("S-1-5-21-1234567890-1001")
                .sidType(1)
                .accountType(512L)
                .caption("User1")
                .description("Local user account")
                .domain("WORKGROUP")
                .name("Egg-03")
                .disabled(false)
                .localAccount(true)
                .lockout(false)
                .passwordRequired(true)
                .passwordExpires(false)
                .passwordChangeable(true)
                .status("OK")
                .build();

        expectedUser2 = Win32UserAccount.builder()
                .sid("S-1-5-21-0987654321-1002")
                .sidType(1)
                .accountType(512L)
                .caption("User2")
                .description("Administrator account")
                .domain("WORKGROUP")
                .name("Admin")
                .disabled(false)
                .localAccount(true)
                .lockout(false)
                .passwordRequired(true)
                .passwordExpires(true)
                .passwordChangeable(true)
                .status("OK")
                .build();
    }

    @BeforeAll
    static void setupJson() {
        JsonArray users = new JsonArray();

        JsonObject user1 = new JsonObject();
        user1.addProperty("SID", "S-1-5-21-1234567890-1001");
        user1.addProperty("SIDType", 1);
        user1.addProperty("AccountType", 512L);
        user1.addProperty("Caption", "User1");
        user1.addProperty("Description", "Local user account");
        user1.addProperty("Domain", "WORKGROUP");
        user1.addProperty("Name", "Egg-03");
        user1.addProperty("Disabled", false);
        user1.addProperty("LocalAccount", true);
        user1.addProperty("Lockout", false);
        user1.addProperty("PasswordRequired", true);
        user1.addProperty("PasswordExpires", false);
        user1.addProperty("PasswordChangeable", true);
        user1.addProperty("Status", "OK");

        JsonObject user2 = new JsonObject();
        user2.addProperty("SID", "S-1-5-21-0987654321-1002");
        user2.addProperty("SIDType", 1);
        user2.addProperty("AccountType", 512L);
        user2.addProperty("Caption", "User2");
        user2.addProperty("Description", "Administrator account");
        user2.addProperty("Domain", "WORKGROUP");
        user2.addProperty("Name", "Admin");
        user2.addProperty("Disabled", false);
        user2.addProperty("LocalAccount", true);
        user2.addProperty("Lockout", false);
        user2.addProperty("PasswordRequired", true);
        user2.addProperty("PasswordExpires", true);
        user2.addProperty("PasswordChangeable", true);
        user2.addProperty("Status", "OK");

        users.add(user1);
        users.add(user2);

        json = new Gson().toJson(users);
    }
    
    @BeforeEach
    void setService() {
        service = new Win32UserAccountService();
    }
    @Test
    void test_get_success() {

        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn(json);

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32UserAccount> userList = service.get();
            assertEquals(2, userList.size());

            assertThat(userList.get(0)).usingRecursiveComparison().isEqualTo(expectedUser1);
            assertThat(userList.get(1)).usingRecursiveComparison().isEqualTo(expectedUser2);
        }
    }

    @Test
    void test_get_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (MockedStatic<PowerShell> powerShellMock = mockStatic(PowerShell.class)) {
            powerShellMock.when(() -> PowerShell.executeSingleCommand(anyString())).thenReturn(mockResponse);

            List<Win32UserAccount> userList = service.get();
            assertTrue(userList.isEmpty());
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

            List<Win32UserAccount> userList = service.get(mockShell);
            assertEquals(2, userList.size());

            assertThat(userList.get(0)).usingRecursiveComparison().isEqualTo(expectedUser1);
            assertThat(userList.get(1)).usingRecursiveComparison().isEqualTo(expectedUser2);
        }
    }

    @Test
    void test_getWithSession_empty() {
        PowerShellResponse mockResponse = mock(PowerShellResponse.class);
        when(mockResponse.getCommandOutput()).thenReturn("");

        try (PowerShell mockShell = mock(PowerShell.class)) {
            when(mockShell.executeCommand(anyString())).thenReturn(mockResponse);

            List<Win32UserAccount> userList = service.get(mockShell);
            assertTrue(userList.isEmpty());
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

        try(MockedStatic<TerminalUtility> mockedTerminal = mockStatic(TerminalUtility.class)){
            mockedTerminal
                    .when(()-> TerminalUtility.executeCommand(anyString(), anyLong()))
                    .thenReturn(json);

            List<Win32UserAccount> userList = service.get(5L);
            assertEquals(2, userList.size());

            assertThat(userList.get(0)).usingRecursiveComparison().isEqualTo(expectedUser1);
            assertThat(userList.get(1)).usingRecursiveComparison().isEqualTo(expectedUser2);
        }
    }

    @Test
    void test_getWithTimeout_invalidJson_throwsException() {

        try(MockedStatic<TerminalUtility> mockedTerminal = mockStatic(TerminalUtility.class)){
            mockedTerminal
                    .when(()-> TerminalUtility.executeCommand(anyString(), anyLong()))
                    .thenReturn("invalid json");

            assertThrows(JsonSyntaxException.class, ()-> service.get(5L));
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
        Field[] declaredClassFields = Win32UserAccount.class.getDeclaredFields();
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
