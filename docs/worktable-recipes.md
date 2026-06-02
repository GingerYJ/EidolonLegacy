# Worktable 配方验证清单

这份文档是临时验证用的 Worktable 配方表。GUI 和 HEI 完善前，可以先照这里摆材料。

## 槽位说明

3x3 合成区：

```text
[1] [2] [3]
[4] [5] [6]
[7] [8] [9]
```

4 个试剂位：

```text
    [A]
[D]     [B]
    [C]
```

下面每个配方中：

- `空` 表示空槽。
- `合成区` 按 3 行摆进 3x3。
- `试剂` 按 A、B、C、D 四个位置摆。
- 括号里的 `eidolon:xxx` 或 `minecraft:xxx` 是物品 ID，方便排错。

## 推荐优先测试

### 邪纹布 x8

产物：邪纹布 x8（`eidolon:wicked_weave`）

合成区：

```text
白色羊毛    白色羊毛    白色羊毛
白色羊毛    暗影宝石    白色羊毛
白色羊毛    白色羊毛    白色羊毛
```

试剂：

```text
A = 亵渎符号
B = 蓝色染料
C = 空
D = 空
```

材料 ID：

- 白色羊毛 = `minecraft:white_wool`，1.12 中映射为 `minecraft:wool` meta 0
- 暗影宝石 = `eidolon:shadow_gem`
- 亵渎符号 = `eidolon:unholy_symbol`
- 蓝色染料 = `forge:dyes/blue`

### 石祭坛 x3

产物：石祭坛 x3（`eidolon:stone_altar`）

合成区：

```text
平滑石台阶  平滑石台阶  平滑石台阶
石头        石头        石头
石头        白镴嵌片    石头
```

试剂：

```text
A = 灵魂碎片
B = 空
C = 空
D = 空
```

材料 ID：

- 平滑石台阶 = `minecraft:smooth_stone_slab`，1.12 中映射为 `minecraft:stone_slab` meta 0
- 石头 = `minecraft:stone`
- 白镴嵌片 = `eidolon:pewter_inlay`
- 灵魂碎片 = `eidolon:soul_shard`

### 术士帽

产物：术士帽（`eidolon:warlock_hat`）

合成区：

```text
空      邪纹布  空
空      邪纹布  空
邪纹布  空      邪纹布
```

试剂：

```text
A = 灵魂碎片
B = 空
C = 灵魂碎片
D = 空
```

材料 ID：

- 邪纹布 = `eidolon:wicked_weave`
- 灵魂碎片 = `eidolon:soul_shard`

## 完整配方索引

### 仪式匕首

产物：仪式匕首（`eidolon:athame`）

合成区：

```text
空        空        白镴锭
空        白镴嵌片  空
末影珍珠  空        空
```

试剂：A = 金粒，B = 银粒，C = 空，D = 空

### 寒骨魔杖

产物：寒骨魔杖（`eidolon:bonechill_wand`）

合成区：

```text
空        白镴锭    怨灵之心
空        木棍      白镴锭
白镴嵌片  空        空
```

试剂：A = 次级灵魂宝石，B = 骨粉，C = 骨粉，D = 骨粉

### 劈裂之斧

产物：劈裂之斧（`eidolon:cleaving_axe`）

合成区：

```text
白镴锭  白镴锭  空
白镴锭  木棍    空
空      木棍    空
```

试剂：A = 亵渎符号，B = 空，C = 白镴嵌片，D = 空

### 死亡使者镰刀

产物：死亡使者镰刀（`eidolon:deathbringer_scythe`）

合成区：

```text
骨头  凋灵骷髅头  骨头
骨头  收割者镰刀  骨头
骨头  骷髅头      骨头
```

试剂：A = 暗影宝石，B = 死亡精华，C = 暗影宝石，D = 死亡精华

### 玻璃之手

产物：玻璃之手（`eidolon:glass_hand`）

合成区：

```text
空  钻石块    空
空  基础护符  空
空  玻璃      空
```

试剂：A = 僵尸之心，B = 次级灵魂宝石，C = 怨灵之心，D = 次级灵魂宝石

### 重力腰带

产物：重力腰带（`eidolon:gravity_belt`）

合成区：

```text
空    末影珍珠  空
羽毛  基础腰带  羽毛
空    次级灵魂宝石  空
```

试剂：A = 末影灰，B = 白镴嵌片，C = 末影灰，D = 白镴嵌片

### 心灵护板

产物：心灵护板（`eidolon:mind_shielding_plate`）

合成区：

```text
铅锭  铅锭  铅锭
铅锭  铅锭  铅锭
皮革  空    皮革
```

试剂：A = 青金石块，B = 空，C = 下界石英，D = 空

### 种植箱

产物：种植箱（`eidolon:planter`）

合成区：

```text
白镴锭  泥土      白镴锭
木板    白镴嵌片  木板
木板    空        木板
```

试剂：A = 附魔灰烬，B = 空，C = 灵魂碎片，D = 空

### 威望之掌

产物：威望之掌（`eidolon:prestigious_palm`）

合成区：

```text
空      邪纹布          空
邪纹布  邪纹布          邪纹布
空      次级灵魂宝石    空
```

试剂：A = 诡异菌芽，B = 末影灰，C = 灵魂碎片，D = 末影灰

### 收割者镰刀

产物：收割者镰刀（`eidolon:reaper_scythe`）

合成区：

```text
白镴锭  白镴锭  空
空      木棍    白镴锭
木棍    空      空
```

试剂：A = 亵渎符号，B = 破布，C = 灵魂碎片，D = 破布

### 坚毅腰带

产物：坚毅腰带（`eidolon:resolute_belt`）

合成区：

```text
空        金嵌片    空
奥术金锭  基础腰带  奥术金锭
空        钻石      空
```

试剂：A = 皮革，B = 灵魂碎片，C = 附魔灰烬，D = 灵魂碎片

### 反转之镐

产物：反转之镐（`eidolon:reversal_pick`）

合成区：

```text
黑曜石  哭泣黑曜石  黑曜石
空      白镴锭      空
空      白镴嵌片    空
```

试剂：A = 末影珍珠，B = 灵魂碎片，C = 次级灵魂宝石，D = 灵魂碎片

### 魂骨护符

产物：魂骨护符（`eidolon:soulbone_amulet`）

合成区：

```text
骨头  基础护符  骨头
骨头  怨灵之心  骨头
空    末影灰    空
```

试剂：A = 末影灰，B = 死亡精华，C = 暗影宝石，D = 死亡精华

### 魂火魔杖

产物：魂火魔杖（`eidolon:soulfire_wand`）

合成区：

```text
空      奥术金锭  暗影宝石
空      木棍      奥术金锭
金嵌片  空        空
```

试剂：A = 次级灵魂宝石，B = 烈焰粉，C = 烈焰粉，D = 烈焰粉

### 灵魂附魔台

产物：灵魂附魔台（`eidolon:soul_enchanter`）

合成区：

```text
空        书        空
奥术金锭  黑曜石    奥术金锭
黑曜石    黑曜石    黑曜石
```

试剂：A = 钻石，B = 金嵌片，C = 钻石，D = 金嵌片

### 亵渎替身

产物：亵渎替身（`eidolon:unholy_effigy`）

合成区：

```text
空    平滑石头  空
石头  石头      石头
空    石头      空
```

试剂：A = 亵渎符号，B = 空，C = 金嵌片，D = 空

### 虚空护符

产物：虚空护符（`eidolon:void_amulet`）

合成区：

```text
空        白镴锭    空
白镴嵌片  基础护符  白镴嵌片
空        黑曜石    空
```

试剂：A = 灵魂碎片，B = 空，C = 灵魂碎片，D = 空

### 守护锁甲

产物：守护锁甲（`eidolon:warded_mail`）

合成区：

```text
空        灵魂碎片  空
附魔灰烬  铁胸甲    附魔灰烬
空        附魔灰烬  空
```

试剂：A = 白镴嵌片，B = 白镴嵌片，C = 白镴嵌片，D = 白镴嵌片

### 术士靴

产物：术士靴（`eidolon:warlock_boots`）

合成区：

```text
空      空  空
邪纹布  空  邪纹布
邪纹布  空  邪纹布
```

试剂：A = 灵魂碎片，B = 空，C = 灵魂碎片，D = 空

### 术士斗篷

产物：术士斗篷（`eidolon:warlock_cloak`）

合成区：

```text
邪纹布  邪纹布  邪纹布
邪纹布  邪纹布  邪纹布
邪纹布  空      邪纹布
```

试剂：A = 灵魂碎片，B = 空，C = 灵魂碎片，D = 空
