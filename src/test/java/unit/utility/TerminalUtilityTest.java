package unit.utility;

import io.github.eggy03.ferrumx.windows.exception.TerminalExecutionException;
import io.github.eggy03.ferrumx.windows.utility.TerminalUtility;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * WARNING: This test will require a valid Windows system to pass because it spawns actual PowerShell sessions
 */
class TerminalUtilityTest {

    private static final long TIMEOUT = 15L;

    @Test
    void testValidCommand() {
        String validCommand = "10+20";
        String result = TerminalUtility.executeCommand(validCommand, TIMEOUT);
        assertThat(result).isEqualTo("30" + System.lineSeparator()); // PowerShell appends a new line after each command
    }

    @Test
    void testInvalidCommand() {
        assertThrows(TerminalExecutionException.class, () -> TerminalUtility.executeCommand("invalidCommand", TIMEOUT));
    }

    @Test
    void testValidScript() {
        String validScript = "$a=10\n$a++\n$a";
        String result = TerminalUtility.executeCommand(validScript, TIMEOUT);
        assertThat(result).isEqualTo("11" + System.lineSeparator());
    }

    @Test
    void testValidScriptFromResource() {

        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(TerminalUtilityTest.class.getResourceAsStream("/SimpleAdditionScript.ps1"))));
        StringBuilder script = new StringBuilder();
        reader.lines().forEach(line -> script.append(line).append(System.lineSeparator()));

        String result = TerminalUtility.executeCommand(script.toString(), TIMEOUT);
        assertThat(result).isEqualTo("3" + System.lineSeparator());
    }

    @Test
    void testTimeout() {
        String sleepCommand = "Start-Sleep -Seconds 30";
        TerminalExecutionException ex = assertThrows(TerminalExecutionException.class, () -> TerminalUtility.executeCommand(sleepCommand, 1));
        assertThat(ex.getMessage()).contains("Was killed after a timeout");
    }

    @Test
    void testErrorStream() {
        String errorCommand = "Write-Error \"fail\"";
        TerminalExecutionException ex = assertThrows(TerminalExecutionException.class, () -> TerminalUtility.executeCommand(errorCommand, TIMEOUT));
        assertThat(ex.getMessage())
                .contains("Terminal Error Output")
                .contains("fail");
    }

    @Test
    void testMixedOutput() {
        String mixedCommand = "Write-Output \"ok\"; Write-Error \"fail\"";

        TerminalExecutionException ex = assertThrows(TerminalExecutionException.class, () -> TerminalUtility.executeCommand(mixedCommand, TIMEOUT));
        assertThat(ex.getMessage())
                .contains("Terminal Error Output")
                .contains("ok")
                .contains("fail");

    }

    @Test
    void testNegativeTimeout() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> TerminalUtility.executeCommand("Write-Output \"Hello\"", -1));
        assertThat(ex.getMessage()).isEqualTo("Timeout cannot be negative");
    }
}
