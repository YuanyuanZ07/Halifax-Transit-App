# 🚌 Halifax Transit Android App

An Android application built with **Kotlin** that visualizes Halifax public transit routes on an interactive map.

Users can browse bus routes, select a route, and immediately see the selected bus highlighted on the map.  
The project demonstrates real mobile app patterns including **MVVM architecture, local persistence, and multi-screen state synchronization**.

---

## ✨ Project Purpose

This app simulates a lightweight transit-tracking companion application.

It combines:
- A map interface
- A route selection screen
- Persistent user selection

The focus of the project is how multiple screens share and react to the same data source.

---

## 📱 Application Screens

### 🚀 Splash Screen
<img width="1080" height="2400" alt="Splash" src="https://github.com/user-attachments/assets/bb9eb345-130b-4b61-bdb1-7de10913a4d7" />

Displayed at startup to initialize resources and prepare navigation state.

Responsibilities:
- App startup simulation
- Map initialization
- Navigation preparation

After loading, the app automatically navigates to the main map screen.

---

### 🗺️ Map Screen (Main Interface)
<img width="1080" height="2400" alt="Map Screen" src="https://github.com/user-attachments/assets/7aabef9e-ba01-4d61-8f91-1ca8aaedc030" />

The main user interface showing transit activity.

**Features**
- Interactive Mapbox map
- User location display
- Multiple bus markers
- Dynamic route highlighting

When a route is selected from the Routes screen, the corresponding bus marker becomes highlighted.

This demonstrates **live UI updates based on shared application state**.

---

### 🧭 Routes Screen
<img width="1080" height="2400" alt="Routes Screen" src="https://github.com/user-attachments/assets/bd3385f2-3a66-42c9-87d9-3014438534d9" />

Allows users to browse and select available transit routes.

**Functions**
- Display route list
- Select a route
- Persist the selection

The selected route is stored locally using Room Database.

When the user returns to the Map screen, the highlighted bus updates automatically.

---

## 🔄 Screen Interaction Flow

1. User opens Routes screen  
2. User selects a route  
3. Selection saved to local database (Room)  
4. ViewModel observes data change  
5. Map UI updates and highlights corresponding bus

<img width="1080" height="2400" alt="Flow" src="https://github.com/user-attachments/assets/576991bc-2c16-4645-90fb-54560e1add46" />

**Concepts demonstrated**
- Shared ViewModel
- Observable UI state
- Persistence across navigation

---

## 🧱 Architecture

The app follows the **MVVM (Model-View-ViewModel)** pattern.

### UI Layer
Activities and screens:
- Splash screen
- Map screen
- Routes screen

Responsible for rendering UI and handling user interaction.

### ViewModel Layer
Handles communication between screens.

Responsibilities:
- Observe selected route
- Notify map to update markers
- Provide lifecycle-safe data to UI

### Data Layer
Handles local data persistence.

Uses:
- Room Database
- Entities
- DAO queries

---

## 🛠 Tech Stack

**Language:** Kotlin  
**IDE:** Android Studio  
**Architecture:** MVVM  
**Database:** Room  
**Map SDK:** Mapbox

**Android Jetpack Components**
- ViewModel
- LiveData
- Navigation Component

---

## 🧠 Key Skills Demonstrated

- Multi-screen Android navigation
- Shared ViewModel communication
- Local database persistence (Room)
- Reactive UI updates
- Map rendering and marker manipulation
- Clean architecture separation

---

## ▶️ Running the Project

Clone the repository:

