<p align="center"> 
  <img src="https://github.com/Egg-03/FerrumX/assets/111327101/9aee9cdf-5213-401b-814d-a9738ee1a24c" alt="FerrumX logo">
</p>

[![Project Stats](https://openhub.net/p/FerrumX/widgets/project_thin_badge.gif)](https://openhub.net/p/FerrumX)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Egg-03_FerrumX&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Egg-03_FerrumX)
![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/Egg-03/FerrumX/.github%2Fworkflows%2Fbuild.yml)

[![License](https://img.shields.io/github/license/Egg-03/FerrumX)](https://github.com/Egg-03/FerrumX/blob/main/LICENSE)
[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.eggy03/ferrumx-windows)](https://central.sonatype.com/artifact/io.github.eggy03/ferrumx-windows)
![Commits to main since latest release](https://img.shields.io/github/commits-since/eggy03/FerrumX/latest)
![Java Version](https://img.shields.io/badge/java-8%2B-blue)

# About
FerrumX-Windows is a lightweight Hardware and Network Information library written in pure Java. It contacts some [Computer System Hardware Classes](https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/computer-system-hardware-classes) and [Operating System Classes](https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/operating-system-classes) of Windows Management Instrumentation (WMI) through PowerShell to fetch comprehensive system details on Windows Operating Systems.

# Supported Operating Systems
The library has been tested to work exclusively on <strong>Windows 8.1, Windows 10 and Windows 11</strong> devices.
For Windows 7 and Vista support, see: [FerrumL](https://github.com/eggy03/FerrumL)

# Download
> **Requires:** Java 8 or higher

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

# Documentation
Documentation can be found [here](https://eggy03.github.io/FerrumX-Documentation/)

# Usage
> [!IMPORTANT]
> More usage examples and migration guides can be found in the [Wiki](https://github.com/eggy03/FerrumX/wiki).

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

# Information about v2 and v3

v2.0.0 to v2.2.0 were transitional test releases that introduced the new service/entity architecture, with v2.0.0 being a complete rewrite of the project.
These versions were made publicly available for early adopters but will no longer be supported after the public release of v3.0.0

The recommended upgrade path for v1.3.7 users is to move directly to v3.0.0, which includes all the refinements introduced after the 2.x testing phase.

For users currently on 2.x.x, you can check what changes have been introduced in each 2.x.x release in the Releases page,
but these release versions are deprecated and unsupported.

- Changes incorporated in v2.0.0 from v1.3.7 can be found in this [PR](https://github.com/eggy03/FerrumX/pull/20)
- Changes incorporated in v3.0.0 from v2.2.0 can be found in this [PR](https://github.com/eggy03/FerrumX/pull/20)

A migration guide for migrating to v3.0.0 from v1.3.7 will be provided soon


