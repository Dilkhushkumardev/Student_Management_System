# 🎨 UI/UX Design System (DESIGN.md)

<div align="center">

# SmartAttend Visual Design System & Component Library
### Modern, High-Contrast, Enterprise Glassmorphic Academic Dashboard

[![Design System](https://img.shields.io/badge/Design%20System-SmartAttend%20v1.0-6366F1?style=for-the-badge)](file:///c:/Users/dilkh/OneDrive/Desktop/SAS/src/main/resources/static/css)
[![CSS Architecture](https://img.shields.io/badge/CSS-Tokens%20%2B%20Bootstrap%205.3-0284C7?style=for-the-badge)](file:///c:/Users/dilkh/OneDrive/Desktop/SAS/src/main/resources/static/css)
[![Accessibility](https://img.shields.io/badge/WCAG-2.1%20AA%20Compliant-10B981?style=for-the-badge)](file:///c:/Users/dilkh/OneDrive/Desktop/SAS/docs/TEST_PLAN.md)

</div>

---

## 1. Visual Philosophy & Design Direction

SmartAttend rejects the clunky, outdated aesthetic of traditional 2000s-era university software. Instead, it is crafted with a **sleek, high-density, modern SaaS aesthetic** featuring:

- **Subtle Glassmorphism:** Translucent panel backdrops (`backdrop-filter: blur(12px)`) with crisp, ultra-thin border highlights (`rgba(255, 255, 255, 0.08)`).
- **High-Contrast Semantic Color Coding:** Instantly distinguishable visual states for the BEU 75% attendance threshold (Emerald for safe $>85\%$, Amber for borderline $75\%\text{--}84\%$, Crimson for critical $<75\%$).
- **Micro-Animations & Live Feedback:** Smooth CSS transition pulses on live biometric counters, circular progress rings, and interactive row expansions.

---

## 2. Color Palette & Design Tokens

### 2.1. Brand & Core Theme Colors

```css
:root {
  /* Brand Accent Tokens */
  --sa-primary: #4f46e5;         /* Indigo 600 - Main Brand Action */
  --sa-primary-light: #6366f1;   /* Indigo 500 - Hover & Active Glow */
  --sa-primary-dark: #3730a3;    /* Indigo 800 - Contrast Text */
  --sa-secondary: #0ea5e9;       /* Sky 500 - Informational & Secondary */
  
  /* Dark / Midnight Theme Backgrounds */
  --sa-bg-dark: #0f172a;         /* Slate 900 - Deep Main Background */
  --sa-card-dark: #1e293b;       /* Slate 800 - Elevated Card Surfaces */
  --sa-card-border: rgba(255, 255, 255, 0.08); /* Crisp Glass Outline */
  --sa-text-primary: #f8fafc;    /* Slate 50 - High Emphasis Text */
  --sa-text-muted: #94a3b8;      /* Slate 400 - Secondary Labels */

  /* Light Theme (Alternative Profile) */
  --sa-bg-light: #f8fafc;
  --sa-card-light: #ffffff;
  --sa-text-light-dark: #0f172a;
}
```

### 2.2. Attendance Status & Regulatory Colors

| Status Token | Hex Code | Visual Meaning | Used In |
| :--- | :--- | :--- | :--- |
| `--sa-success` | `#10B981` (Emerald) | Safe ($\ge 85\%$) / Present | 5 & 4 Marks Badge, Present Status |
| `--sa-warning` | `#F59E0B` (Amber) | Warning ($75\% - 84\%$) / Late | 2 & 1 Marks Badge, Late Tardy Flag |
| `--sa-danger` | `#EF4444` (Crimson) | Critical Shortage ($< 75\%$) | Debarred Alert, Absent Tally |
| `--sa-condonation` | `#F43F5E` (Rose) | Medical Condonation Zone ($60\% - 74.99\%$) | Principal Review Flag |
| `--sa-excused` | `#8B5CF6` (Purple) | Official Duty / Medical Leave | Excused Event Pill |

---

## 3. Typography Hierarchy

SmartAttend uses a tri-font modern hierarchy optimized for academic legibility and data density:

```
Headings & Display  →  "Outfit", sans-serif      (700/600 Semi-Bold)
Body & Data Labels  →  "Inter", sans-serif       (400 Regular / 500 Medium)
Codes & Roll No.    →  "JetBrains Mono", monospace (500 Medium)
```

| Element | Font Family | Size | Weight | Line Height | Usage |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Hero Title (H1)** | Outfit | 32px (2.0rem) | 700 Bold | 1.2 | Page Header & Dashboard Welcome |
| **Section Title (H2)** | Outfit | 22px (1.375rem) | 600 Semi-Bold | 1.3 | Panel Headings & Metric Titles |
| **Card Subtitle (H3)** | Outfit | 16px (1.0rem) | 600 Semi-Bold | 1.4 | Module Headers & Modal Titles |
| **Body Standard** | Inter | 14px (0.875rem) | 400 Regular | 1.5 | Paragraphs, Table Data, Labels |
| **Small / Meta** | Inter | 12px (0.75rem) | 500 Medium | 1.4 | Timestamps, Footers, Sub-metrics |
| **Roll No / Code** | JetBrains Mono | 13px (0.8125rem) | 500 Medium | 1.0 | Student Roll (`26105110001`), Subject Code (`100102`) |

---

## 4. UI Component Library

### 4.1. Metric KPI Cards

Used on dashboards to display high-level statistics:

```html
<div class="card metric-card shadow-sm border-0">
  <div class="card-body d-flex align-items-center">
    <div class="metric-icon-box bg-primary-subtle text-primary rounded-3 me-3">
      <i class="fas fa-calendar-check fa-lg"></i>
    </div>
    <div>
      <span class="text-muted small fw-semibold text-uppercase">Total Classes</span>
      <h3 class="mb-0 fw-bold">48 <small class="text-muted fs-6">held</small></h3>
    </div>
  </div>
</div>
```

### 4.2. Circular Attendance Progress Meter

Used on the Student Portal to display individual subject attendance with the 75% target ring:

```html
<div class="attendance-meter-container text-center">
  <div class="circular-progress" data-percentage="84.5" style="--val: 84.5; --color: #10B981;">
    <span class="progress-value">84.5%</span>
  </div>
  <p class="subject-title mt-2 fw-semibold">Engineering Mathematics - I</p>
  <span class="badge bg-success-subtle text-success border border-success-subtle">
    <i class="fas fa-check-circle me-1"></i> Safe (+4 Margin)
  </span>
</div>
```

### 4.3. Real-Time Live Counter Widget (Faculty Portal)

```html
<div class="live-counter-panel rounded-4 p-3 bg-dark text-white border border-secondary-subtle">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <span class="badge bg-danger animate-pulse"><i class="fas fa-circle me-1"></i> LIVE SESSION</span>
    <span class="text-secondary small">Period 2 • CSE-A</span>
  </div>
  <div class="row g-2 text-center">
    <div class="col-4">
      <div class="stat-box p-2 rounded bg-slate-800">
        <h4 class="text-emerald-400 mb-0 fw-bold" id="livePresentCount">26</h4>
        <span class="text-muted fs-xs">Present</span>
      </div>
    </div>
    <div class="col-4">
      <div class="stat-box p-2 rounded bg-slate-800">
        <h4 class="text-rose-400 mb-0 fw-bold" id="liveAbsentCount">4</h4>
        <span class="text-muted fs-xs">Absent</span>
      </div>
    </div>
    <div class="col-4">
      <div class="stat-box p-2 rounded bg-slate-800">
        <h4 class="text-amber-400 mb-0 fw-bold" id="liveLateCount">0</h4>
        <span class="text-muted fs-xs">Late</span>
      </div>
    </div>
  </div>
</div>
```

---

## 5. Responsive Breakpoint Standards

```
Mobile (Portrait)   : 375px  → Single column, full-width cards, compact bottom navigation
Mobile (Landscape)  : 576px  → 2-column metric cards, responsive tables with horizontal scroll
Tablet              : 768px  → Collapsible sidebar navigation, 3-column stats
Laptop / Desktop    : 1024px → Fixed left sidebar navigation, full multi-pane layout
Ultra-Wide Screen   : 1440px → Max-width centered content container (1400px limit)
```

---

## 6. UX Interaction Guidelines & States

### 6.1. Loading & Async States
- **Skeleton Loaders:** When fetching API data, table rows render gray shimmering skeletons instead of blank whitespaces.
- **Button Spinner:** Submitting a form replaces the button label with `<span class="spinner-border spinner-border-sm"></span> Processing...`.

### 6.2. Empty States
- When no records are found (e.g. No sessions found today), display an illustrative SVG icon, informative heading (`No Lecture Sessions Scheduled`), and an actionable button (`Create New Session`).

### 6.3. Feedback Toasts
- **Success:** Emerald toast notification bottom-right with checkmark icon (Auto-dismisses in 3000ms).
- **Error:** Crimson toast notification with error description and recovery hint (e.g., *Session already locked by Faculty*).
