---
name: Bug report
about: Reporting bugs encountered while using the library
title: ''
labels: bug
assignees: eggy03

---

## Describe the bug
A clear and concise description of what the bug is.

## To Reproduce
Steps to reproduce the behavior:
-
-
-

## Expected behavior
A clear and concise description of what you expected to happen.

## Screenshots
If applicable, add screenshots to help explain your problem.

## Stacktrace
If you have run into an exception, provide the stacktrace here

## PowerShell Output
If your logging is enabled at `trace` level, ferrumx-windows should print the whole json data supplied by the PowerShell for the particular service called. You can paste it here.

## Windows and PowerShell Version

Provide the Windows and PowerShell version of the target machine

```shell
winver
```

```shell
$PSVersionTable.PSVersion
```

## System Locale, Output Encoding and Execution Policy

```shell
Get-WinSystemLocale
```

```shell
$OutputEncoding
```

```shell
Get-ExecutionPolicy -List
```

## WMI Repository Integrity

```shell
winmgmt /verifyrepository
```

Also check if your AV or GroupPolicy changes prevent WMI from being called in the target machine**
