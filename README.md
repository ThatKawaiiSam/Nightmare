# Nightmare
> Unique Factions Event Plugin.

---

## About 
Nightmare is an event in which Capzones can be captured and controlled by factions at any time in order to gain access to different reward tiers. The first 3 tiers gain your entire faction higher sell multipliers (0.5x - 2.0x). The final tier once all capzones are controlled at the same time will grant the faction access to the Nightmare realm which contains various mysterious and illusive perks.

---

## Custom Events
Within the plugin, there are various custom events which external developers can use and implement into their own plugins. To access these, simply look in the ``io.github.thatkawaiisam.nightmare.modules.zones.events`` directory.

---

## Commands

**Normal Commands**

| Command        | Description |
| ------------- | ----- |
| /nightmare help | Lists all of the Nightmare commands. |
| /nightmare info | Shows information about all of the capzones. |
| /nightmare perks | Set's the region of a zone. |

**Admin Commands**

| Command        | Permission | Description |
| ------------- |:-------------:| -----|
| /nightmare admin | Nightmare.Command.Admin | Lists all of the admin commands. |
| /nightmare reset | Nightmare.Command.Reset | Resets all of the current factions capping along with their rewards. |
| /nightmare create (id) | Nightmare.Command.Create | Creates a new zone. |
| /nightmare delete (id) | Nightmare.Command.Delete | Deletes a zone. |
| /nightmare setregion (id) | Nightmare.Command.SetRegion | Set's the region of a zone. |
| /nightmare teleport (id) | Nightmare.Command.Teleport | Teleports you to the zone. |

---

## Requirements
To build Nightmare, the following will need to be installed and available on your system:

* [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Git](https://git-scm.com)
* [Maven](https://maven.apache.org)

---

## Contributing
When contributing, please create a pull request with the branch named as follows ``<feature/fix>/<title>``.

To compile, run the maven command: ``mvn clean install``

To run unit tests, run the maven command ``mvn test``

---

## Contact

- Discord: ThatKawaiiSam#2882
- Telegram: ThatKawaiiSam