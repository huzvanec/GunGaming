### Additions

Updated to Paper 1.21.4
Added an info message when trying to consume an instant heal when at full health
Added custom texture to Test Item
Added damage per shot gun statistic
Added `/_ggdev` development command
Added sounds and textures to all internal throwables (mine, rocket, air strike bomb)

### Reworks

Enemy Tracker and Teammate Tracker now work even when not held in hand.
Changed some in-game warnings and informational messages to better reflect their meaning.

### Balance Changes

Nerfed Medkit consumation duration (1.6s -> 10s)
Nerfed Bandage consumation duration (1.6s -> 2s)
Nerfed Pills consumation duration (1.6s -> 5s)

Nerfed Pickaxe durability (1561 -> 50)
Buffed Diamond Pickaxe mining speed (8 -> 9)
Nerfed Shovel durability (1561 -> 50)
Buffed Diamond Shovel mining speed (8 -> 9)

Buffed Diamond Sword attack speed (-2.5 -> -2.4)
Buffed Iron Sword attack speed (-2.5 -> -2.4)
Buffed Dirty Sword attack speed (-2.5 -> -2.4)

Buffed Detector Boots durability (10 -> 25)
Buffed Stealth Helmet durability (10 -> 25)
Buffed Thermal Goggles durability (10 -> 40)

Nerfed Remington M1100 shoot cooldown (0.4s -> 0.5s)
Nerfed Remington M1100 damage (1.3 -> 1.1)
Buffed Remington M870 shoot cooldown (1.25s -> 1.2s)
Nerfed Remington M870 damage (2.3 -> 2.2)
Buffed SPAS-12 damage (1.7 -> 2.2)

### Improvements & Bugfixes

Fixed players being able to shoot during enabled lobby
Fixed (hopefully all) event distribution related issues
Fixed reload message always using the `F` key as the reload button
Fixed eating and drinking sounds not playing for vanilla consumable items

### Internals

Updated dependencies
Switched to Gradle Kotlin DSL
Dependencies are now shaded (fixed ClassGraph related server crashes)
Decrased plugin jar size
Switched from Jetbrains annotations to Jspecify (increased code clarity)
Most of the items are now backed by Popped Chorus Fruit, as it has less usages in game and therefore introduces less
buggy vanillla interactions.