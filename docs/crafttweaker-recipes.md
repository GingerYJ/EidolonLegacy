# CraftTweaker 配方接入

EidolonLegacy 目前把 Worktable、Crucible、Altar Ritual、Altar Offering、Athame Harvest、Research 几类配方或规则开放给 CraftTweaker 2。

配方 id 使用普通资源路径。建议写完整命名空间，例如 `eidolon:athame` 或 `pack:test_worktable`。如果要替换内置配方，可以先 `removeById`，再用相同 id 添加新配方。

说明里的示例尽量按“每一行都写注释”的方式写。真实脚本中可以删掉注释，只保留调用本身。

## Worktable

### 参数速查

`Worktable.addRecipe(id, output, grid, reagents)`

- `id`：配方 id，字符串，建议带命名空间，例如 `"pack:test_worktable"`。
- `output`：产物，`IItemStack`，例如 `<eidolon:soul_shard>`。
- `grid`：3x3 主输入，共 9 个 `IIngredient` 或 `null`。
- `reagents`：Worktable 四个试剂槽，共 4 个 `IIngredient` 或 `null`。

```zenscript
// 导入 Eidolon Worktable 的 CT 接口；没有这一行就不能写 Worktable.addRecipe。
import mods.eidolon.Worktable;

// 添加一条 Worktable 配方；括号内参数顺序固定为：id、产物、3x3 主输入、4 个试剂槽。
Worktable.addRecipe(
    // 第 1 个参数：配方 id；pack 是自定义命名空间，test_worktable 是配方名。
    "pack:test_worktable",
    // 第 2 个参数：配方输出；这里表示合成结果是 1 个灵魂碎片。
    <eidolon:soul_shard>,
    // 第 3 个参数：3x3 主输入数组；必须刚好写 9 个位置。
    [
        // 第 1 行第 1 格；对应 GUI 左上角格子。
        <minecraft:stick>,
        // 第 1 行第 2 格；对应 GUI 上排中间格子。
        <minecraft:stick>,
        // 第 1 行第 3 格；对应 GUI 右上角格子。
        <minecraft:stick>,
        // 第 2 行第 1 格；对应 GUI 中排左侧格子。
        <minecraft:stick>,
        // 第 2 行第 2 格；对应 GUI 正中格子。
        <minecraft:diamond>,
        // 第 2 行第 3 格；对应 GUI 中排右侧格子。
        <minecraft:stick>,
        // 第 3 行第 1 格；对应 GUI 左下角格子。
        <minecraft:stick>,
        // 第 3 行第 2 格；对应 GUI 下排中间格子。
        <minecraft:stick>,
        // 第 3 行第 3 格；对应 GUI 右下角格子。
        <minecraft:stick>
    ],
    // 第 4 个参数：四个试剂槽数组；必须刚好写 4 个位置。
    [
        // 试剂槽 1；这里需要 1 个白镴镶嵌。
        <eidolon:pewter_inlay>,
        // 试剂槽 2；null 表示这个槽位不需要物品。
        null,
        // 试剂槽 3；null 表示这个槽位不需要物品。
        null,
        // 试剂槽 4；null 表示这个槽位不需要物品。
        null
    ]
);

// 按配方 id 删除；这里会删除内置的 eidolon:athame 配方。
Worktable.removeById("eidolon:athame");
// 按产物删除；所有输出匹配收割者镰刀的 Worktable 配方都会被删除。
Worktable.removeByOutput(<eidolon:reaper_scythe>);
// 删除全部 Worktable 配方；通常只在整包完全重写配方时使用。
// Worktable.removeAll();
```

## Crucible

### 参数速查

`Crucible.addRecipe(id, output, stirs, steps, stirrer, fluid)`

- `id`：配方 id，字符串。
- `output`：坩埚产物，`IItemStack`。
- `stirs`：每一步需要搅拌的次数，`int[]`，数量必须和 `steps` 一致。
- `steps`：每一步投入的物品，`IIngredient[][]`。
- `stirrer`：搅拌物，可省略；省略时默认为木棍。
- `fluid`：起始流体，可省略；省略时默认为一桶水。

```zenscript
// 导入 Eidolon Crucible 的 CT 接口；没有这一行就不能写 Crucible.addRecipe。
import mods.eidolon.Crucible;

// 添加一条 Crucible 配方；参数顺序固定为：id、产物、每步搅拌次数、每步投入物、搅拌物、流体。
Crucible.addRecipe(
    // 第 1 个参数：配方 id；用于删除、替换和日志定位。
    "pack:test_crucible",
    // 第 2 个参数：配方输出；这里表示完成后产出死亡精华。
    <eidolon:death_essence>,
    // 第 3 个参数：每一步需要搅拌几次；数组长度必须等于 steps 的步数。
    [
        // 第 1 步需要搅拌 0 次；投入后可直接确认该步。
        0,
        // 第 2 步需要搅拌 2 次；玩家需要用搅拌物右键两次。
        2,
        // 第 3 步需要搅拌 0 次；投入后可直接确认该步。
        0
    ],
    // 第 4 个参数：每一步需要投入的物品；外层数组表示步骤，内层数组表示该步骤的投入物。
    [
        // 第 1 步投入物列表；同一步内可以有多个物品。
        [
            // 第 1 步第 1 个投入物；这里需要腐肉。
            <minecraft:rotten_flesh>,
            // 第 1 步第 2 个投入物；这里需要附魔灰烬。
            <eidolon:enchanted_ash>
        ],
        // 第 2 步投入物列表；这里该步骤只需要一个物品。
        [
            // 第 2 步第 1 个投入物；这里需要灵魂碎片。
            <eidolon:soul_shard>
        ],
        // 第 3 步投入物列表；这里该步骤只需要一个物品。
        [
            // 第 3 步第 1 个投入物；minecraft:coal:1 表示木炭。
            <minecraft:coal:1>
        ]
    ],
    // 第 5 个参数：搅拌物；玩家必须拿这个物品右键坩埚完成搅拌次数。
    <minecraft:stick>,
    // 第 6 个参数：起始流体；这里表示需要 1000 mB 水，也就是一桶水。
    <liquid:water> * 1000
);

// 按配方 id 删除；这里会删除 eidolon:ender_calx 坩埚配方。
Crucible.removeById("eidolon:ender_calx");
// 按产物删除；所有输出匹配暗影宝石的坩埚配方都会被删除。
Crucible.removeByOutput(<eidolon:shadow_gem>);
// 删除全部坩埚配方；通常只在整包完全重写坩埚体系时使用。
// Crucible.removeAll();
```

## Altar Ritual

祭坛仪式会读取火盆、Stone Hand、Necrotic Focus、祭坛周围供物和玩家生命等条件。不同仪式类型使用不同方法，但基础参数含义相同。

### 普通产物仪式

`Altar.addItemResult(id, output, capacity, power, offerings, sacrifice)`

- `id`：仪式 id。
- `output`：仪式最终产物。
- `capacity`：需要的祭坛容量。
- `power`：需要的祭坛力量。
- `offerings`：Stone Hand 上需要消耗的物品列表；如果不单独写 `sacrifice`，第一个物品也会作为火盆祭品。
- `sacrifice`：火盆上的祭品，可省略。

```zenscript
// 导入 Eidolon Altar 的 CT 接口；祭坛仪式和祭坛供物都使用这个类。
import mods.eidolon.Altar;

// 添加普通产物仪式；这种仪式完成后直接生成 output。
Altar.addItemResult(
    // 第 1 个参数：仪式 id；用于删除、替换和日志定位。
    "pack:test_altar_result",
    // 第 2 个参数：仪式产物；这里完成后生成死亡精华。
    <eidolon:death_essence>,
    // 第 3 个参数：容量需求；祭坛周围供物提供的容量总值必须至少为 3.0。
    3.0,
    // 第 4 个参数：力量需求；祭坛周围供物提供的力量总值必须至少为 3.0。
    3.0,
    // 第 5 个参数：仪式物品列表；通常由 Stone Hand 提供并在仪式过程中消耗。
    [
        // 第 1 个仪式物品；未指定 sacrifice 时，它也会作为火盆祭品。
        <eidolon:soul_shard>,
        // 第 2 个仪式物品；由 Stone Hand 提供并被消耗。
        <minecraft:bone>
    ]
    // 第 6 个参数 sacrifice 省略；因此 offerings 的第一个物品会同时作为火盆祭品。
);
```

### 转化仪式

`Altar.addTransform(id, output, focus, capacity, power, offerings, health, sacrifice)`

- `output`：最终转化后的物品。
- `focus`：Necrotic Focus 中需要放入的被转化物。
- `health`：玩家生命消耗，可省略；单位使用生命值，`20.0` 等于 10 颗心。

```zenscript
// 添加转化仪式；这种仪式会在结束时把 focus 转化为 output。
Altar.addTransform(
    // 第 1 个参数：仪式 id；用于删除、替换和日志定位。
    "pack:test_altar_transform",
    // 第 2 个参数：转化结果；仪式完成后得到吸血剑。
    <eidolon:sapping_sword>,
    // 第 3 个参数：转化目标；玩家需要把铁剑放入 Necrotic Focus。
    <minecraft:iron_sword>,
    // 第 4 个参数：容量需求；祭坛扫描到的容量必须至少为 4.0。
    4.0,
    // 第 5 个参数：力量需求；祭坛扫描到的力量必须至少为 4.0。
    4.0,
    // 第 6 个参数：仪式物品列表；通常由 Stone Hand 提供并依次消耗。
    [
        // 第 1 个仪式物品；这里需要暗影宝石。
        <eidolon:shadow_gem>,
        // 第 2 个仪式物品；这里需要灵魂碎片。
        <eidolon:soul_shard>
    ],
    // 第 7 个参数：生命消耗；这里消耗 20 点生命，也就是 10 颗心。
    20.0
    // 第 8 个参数 sacrifice 省略；因此 offerings 的第一个物品会同时作为火盆祭品。
);
```

### 充能仪式

`Altar.addCharge(id, output, focus, capacity, power, offerings, sacrifice)`

- `output`：充能完成后的物品。
- `focus`：Necrotic Focus 中需要放入的待充能物品。

```zenscript
// 添加充能仪式；这种仪式会把 focus 中的物品变为 output。
Altar.addCharge(
    // 第 1 个参数：仪式 id；用于删除、替换和日志定位。
    "pack:test_altar_charge",
    // 第 2 个参数：充能结果；这里显示/产出充能后的魂火魔杖。
    <eidolon:soulfire_wand>,
    // 第 3 个参数：待充能物；玩家需要把魂火魔杖放入 Necrotic Focus。
    <eidolon:soulfire_wand>,
    // 第 4 个参数：容量需求；祭坛扫描到的容量必须至少为 3.0。
    3.0,
    // 第 5 个参数：力量需求；祭坛扫描到的力量必须至少为 3.0。
    3.0,
    // 第 6 个参数：仪式物品列表；通常由 Stone Hand 提供并依次消耗。
    [
        // 第 1 个仪式物品；这里需要次级灵魂宝石。
        <eidolon:lesser_soul_gem>,
        // 第 2 个仪式物品；这里需要烈焰粉。
        <minecraft:blaze_powder>
    ]
    // 第 7 个参数 sacrifice 省略；因此 offerings 的第一个物品会同时作为火盆祭品。
);
```

### 召唤仪式

`Altar.addSummon(id, displayOutput, entityId, focus, capacity, power, offerings, sacrifice)`

- `displayOutput`：只用于 HEI / Codex 显示，通常写对应实体的刷怪蛋。
- `entityId`：实际召唤的实体 id。
- `focus`：Necrotic Focus 中需要放入的物品。

```zenscript
// 添加召唤仪式；这种仪式会在完成后生成 entityId 对应实体。
Altar.addSummon(
    // 第 1 个参数：仪式 id；用于删除、替换和日志定位。
    "pack:test_altar_summon",
    // 第 2 个参数：显示产物；只给 HEI 和 Codex 用，不决定实际召唤内容。
    <minecraft:spawn_egg>.withTag({EntityTag: {id: "minecraft:zombie"}}),
    // 第 3 个参数：实际召唤实体 id；这里表示召唤僵尸。
    "minecraft:zombie",
    // 第 4 个参数：焦点物品；玩家需要把木炭放入 Necrotic Focus。
    <minecraft:coal:1>,
    // 第 5 个参数：容量需求；祭坛扫描到的容量必须至少为 3.0。
    3.0,
    // 第 6 个参数：力量需求；祭坛扫描到的力量必须至少为 3.0。
    3.0,
    // 第 7 个参数：仪式物品列表；通常由 Stone Hand 提供并依次消耗。
    [
        // 第 1 个仪式物品；这里需要灵魂碎片。
        <eidolon:soul_shard>,
        // 第 2 个仪式物品；这里需要腐肉。
        <minecraft:rotten_flesh>
    ]
    // 第 8 个参数 sacrifice 省略；因此 offerings 的第一个物品会同时作为火盆祭品。
);
```

### 吸收仪式

`Altar.addAbsorption(id, output, focus, capacity, power, offerings, sacrifice)`

- `output`：吸收完成后的物品。
- `focus`：Necrotic Focus 中需要放入的物品。

```zenscript
// 添加吸收仪式；这种仪式会让焦点物品吸收目标实体或吸收值。
Altar.addAbsorption(
    // 第 1 个参数：仪式 id；用于删除、替换和日志定位。
    "pack:test_altar_absorption",
    // 第 2 个参数：仪式结果；这里仍然显示召唤法杖。
    <eidolon:summoning_staff>,
    // 第 3 个参数：焦点物品；玩家需要把召唤法杖放入 Necrotic Focus。
    <eidolon:summoning_staff>,
    // 第 4 个参数：容量需求；祭坛扫描到的容量必须至少为 4.0。
    4.0,
    // 第 5 个参数：力量需求；祭坛扫描到的力量必须至少为 4.0。
    4.0,
    // 第 6 个参数：仪式物品列表；通常由 Stone Hand 提供并依次消耗。
    [
        // 第 1 个仪式物品；这里需要死亡精华。
        <eidolon:death_essence>,
        // 第 2 个仪式物品；这里需要灵魂碎片。
        <eidolon:soul_shard>
    ]
    // 第 7 个参数 sacrifice 省略；因此 offerings 的第一个物品会同时作为火盆祭品。
);

// 按仪式 id 删除；这里会删除 eidolon:lesser_soul_gem 仪式。
Altar.removeById("eidolon:lesser_soul_gem");
// 按产物删除；所有输出匹配死亡精华的祭坛仪式都会被删除。
Altar.removeByOutput(<eidolon:death_essence>);
// 删除全部祭坛仪式；通常只在整包完全重写仪式体系时使用。
// Altar.removeAll();
```

## Altar Offering

Altar Offering 是祭坛周围供物提供容量和力量的规则，不是仪式配方本身。

### 参数速查

- `id`：供物规则 id，同时也是数值分组 key。
- `item`：提供数值的物品。
- `capacity`：该物品提供的容量。
- `power`：该物品提供的力量。
- `blockOffering`：是否在“方块放在祭坛上方”时生效。
- `plateOffering`：是否在“物品放入祭品盘”时生效。

```zenscript
// 导入 Eidolon Altar 的 CT 接口；祭坛仪式和祭坛供物都使用这个类。
import mods.eidolon.Altar;

// 添加默认模式供物；ItemBlock 默认作为方块供物，普通物品默认作为祭品盘供物。
Altar.addOffering(
    // 第 1 个参数：供物规则 id；相同 id/key 不会重复叠加，只取最大值。
    "pack:test_offering",
    // 第 2 个参数：供物物品；这里表示钻石。
    <minecraft:diamond>,
    // 第 3 个参数：容量数值；这里提供 2.0 容量。
    2.0,
    // 第 4 个参数：力量数值；这里提供 1.0 力量。
    1.0
);

// 添加只在方块放到祭坛上方时生效的供物规则。
Altar.addBlockOffering(
    // 第 1 个参数：供物规则 id；用于删除和分组。
    "pack:test_block_offering",
    // 第 2 个参数：供物物品；物品本身必须能放置成方块。
    <minecraft:redstone_torch>,
    // 第 3 个参数：容量数值；这里提供 0.0 容量。
    0.0,
    // 第 4 个参数：力量数值；这里提供 2.0 力量。
    2.0
);

// 添加只在物品放入祭品盘时生效的供物规则。
Altar.addPlateOffering(
    // 第 1 个参数：供物规则 id；用于删除和分组。
    "pack:test_plate_offering",
    // 第 2 个参数：供物物品；这里表示绿宝石。
    <minecraft:emerald>,
    // 第 3 个参数：容量数值；这里提供 1.0 容量。
    1.0,
    // 第 4 个参数：力量数值；这里提供 2.0 力量。
    2.0
);

// 添加手动模式供物；可以自己决定方块模式和祭品盘模式是否生效。
Altar.addOfferingModes(
    // 第 1 个参数：供物规则 id；用于删除和分组。
    "pack:test_skull_offering",
    // 第 2 个参数：供物物品；这里表示普通骷髅头。
    <minecraft:skull:0>,
    // 第 3 个参数：容量数值；这里提供 2.0 容量。
    2.0,
    // 第 4 个参数：力量数值；这里提供 0.0 力量。
    0.0,
    // 第 5 个参数：blockOffering；true 表示放在祭坛上方时会被读取。
    true,
    // 第 6 个参数：plateOffering；true 表示放进祭品盘时也会被读取。
    true
);

// 按供物规则 id 删除；这里会删除 eidolon:light 供物规则。
Altar.removeOfferingById("eidolon:light");
// 按物品删除；所有以钻石为供物物品的规则都会被删除。
Altar.removeOfferingByItem(<minecraft:diamond>);
// 删除全部供物规则；通常只在整包完全重写祭坛数值体系时使用。
// Altar.removeAllOfferings();
```

Altar Offering 的 id 同时也是数值分组 key。相同 key 的多个供物不会重复叠加，只取该 key 下最大的容量/力量；不同 key 会相加。内置 key 包括：

- `eidolon:light`
- `eidolon:skull`
- `eidolon:goblet`
- `eidolon:essence`

当前 Altar Offering 按 `Item` 注册，不区分 metadata 或 NBT。

## Athame Harvest

### 参数速查

`Athame.addRecipe(id, source, output, sourceLabel)`

- `id`：采集规则 id。
- `source`：被仪式匕首右键采集的方块物品形态。
- `output`：成功采集后掉落的产物。
- `sourceLabel`：Codex 中显示的来源文字，可省略。

```zenscript
// 导入 Eidolon Athame 的 CT 接口；没有这一行就不能写 Athame.addRecipe。
import mods.eidolon.Athame;

// 添加仪式匕首采集规则；玩家用仪式匕首右键 source 对应植物时有概率掉落 output。
Athame.addRecipe(
    // 第 1 个参数：采集规则 id；用于删除、替换和日志定位。
    "pack:test_athame_harvest",
    // 第 2 个参数：可采集来源；这里表示红花 metadata 0，也就是玫瑰。
    <minecraft:red_flower:0>,
    // 第 3 个参数：采集产物；这里表示成功时掉落灵魂碎片。
    <eidolon:soul_shard>,
    // 第 4 个参数：来源显示文字；会显示在 Codex 的获取方式说明中。
    "玫瑰"
);

// 按采集规则 id 删除；这里会删除内置蕨类采集规则。
Athame.removeById("eidolon:avennian_sprig_from_fern");
// 按产物删除；所有产物匹配 Oanna Bloom 的采集规则都会被删除。
Athame.removeByOutput(<eidolon:oanna_bloom>);
// 按来源删除；所有来源匹配睡莲的采集规则都会被删除。
Athame.removeBySource(<minecraft:waterlily>);
// 删除全部仪式匕首采集规则；通常只在整包完全重写草药获取时使用。
// Athame.removeAll();
```

内置 Athame Harvest 配方 id：

- `eidolon:avennian_sprig_from_fern`
- `eidolon:merammer_root_from_oxeye_daisy`
- `eidolon:oanna_bloom_from_lily_pad`
- `eidolon:sildrian_seed_from_jungle_leaves`

CraftTweaker 脚本修改后，HEI 和 Codex 应读取运行时配方列表，因此应能同步显示新增或删除后的配方。

## Research

### 参数速查

`Research.addResearch(id, name, stars, prerequisites, stepItems, source, description)`

- `id`：研究 id。
- `name`：研究显示名，可直接写中文。
- `stars`：研究星级，必须是 `1-5`。
- `prerequisites`：前置研究 id 数组；没有前置就写 `[]`。
- `stepItems`：每一级研究桌可选择提交的物品；外层数量必须等于 `stars`，内层每个物品会生成一条可选任务，提交其中任意一条就推进该级。
- `source`：Codex 来源文字，可直接写中文。
- `description`：Codex 研究描述，可直接写中文。

```zenscript
// 导入 Eidolon Research 的 CT 接口；没有这一行就不能写 Research.addResearch。
import mods.eidolon.Research;

// 添加或替换一条研究；如果 id 和内置研究相同，就会覆盖同 id 研究。
Research.addResearch(
    // 第 1 个参数：研究 id；生成笔记、学习研究、前置判断都用这个 id。
    "pack:test_research",
    // 第 2 个参数：研究显示名；会显示在 Codex、研究笔记和完成研究 tooltip 中。
    "测试研究",
    // 第 3 个参数：研究星级；必须是 1 到 5，这里表示两级研究。
    2,
    // 第 4 个参数：前置研究列表；玩家必须先掌握这些研究才算解锁。
    [
        // 第 1 个前置研究；这里要求玩家先掌握秘典研究。
        "eidolon:codex"
    ],
    // 第 5 个参数：每一级可选择提交的物品；外层数组数量必须等于星级。
    [
        // 第 1 级研究桌任务组；这一数组中的每个物品都会生成一条可选任务。
        [
            // 第 1 级可选任务 1；提交 2 张纸即可完成第 1 级。
            <minecraft:paper> * 2,
            // 第 1 级可选任务 2；提交魔法墨水也可完成第 1 级。
            <eidolon:magic_ink>
        ],
        // 第 2 级研究桌任务组；完成第 1 级进度后才会出现。
        [
            // 第 2 级可选任务 1；提交羊皮纸即可完成第 2 级。
            <eidolon:parchment>,
            // 第 2 级可选任务 2；提交金锭也可完成第 2 级。
            <minecraft:gold_ingot>
        ]
    ],
    // 第 6 个参数：来源说明；会显示在 Codex 的“来源”区域。
    "用笔记工具右键测试方块。",
    // 第 7 个参数：研究描述；会显示在 Codex 的研究详情页。
    "这是由 CraftTweaker 定义的研究描述。"
);

// 如果想继续使用 lang 文件，可以使用 addResearchLang。
Research.addResearchLang(
    // 第 1 个参数：研究 id；名称键会自动使用 research.pack.lang_research。
    "pack:lang_research",
    // 第 2 个参数：研究星级；这里表示 1 级研究。
    1,
    // 第 3 个参数：前置研究列表；空数组表示没有前置。
    [],
    // 第 4 个参数：每一级可选择提交的物品；外层数量必须等于星级。
    [
        // 第 1 级研究桌任务组；这里只提供一本书这一种可选任务。
        [
            // 第 1 级可选任务 1；提交书即可完成该级。
            <minecraft:book>
        ]
    ],
    // 第 5 个参数：来源翻译键；会用 lang 文件里的 research_source.pack.lang_research。
    "research_source.pack.lang_research"
);

// 添加方块触发；玩家用笔记工具右键黑曜石时会生成 pack:test_research 的研究笔记。
Research.addBlockTrigger("minecraft:obsidian", "pack:test_research");
// 添加实体触发；玩家用笔记工具右键猪时会生成 pack:test_research 的研究笔记。
Research.addEntityTrigger("minecraft:pig", "pack:test_research");
// 添加维度触发；玩家在主世界右键空气时会生成 pack:test_research 的研究笔记。
Research.addDimensionTrigger(0, "pack:test_research");
// 添加流体触发；玩家用笔记工具右键静态水时会生成 pack:test_research 的研究笔记。
Research.addFluidTrigger("minecraft:water", "pack:test_research");
// 添加流体触发；玩家用笔记工具右键流动水时会生成 pack:test_research 的研究笔记。
Research.addFluidTrigger("minecraft:flowing_water", "pack:test_research");

// 按研究 id 删除；这里会删除内置贪食研究，同时移除它已有的触发绑定。
Research.removeById("eidolon:gluttony");
// 删除某个方块的全部研究触发；这里移除黑曜石相关触发。
Research.removeBlockTriggers("minecraft:obsidian");
// 删除某个实体的全部研究触发；这里移除猪相关触发。
Research.removeEntityTriggers("minecraft:pig");
// 删除某个维度的全部研究触发；这里移除主世界相关触发。
Research.removeDimensionTriggers(0);
// 删除某个流体方块的全部研究触发；这里移除静态水相关触发。
Research.removeFluidTriggers("minecraft:water");
// 删除全部研究；通常只在整包完全重写研究体系时使用。
// Research.removeAll();
// 删除全部研究触发；通常只在整包完全重写笔记生成来源时使用。
// Research.removeAllTriggers();
```

自定义研究替换内置研究时，可以用相同 id 再 `addResearch`。已有触发会自动指向新的研究对象；如果要彻底重写触发来源，先调用对应的 `remove...Triggers` 再重新添加。

同一个触发来源可以绑定多个研究。例如 `minecraft:water` 同时绑定内置水研究和 CT 自定义研究时，玩家每次用笔记工具右键水，会按该来源的可用研究列表轮换生成笔记：第一次给第一个研究，第二次给第二个研究，之后继续循环。轮换进度按玩家保存，并且方块、实体、维度、流体各自独立。
