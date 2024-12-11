GDSC-MBU Android Project

Welcome to the GDSC-MBU Android Project repository! This guide provides all the necessary details to get started, including prerequisites, installation steps, project configuration, and more.

🚀 Getting Started

Prerequisites

Ensure you have the following installed:

Android Studio

Java Development Kit (JDK) 8 or higher

Android SDK

🔧 Installation

Clone the repository and navigate to the project directory:

git clone https://github.com/your-repo/gdsc-mbu.git
cd gdsc-mbu

Open the project in Android Studio and sync it with Gradle files.

🛠️ Building the Project

To build the project, execute the following command:

./gradlew build

▶️ Running the Project

To run the project on an emulator or a connected device, use:

./gradlew installDebug

⚙️ Project Configuration

Gradle Configuration

The project utilizes Kotlin DSL for Gradle configuration. Key configuration files include:

build.gradle.kts

settings.gradle.kts

gradle.properties

ProGuard

ProGuard rules are defined in the file:

proguard-rules.pro

Android Manifest

The Android manifest file can be found at:

AndroidManifest.xml

✨ Key Features

Navigation Drawer: Defined in navigation_drawer.xml

Welcome Screen: Layout specified in welcome_screen.xml

Clear App Data: Functionality implemented in Menu.kt

📜 License

This project is licensed under the MIT License. See the LICENSE file for details.

🤝 Contributing

We welcome contributions! Please read CONTRIBUTING.md for details on our code of conduct and the process for submitting pull requests.

👥 Authors

Your Name

🙌 Acknowledgments

Google Developers

JetBrains for Kotlin and IntelliJ IDEA

Android Open Source Project

v