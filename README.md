# GunGaming

## About

GunGaming is a Minecraft battle royale minigame plugin designed for fast-paced, gun-based PvP gameplay.

## Supported versions

✅ **Paper 1.21.5**  
❌ Bukkit and Spigot are not supported

## Installation

1. Download the plugin jar from
   the [latest GitHub release page](https://github.com/huzvanec/GunGaming/releases/latest)
2. Add the jar to your server's `plugins/` folder

## Building the Plugin (Linux/Mac/Windows)

1. Install required tooling:
    - [Java 21](https://www.oracle.com/java/technologies/downloads/#java21)
    - [Git](https://git-scm.com/downloads)
2. Clone and build:
   ```bash
   git clone https://github.com/huzvanec/GunGaming.git
   cd GunGaming/
   ./gradlew build
   ```
3. The plugin JAR will be located in `build/libs/`

## Packaging the Resource Pack (Linux only)

Currently, the resource pack build script supports Linux only. Contributions for Windows/macOS support are welcome!

1. Install dependencies:
    ```bash
    # Debian/Ubuntu
    sudo apt install zsh pngquant zip coreutils gawk
    
    # Fedora
    sudo dnf install zsh pngquant zip coreutils gawk
    
    # Arch
    sudo pacman -S zsh pngquant zip coreutils gawk
    ```
2. Run packaging script:
   ```bash
   zsh ./package-resource-pack.sh
   ```
3. You will find the output files in `build/`:
    - `resource-pack.zip` – the zipped pack
    - `resource-pack.sha1` – hash used by the Minecraft client to verify the downloaded ZIP file