# 📅 UniCal - Universal Calendar Integration

### A Unified Calendar Application that seamlessly integrates Google Calendar, Notion, and Outlook calendars into a Single, Powerful Interface.

---
*Authors:*
- Aarav Tandon
- Aryan Aneja
- Ashish Ajin Thomas
- Raviit Vij
- Vishwesh Manishbhai Patel

## 📑 Table of Contents
- [Project Overview](#project-overview)
- [Features](#features)
- [Screenshots](#screenshots)
- [Technical Requirements](#technical-requirements)
- [Installation Guide](#installation-guide)
- [Usage Guide](#usage-guide)
- [Contributing](#contributing)
- [Feedback](#feedback)
- [License](#license)

## 🎯Project Overview
UniCal solves the common problem of managing multiple calendars across different platforms. By bringing together Google Calendar, Notion, and Outlook calendars into a unified interface, users can view and manage all their events in one place without constantly switching between applications.

### Why UniCal?
- Eliminates the need to check multiple calendar applications
- Provides a consistent interface for all calendar operations
- Saves time and reduces the risk of scheduling conflicts
- Built using Clean Architecture principles for maintainability and extensibility

## ✨Features

### 1. Unified Calendar View
- Monthly calendar display with event previews
- Color-coded events based on source calendar
- Quick navigation between month and day view

### 2. Day View Management
- Detailed daily event listing
- Add, view, and delete events
- Real-time synchronization with source calendars

### 3. Multi-Calendar Support
- Google Calendar integration
- Notion Calendar integration
- Outlook Calendar integration
- Easy switching between calendar services

## 📸Screenshots

[//]: # ([Note: Add screenshots here showing:)

[//]: # (1. Monthly calendar view)

[//]: # (2. Daily event view)

[//]: # (3. Add event interface)

[//]: # (4. Calendar selection interface])

## 🛠Technical Requirements

### Dependencies
- Google Calendar API Client Library v4.0+
- Microsoft Graph API SDK v5.0+
- OkHttp3
- JSON libraries (org.json)

## 📥Installation Guide

### 1. Prerequisites Installation
bash
# Install Java 8 or higher
`java -version`  # Verify Java installation

# Download project dependencies
`mvn install`    # If using Maven


### 2. API Credentials Setup

#### Google Calendar
1. Create a project in Google Cloud Console
2. Enable Calendar API
3. Create service account credentials
4. Save credentials as JSON:
   `{
   "type": "service_account",
   "project_id": "your-project-id",
   ...
   }`


#### Notion
1. Create an integration in Notion
2. Configure database access
3. Save authentication token:

Authentication Token: your_notion_token
Database ID: your_database_id


#### Outlook
1. Register application in Azure Portal
2. Configure OAuth settings
3. Save credentials:
   `{
   "client_id": "your_client_id",
   "client_secret": "your_client_secret",
   "tenant_id": "your_tenant_id",
   "redirect_uri": "http://localhost"
   }`


### 3. Application Setup
bash
# Clone the repository
`git clone https://github.com/NeuralNetNinja1729/CSC207_UniCal.git`

`cd unical`

# Compile & Run the project
Run `src/main/java/app/Main.java`


## 📖Usage Guide

### Basic Operations
1. *Switch Between Calendars*
    - Click calendar buttons on the left panel
    - Only one calendar active at a time

2. *Navigate Calendar*
    - Use month/year dropdowns at top
    - Click on any date to view daily events

3. *Add Events*
   - In Day View Click Add Event and Fill in the details.

4. *Delete Events*
   - In Day View CLick on Event and Click Delete Event.

## 🤝Contributing

### Getting Started
1. Fork the repository
2. Create feature branch: git checkout -b feature/YourFeature
3. Commit changes: git commit -m 'Add YourFeature'
4. Push to the branch: git push origin feature/YourFeature
5. Submit a Pull Request

### Guidelines
- Follow existing code style and architecture
- Include unit tests for new features
- Update documentation as needed
- One feature per pull request

### Review Process
1. Code review by maintainers
2. Automated tests must pass
3. Documentation must be updated
4. Changes must follow Clean Architecture principles

## 💭Feedback

We welcome your feedback to improve UniCal!

Please include:
- Clear description of the issue/suggestion
- Steps to reproduce (for bugs)
- Expected vs actual behavior
- Screenshots if applicable

## 📄License

This project is licensed under the Creative Commons Zero v1.0 Universal License. See the [LICENSE](LICENSE) file for details.

---

*Note*: This project is under active development. Features and documentation may be updated frequently.
