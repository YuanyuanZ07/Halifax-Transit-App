Halifax Transit Android App 

A Kotlin-based Android mobile application that visualizes public transit activity in Halifax on an interactive map.
Users can browse available bus routes, select a route, and immediately see the selected bus highlighted on the map.

This project was developed as a portfolio project to demonstrate Android development skills, including MVVM architecture, local persistence, and multi-screen communication.

✨ Overview

The application simulates a lightweight transit-tracking companion app.
It combines a map interface with a route browser and demonstrates how different screens share state using ViewModel and local storage.

The app contains three primary screens:

Splash Screen (App startup)

Map Screen (Transit visualization)

Routes Screen (Route selection)

📱 Screens & Functionality
1. Splash Screen

The app launches with a branded splash screen displaying the HT Transit logo and app tagline.

Purpose:

Simulate real production app startup

Load map resources

Prepare navigation state

After initialization, the user is automatically redirected to the main map interface.

2. Map Screen (Main Interface)

The Map screen is the primary interface of the application.

Features:

Interactive Mapbox map

Displays user location

Displays multiple bus markers

Route highlighting based on user selection

Real-time UI update when route changes

Each bus marker represents a transit route operating in Halifax.

When a route is selected from the Routes screen, the corresponding bus marker is highlighted on the map so the user can visually identify it.

This demonstrates screen-to-screen state synchronization.

3. Routes Screen

The Routes screen allows users to browse and select transit routes.

Functions:

Displays list of available routes

Tap a route to select it

Selected route is persisted locally

Selected routes are stored locally using Room Database.

When the user returns to the Map screen, the application reads the stored route and updates the highlighted bus automatically.

🔄 Screen Interaction Flow

User opens the Routes page

User selects a route (e.g., Route 6 – Portland Hills)

Selection is saved locally (Room database)

Map screen observes the data

Corresponding bus icon becomes highlighted

This demonstrates:

Shared ViewModel usage

Observable UI state

Persistence across navigation

🧱 Architecture

The application follows the MVVM (Model-View-ViewModel) architecture.

UI Layer (ui)

Activities and UI components:

Map screen

Routes screen

Splash screen

Responsible for rendering UI and user interaction.

ViewModel Layer

Handles UI state and communication between screens.

Responsibilities:

Observes selected route

Notifies map to update highlighted marker

Connects UI to local data source

Data Layer (data)

Handles local persistence and data management.

Uses:

Room Database

Entity models

DAO queries

🛠 Tech Stack

Language: Kotlin

IDE: Android Studio

Architecture: MVVM

Database: Room

Map SDK: Mapbox

Android Jetpack:

ViewModel

LiveData

Navigation Components

🧠 Key Concepts Demonstrated

This project demonstrates understanding of:

Android Activity navigation

MVVM separation of concerns

Persistent local storage

UI state observation

Multi-screen communication

Map rendering & marker manipulation

▶️ How to Run

Clone the repository

git clone https://github.com/YuanyuanZ07/Halifax-Transit-App.git


Open the project in Android Studio

Create a Mapbox token

Create the following file:

app/src/main/res/values/mapbox_access_token.xml


Add:

<resources>
    <string name="mapbox_access_token">YOUR_TOKEN_HERE</string>
</resources>


Build and run on emulator or Android device

📌 Notes

The Mapbox access token is not included in the repository for security reasons.

👩‍💻 Author

Yuanyuan Zhou
NSCC IT Programming Student
Halifax, Nova Scotia
