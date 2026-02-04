# Security Policy

## Supported Versions

FerrumX-Windows is maintained by a single contributor. Only the versions listed below continue to receive security-related fixes.
Due to immutability of library releases, any change to the library will result in a version numbers being incremented and only the latest released version will be supported.

| Version | Supported     |
|---------|---------------|
| 4.x.x   | Supported     |
| 3.x.x   | Not Supported |
| 2.x.x   | Not Supported |
| 1.x.x   | Not Supported |
| < 1.x   | Not Supported |

> Pre-3.x versions contain known architectural limitations and are not recommended for production use.

---

## Reporting a Vulnerability

If you discover a vulnerability, please disclose it responsibly using the steps below.

### 1. Where to Report

You can report vulnerabilities **privately** using GitHub Security Advisories:

**https://github.com/eggy03/ferrumx-windows/security/advisories/new**

If that is not possible, you may email:

**eggzerothree@proton.me**

---

### 2. What to Include

To help diagnose and fix the issue, include:

- Description of the vulnerability
- Steps to reproduce
- Affected version(s)
- Your environment (Windows version, PowerShell version)
- Relevant logs or output (only if safe to share)
- Potential impact (optional)

---

### 3. What to Expect

- **Acknowledgement:** within 72 hours
- **Status updates:** every 3–7 days until resolution
- **Fix timelines:**
    - Critical: as soon as possible
    - Medium: next patch/minor release
    - Low: scheduled based on project bandwidth

If you want public credit for your discovery, you will be acknowledged in the release notes.

---

## Responsible Disclosure

To protect users:

- Please **do not publicly disclose** the issue until a fix is released.
- Avoid posting proof-of-concept exploits in public areas before coordinated disclosure is complete.

---

## Scope

### In Scope

Security issues involving:

- Improper handling of PowerShell/WMI output
- Injection vulnerabilities in command execution
- Unsafe parsing or deserialization
- Sensitive data leakage
- Potential RCE or privilege escalation via library APIs
- Dependency-related security issues

### Out of Scope

These are **not** considered vulnerabilities within FerrumX-Windows:

- Bugs in PowerShell or WMI themselves
- Issues caused by corrupted WMI repositories
- Misconfigured user environments
- Problems requiring administrator access to exploit
- Incorrect or missing hardware fields returned by WMI
- Performance delays related to PowerShell startup
- Issues on unsupported OS or PowerShell versions

---

## Security Philosophy

FerrumX-Windows interacts with PowerShell and WMI, which inherently access system-level information. To minimize risk:

- All operations are read-only
- No administrator privileges required
- No external network communication
- No personal data collection
- Full transparency in reporting and patching

---

If you are unsure whether something qualifies as a vulnerability, you may open a GitHub Discussion for clarification.
