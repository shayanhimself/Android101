# SlipStreamGUI Security Review

**Repository:** https://github.com/mirzaaghazadeh/SlipStreamGUI
**Version reviewed:** 1.4.4
**Date:** 2026-03-19
**Reviewer:** Automated source code audit

---

## Executive Summary

**SlipStreamGUI is NOT malware/spyware.** It is a legitimate open-source Electron-based GUI for the SlipStream DNS-tunnel VPN client. The source code contains no data exfiltration, no remote code execution, no cryptocurrency miners, no keyloggers, and no hidden backdoors.

However, there are some **security considerations** (not malicious, but worth knowing) documented below.

---

## Verdict: SAFE (with caveats)

| Category | Status |
|---|---|
| Data exfiltration / phone-home | None found |
| Hidden backdoors | None found |
| Keyloggers / input capture | None found |
| Cryptocurrency mining | None found |
| Obfuscated or encoded malicious code | None found |
| Remote code loading | None found |
| Supply chain (npm dependencies) | Minor known CVE (see below) |
| Bundled binaries (closed-source) | Risk area (see below) |

---

## Files Reviewed

| File | Lines | Verdict |
|---|---|---|
| `package.json` | 206 | Clean |
| `main.js` | 2450 | Clean |
| `index.html` | ~3400 | Clean |
| `tun-manager.js` | 365 | Clean |
| `check-system-proxy.js` | 83 | Clean |
| `scripts/download-binaries.js` | 230 | Clean |
| `scripts/verify-binaries.js` | 148 | Clean |
| `.github/workflows/ci.yml` | 83 | Clean |
| `.github/workflows/release.yml` | 496 | Clean |
| `.npmrc` | 2 | Clean |
| `.gitignore` | 56 | Clean |

---

## Detailed Findings

### 1. No Data Exfiltration (CLEAN)

- The app makes **zero** network requests to any external analytics, telemetry, or tracking server.
- The only outbound HTTPS request is to `api.github.com` to check for app updates (`main.js:1843`), which is standard and expected behavior.
- `index.html` contains no `fetch()`, `XMLHttpRequest`, or `WebSocket` calls. All renderer-to-main communication uses Electron IPC.
- User settings (resolver, domain, credentials) are stored **locally only** in Electron's `userData` directory.

### 2. No Remote Code Execution (CLEAN)

- No `eval()`, `new Function()`, or `document.write()` with dynamic content.
- No external `<script src="...">` tags or CDN includes.
- `index.html` is entirely self-contained (~3400 lines of inline HTML/CSS/JS).

### 3. No Hidden Functionality (CLEAN)

- No hidden iframes, no invisible elements loading external content.
- No cryptocurrency mining code or WebAssembly payloads.
- The only "hidden" feature is a 4% chance fun message encouraging GitHub stars (`index.html:~2314`) -- harmless self-promotion.

### 4. Electron Security Configuration (MODERATE CONCERN)

- **`nodeIntegration: true` and `contextIsolation: false`** (`main.js:225-226`): This is a known Electron security anti-pattern. It means the renderer process has full Node.js access. While this is common in simpler Electron apps and does NOT make it malware, it means that if a web page were loaded in the window (e.g., via a link), it could execute arbitrary Node.js code. In this app's case, only local `index.html` is loaded, so the practical risk is low.

### 5. HTTP Proxy Binds to 0.0.0.0 (LOW CONCERN)

- The HTTP proxy listens on `0.0.0.0:8080` (`main.js:1063`), meaning **any device on your local network** can use it. This is intentional (for phone proxy sharing), but users should be aware their proxy is network-accessible when running.

### 6. Shell Command Execution (EXPECTED FOR VPN)

- The app executes shell commands for:
  - System proxy configuration via `networksetup` (macOS), `netsh`/`powershell` (Windows), `gsettings` (Linux)
  - Network routing via `route` commands (TUN mode)
  - Port cleanup via `lsof`/`kill` (when port 5201 is in use)
- All commands use **hardcoded, predictable arguments** with no user-controlled string interpolation that could lead to command injection. The interface names from `networksetup` are quoted properly.

### 7. Bundled Closed-Source Binaries (TRUST CONCERN)

This is the **most important caveat**: The GUI itself is clean, but it downloads and runs **pre-compiled binaries** from:
- `mirzaaghazadeh/slipstream-rust-deploy` (the SlipStream VPN client)
- `mirzaaghazadeh/SlipNet` (NoizDNS support)
- `SamNet-dev/findns` (DNS resolver scanner)

These binaries are **not auditable from this repository's source code alone**. The GUI is just a wrapper. If you want full assurance, you would need to:
1. Verify the Rust source code of the SlipStream client separately
2. Build the binaries from source yourself
3. Or trust the maintainer's GitHub releases

### 8. npm Dependency Vulnerabilities

**Runtime dependencies (4 packages):**

| Package | Version | Status |
|---|---|---|
| `http-proxy` | 1.18.1 | No known CVEs (latest version, but unmaintained) |
| `ip` | ^2.0.1 | **CVE-2023-42282, CVE-2024-29415** (SSRF via `isPublic()`) |
| `socks` | ^2.7.1 | Transitive `ip` vulnerability via older versions |
| `socks-proxy-agent` | ^8.0.2 | Clean (latest 8.0.5 recommended) |

**The `ip` package vulnerability (CVE-2024-29415):** The `isPublic()` function can misclassify certain private IP addresses as public. In this app's context, `ip` is used by the `socks` package internally, and the app itself only deals with `127.0.0.1` and user-specified DNS servers. **Practical exploitation risk is very low** for this use case, but upgrading `socks` to >=2.8.x would resolve it.

**Dev dependencies:**
| Package | Version | Status |
|---|---|---|
| `electron` | ^28.0.0 | OK (dev only) |
| `electron-builder` | ^24.9.1 | OK (dev only) |

### 9. CI/CD Pipeline (CLEAN)

- GitHub Actions workflows use pinned major versions of official actions (`actions/checkout@v4`, `actions/setup-node@v4`).
- Binaries are downloaded from known GitHub repos using `gh release download` with `${{ github.token }}`.
- No suspicious post-build steps, no artifact tampering.
- The `verify-binaries.js` script checks that binaries aren't Git LFS pointers and are larger than 10KB.

---

## Recommendations Before Installing

1. **The GUI source code is safe** -- no malware, no spyware, no data theft.
2. **Understand what it does:** This is a DNS-tunneling VPN tool. It routes your traffic through DNS queries. This is a legitimate privacy/censorship-circumvention tool.
3. **The bundled binaries are the trust boundary.** If you download a pre-built release from GitHub, you're trusting the maintainer's compiled binaries. Building from source gives higher assurance.
4. **macOS Gatekeeper:** The build config disables `hardenedRuntime` and `gatekeeperAssess` (`package.json:131-132`), so macOS will warn you the app is unsigned. This is normal for open-source Electron apps not distributed via the Mac App Store.
5. **Firewall consideration:** The proxy listens on `0.0.0.0:8080` when active, so ensure your firewall blocks external access to port 8080 if you don't want network sharing.

---

## Conclusion

**SlipStreamGUI's source code is clean and safe to run.** It does exactly what it claims: provides a GUI for the SlipStream DNS-tunnel VPN client with HTTP proxy support. There is no malicious code, no data exfiltration, and no hidden functionality. The only trust decision is whether you trust the pre-compiled VPN client binaries that ship alongside it.
