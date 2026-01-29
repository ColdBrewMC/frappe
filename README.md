# Conduit
Khronos-inspired rendering extensions for the Fabric Renderer API.

## Why?
Due to Sodium's omnipresence in the Fabric modding community and in modpacks, many rendering-heavy
mods resort to using Mixin on Indigo and Sodium. Mockingly, Fabric Renderer API (also called FRAPI)
exists for mods like Sodium, but its API is heavily limited and almost exclusively useful for
complicated block models. Thus, Conduit was borne out of the desire for a cohesive, powerful renderer
API.

## How?
Enter Conduit extensions.
Conduit extensions are additional, optional APIs that renderers may opt into implementing.
However, other mods may add support for features that renderers do not support (e.g. Sodium-Conduit
Compatibility), and this is the primary advantage Conduit has over traditional renderer
implementations and extensions.

Conduit's extension system is heavily inspired by Khronos Group's OpenGL and Vulkan extension APIs.
In other words, "Extension query goes in, extension API comes out."

## PR this into FRAPI!
No, this is far out of scope. The primary concerns are maintainability and use-case, and FRAPI is
already quite difficult to maintain when it breaks. Please do not bother the FRAPI developers.

## Will this be added to Sodium? Iris? etc.?
No, absolutely not. Conduit's API is not in scope for individual renderer mods, and Cerise—Conduit's
default Indigo and Sodium implementation—is licensed MPL-2.0. Unless an agreement is made on labor
and licensing, Cerise/Conduit will remain a separate mod.
