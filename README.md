# Modern Essentials

**Modern Essentials** is a lightweight [Paper](https://papermc.io/software/paper) plugin that provides essential
commands and features.

---

## Features

- **Custom MOTD**
- **Customizable Tab List**
- **Custom Chat Formatting**

## Commands

| Command                                        | Description                            | Permission                     |
|------------------------------------------------|----------------------------------------|--------------------------------|
| `/fly`                                         | Toggle flight mode                     | `essentials.fly`               |
| `/god`                                         | Enable invincibility                   | `essentials.god`               |
| `/vanish`                                      | Become invisible to other players      | `essentials.vanish`            |
| `/speed <speed>`                               | Set walk speed                         | `essentials.speed`             |
| `/flyspeed <flyspeed>`                         | Set fly speed                          | `essentials.flyspeed`          |
| `/kickall`                                     | Kick all other players from the server | `essentials.kickall`           |
| `/repair`                                      | Repair item in main-hand               | `essentials.repair`            |
| `/enderchest`                                  | Open your Ender Chest from anywhere    | `essentials.enderchest`        |
| `/enderchest <player>`                         | Open other players Ender Chests        | `essentials.enderchest.others` |
| `/inventorysee <player>`                       | View another player’s inventory        | `essentials.inventorysee`      |
| `/workbench`                                   | Open a crafting table                  | `essentials.workbench`         |
| `/anvil`                                       | Open an anvil                          | `essentials.anvil`             |
| `/cartographytable`                            | Open a cartographytable                | `essentials.cartographytable`  |
| `/grindstone`                                  | Open a grindstone                      | `essentials.grindstone`        |
| `/loom`                                        | Open a loom                            | `essentials.loom`              |
| `/smithingtable`                               | Open a smithingtable                   | `essentials.smithingtable`     |
| `/stonecutter`                                 | Open a stonecutter                     | `essentials.stonecutter`       |
| `/day\|night\|noon\|midnight\|sunrise\|sunset` | Set the time                           | `essentials.time`              |
| `/sun\|/rain\|/thunder`                        | Change the weather                     | `essentials.weather`           |
| `/essentials reload`                           | Reload plugin config                   | `essentials.reload`            |

---

## Placeholders

Modern Essentials supports [MiniPlaceholders](https://modrinth.com/plugin/miniplaceholders) but also has some built-in
placeholders

| Placeholder       | Description                                                                                 |
|-------------------|---------------------------------------------------------------------------------------------|
| `<online>`        | Current number of online players                                                            |
| `<online_max>`    | Maximum allowed players on the server                                                       |
| `<time:'format'>` | Current real-world time in your specified format, for example `'EEE, LLL dd yyyy HH:mm:ss'` |
| `<player>`        | Player's username                                                                           |
| `<displayname>`   | Player's display name (may include prefix/suffix)                                           |
| `<uuid>`          | Player's UUID                                                                               |
| `<ping>`          | Player's current ping                                                                       |
| `<world>`         | Name of the world the player is currently in                                                |
| `<health>`        | Player's current health                                                                     |

---
