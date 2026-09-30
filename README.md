# 3D Java Game Engine

3D Java Game Engine is a simple game engine in development since summer 2022, written in Java using the low-level 
technologies provided by LWJGL. The original intention was to begin making a game after making a small engine, which 
was thought to take around a couple of weeks. Of course, this was a serious underestimate of the time it would take. 
Today, the game engine is still being developed, features being added whenever time permits 
(but still no game!).

## Overview and Features

The main technologies used from LWJGL are OpenGL, GLFW, Assimp, and OpenAL.

### Main features

- Firstly, the game engine has multiplayer support via Java's TCP networking API. In fact, multiplayer is an integral 
part of the engine that influences many design and code structure decisions. It is accounting for multiplayer that
determines how subsystems should relate to other subsystems. Structuring subsystems carefully in this way allows for a 
well-contained subset of code to be used as the base for the standalone game engine server program, which is a separate 
repository.
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

Note: this README document is new, so there is not much to see. More comprehensive info is coming soon.