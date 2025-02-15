# MoreParticle
## [ENGLISH](README-EN.MD) 
## 在Minecraft中自定义粒子特效

### 命令文档

粒子生成命令均采用Minecraft原版particle命令

``` mclang
particle <name>{<NBT>} [<pos>] [<delta>] <speed> <count> [force|normal] [viewers] [tag]
```

- 所有自定义粒子通过<NBT>字段自定义
- **force**参数请务必填入，避免部分情况下不显示的问题
- 计时单位统一为`tick`, 正常情况下`1t=0.05s`
- 若粒子量过大(超过16384个粒子)，可能会导致老年粒子提前消失，建议配合解除粒子上限模组使用
- tag是用于定义粒子的标签的，配合`killparticle`命令使用

### killparticle 清除粒子命令

``` mclang
killparticle <players> <tag>
```

若不指定玩家，则默认为`@a`；若不指定`tag`，则默认清除全部粒子。

### 粒子渲染模式枚举

| 类型                         | 说明                 |
|----------------------------|--------------------|
| TERRAIN_SHEET              | 地形粒子，貌似是内部测试粒子     |
| PARTICLE_SHEET_TRANSLUCENT | 支持透明度的粒子(光影下不支持水反) |
| PARTICLE_SHEET_OPAQUE      | 不支持透明度的粒子(光影下支持水反) |
| PARTICLE_SHEET_LIT         | 不支持透明度的粒子(光影下支持水反) |

### 粒子光照参数说明

在Minecraft中，光照分为两种，一种是方块亮度，一种是天空亮度，粒子光照也有这两个亮度

$粒子的亮度=方块亮度 * 16 + 天空亮度 * 65536$

## **soy:calc** 函数计算粒子

函数计算粒子适用于**测试**粒子使用，不建议大规模使用此类粒子，容易造成卡顿

### NBT参数如下：

| 名称       | 类型         | 说明                         | 必要? | 默认值        |
|----------|------------|----------------------------|-----|------------|
| `xfun`   | __string__ | 粒子x方向的 __计算函数__(相对于粒子的初始位置) | √   |
| `yfun`   | __string__ | 粒子y方向的 __计算函数__(相对于粒子的初始位置) | √   |
| `zfun`   | __string__ | 粒子z方向的 __计算函数__(相对于粒子的初始位置) | √   |
| `cfun`   | __string__ | 粒子颜色的 __计算函数__ |     | "16777215" |
| `age`    | __int__    | 粒子寿命                       | √   |
| `random` | __int__    | 粒子寿命的随机额外寿命                |     | 1          |    

## **soy:seq** 序列粒子

序列粒子使用数组存储每tick粒子的行为

### NBT参数如下：

| 名称       | 类型           | 说明                          | 必要? | 默认值                          |
|----------|--------------|-----------------------------|-----|------------------------------|
| `xlist`  | __double[]__ | 粒子x方向的 __位置序列__(相对于粒子的初始位置) | √   |
| `ylist`  | __double[]__ | 粒子y方向的 __位置序列__(相对于粒子的初始位置) | √   |
| `zlist`  | __double[]__ | 粒子z方向的 __位置序列__(相对于粒子的初始位置) | √   |
| `age`    | __int__      | 粒子寿命                        | √   |
| `random` | __int__      | 粒子寿命的随机额外寿命                 |     | 1                            |         |
| `clist`  | __int[]__    | 粒子的 __颜色序列(rgba)__          |     | [16777215]                   |
| `alist`  | __int[]__    | 粒子的 __大小序列__                |     | [1f]                         |
| `light`  | __int[]__    | 粒子的 __亮度序列__                |     | [15728880]                   |
| `render` | __enum__     | 粒子的 __渲染模式__                |     | "PARTICLE_SHEET_TRANSLUCENT" |

## **soy:seqt** 序列材质粒子

序列材质粒子使用数组存储每tick粒子的行为，支持使用自定义材质来改变粒子外观

### NBT参数如下：

<table>
<thead><tr><td><b>名称</b></td><td><b>类型</b></td><td><b>说明</b></td><td><b>必要?</b></td><td><b>默认值</b></td></tr></thead>
<tr><td align="center" colspan="5"><b>继承soy:seq的全部参数</b></td></tr>
<tr><td><code>texture</code></td><td><b>string</b></td><td>材质的名称</td><td></td><td>""</td></tr>
</table>

## **soy:seqv** 序列视角粒子

序列视角粒子使用数组存储每tick粒子的行为，同时为粒子添加了视角变换功能

### NBT参数如下：

<table>
<thead><tr><td><b>名称</b></td><td><b>类型</b></td><td><b>说明</b></td><td><b>必要?</b></td><td><b>默认值</b></td></tr></thead>
<tr><td align="center" colspan="5"><b>继承soy:seqt的全部参数</b></td></tr>

<tr><td><code>relative</code></td><td><b>boolean</b></td><td>视角是否相对于玩家的视角变化</td><td></td><td>true</td></tr>
<tr><td><code>angleX</code></td><td><b>float[]</b></td><td>x方向旋转</td><td></td><td>[0f]</td></tr>
<tr><td><code>angleY</code></td><td><b>float[]</b></td><td>y方向旋转</td><td></td><td>[0f]</td></tr>
<tr><td><code>angleZ</code></td><td><b>float[]</b></td><td>z方向旋转</td><td></td><td>[0f]</td></tr>
</table>
