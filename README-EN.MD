# MoreParticle

## Create particle with customization

### Commands

The MoreParticle use vanilla /particle command

``` mclang
particle <name>{<NBT>} [<pos>] [<delta>] <speed> <count> [force|normal] [viewers] [tag]
```

- all custom particles are customized by <NBT> field
- **force** always useful for display particles
- the time unit is `tick`, in ususally `1t=0.05s`
- if particles count over 16384 it makes older particle dead, use particle limit changer mod to remove limit
- `tag` field is for define the tag for particle,used by `killparticle` command

### killparticle command

``` mclang
killparticle <players> <tag>
```

if has no `player` felid ,select all player; if has no `tag` field, kill all particle

### particle render modes(enum)

| type                       | description                                    |
|----------------------------|------------------------------------------------|
| TERRAIN_SHEET              | terrain particle, it may use for inner testing |
| PARTICLE_SHEET_TRANSLUCENT | support transparency                           |
| PARTICLE_SHEET_OPAQUE      | unsupport transparency                         |
| PARTICLE_SHEET_LIT         | unsupport transparency                         |

### particle lights

particle has two lights, one is block light, another is sky light

$light = blockLight * 16 + skyLight * 65536$

## [**soy:calc**] function calculate particle

function calculate particle designed for **testing**, calculate is slow and too much function calculate particle will
cause lagging

### NBT fields

| name     | type       | description                                                 | required? | default value |
|----------|------------|-------------------------------------------------------------|-----------|---------------|
| `xfun`   | __string__ | x direction's  __calculate function__(position is relative) | √         |               |
| `yfun`   | __string__ | y direction's  __calculate function__(position is relative) | √         |               |
| `zfun`   | __string__ | z direction's  __calculate function__(position is relative) | √         |               |
| `cfun`   | __string__ | color's __calculate function__                              |           | "16777215"    |
| `age`    | __int__    | particle's life                                             | √         |               |
| `random` | __int__    | particle's extra life in random                             |           | 1             |    

## [**soy:seq**] sequence particle

sequence particle use array to store particle's action in every tick

### NBT fields

| name     | type         | description                                      | required? | default value                |
|----------|--------------|--------------------------------------------------|-----------|------------------------------|
| `xlist`  | __double[]__ | x direction's __sequence__(position is relative) | √         |
| `ylist`  | __double[]__ | y direction's __sequence__(position is relative) | √         |
| `zlist`  | __double[]__ | z direction's __sequence__(position is relative) | √         |
| `age`    | __int__      | particle's life                                  | √         |                              |
| `random` | __int__      | particle's extra life in random                  |           | 1                            |    
| `clist`  | __int[]__    | color's __sequence(rgba model)__                 |           | [16777215]                   |
| `alist`  | __int[]__    | size __sequence__                                |           | [1f]                         |
| `light`  | __int[]__    | light __sequence__                               |           | [15728880]                   |
| `render` | __enum__     | render __mode__                                  |           | "PARTICLE_SHEET_TRANSLUCENT" |

## [**soy:seqt**] sequence particle with texture

sequence particle with texture use array to store particle's action in every tick, and support custom texture

### NBT field

<table>
<thead><tr><td><b>name</b></td><td><b>type</b></td><td><b>description</b></td><td><b>required?</b></td><td><b>default value</b></td></tr></thead>
<tr><td align="center" colspan="5"><b>extended by [soy:seq]</b></td></tr>
<tr><td><code>texture</code></td><td><b>string</b></td><td>texture namespace</td><td></td><td>""</td></tr>
</table>

## **soy:seqv** sequence particle with view

sequence particle with texture use array to store particle's action in every tick,
and added custom viewing support

### NBT参数如下：

<table>
<thead><tr><td><b>name</b></td><td><b>type</b></td><td><b>description</b></td><td><b>required?</b></td><td><b>default value</b></td></tr></thead>
<tr><td align="center" colspan="5"><b>extended by [soy:seqt]</b></td></tr>
<tr><td><code>relative</code></td><td><b>boolean</b></td><td>particle's angle is relateive with camera</td><td></td><td>true</td></tr>
<tr><td><code>angleX</code></td><td><b>float[]</b></td><td>X rotation</td><td></td><td>[0f]</td></tr>
<tr><td><code>angleY</code></td><td><b>float[]</b></td><td>Y rotation</td><td></td><td>[0f]</td></tr>
<tr><td><code>angleZ</code></td><td><b>float[]</b></td><td>Z rotation</td><td></td><td>[0f]</td></tr>
</table>
