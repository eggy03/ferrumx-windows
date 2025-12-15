package io.github.eggy03.ferrumx.windows.utility;

import io.github.eggy03.ferrumx.windows.exception.TerminalExecutionException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;

@UtilityClass
@Slf4j
public class TerminalUtility {

    public static String executeCommand(String command, long timeoutSeconds) {

        CommandLine cmdLine = new CommandLine("powershell.exe");
        cmdLine.addArgument(command, false);

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        ExecuteWatchdog watchdog = ExecuteWatchdog.builder().setTimeout(Duration.ofSeconds(timeoutSeconds)).get();

        DefaultExecutor executor = DefaultExecutor.builder().get();
        executor.setStreamHandler(new PumpStreamHandler(result, err));
        executor.setWatchdog(watchdog);

        try {
            int exitCode = executor.execute(cmdLine);
            log.debug("\nCommand Executed: {}\nExit code: {}\nError Stream: {}\nResult Stream: {}\n", command, exitCode, err, result);
            return result.toString();
        } catch (ExecuteException e) {
            String reason = watchdog.killedProcess() ?
                    "\nProcess executing the following command: " + command + "\nWas killed after a timeout of " + timeoutSeconds + " seconds\n" :
                    "\nProcess executing the following command: " + command + "\nExited with a non-zero exit code\nTerminal Error Output: "+ err;

            throw new TerminalExecutionException(reason, e);
        } catch (IOException e) {
            String reason = "An I/O Exception occurred during executing the following command:\n" + command;
            throw new TerminalExecutionException(reason, e);
        }
    }
}
