# MoreParticle

## 在Minecraft中自定义粒子特效

### 命令文档

所有命令均采用Minecraft原版Particle命令

```cmd
particle <name> [<pos>] [<delta>] <speed> <count> [force|normal] [viewers] [tag]
```

- 所有自定义粒子通过<name>字段自定义
- **force**参数请务必填入，避免部分情况下不显示的问题
- 计时单位统一为`tick`, 正常情况下`1t=0.05sec`
- 若粒子量过大，可能会导致老年粒子提前消失，建议配合瓶子的解除上限模组使用

- 数据类型解释:
    - `int`: 整数类型参数
    - `number`: 整数，浮点数均可
    - `string`: 字符串参数，需要用单引号包裹

- 参数解释:
    - `x/y/zExp: [string]`: 表达式，可以是一个数值，也可以是一个标准数学表达式，函数自变量支持`t`
      ，为粒子出生起到当前的时间，例如`'114514'`,`'sin(t)'`,`'if(t<20,t*3,t/2)'`
    - `life: int`: 粒子的持续时间tick
    - `random: int`: 粒子持续时间的随机附加值，**该参数值最小必须为1**
    - `colorExp: string`: 颜色表达式，RGB十进制整数值，例如红色(`#FF0000`)则应该输入字符串`'16711680'`
    - `color: int`: 颜色十进制整数值，例如红色(`#FF0000`)则应该输入`16711680`
    - `x/y/z/colorList: [int]`: 列表，序列粒子的x/y/z/color/scale列表
    - `texture: string`: 粒子的纹理路径，例如`'path/to/texture.png'`

- ### **soy:best** 自定义粒子

```cmd
soy:best <xExp: string> <yExp: string> <zExp: string> <life: int> <random: int> <colorExp: string>
e.g.
particle soy:best 'sin(t/20*2*pi)' '0' 'cos(t/20*2*pi)' 20 5 '16711680' ~ ~1 ~ 0 0 0 0 1 force @a
此命令会生成一个做圆周运动的红色粒子
```

- ### **soy:color_particle** 自定义颜色的粒子

```cmd
soy:color_particle <color>
```

- ### **soy:life_color_particle** 自定义生命和颜色的粒子

```cmd
soy:life_color_particle <color> <life> <random>
```

- ### **soy:life_color_texture** 自定义生命、颜色和纹理的粒子

```cmd
soy:life_color_texture <color> <life> <random> <scale> <texture>
```

- ### **soy:life_end_rod** 自定义生命的原版末地烛粒子

```cmd
soy:life_end_rod <life> <random>
```

- ### **soy:life_firework** 自定义生命的原版烟花粒子

```cmd
soy:life_firework <life> <random>
```

- ### **soy:seq** 序列粒子
这个粒子适合用于穷举运动位置，颜色，大小，貌似是每tick变化一次，我没有测试过
```cmd
soy:seq <xList> <yList> <zList> <life> <random> <colorList> <scaleList>
```

- ### **soy:seqt** 自定义纹理序列粒子
```cmd
soy:seq <xList> <yList> <zList> <life> <random> <colorList> <scaleList> <texture>
```

**文档由 [SnowyKami](https://github.com/snowykami) 提供**