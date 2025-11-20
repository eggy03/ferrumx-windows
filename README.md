[![Project Stats](https://img.shields.io/badge/OpenHub-ferrumx%20windows-yellow?style=for-the-badge)](https://openhub.net/p/ferrumx-windows)
[![License](https://img.shields.io/github/license/eggy03/ferrumx-windows?style=for-the-badge&color=white)](https://github.com/eggy03/ferrumx-windows/blob/main/LICENSE)
[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.eggy03/ferrumx-windows?style=for-the-badge&color=pink)](https://central.sonatype.com/artifact/io.github.eggy03/ferrumx-windows)
![Minimum JDK Version](https://img.shields.io/badge/Minimum%20JDK%20Version-8-blue?style=for-the-badge)

[![Javadocs](https://img.shields.io/badge/Docs-Javadocs-orange?style=for-the-badge)](https://eggy03.github.io/ferrumx-windows-documentation/)
[![Examples](https://img.shields.io/badge/Docs-Examples-orange?style=for-the-badge)](https://github.com/eggy03/ferrumx-windows-examples)
[![Migration Guide](https://img.shields.io/badge/Wiki-Migration%20Guide-white?style=for-the-badge)](https://github.com/eggy03/ferrumx-windows/wiki/Migration-Guides)
[![FAQ](https://img.shields.io/badge/Wiki-FAQ-white?style=for-the-badge)](https://github.com/eggy03/ferrumx-windows/wiki/FAQ)


# About
FerrumX-Windows is a free powershell based Hardware and Network Information library for Java.
It contacts some [Computer System Hardware Classes](https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/computer-system-hardware-classes)
and [Operating System Classes](https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/operating-system-classes) of Windows Management Instrumentation (WMI) through PowerShell
to fetch details about your hardware and OS.

# Supported Versions
- v3.x.x - Actively Developed and Supported
- v2.x.x - Transitional Release (not maintained, not for general use)
- v1.x.x - No longer actively maintained

# Cross-Platform Support
Work on cross-platform compatibility is currently in active planning stage.

# Supported Operating Systems
- Windows: `7SP1*`, `8.1*`, `10` and `11`
- PowerShell: `5.1 and above`

*For `Windows 8.1` and `7SP1`you can install
[Windows Management Framework 5.1](https://www.microsoft.com/en-us/download/details.aspx?id=54616) to upgrade to PowerShell 5.1

# CI Stats
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=eggy03_ferrumx-windows&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=eggy03_ferrumx-windows)
![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/eggy03/ferrumx-windows/.github%2Fworkflows%2Fbuild.yml)
![Commits to main since latest release](https://img.shields.io/github/commits-since/eggy03/ferrumx-windows/latest)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=eggy03_ferrumx-windows&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=eggy03_ferrumx-windows)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=eggy03_ferrumx-windows&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=eggy03_ferrumx-windows)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=eggy03_ferrumx-windows&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=eggy03_ferrumx-windows)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=eggy03_ferrumx-windows&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=eggy03_ferrumx-windows)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=eggy03_ferrumx-windows&metric=coverage)](https://sonarcloud.io/summary/new_code?id=eggy03_ferrumx-windows)

# Download
> **Minimum Supported Java Version:** 8

Maven:
```xml
<dependency>
    <groupId>io.github.eggy03</groupId>
    <artifactId>ferrumx-windows</artifactId>
    <version>VERSION</version>
</dependency>
```

Gradle:
```gradle
implementation group: 'io.github.eggy03', name: 'ferrumx-windows', version: 'VERSION'
```
> Replace `VERSION` with the latest version available in maven central

For other build ecosystems, check out the [Maven Central Repository](https://central.sonatype.com/artifact/io.github.eggy03/ferrumx-windows/overview)

> [!NOTE]
> The `sources.jar` published with this library includes de-lomboked code which should prevent the IDEs from complaining
> about source mismatch between the decompiled class files and the downloaded sources. It should also make your debugging
> easier, should you step into the library code during the debugging process of your project.

# Documentation
- [Javadocs](https://eggy03.github.io/ferrumx-windows-documentation/) - class level docs
- [Wiki](https://github.com/eggy03/ferrumx-windows/wiki) - contains migration guides, info about breaking changes
and explanation of the internal working of the library
- [Examples](https://github.com/eggy03/ferrumx-windows-examples) - contains usage examples

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
This project is licensed under the [MIT License](/LICENSE).