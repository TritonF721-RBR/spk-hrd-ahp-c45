# 🏥 Enterprise HR Analytics & Performance Support System (SPK)
A **Fullstack Web-based application** designed to assist HR departments and organizations in objectively and automatically evaluating top-tier employee performance.

## 🧠 Algorithms Used
This system integrates two intelligent decision-making layers:

1. **AHP (Analytical Hierarchy Process) & SAW:** Used for multi-criteria weighting (Work Quality, Discipline, Responsibility, etc.) and initial candidate ranking based on mathematical precision.
2. **C4.5 (Decision Tree / Data Mining):** Acts as a primary **Machine Learning layer** to extract patterns from AHP results, automatically generating final classification rules (TOP PERFORMER, QUALIFIED, or FAILED) with specific decision notes.

## 🚀 Key Features
* **Secure Authentication:** Robust login implementation using JWT (JSON Web Token) and Spring Security.
* **Bulk Upload Excel:** High-speed import feature for employee data and scores from `.xlsx` files.
* **Dynamic Evaluation:** System validates and converts raw inputs (e.g., number of absences) into real-time weighted scales.
* **Automated Reporting:** Generate final decision reports and Decision Tree patterns ready for PDF export.

## 🛠 Tech Stack
* **Backend:** Java, Spring Boot, Spring Security, JWT, Spring Data JPA.
* **Database:** PostgreSQL.
* **Frontend:** HTML5, CSS3, JavaScript, Bootstrap 5, SheetJS.

## 📸 Documentation / Screenshots
*(Note: Drag and drop your dashboard, ranking, and classification screenshots here on GitHub)*

---
*Developed as a showcase of Fullstack Development & Algorithm Implementation.*
