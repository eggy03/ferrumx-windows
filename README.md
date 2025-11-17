[![Project Stats](https://openhub.net/p/ferrumx-windows/widgets/project_thin_badge.gif)](https://openhub.net/p/ferrumx-windows)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Egg-03_FerrumX&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Egg-03_FerrumX)
![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/eggy03/ferrumx-windows/.github%2Fworkflows%2Fbuild.yml)

[![License](https://img.shields.io/github/license/eggy03/ferrumx-windows)](https://github.com/eggy03/ferrumx-windows/blob/main/LICENSE)
[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.eggy03/ferrumx-windows)](https://central.sonatype.com/artifact/io.github.eggy03/ferrumx-windows)
![Commits to main since latest release](https://img.shields.io/github/commits-since/eggy03/ferrumx-windows/latest)
![Java Version](https://img.shields.io/badge/java-8%2B-blue)

# About
FerrumX-Windows is a lightweight Hardware and Network Information library written in pure Java. It contacts some [Computer System Hardware Classes](https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/computer-system-hardware-classes) and [Operating System Classes](https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/operating-system-classes) of Windows Management Instrumentation (WMI) through PowerShell to fetch comprehensive system details on Windows Operating Systems.

# Supported Operating Systems
The library has been tested to work exclusively on <strong>Windows 8.1, Windows 10 and Windows 11</strong> devices.
Windows 7 and earlier versions may be supported if PowerShell 5 can be installed

# Download
> **Minimum Supported Java Version:** 8

Maven:
```xml
<dependency>
    <groupId>io.github.eggy03</groupId>
    <artifactId>ferrumx-windows</artifactId>
    <version>3.0.0</version>
</dependency>
```

Gradle:
```gradle
implementation group: 'io.github.eggy03', name: 'ferrumx-windows', version: '3.0.0'
```

For other build ecosystems, check out the [Maven Central Repository](https://central.sonatype.com/artifact/io.github.eggy03/ferrumx-windows/overview)

> [!NOTE]
> The `sources.jar` published with this library includes de-lomboked code which should prevent the IDEs from complaining
> about source mismatch between the decompiled class files and the downloaded sources. It should also make your debugging
> easier, should you step into the library code during the debugging process of your project.

# Documentation
Documentation can be found [here](https://eggy03.github.io/ferrumx-windows-documentation/)

# Usage
> [!IMPORTANT]
> More usage examples can be found [here](https://github.com/eggy03/ferrumx-windows-examples).

```java
public class ProcessorExample {

    static void main(String[] args) {
        
        List<Win32Processor> processorList = new Win32ProcessorService().get();
        
        // you can also create and manage your own re-usable PowerShell session
        // good for cases where you need to fetch results for multiple queries
        try(PowerShell session = PowerShell.openSession()) {
            List<Win32Processor> processorListTwo = new Win32ProcessorService().get(session);
            processorListTwo.forEach(processor -> log.info(processor.toString()));
        }

        // individual fields are accessible via getter methods
        log.info("Processor Name: {}", processorList.get(0).getName());
        log.info("Processor Manufacturer: {}", processorList.get(0).getManufacturer());
        log.info("Processor Max Clock Speed: {} MHz", processorList.get(0).getMaxClockSpeed());
    }
}
```

# License
This project is licensed under the MIT License.

# Supported Versions

- v3.x.x - Actively Developed and Supported
- v2.x.x - Transitional Release (not maintained)
- v1.x.x - No longer actively maintained

# Information about v2 and v3

### About v2 Pre-release

The **2.x** versions (`v2.0.0`–`v2.2.0`) served as **pre-releases** that introduced the new service-layered architecture.

- **`v2.0.0`** was a complete rewrite of the project. See [v2.0.0 pre-release notes](https://github.com/eggy03/ferrumx-windows/releases/tag/v2.0.0)
- **`v2.1.0`** and **`v2.2.0`** built upon that foundation with architectural enhancements and breaking changes.
- Each 2.x pre-release was **incompatible** with the others.
- For details about these breaking changes, see the release notes linked at the end of this document.

These versions are made publicly available but will **no longer be supported** after the release of **`v3.0.0`**.

### About v3
**`v3.0.0`** is the stable release that is built upon the 2.x architecture.  
It introduces new MSFT classes and re-introduces several entities from **`v1.3.7`** that were not present in the 2.x series.
It also renames all of the `v2.x` classes to have a `Win32` prefix, similar to the `v1.x` naming convention.

### Migration guide

>[!NOTE]
> Migration guide can be found in the [wiki](https://github.com/eggy03/ferrumx-windows/wiki)

- Users on **`v1.3.7`** should **upgrade directly to `v3.0.0`**.
- The migration guide for migrating to **`v1.3.7` to `v3.0.0`** will be available in the [Wiki](https://github.com/eggy03/ferrumx-windows/wiki)
- No migration guide will be provided for migrating to **`v2.x` to `v3.0.0`**. However, you can check the release notes
at the [Releases Page](https://github.com/eggy03/ferrumx-windows/releases) or towards the end of this readme to see what changes have been made from `v2` to `v3`.

### Release Changelogs

- Changes from **`v1.3.7` to`v2.0.0`**: [Pull Request #20](https://github.com/eggy03/ferrumx-windows/pull/20)
- Changes from **`v2.0.0` to `v2.0.1`**: [Release Notes for v2.0.1](https://github.com/eggy03/ferrumx-windows/releases/v2.0.1)
- Changes from **`v2.0.1` to `v2.1.0`**: [Release Notes for v2.1.0](https://github.com/eggy03/ferrumx-windows/releases/v2.1.0)
- Changes from **`v2.1.0` to `v2.2.0`**: [Release Notes for v2.2.0](https://github.com/eggy03/ferrumx-windows/releases/v2.2.0)
- Changes from **`v2.2.0` to `v3.0.0`**: [Pull Request #XX]()

