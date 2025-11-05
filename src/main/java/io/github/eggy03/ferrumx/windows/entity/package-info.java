/**
 * Contains immutable representations of Windows WMI and CIM classes.
 * <p>
 * Each class in this package directly maps to a corresponding WMI or CIM class
 * (e.g., {@code Win32_DesktopMonitor}, {@code Win32_Processor}, {@code MSFT_NetAdapter}).
 * PowerShell query results are deserialized into instances of these entity classes.
 * </p>
 * <p>
 * All entities are designed to be immutable and thread-safe, allowing them
 * to be safely shared or cached across threads.
 * </p>
 * <p>
 *     Not all classes may represent a WMI or CIM class
 * </p>
 *
 * @since 2.0.0
 * @author Egg-03
 */
package io.github.eggy03.ferrumx.windows.entity;
