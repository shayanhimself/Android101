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

---

## Appendix: Upstream Binary Repository Audits

The GUI bundles binaries from three upstream repos. Each was audited separately.

---

### A. slipstream-rust-deploy (VPN Client)

**Repo:** https://github.com/mirzaaghazadeh/slipstream-rust-deploy
**Fork of:** https://github.com/AliRezaBeigy/slipstream-rust-deploy (303 stars, 67 forks)
**Verdict: CLEAN**

- **No pre-compiled binaries in the repo** -- everything is human-readable text (shell scripts, CI/CD YAML, patch files, C source).
- The CI/CD workflow builds binaries **from the upstream Rust source** (`Mygod/slipstream-rust`) using GitHub Actions. The build process is fully auditable.
- The main script (`slipstream-rust-deploy.sh`) is a standard server deployment script. It installs Rust toolchains, compiles from source or downloads from GitHub Releases, creates systemd services, and configures routing.
- **No obfuscation, no backdoors, no miners, no data exfiltration.**
- All external URLs are legitimate: `Mygod/slipstream-rust` (upstream), `sh.rustup.rs` (Rust installer), `AliRezaBeigy/slipstream-rust-deploy` (parent repo).
- The patches in `patches/` are legitimate Windows/macOS cross-compilation fixes.
- 19 commits, 2 authors, clean git history.

**Risk: LOW** -- Standard build/deployment tooling for a well-known open-source DNS tunnel.

---

### B. SlipNet (NoizDNS / Android VPN)

**Repo:** https://github.com/mirzaaghazadeh/SlipNet
**Fork of:** https://github.com/anonvector/SlipNet
**Verdict: CLEAN (with caveats about pre-compiled .so files)**

- **Full source code is available**: ~90 Kotlin files, ~30 Rust files, ~7 Go files.
- Audited all source code. **No telemetry, no data exfiltration, no backdoors found.**
- The only external network call (besides user-initiated VPN traffic) is a **read-only GitHub API update check** to `api.github.com/repos/anonvector/SlipNet/releases/latest`. It sends no device data or identifying information.
- All hardcoded IPs are expected: `8.8.8.8`, `1.1.1.1`, `9.9.9.9` (public DNS), `10.255.255.1` (VPN interface), `127.0.0.1` (localhost), RFC 5737 test addresses.
- `DeviceIdUtil.getScrambledDeviceId()` hashes the Android ID but it is **only used locally** for profile binding -- never transmitted.
- No command injection: uses `exec.Command()` (Go) and structured arguments (Kotlin), never `sh -c`.
- CI/CD builds the Rust slipstream-client from source. Standard GitHub Actions.

**Pre-compiled binary blobs in the repo** (the main caveat):

| Binary | Claimed Origin | strings Analysis |
|--------|---------------|------------------|
| `libtor.so` | Tor Project | Confirmed: contains torproject.org URLs, GPL text, OpenSSL strings |
| `libnaive.so` | NaiveProxy (Chromium-based) | Confirmed: Chromium network stack strings, DoH server URLs |
| `libobfs4proxy.so` | Tor lyrebird/obfs4 | Confirmed: Go runtime, WebRTC/Snowflake, Tor transport strings |
| `golibs.aar` | DNSTT + Snowflake (Go mobile) | Confirmed: DNSTT tunnel strings, AWS Snowflake infrastructure |

All binaries appear legitimate based on `strings` analysis, but cannot be verified as built from the exact open-source versions without reproducible build infrastructure.

**Risk: LOW-MEDIUM** -- Source code is clean. Pre-compiled native libraries require trust in the maintainer's build process.

---

### C. findns (DNS Resolver Scanner)

**Repo:** https://github.com/SamNet-dev/findns
**Verdict: CLEAN**

- **100% source code, zero binaries in the repo.** All 55 files are text.
- Written in Go. **Every line of source code was audited.**
- **No telemetry, no analytics, no phone-home behavior.**
- Network calls are only made when the user explicitly runs `findns fetch` (downloads public resolver lists from well-known GitHub repos) or during DNS scanning (queries user-specified resolvers).
- All hardcoded IPs are public DNS resolvers (`8.8.8.8`, `1.1.1.1`) or RFC 5737 test addresses.
- No command injection: uses `exec.CommandContext()` with structured arguments.
- No obfuscated code. Clean, well-commented Go.
- CI/CD builds from source using `go build`. Standard GitHub Actions.
- 72 commits, single author, clean git history.
- Good security practices: HTTP response size limits (10MB), CIDR expansion caps (1M IPs), proper context cancellation.

**Risk: LOW** -- Fully auditable, clean source code with no concerns.

---

## Final Overall Assessment

| Component | Has Source Code | Malicious Code Found | Risk Level |
|-----------|----------------|---------------------|------------|
| SlipStreamGUI (Electron wrapper) | Yes (100%) | No | LOW |
| slipstream-rust-deploy (build tooling) | Yes (100%) | No | LOW |
| SlipNet (Android VPN app) | Yes (95%, some .so blobs) | No | LOW-MEDIUM |
| findns (DNS scanner) | Yes (100%) | No | LOW |

**All four repositories have been audited and no malware, spyware, backdoors, or data exfiltration was found.** The entire ecosystem is a legitimate anti-censorship/DNS-tunneling VPN tool.
