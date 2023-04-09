# GunGaming
## This project is still in ALPHA and is not prepared for normal usage.
## About
GunGaming ~~is~~ *will be* an awesome minigame with guns, crates and way more.
## Supported versions
Spigot based servers 1.19.4, Bukkit is **not** supported!
## Build
Requirements: <br>
[Java](https://java.com), [Git](https://git-scm.com/), [Maven](https://maven.apache.org/), [Wget](https://www.gnu.org/software/wget/)<br>
Clone the repository:<br>
```git clone https://github.com/Mandlemankiller/GunGaming.git``` <br>
Move to the folder:<br>
```cd GunGaming``` <br>
Create a folder for build tools:<br>
```mkdir BuildTools``` <br>
Move to the folder:<br>
```cd BuildTools``` <br>
Download BuildTools:<br>
```wget "https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar"``` <br>
Run BuildTools: <br>
```java -jar BuildTools.jar --rev 1.19.4 --remapped``` <br>
After BuildTools finish, move back: <br>
```cd ..``` <br>
Run maven: <br>
```mvn package``` <br>
Done! The jars are now in the target directory. For spigot based servers use ```GunGaming-1.0-SNAPSHOT.jar```