# 3D Java Game Engine

3D Java Game Engine is a simple game engine in development since summer 2022, written in Java using the low-level
technologies provided by LWJGL. The original intention was to begin making a game after making a small engine, which
was thought to take around a couple of weeks (a serious underestimate). Today, the game engine is still being
developed, features being added whenever time permits.

## Overview and Features

The main technologies used from LWJGL are OpenGL, GLFW, Assimp, and OpenAL.

### Main features

- First, the game engine has multiplayer support via Java's TCP networking API. In fact, multiplayer is an integral
  part of the engine that influences many design and code structure decisions. It is accounting for multiplayer that
  determines how subsystems should relate to other subsystems. Structuring subsystems carefully in this way allows for a
  well-contained subset of code to be used as the base for the standalone game engine server program, which is a separate
  repository (found [here](https://github.com/james-dire-1/EngineServer)).
- Keyboard and mouse input
- Renderers for textured and colour geometry, GLSL shader programs
- Multitexturing using blend maps
- Model and scene importing from Collada (.dae) files exported from Blender
- Pixel-sized and normalized-sized GUIs that can be anchored and animated
- Collision detection (ellipsoid vs mesh, ray vs sphere, ray vs mesh, ray vs AABB, sphere vs sphere)
- Customizable text rendering (options for font, size, alignment, colour, gradients)
- Text fields
- Multiple threads to separately handle client-side rendering, server-side simulation, and networking I/O
- Diffuse and specular lighting; point and directional lights
- 3D audio support
- Mouse picking
- Particle effects, skyboxes, fog

### Game-specific feature tests
(In addition to pure engine features)

- Camera controls and effects
- Smooth player movement
- Gravity and player falling recovery
- Chat screen to talk with other players
- Debug screen displaying various statistics

## Showcase

Any folder in this repository that has a name beginning with `G_` is meant to showcase the game engine, and is not necessary for proper functioning.

Right now, the only such folder is `G_miscscreenshots`, which contains many screenshots taken throughout development. These screenshots show works in progress for various features, strange graphical effects, weird/amusing bugs, the results of changing in-code values to absurd values, and some math calculations. These screenshots are not curated and do not show the game engine in its most polished state. Curated screenshots will be added to the repository at a later date.

## Project Structure

```
the-game-engine/
│
├── src/             # Where the code is located
├── doc/             # Various files that might be useful later
├── old/             # Outdated files no longer needed
├── lib/             # Contains the LWJGL natives, sources, and docs
├── LICENSE          # MIT License file
├── README.md        # This file
├── res              # Project resources
│   ├── textures/
│   ├── models/
│   ├── fonts/
│   └── audio/
└── src              # Where the code is located
    ├── com/james/
    ├── templates/
    ├── game/
    └── newStuff/
```

- `src/com/james/` contains the main game engine code. The code in here is designed to not have to be modified by those making games using the engine; instead, it is intended to be used as-is. (Of course, it can still be modified by users if need be.)
- `src/templates/` contains code that is a mix between pure engine code and game-specific code. Most of the files in here are supposed to be nearly empty for users to fill in, or are already filled with basic features but are intended to be added on to for game-specific features. For instance, multiplayer networking events can be found here. There are some common ones necessary for almost every game which are already included (like broadcasting player positions), whereas game-specific events (like player inventory changes) can be included by users themselves.
- `src/game/` contains game-specific code. For new games being made, this folder should start off empty. In this repository however, there is already code in this folder due to recent testing I have been doing.
- `src/newStuff/` contains new code that hasn't been sorted yet.

Within each of the three former folders, there are additional subfolders (and sub-subfolders and sub-sub-folders, etc.) to further organize the project. The most notable top-level subfolders however are `serverSide` and `common`. The code present in these two subfolders is also present as-is in the [game engine server repository](https://github.com/james-dire-1/EngineServer). Having a separate repository for the multiplayer server (and having the code in that separate repository be a subset of the code in the main repository) makes creating online games easier.

`com/james/` contains its own `common` and `serverSide`; `templates/` contains its own `common` and `serverSide`; and `game/` contains its own `serverSide`.

## How to Run

The project can be imported into IntelliJ (or any other Java IDE) and run normally. LWJGL is already included with the project (in the `lib/` folder), so all that is needed is to modify the project settings in your IDE to recognize LWJGL as a library.

The project uses features up until and including Java 8 and uses LWJGL 3.3.0 build 21. The project is tested using Java 17, but any modern Java version will probably work.

More details on how to run will come at a later date.

## Future Plans

- Improve the code documentation. After much refactoring and redesign decisions, much of the documentation present has become outdated and in many cases incorrect. Also, many of the newer features added within the last year have not been documented at all.
- Make a comprehensive user guidebook for learning how to use the game engine to make games. This seems like a very daunting task though, as there is a lot that needs to be explained in a sequential order. Not sure if I will carry on with this idea.
- Add model animation support
- Improve the model/scene importing to support more features from Blender. For instance, right now, if one would like a scene to have coloured lights in specific places, these lights need to be described in the code, as the current model loader does not parse light data coming from Blender.
- Add many more graphical effects like realistic water, bloom, Gaussian blur, etc.
- Begin making a game with the game engine to see how well it holds up, and to see what else needs to be added.

## Timeline

TODO

## Acknowledgements

TODO

## Libraries Used

- [The Lightweight Java Game Library](https://www.lwjgl.org/)
- [JLine](https://jline.org/) (used only in the server repository)

## License

This project is licensed under the [MIT License](LICENSE).

This project uses [LWJGL](https://www.lwjgl.org/) (The Lightweight Java Game Library). Its license can be found in [`lib/LWJGL_LICENSE`](lib/LWJGL_LICENSE).

This project's server repository uses [JLine](https://jline.org/). Its license can be found in [`lib/JLINE_LICENSE`](lib/JLINE_LICENSE).