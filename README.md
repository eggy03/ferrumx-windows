[![Project Stats](https://openhub.net/p/ferrumx-windows/widgets/project_thin_badge.gif)](https://openhub.net/p/ferrumx-windows)
[![License](https://img.shields.io/github/license/eggy03/ferrumx-windows)](https://github.com/eggy03/ferrumx-windows/blob/main/LICENSE)
[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.eggy03/ferrumx-windows)](https://central.sonatype.com/artifact/io.github.eggy03/ferrumx-windows)
![Java Version](https://img.shields.io/badge/java-8%2B-blue)

# About
FerrumX-Windows is a free powershell based Hardware and Network Information library for Java.
It contacts some [Computer System Hardware Classes](https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/computer-system-hardware-classes)
and [Operating System Classes](https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/operating-system-classes) of Windows Management Instrumentation (WMI) through PowerShell
to fetch details about your hardware and OS.
The library is still in its early stages and is being developed by a single, relatively inexperienced contributor.

# Supported Versions
- v3.x.x - Actively Developed and Supported
- v2.x.x - Transitional Release (not maintained, not for general use)
- v1.x.x - No longer actively maintained

# Cross-Platform Support
Work on cross-platform compatibility is currently in active planning stage.

# Supported Operating Systems
The library has been tested to work exclusively on <strong>Windows 8.1, Windows 10 and Windows 11</strong> devices.
Windows 7 and earlier versions may be supported if PowerShell 5 can be installed

# CI Stats
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Egg-03_FerrumX&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Egg-03_FerrumX)
![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/eggy03/ferrumx-windows/.github%2Fworkflows%2Fbuild.yml)
![Commits to main since latest release](https://img.shields.io/github/commits-since/eggy03/ferrumx-windows/latest)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Egg-03_FerrumX&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=Egg-03_FerrumX)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Egg-03_FerrumX&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=Egg-03_FerrumX)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Egg-03_FerrumX&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=Egg-03_FerrumX)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Egg-03_FerrumX&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=Egg-03_FerrumX)

[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=Egg-03_FerrumX&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=Egg-03_FerrumX)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=Egg-03_FerrumX&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=Egg-03_FerrumX)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Egg-03_FerrumX&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Egg-03_FerrumX)

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

# FAQ

### **Q: What version of Windows is required to use this library?**
FerrumX-Windows has been tested on **Windows 8.1, Windows 10, and Windows 11**.
Older systems may work if **PowerShell 5** can be installed.

---

### **Q: Does this library work on Linux or macOS?**
Not yet. Information gathered by this library comes from WMI classes.
Cross-platform support is planned but not yet implemented.

---

### **Q: What Java versions are supported?**
**Java 8 and newer**.

---

### **Q: Do I need PowerShell installed?**
Yes. PowerShell is required to query WMI classes. **PowerShell 5.1+** is recommended.
PowerShell should be available by default on all supported windows editions unless explicitly disabled by your
administrator.

---

### **Q: Is administrator privilege required?**
As of now, NONE of the queries or scripts require any sort of admin privileges.

---

### **Q: Why are calls slow or delayed sometimes?**
Each function call causes a powershell session to spawn, which adds overhead. Re-using the session for batched queries
greatly improves performance after the initial startup. You can keep a session (or multiple ones) open
for as long as you want to but don't forget to close them before exiting your program. There are also auto-managed sessions
that save you the headache of managing your own sessions but they immediately close the session after each call (intended behavior).

---

### **Q: How can I re-use a session?**
Create your own Powershell session and use it like the example below
```java
public class ReusableSessionExample {
    static void main(String[] args){
        try (PowerShell session = PowerShell.openSession()) {
            List<Win32Processor> processors = new Win32ProcessorService().get(session);
            List<Win32CacheMemory> cacheMemoryList = new Win32CacheMemoryService().get(session);
        }
    }
}
```
---

### **Q: Why do some fields return null or appear missing?**

There could be many reasons for that. Some of them could be:
- Your hardware does not expose the field
- The WMI provider does not support it on your Windows version
- Unsupported PowerShell version
- WMI or PowerShell is blocked by your sysadmin or the session is restricted
- Your WMI repository is corrupted
- The output of your shell is not parsable (you can set your logging environment to TRACE level, to see the full output
of your powershell)

---

### **Q: What can I do if I face such an issue ?**

You can:
- Check for existing issues in the issues page
- Check whether your powershell and or Windows version is supported
- Check that your WMI repository is not corrupted
- Check your powershell output for errors (either by enabling TRACE in your logging framework or manually querying in PowerShell)
- Fill up an issue in the issues page with steps to reproduce, windows and powershell versions along with the trace output
Check out the contribution guidelines to see how to report an issue

---

### **Q: How stable is the library?**

The API of this library has evolved greatly over the course of two years. Each major version saw huge changes in the 
syntax of its API and as of v3, the library is stable enough to be used in production environments. That being said, there
is no guarantee about the syntax evolvement of the API in the near future, which might introduce a lot of breaking changes.

---

### **Q: How do I contribute to this project?**

You can contribute via one of the following methods :)
- Contribute to the project's wiki (it's sparse)
- Contribute to the [examples repository](https://github.com/eggy03/ferrumx-windows-examples)
- Test the library in your system and submit suggestions and bugs by opening an [issue](https://github.com/eggy03/ferrumx-windows/issues)
- Report vulnerabilities
- Check out the [Contribution Guideline]() to know more about contributing to code and other ways of contributing

---

### **Q: Is there an alternative library I can use instead ?**

Yes :) And in many ways, it's way better than this one :)
[OSHI](https://github.com/oshi/oshi) has been in development since 2010 and has a very mature ecosystem :)
They use JNA (which is native and fast) and thus, require no PowerShell and have also been working on migrating to
JDK25's FFM API which is a much faster API than JNA. They also support cross-platform calls.

My version of JNA based implementation is called [PineTree,](https://github.com/eggy03/PineTree) but it is in very early
stages of development.

### **Q: If OSHI exists, why did you make this library ?**

OSHI is a very mature library maintained by an experienced team, and I don’t see FerrumX as a competitor or replacement for it.
This project actually began in a much simpler way: as a small set of PowerShell-based scripts I wrote
to help the IT team for a company, gather hardware information from employees’ machines for auditing and diagnostic
purposes.

Later, a need arose for a small [GUI](https://github.com/eggy03/FerrumX-GUI)
that non-technical staff could run themselves, without admin privileges.
My initial thoughts were to use the CPU-Z SDK, but that wasn't free and would require admin access.
Any other available and trusted GUI application would need admin access as well.
And on top of that, the only GUI Framework I was comfortable with was Java Swing.
That led me to experiment with capturing PowerShell output through Java’s ProcessBuilder,
which eventually grew into the foundation of this library.

By the time I discovered OSHI, I was already deep into building the library and
if I am being honest, I'm glad I discovered it late because creating this library has taught me a lot :)
It taught me CI/CD, Version Control, Build Tooling (Maven), Documenting,Publishing and Releasing a library,
Semantic Versioning, Vulnerabilities, Building Patterns to name a few.

It initially existed as a closed source library but as I started adding more features to it, I felt as if it was
functional enough to be made open-source. FerrumX-Windows exposes a lot of information about your system, it could be
more than what OSHI exports or less but for most use cases, OSHI might still be a better option. However, I’ll continue working
on FerrumX because it helps me learn and grow :)

# License
This project is licensed under the MIT License.