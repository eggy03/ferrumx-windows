/**
 * Contains classes responsible for deserializing PowerShell JSON output
 * to entity classes defined in the entity package.
 *
 * <p>Mapping classes in this package are typically:</p>
 * <ul>
 *   <li>Utilized by classes in the {@link io.github.eggy03.ferrumx.windows.service} package</li>
 *   <li>where they map the resulting JSON output from PowerShell</li>
 *   <li>into instances of entity classes defined in the {@link io.github.eggy03.ferrumx.windows.entity} package</li>
 * </ul>
 * <p>
 *      Most mappers in this package implement {@link io.github.eggy03.ferrumx.windows.mapping.CommonMappingInterface}
 *      to provide a consistent JSON deserialization mechanism.
 *      However, some mappers may define custom logic that does not require deserialization or use of the interface.
 * </p>
 * <p>
 *      <strong>Note:</strong> The contents of this package are intended for internal use within the library.
 * </p>
 *
 * @author Egg-03
 * @since 3.0.0
 */
package io.github.eggy03.ferrumx.windows.mapping;

