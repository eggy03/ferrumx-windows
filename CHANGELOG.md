# Changelog

All notable changes to this project will be documented in this file.

Please check out the [Releases](https://github.com/eggy03/ferrumx-windows/releases) page to know more about the
commits and PRs that contributed to each of the releases.

This project tries its best to adhere to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [3.1.2] - January 28, 2026

### Dependency Updates

- Update AssertJ Core to 3.27.7 to mitigate CVE-2026-24400

## [3.1.1] - January 05, 2026

### Bug Fixes

- Fixed a bug in the `get(timeout)` service function in `Win32DiskDriveToPartitionAndLogicalDisk` class
that caused it to execute the wrong script and always fetch null values 1619a732365c2d210fa9b46defd43b9906d9cac0

### New Features

- add `architecture` property to `Win32Processor` b8aa0e87bd6c582b52bff3fd66344c05578c49f4

## [3.1.0] - December 30, 2025

### New Features

- Introduce `TerminalUtility` for executing PowerShell commands with a timeout. This utility uses Commons Exec instead
of `jPowerShell` to launch and close PowerShell sessions bc38f98a6bfe19dd2c0567a035ed3619551f53fb

- Service classes now implement a new `get(long timeout)` method which uses `TerminalUtility` and allows you to specify
how long the PowerShell session will run before it is forcefully terminated regardless of the completion status
of the script or command. This method does not rely on `jPowerShell` and can be safely used in Executor based workflows
unlike the other methods bc38f98a6bfe19dd2c0567a035ed3619551f53fb

### Removed Features

- None

### Bug Fixes

- None

### Breaking Changes

- None

### Non-Breaking Changes

- None

### Dependency Updates

- None

### Documentation

- Documented the new code
- Corrected`PCMCIA Type II (18)` to `PCMCIA Type I (18)` in `Win32PortConnector.java` b7556bf032289364a8fddd21c1e00aaccf53c117


### Known Issues

- `TerminalUtility` has support for UTF-8 encoding and English Language support only [Limitation]

---

## [3.0.0] - November 21, 2025

This update marks the first public release since v1.3.7 and is a complete re-write of the library.
Please consult the documentation in README, WIKI, Javadocs and Examples to know everything about the changes.

### New Features

*Added the following new and previously removed associated and simple WMI retrieval functions*

- Win32AssociatedProcessorMemory 4be83b3a8aba9e98e769ca7c76b68232516dcff5
- Win32NetworkAdapterSetting 520f69ecc138096aeb5caa17990ea7f8be469e96
- Win32DiskDriveToPartition 45390f10e23e69a2781739c32a888cafb71fa367
- Win32LogicalDisk cd6aa4998df21175f3fe9312c55dfa712a900365
- Win32LogicalDiskToPartition 21e737e1b945998ad880653ac2826a290d927ab3
- MSFTNetAdapter c575179b6f740feb329188f2dd19e38f16769ed9
- MSFTNetIPAddress, MSFTDNSClientServerAddress, MSFTNetConnectionProfile 762b43cce80451cde99f5f7180d1220af544e159
- Win32Environment 6d3605be184502f325212d85b33b5007140d43af
- Win32Printer 7098aaddf99688d4df7861b8b324e6e850661b62
- Win32UserAccount af7f8b931483f22b75c59a018e0a02091daec827
- Win32Process fd8c8d698acdfc633945a2a041ee83c4f64bf101
- Win32SoundDevice 4173b4e3a56f85e34a309a7f5a4c3d23487c769e
- Win32PnPDevice bbabdca77c4bddca5a1b596d5f8d8da8935c9b45

*Add the following new compounded WMI retrieval functions*

- HardwareId, 
- MsftNetAdapterToIpAndDnsAndProfile, 
- Win32DiskDriveToPartitionAndLogicalDisk,
- Win32DiskPartitionToLogicalDisk,
- Win32NetworkAdapterToConfiguration,
- Win32ProcessorToCacheMemory d25c37b2cf06489a7a6208cd3a43b0adf8d1af6d


### Removed Features

- N/A

### Bug Fixes

- Corrected the APM reversed query mapping (c9a33dbe5dfd4aa1d85dc8ea166fb1bb5901d523). This fix is related to a new feature introduced in this PR: AssociatedProcessorMemory (4be83b3a8aba9e98e769ca7c76b68232516dcff5)
- Refactor PowerShell script loading to use input stream (678c6c39a96140ca30b7d1bb38a12848b376cce2). This fix is related to a new feature introduced in this PR : Compounded Entity Classes (ca885e82e903ec69127d44c36dd425f0a0f2e672)

### Non-Breaking Changes

- Added a ReflectionUtility utility class (45a21c72bc529fd5fde9af17baf91efbef50349b).
This change solves issue #22 which dynamically retrieves the properties to fetch, from the entity classes, so that
only select properties to be fetched are passed on to the PowerShell query at runtime, instead of fetching all the properties.
- Add `@Nullable` annotations in missing entity fields 9da65f3d3bb83a6cbcd5bcce48d6ddd370290dca
- Added `trace` level logging for service methods 5d291c4c00129721d863420cb0dc66cf147fbe26
- Re-organize unit test package structure 121731bfdd0c28ab40cc409f5f4966479d1f8857
- Tests now validate entity fields 2597f911482536496047bc1eadbe99ceb47f16b1
- Test method name changes following the 2.2.0 Interface Update 26dbf045cca23af38ff13c8c3f3832266469c59e
- Tests added for all the new code
- Centralize dependency versions and generate de-lombok source.jar (pom.xml) 4b5832d896dcf2e8214629a40f511559a0b3cc31

### Breaking Changes

- Replace static MapperUtil with CommonMappingInterface c9f9390ae83998f28fa653f0a1a456f083d614f4
- Transfer Battery entities, services, mappers and tests to the peripheral package 93155e59f40739b51fa8b439f99b89857955fd6e
- Rename CimQuery to Cimv2Namespace and extracted the MSFT queries into StandardCimv2Namespace enum e79f5800461b0559383d7e79a437e4f7ccb12cde
- Replace Win32ComputerSystemProduct with Win32ComputerSystem WMI class 4723db1dcc632ae0fdd02e2368cb95dce16444da
- Move Win32OperatingSystem class to the system package
- Rename all the query enum names in Cimv2Namespace to have the "Win32_" prefix 6252187e6b7d5f8e245cc99fdc0ae788c4bb22f5
- Update the return types of certain Win32 Entity fields to match with the Microsoft docs bd206dc6c2ca48484f9a25aac8caefba29a4adb8
- Make Win32BaseboardService implement CommonServiceInterface instead of OptionalCommonServiceInterface c847d10708eb6874d5874f50225f3ffa739e9a31
- Entity fields which return Boolean now have custom getters instead of Lombok generated ones ff9e5d3e7fbdba8fafae16b2253656924c5ef39c

### Dependency Updates

- N/A

### Documentation

- Document all the new classes
- Add granular `@since` tags to individual methods
- Fix API usage examples e588a9be37dc280279919167120be9a4a03895ab
- Clarify use cases of the interfaces 714abe84440e5b3463c1da5260d57d354fee2f51
- Add package-info docs for base packages a33387fdf7b887c22e08d381d669f9b3074aab8d
- Update thread safety messages for entities 3562f80fddbdab826cd11f1ec72d7041af08bec7
- Document all the entity fields 5e30710c6cf6465df3aeb684332f94dfe73c67d9 2e11068d4b2d84525983c6ca95b709171acd844b 1efa49956cf1a7a469e79bf228a5eb050858121c c3217428da1be9bf120e3af3c02b58f212667d3c c548f3fde30f3dd7a1924870e0c119295e1da703 dd854697f534c3d0e6cd06086379f9f53617a69e
- Add copyright headers 88085e49eba59c21e07a812a75fe432ab061c22a

### Known Issues

- N/A

---

## [2.2.0 Pre-Release] - October 17, 2025

### New Features

- `Processor` class now has a new field known as `numberOfEnabledCores`

### Removed Features

- Deprecated `getProcessor()` method of return type `Optional<Processor>` has now been removed.

### Bug Fixes

- N/A

### Non-Breaking Changes

- The project is now Java 8 compatible

### Breaking Changes

- Reversed from multi-module project to single module. The module that contained examples have been shifted to a new repository.
- Base package has been renamed from `org.ferrumx` to `io.github.eggy03.ferrumx.windows` 6a669fa6f6aaa1c60ff655ebf1f7b3a764cfe5bf
- Maven `group id` changed from `io.github.egg-03` to `io.github.eggy03`. The artifact id has been changed from `ferrumx-core` to `ferrumx-windows` 2aa116415fc6a25f40efed73d2f23822a2469c1f
- `MapperUtil` has been moved from the `Utility` package to the `Mapper` package. 50dd942899e1c17aba1e710d0a9b166b5e7d23f7
- All service classes now implement either a `CommonServiceInterface<T>` or an `OptionalCommonServiceInterface<T>`. Accordingly, all service methods have been renamed to either `get()` or `get(Powershell powershell)`, defined in the interface contract. 046d405175e6d412d96d04274adca691d1580e75

### Dependency Updates

Updated dependencies: f92436ae4e560008879e21e00ec81fd5dc7228ad
- lombok: 1.18.38 -> 1.18.42
- org.jetbrains:annotations: 13.0 -> 26.0.2
- junit-jupiter-engine: 5.13.4 -> 6.0.0
- mockito-core: 5.19.0 -> 5.20.0

Updated plugins:
- maven-javadoc-plugin: 3.11.2 -> 3.12.0
- central-publishing-maven-plugin: 0.8.0 -> 0.9.0

### Documentation

- Updated documentation to reflect the changes in this version

### Known Issues

- N/A

---

## [2.1.0 Pre-Release] - October 5, 2025

### New Features

- Service classes now have overloaded service methods that accept a PowerShell session,
  allowing callers to manage and reuse sessions.

### Removed Features

- N/A

### Bug Fixes

- Fixed typo in the `@SerializedName` annotation for the `smbiosPresent`
  field that caused the serialized values to return null 20e6acf10a6618fcb6df70abda13b929a9f73fa2

### Non-Breaking Changes

- N/A

### Breaking Changes

- Deprecated the `getProcessor()` method of the `ProcessorService` class as it only returns a single processor
and fails in case of systems with multiple CPUs. It is now encouraged to use the `getProcessors()` method instead,
which returns a list of processors 85dae961c30cc88f4a905777086b2b45e1d8eb27

- Entity classes now have a builder pattern 70ea51d92d4c7dee0b6481762e29ac238b09bbed

### Dependency Updates

- N/A

### Documentation

- Updated documentation to reflect the changes in this version

### Known Issues

- N/A

---

## [2.0.1 Pre-Release] - September 29, 2025

### New Features

- N/A

### Removed Features

- N/A

### Bug Fixes

- Fix lombok scope configuration 59a495df3462f5f50ca3e00d9c49139075f61174

### Non-Breaking Changes

- N/A

### Breaking Changes

- N/A

### Dependency Updates

- N/A

### Documentation

- N/A

### Known Issues

- N/A

---

## [2.0.0 Pre-Release] - September 29, 2025

This change features a complete re-write of the project from ground up. The legacy Map<String, String> structure
has been replaced with a more fluent Entity-Service structure. No new features have been added but a lot of features
have been removed which will slowly be re-added with the upcoming pre-view releases.

### New Features

- A new class `ComputerSystemProductService` fetches detailed product information like vendor, name, and UUID.

### Removed Features

- Removed custom HWID generation logic.
- Removed `Win32_Printer` and `Win32_SoundDevice` classes.
- Removed the associative classes `Win32_AssociatedProcessorMemory`, `Win32_NetworkAdapterSetting`, `Win32_DiskDriveToDiskPartition` and `Win32_LogicalDiskToPartition`


### Bug Fixes

- N/A

### Non-Breaking Changes

- N/A

### Breaking Changes

- The legacy shell classes and custom parsing logic have been completely removed and replaced with a new service/entity structure.
- Each service now runs a PowerShell query via `jPowershell` that parses the JSON output to its respective entity class via GSON. Instead of a Map data structure in v1, you now get typed objects with their fields which are accessible via the provided getters.
- Removed all forced checked exceptions. The only time an unchecked exception may be thrown is if the JSON is malformed,
or if a PowerShell session fails to load.
- Improved null safety with the usage of Optional and empty Lists.
- Multi-Module Project: The codebase is now split into a multi-module Maven project:
*_ferrumx-core_*: Contains the main library logic.
*_ferrumx-examples_*: Provides practical usage demonstrations.

- Package Refactoring: All core packages have been moved under the `org.ferrumx.core` namespace, and the root package was renamed from `com.ferrumx` to `org.ferrumx` to align with open-source conventions.


### Dependency Updates

- Added `jPowershell` 3.1.1
- Added Google `gson` 2.13.2
- Added `lombok` 1.18.38
- Added `Jetbrains Annotations` 13.0
- Added `Mockito `Core` 5.19.0

### Documentation

- New documentation added for new code

### Known Issues

- N/A

---

## [1.3.7] - August 7, 2025


### New Features

- N/A

### Removed Features

- N/A

### Bug Fixes

- N/A

### Non-Breaking Changes

- N/A

### Breaking Changes

- N/A

### Dependency Updates

- Bump `org.apache.commons:commons-lang3` from 3.17.0 to 3.18.0
- Bump `commons-codec` from 1.18.0 to 1.19.0
- Bump `central-publishing-maven-plugin` from 0.7.0 to 0.8.0
- Bump `junit-jupiter-engine` from 5.13.0-M2 to 5.13.4

### Documentation

- N/A

### Known Issues

- N/A

---

## [1.3.6] - April 28, 2025


### New Features

- N/A

### Removed Features

- N/A

### Bug Fixes

- N/A

### Non-Breaking Changes

- HardwareID now supports collection of multiple CPU IDs in case of systems where there is more than 1 Physical CPU

### Breaking Changes

- N/A

### Dependency Updates

- N/A

### Documentation

- N/A

### Known Issues

- N/A

---

## [1.3.5] - April 28, 2025


### New Features

- N/A

### Removed Features

- N/A

### Bug Fixes

- N/A

### Non-Breaking Changes

- HardwareID now generates a SHA256 digest of CPUID, MotherboardID and DiskIDs.

### Breaking Changes

- Renamed the methods in the parser classes (CIM_ML and CIM_SL) to for a better understanding of what they do

### Dependency Updates

- N/A

### Documentation

- N/A

### Known Issues

- N/A

---

## [1.3.4] - April 9, 2025


### New Features

- N/A

### Removed Features

- N/A

### Bug Fixes

- N/A

### Non-Breaking Changes

- Removed custom `tinylog.properties` 8060684eee2dcbbef25b206e9626da472774f67d
- This will be replaced by SLF-4J API in the future

### Breaking Changes

- N/A

### Dependency Updates

- N/A

### Documentation

- N/A

### Known Issues

- N/A

---

## [1.3.3] - April 8, 2025


### New Features

- N/A

### Removed Features

- N/A

### Bug Fixes

- N/A

### Non-Breaking Changes

- Project now supports Maven Build Tools

### Breaking Changes

- Associated classes have been moved to a separate package with its custom formatter 64a0fe8acab298b4f5bf98be16cb1ecebb715352.
This has caused the following imports to change:

```java
    import com.ferrumx.system.hardware.Win32_AssociatedProcessorMemory;
    import com.ferrumx.system.networking.Win32_NetworkAdapterSetting;
    import com.ferrumx.system.operating_system.Win32_DiskDriveToDiskPartition;
    import com.ferrumx.system.operating_system.Win32_LogicalDiskToPartition;
```
to

```java
    import com.ferrumx.system.associatedclasses.Win32_AssociatedProcessorMemory;
    import com.ferrumx.system.associatedclasses.Win32_NetworkAdapterSetting;
    import com.ferrumx.system.associatedclasses.Win32_DiskDriveToDiskPartition;
    import com.ferrumx.system.associatedclasses.Win32_LogicalDiskToPartition;
```

### Dependency Updates

- N/A

### Documentation

- N/A

### Known Issues

- N/A

---

## [1.3.2] - December 25, 2024


### New Features

- N/A

### Removed Features

- N/A

### Bug Fixes

- N/A

### Non-Breaking Changes

- Fixed some typos in the Javadoc, removed the version tag and updated references
- Normal tests converted to JUnit Test Cases
- Reduced duplicated code


### Breaking Changes

- N/A

### Dependency Updates

- N/A

### Documentation

- N/A

### Known Issues

- N/A

---

## [1.3.1] - December 11, 2024


### New Features

- N/A

### Removed Features

- N/A

### Bug Fixes

- N/A

### Non-Breaking Changes

- Removed the ErrorLog class which was deprecated since v1.2.4
- The GUI code has been moved to a new repository called FerrumX-GUI

### Breaking Changes

- N/A

### Dependency Updates

- N/A

### Documentation

- N/A

### Known Issues

- N/A

---

## [1.3.0] - September 11, 2024

*GUI Changelogs are omitted since they are out of scope for the library. GUI code is maintained in a separate repository.*

### New Features

- Added two new battery classes: Win32_Battery and Win32_PortableBattery
- Added a monitor class: Win32_DesktopMonitor

### Removed Features

- N/A

### Bug Fixes

- N/A

### Non-Breaking Changes

- N/A

### Breaking Changes

- *Deprecate the custom ErrorLog:* Up till version 1.2.4, all PowerShell errors were automatically logged in a text file. Starting from v1.3.0, this behavior has been replaced with a custom exception called ShellException that gets thrown in case of any PowerShell errors. All the Win32 classes now rethrow this exception. The developer needs to catch this exception and either rethrow it or handle it accordingly.

### Dependency Updates

- N/A

### Documentation

- N/A

### Known Issues

- N/A

---

## [1.2.4] - June 23, 2024

*GUI Changelogs are omitted since they are out of scope for the library. GUI code is maintained in a separate repository.*

### New Features

- N/A

### Removed Features

- N/A

### Bug Fixes

- N/A

### Non-Breaking Changes

HardwareID changes:
- Removed Username, DeviceName, RAM Count and Storage Count
- Added DriveIDs

New ID format: CPUName/CPUID/MotherboardName/DriveIDs [58741d85f84727720c90dad059e3596771c1e396]

### Breaking Changes

- N/A

### Dependency Updates

- N/A

### Documentation

- N/A

### Known Issues

- N/A

---

## [1.2.3] - June 21, 2024

*GUI Changelogs are omitted since they are out of scope for the library. GUI code is maintained in a separate repository.*

### New Features

- N/A

### Removed Features

- N/A

### Bug Fixes

- N/A

### Non-Breaking Changes

- Removed the unnecessary hard coded System Drive letter from the commands as it does not need them

### Breaking Changes

- N/A

### Dependency Updates

- N/A

### Documentation

- N/A

### Known Issues

- N/A

---

## [1.2.2] - June 11, 2024

*GUI Changelogs are omitted since they are out of scope for the library. GUI code is maintained in a separate repository.*

### New Features

- N/A

### Removed Features

- N/A

### Bug Fixes

- N/A

### Non-Breaking Changes

- The ExecutorService object in HardwareID class now uses the AutoCloseable interface
- The implicit constructor of HardwareID class has it's access modifier changed from private to protected to allow inheritance

### Breaking Changes

- N/A

### Dependency Updates

- N/A

### Documentation

- N/A

### Known Issues

- N/A

---

## [1.2.1] - June 11, 2024

*GUI Changelogs are omitted since they are out of scope for the library. GUI code is maintained in a separate repository.*

### New Features

- Win32_Processor class includes a new property called "NumberOfLogicalProcessors".
This is different from "ThreadCount" in a way that "ThreadCount always reports the number of hardware threads in a CPU,
whereas the other reports the number of threads that the OS has been allowed to access.
For example, you have a CPU that has 12 hardware threads, and you have configured your OS to boot with 8 threads.
In this case, the "ThreadCount" would be 12 and "NumberOfLogicalProcessors" would be 8.

### Removed Features

- N/A

### Bug Fixes

- N/A

### Non-Breaking Changes

- N/A

### Breaking Changes

- N/A

### Dependency Updates

- N/A

### Documentation

- N/A

### Known Issues

- N/A

---

## [1.2.0] - May 7, 2024

*GUI Changelogs are omitted since they are out of scope for the library. GUI code is maintained in a separate repository.*

### New Features

- Only GUI updates

### Removed Features

- N/A

### Bug Fixes

- N/A

### Non-Breaking Changes

- N/A

### Breaking Changes

- N/A

### Dependency Updates

- N/A

### Documentation

- N/A

### Known Issues

- N/A

---

## [1.1.0] - May 7, 2024

*GUI Changelogs are omitted since they are out of scope for the library. GUI code is maintained in a separate repository.*

### New Features

- N/A

### Removed Features

- N/A

### Bug Fixes

- N/A

### Non-Breaking Changes

- N/A

### Breaking Changes

- The formatters are now classified into CIM_ML and CIM_SL
ML stands for Multi-Line and SL for Single-Line
The Win32 Relation Classes will still have their own formatter.
The rest of the Win32_Classes will call either CIM_SL or CIM_ML for formatting.
The Win32_Classes now call either CIM_SL or CIM_ML methods and pass on the attributes, which then carry out the parsing,
formatting and error handling.
HWID Generation retains it's Multi-threading capability following CIM_SL refactor
The Win32 Relation Classes still have their own formatter.

- Win32_SystemDrivers has been removed
- Changed the package naming scheme from `com.egg.*` to `com.ferrumx.*`


### Dependency Updates

- N/A

### Documentation

- N/A

### Known Issues

- N/A

---

## [1.0.2] - April 16, 2024

*GUI Changelogs are omitted since they are out of scope for the library. GUI code is maintained in a separate repository.*

### New Features

- N/A

### Removed Features

- N/A

### Bug Fixes

- Removed () from the class name literal [980c1a36e42d5f1be9f31c82926c45bcbc44f1e4]
- Fixed an error in naming a property in `Win32_Printer` which caused the property's value to be not displayed [1bf322074981bb695cbb96abe0cbefed29ab9153]
- Fixed HWID generation functions [dce061f23799fcc8877786bce3da5a07ff5d6129]
They will now be generated based on the following nomenclature: Username/DeviceName/CPU/CPUID/MotherboardName/RAM-COUNT/STORAGE-COUNT

### Non-Breaking Changes

- Log files will now be generated as FerrumX_ERRORLOG-DateStamp.log [e76dafc19c69cc2da3cc4aa41ae8fbe499f4b426]

### Breaking Changes

- N/A

### Dependency Updates

- N/A

### Documentation

- N/A

### Known Issues

- N/A

---

## [1.0.1] - April 13, 2024

*GUI Changelogs are omitted since they are out of scope for the library. GUI code is maintained in a separate repository.*

### New Features

- N/A

### Removed Features

- N/A

### Bug Fixes

- N/A

### Non-Breaking Changes

- Shifted WMIC to FerrumL (a.k.a. Ferrum Legacy) that supports Windows Vista and Windows 7
This does not break functionality since none of the existing functions depend on WMIC
for information retrieval

- If a PowerShell process fails, the functions will now return empty Collections.

### Breaking Changes

- Changed `Win32_Processor` function name from `getDeviceIDList()` to `getProcessorList()`

### Dependency Updates

- N/A

### Documentation

- N/A

### Known Issues

- N/A

---

## [1.0.0] - April 10, 2024

*GUI Changelogs are omitted since they are out of scope for the library. GUI code is maintained in a separate repository.*

Project name changed from `WSIL` to `FerrumX`

### New Features

- A semi-unique, multithreaded HWID Generation function based on the following format:
  Username/Device-name/CPUName/CPUID/MotherboardName/RAMCount/StorageCount

### Removed Features

- N/A

### Bug Fixes

- Accommodated for multi-line value parsing for property values spanning more than a single line in PowerShell

### Non-Breaking Changes

- Update CIMFormat to make it able to log PowerShell errors and support multi-line property values

### Breaking Changes

- Removed the Bank variable from `Win32_PhysicalMemory` as a form of device ID collection method (when Tag cannot be found)
as it may no longer be necessary

### Dependency Updates

- N/A

### Documentation

- N/A

### Known Issues

- N/A

---