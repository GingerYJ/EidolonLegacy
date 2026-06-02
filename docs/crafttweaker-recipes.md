# EidolonLegacy CraftTweaker 配方文档

本文说明 EidolonLegacy 当前开放给 CraftTweaker 2 的接口。示例尽量写成“每一行都有注释”的形式，方便直接复制后修改。

脚本通常放在实例的 `scripts` 目录，例如：

```text
.minecraft/scripts/eidolon_legacy.zs
```

如果脚本放在子目录中也可以，例如：

```text
.minecraft/scripts/DLC/EL/my_recipes.zs
```

建议所有自定义 id 都使用自己的命名空间，例如：

```text
pack:test_recipe
ctrecipes:cosmic_meatballs_crucible
```

## 通用规则

- `id` 是配方或规则的唯一标识。建议写成 `"命名空间:名称"`。
- `IItemStack` 写法通常是 `<modid:item>`，例如 `<minecraft:diamond>`。
- 带数量的物品写法是 `<minecraft:paper> * 2`。
- 带 metadata 的物品写法是 `<minecraft:dye:4>`。
- 空槽一般使用 `null`。
- 删除内置内容时，优先使用 `removeById`，不要轻易使用 `removeAll`。
- 新增或删除配方后，HEI 和 Codex 会读取运行时配方列表，通常会同步显示。

## Worktable

Worktable 是特殊工作台配方。它包含一个 3x3 主输入区和 4 个试剂槽。

### 导入

```zenscript
// 导入 EidolonLegacy 的 Worktable CT 接口。
// 没有这一行，就不能调用 Worktable.addRecipe、Worktable.removeById 等方法。
import mods.eidolon.Worktable;
```

### 添加配方

方法：

```zenscript
Worktable.addRecipe(id, output, grid, reagents);
```

参数：

- `id`：字符串，配方 id。
- `output`：`IItemStack`，输出物品。
- `grid`：`IIngredient[]`，3x3 主输入，必须正好 9 个元素。
- `reagents`：`IIngredient[]`，4 个试剂槽，必须正好 4 个元素。

逐行注释示例：

```zenscript
// 添加一条 Worktable 配方。
Worktable.addRecipe(
    // 第 1 个参数：配方 id。
    // pack 是你自己的命名空间，test_worktable 是这条配方的名字。
    "pack:test_worktable",

    // 第 2 个参数：输出物品。
    // 这里表示合成结果是 1 个测试符印。
    <eidolon:test_sigil>,

    // 第 3 个参数：3x3 主输入。
    // 必须正好写 9 个元素，顺序是从左到右、从上到下。
    [
        // 第 1 行第 1 格：左上角。
        <minecraft:stick>,
        // 第 1 行第 2 格：上方中间。
        <minecraft:stick>,
        // 第 1 行第 3 格：右上角。
        <minecraft:stick>,

        // 第 2 行第 1 格：左侧中间。
        <minecraft:stick>,
        // 第 2 行第 2 格：正中心。
        <minecraft:diamond>,
        // 第 2 行第 3 格：右侧中间。
        <minecraft:stick>,

        // 第 3 行第 1 格：左下角。
        <minecraft:stick>,
        // 第 3 行第 2 格：下方中间。
        <minecraft:stick>,
        // 第 3 行第 3 格：右下角。
        <minecraft:stick>
    ],

    // 第 4 个参数：4 个试剂槽。
    // 必须正好写 4 个元素，null 表示该试剂槽为空。
    [
        // 试剂槽 1：需要白镴嵌片。
        <eidolon:pewter_inlay>,
        // 试剂槽 2：不需要物品。
        null,
        // 试剂槽 3：不需要物品。
        null,
        // 试剂槽 4：不需要物品。
        null
    ]
);
```

### 删除配方

```zenscript
// 按配方 id 删除。
// 这里会删除内置的仪式匕首工作台配方。
Worktable.removeById("eidolon:athame");

// 按输出物品删除。
// 所有输出匹配收割者镰刀的 Worktable 配方都会被删除。
Worktable.removeByOutput(<eidolon:reaper_scythe>);

// 删除全部 Worktable 配方。
// 只有在整包完全重写 Worktable 系统时才建议使用。
// Worktable.removeAll();
```

## Crucible

Crucible 是坩埚配方。它由多个步骤组成，每一步可以投入多个物品，并可要求搅拌指定次数。

### 导入

```zenscript
// 导入 EidolonLegacy 的 Crucible CT 接口。
// 没有这一行，就不能调用 Crucible.addRecipe、Crucible.removeById 等方法。
import mods.eidolon.Crucible;
```

### 添加配方

方法：

```zenscript
Crucible.addRecipe(id, output, stirs, steps, stirrer, fluid);
```

参数：

- `id`：字符串，配方 id。
- `output`：`IItemStack`，最终产物。
- `stirs`：`int[]`，每一步需要搅拌的次数。
- `steps`：`IIngredient[][]`，每一步投入物。
- `stirrer`：`IIngredient`，搅拌物，可省略；默认是木棍。
- `fluid`：`ILiquidStack`，起始流体，可省略；默认是 1000 mB 水。

限制：

- `stirs` 的数量必须和 `steps` 的步骤数量一致。
- 每一步最多 6 个投入物。
- 同一步内投入物不区分顺序。
- 步骤之间必须按顺序完成。
- HEI 和 Codex 每页显示 5 步，长配方可用按钮或滚轮翻看。

逐行注释示例：

```zenscript
// 添加一条 Crucible 配方。
Crucible.addRecipe(
    // 第 1 个参数：配方 id。
    // 用于日志定位、删除和替换。
    "pack:test_crucible",

    // 第 2 个参数：坩埚最终产物。
    // 这里表示完成全部步骤后产出 1 个死亡精华。
    <eidolon:death_essence>,

    // 第 3 个参数：每一步需要搅拌的次数。
    // 数组长度必须等于下面 steps 的步骤数。
    [
        // 第 1 步：不需要搅拌，投入后空手右键确认即可。
        0,
        // 第 2 步：需要用搅拌物右键坩埚 2 次。
        2,
        // 第 3 步：需要用搅拌物右键坩埚 1 次。
        1
    ],

    // 第 4 个参数：每一步投入物。
    // 外层数组表示步骤，内层数组表示该步骤要投入的物品。
    [
        // 第 1 步投入物。
        // 这一轮需要腐肉和附魔灰烬。
        [
            // 第 1 步第 1 个投入物：腐肉。
            <minecraft:rotten_flesh>,
            // 第 1 步第 2 个投入物：附魔灰烬。
            <eidolon:enchanted_ash>
        ],

        // 第 2 步投入物。
        // 这一轮需要 3 个物品，投入顺序不重要。
        [
            // 第 2 步第 1 个投入物：灵魂碎片。
            <eidolon:soul_shard>,
            // 第 2 步第 2 个投入物：骨头。
            <minecraft:bone>,
            // 第 2 步第 3 个投入物：红石粉。
            <minecraft:redstone>
        ],

        // 第 3 步投入物。
        // 这一轮只需要木炭。
        [
            // minecraft:coal:1 表示木炭。
            <minecraft:coal:1>
        ]
    ],

    // 第 5 个参数：搅拌物。
    // 需要搅拌的步骤必须手持该物品右键坩埚。
    <minecraft:stick>,

    // 第 6 个参数：起始流体。
    // 这里表示需要 1000 mB 水，也就是一桶水。
    <liquid:water> * 1000
);
```

### 长配方示例

```zenscript
// 这条配方有 6 个步骤，每一步正好 6 个投入物。
// 可用于测试 HEI/Codex 的坩埚步骤翻页显示。
Crucible.addRecipe(
    // 配方 id。
    "pack:long_crucible_test",

    // 输出物品。
    <minecraft:diamond_block>,

    // 每一步的搅拌次数，共 6 项。
    [
        // 第 1 步：0 次。
        0,
        // 第 2 步：1 次。
        1,
        // 第 3 步：2 次。
        2,
        // 第 4 步：3 次。
        3,
        // 第 5 步：0 次。
        0,
        // 第 6 步：1 次。
        1
    ],

    // 6 个步骤的投入物。
    [
        // 第 1 步，6 个投入物。
        [
            <minecraft:apple>,
            <minecraft:bread>,
            <minecraft:carrot>,
            <minecraft:potato>,
            <minecraft:wheat>,
            <minecraft:sugar>
        ],

        // 第 2 步，6 个投入物。
        [
            <minecraft:coal>,
            <minecraft:redstone>,
            <minecraft:dye:4>,
            <minecraft:quartz>,
            <minecraft:glowstone_dust>,
            <minecraft:gunpowder>
        ],

        // 第 3 步，6 个投入物。
        [
            <minecraft:iron_ingot>,
            <minecraft:gold_ingot>,
            <minecraft:diamond>,
            <minecraft:emerald>,
            <minecraft:ender_pearl>,
            <minecraft:blaze_rod>
        ],

        // 第 4 步，6 个投入物。
        [
            <minecraft:bone>,
            <minecraft:string>,
            <minecraft:feather>,
            <minecraft:leather>,
            <minecraft:slime_ball>,
            <minecraft:magma_cream>
        ],

        // 第 5 步，6 个投入物。
        [
            <minecraft:cobblestone>,
            <minecraft:stone>,
            <minecraft:sand>,
            <minecraft:gravel>,
            <minecraft:clay_ball>,
            <minecraft:brick>
        ],

        // 第 6 步，6 个投入物。
        [
            <minecraft:paper>,
            <minecraft:book>,
            <minecraft:compass>,
            <minecraft:clock>,
            <minecraft:name_tag>,
            <minecraft:bowl>
        ]
    ],

    // 搅拌物：木棍。
    <minecraft:stick>,

    // 起始流体：一桶水。
    <liquid:water> * 1000
);
```

### 删除配方

```zenscript
// 按配方 id 删除。
// 这里会删除内置的 ender_calx 坩埚配方。
Crucible.removeById("eidolon:ender_calx");

// 按输出物品删除。
// 所有输出匹配暗影宝石的坩埚配方都会被删除。
Crucible.removeByOutput(<eidolon:shadow_gem>);

// 删除全部坩埚配方。
// 只有在整包完全重写坩埚系统时才建议使用。
// Crucible.removeAll();
```

## Altar Ritual

祭坛仪式会读取祭坛容量、力量、火盆祭品、Stone Hand 物品、Necrotic Focus 物品和玩家生命等条件。

### 导入

```zenscript
// 导入 EidolonLegacy 的 Altar CT 接口。
// 祭坛仪式和祭坛供物都使用这个类。
import mods.eidolon.Altar;
```

### 普通产物仪式

方法：

```zenscript
Altar.addItemResult(id, output, capacity, power, offerings, sacrifice);
```

参数：

- `id`：字符串，仪式 id。
- `output`：`IItemStack`，仪式完成后生成的物品。
- `capacity`：`double`，容量需求。
- `power`：`double`，力量需求。
- `offerings`：`IIngredient[]`，Stone Hand 上依次消耗的物品。
- `sacrifice`：`IIngredient`，火盆祭品，可省略；省略时使用 `offerings` 的第一个物品。

逐行注释示例：

```zenscript
// 添加一条普通产物仪式。
Altar.addItemResult(
    // 第 1 个参数：仪式 id。
    "pack:test_altar_result",

    // 第 2 个参数：仪式产物。
    // 仪式完成后会生成死亡精华。
    <eidolon:death_essence>,

    // 第 3 个参数：容量需求。
    // 祭坛读取到的容量必须至少为 3.0。
    3.0,

    // 第 4 个参数：力量需求。
    // 祭坛读取到的力量必须至少为 3.0。
    3.0,

    // 第 5 个参数：Stone Hand 物品列表。
    // 仪式过程中会按顺序消耗这些物品。
    [
        // 第 1 个 Stone Hand 物品：灵魂碎片。
        <eidolon:soul_shard>,
        // 第 2 个 Stone Hand 物品：骨头。
        <minecraft:bone>
    ],

    // 第 6 个参数：火盆祭品。
    // 玩家需要把腐肉放在火盆上，并用打火石点燃。
    <minecraft:rotten_flesh>
);
```

### 转化仪式

方法：

```zenscript
Altar.addTransform(id, output, focus, capacity, power, offerings, health, sacrifice);
```

参数：

- `id`：字符串，仪式 id。
- `output`：`IItemStack`，转化后的物品。
- `focus`：`IIngredient`，Necrotic Focus 中的被转化物。
- `capacity`：`double`，容量需求。
- `power`：`double`，力量需求。
- `offerings`：`IIngredient[]`，Stone Hand 上依次消耗的物品。
- `health`：`double`，玩家生命消耗，可省略；`20.0` 等于 10 颗心。
- `sacrifice`：`IIngredient`，火盆祭品，可省略。

逐行注释示例：

```zenscript
// 添加一条转化仪式。
Altar.addTransform(
    // 第 1 个参数：仪式 id。
    "pack:test_altar_transform",

    // 第 2 个参数：转化结果。
    // 仪式结束后，焦点物品会变成汲取之剑。
    <eidolon:sapping_sword>,

    // 第 3 个参数：焦点物品。
    // 玩家需要把铁剑放入 Necrotic Focus。
    <minecraft:iron_sword>,

    // 第 4 个参数：容量需求。
    4.0,

    // 第 5 个参数：力量需求。
    4.0,

    // 第 6 个参数：Stone Hand 物品列表。
    [
        // 第 1 个消耗物：暗影宝石。
        <eidolon:shadow_gem>,
        // 第 2 个消耗物：灵魂碎片。
        <eidolon:soul_shard>
    ],

    // 第 7 个参数：玩家生命消耗。
    // 20.0 表示 10 颗心。
    20.0,

    // 第 8 个参数：火盆祭品。
    <minecraft:redstone>
);
```

### 充能仪式

方法：

```zenscript
Altar.addCharge(id, output, focus, capacity, power, offerings, sacrifice);
```

参数：

- `id`：字符串，仪式 id。
- `output`：`IItemStack`，HEI/Codex 显示的充能后物品。
- `focus`：`IIngredient`，Necrotic Focus 中需要充能的物品。
- `capacity`：`double`，容量需求。
- `power`：`double`，力量需求。
- `offerings`：`IIngredient[]`，Stone Hand 上依次消耗的物品。
- `sacrifice`：`IIngredient`，火盆祭品，可省略。

逐行注释示例：

```zenscript
// 添加一条充能仪式。
Altar.addCharge(
    // 第 1 个参数：仪式 id。
    "pack:test_altar_charge",

    // 第 2 个参数：显示产物。
    // HEI 和 Codex 会显示充能后的魂火魔杖。
    <eidolon:soulfire_wand>,

    // 第 3 个参数：待充能物品。
    // 玩家需要把魂火魔杖放入 Necrotic Focus。
    <eidolon:soulfire_wand>,

    // 第 4 个参数：容量需求。
    3.0,

    // 第 5 个参数：力量需求。
    3.0,

    // 第 6 个参数：Stone Hand 物品列表。
    [
        // 第 1 个消耗物：次级灵魂宝石。
        <eidolon:lesser_soul_gem>,
        // 第 2 个消耗物：烈焰粉。
        <minecraft:blaze_powder>
    ],

    // 第 7 个参数：火盆祭品。
    <minecraft:glowstone_dust>
);
```

### 召唤仪式

方法：

```zenscript
Altar.addSummon(id, displayOutput, entityId, focus, capacity, power, offerings, sacrifice);
```

参数：

- `id`：字符串，仪式 id。
- `displayOutput`：`IItemStack`，HEI/Codex 中显示的输出，通常写刷怪蛋。
- `entityId`：字符串，实际召唤的实体 id。
- `focus`：`IIngredient`，Necrotic Focus 中的焦点物品。
- `capacity`：`double`，容量需求。
- `power`：`double`，力量需求。
- `offerings`：`IIngredient[]`，Stone Hand 上依次消耗的物品。
- `sacrifice`：`IIngredient`，火盆祭品，可省略。

逐行注释示例：

```zenscript
// 添加一条召唤仪式。
Altar.addSummon(
    // 第 1 个参数：仪式 id。
    "pack:test_altar_summon",

    // 第 2 个参数：显示产物。
    // 这里只用于 HEI/Codex 展示，不决定实际召唤内容。
    <minecraft:spawn_egg>.withTag({EntityTag: {id: "minecraft:zombie"}}),

    // 第 3 个参数：实际召唤的实体 id。
    "minecraft:zombie",

    // 第 4 个参数：Necrotic Focus 中的焦点物品。
    // 这里要求放入木炭。
    <minecraft:coal:1>,

    // 第 5 个参数：容量需求。
    3.0,

    // 第 6 个参数：力量需求。
    3.0,

    // 第 7 个参数：Stone Hand 物品列表。
    [
        // 第 1 个消耗物：灵魂碎片。
        <eidolon:soul_shard>,
        // 第 2 个消耗物：腐肉。
        <minecraft:rotten_flesh>
    ],

    // 第 8 个参数：火盆祭品。
    <minecraft:bone>
);
```

### 吸收仪式

方法：

```zenscript
Altar.addAbsorption(id, output, focus, capacity, power, offerings, sacrifice);
```

参数：

- `id`：字符串，仪式 id。
- `output`：`IItemStack`，HEI/Codex 中显示的输出。
- `focus`：`IIngredient`，Necrotic Focus 中的吸收载体。
- `capacity`：`double`，容量需求。
- `power`：`double`，力量需求。
- `offerings`：`IIngredient[]`，Stone Hand 上依次消耗的物品。
- `sacrifice`：`IIngredient`，火盆祭品，可省略。

逐行注释示例：

```zenscript
// 添加一条吸收仪式。
Altar.addAbsorption(
    // 第 1 个参数：仪式 id。
    "pack:test_altar_absorption",

    // 第 2 个参数：显示产物。
    // 这里显示召唤法杖。
    <eidolon:summoning_staff>,

    // 第 3 个参数：吸收载体。
    // 玩家需要把召唤法杖放入 Necrotic Focus。
    <eidolon:summoning_staff>,

    // 第 4 个参数：容量需求。
    4.0,

    // 第 5 个参数：力量需求。
    4.0,

    // 第 6 个参数：Stone Hand 物品列表。
    [
        // 第 1 个消耗物：死亡精华。
        <eidolon:death_essence>,
        // 第 2 个消耗物：灵魂碎片。
        <eidolon:soul_shard>
    ],

    // 第 7 个参数：火盆祭品。
    <minecraft:bone>
);
```

### 删除仪式

```zenscript
// 按仪式 id 删除。
Altar.removeById("eidolon:lesser_soul_gem");

// 按显示输出或实际输出删除。
Altar.removeByOutput(<eidolon:death_essence>);

// 删除全部祭坛仪式。
// 只有在整包完全重写仪式系统时才建议使用。
// Altar.removeAll();
```

## Altar Offering

Altar Offering 是祭坛供物规则，用来给祭坛提供容量和力量。

### 导入

```zenscript
// 导入 EidolonLegacy 的 Altar CT 接口。
// 供物规则也写在 Altar 类里。
import mods.eidolon.Altar;
```

### 添加默认供物

方法：

```zenscript
Altar.addOffering(id, item, capacity, power);
```

参数：

- `id`：字符串，供物规则 id，同时也是数值分组 key。
- `item`：`IItemStack`，供物物品。
- `capacity`：`double`，提供的容量。
- `power`：`double`，提供的力量。

逐行注释示例：

```zenscript
// 添加一条默认供物规则。
Altar.addOffering(
    // 第 1 个参数：供物规则 id。
    // 相同 id/key 的供物不会重复叠加。
    "pack:test_offering",

    // 第 2 个参数：供物物品。
    // 这里表示钻石。
    <minecraft:diamond>,

    // 第 3 个参数：提供的容量。
    2.0,

    // 第 4 个参数：提供的力量。
    1.0
);
```

### 仅方块供物

```zenscript
// 只在物品作为方块放置时生效。
Altar.addBlockOffering(
    // 供物规则 id。
    "pack:test_block_offering",

    // 供物物品。
    // 这里是红石火把。
    <minecraft:redstone_torch>,

    // 提供容量。
    0.0,

    // 提供力量。
    2.0
);
```

### 仅祭品盘供物

```zenscript
// 只在物品放进祭品盘时生效。
Altar.addPlateOffering(
    // 供物规则 id。
    "pack:test_plate_offering",

    // 供物物品。
    // 这里是绿宝石。
    <minecraft:emerald>,

    // 提供容量。
    1.0,

    // 提供力量。
    2.0
);
```

### 手动指定供物模式

方法：

```zenscript
Altar.addOfferingModes(id, item, capacity, power, blockOffering, plateOffering);
```

参数：

- `blockOffering`：布尔值，是否作为方块供物生效。
- `plateOffering`：布尔值，是否作为祭品盘供物生效。

逐行注释示例：

```zenscript
// 手动指定供物可以在哪些模式下生效。
Altar.addOfferingModes(
    // 第 1 个参数：供物规则 id。
    "pack:test_skull_offering",

    // 第 2 个参数：供物物品。
    <minecraft:skull:0>,

    // 第 3 个参数：提供容量。
    2.0,

    // 第 4 个参数：提供力量。
    0.0,

    // 第 5 个参数：是否作为方块供物生效。
    true,

    // 第 6 个参数：是否作为祭品盘供物生效。
    true
);
```

规则说明：

- 相同 `id` 也就是相同 key 的供物不会重复叠加，只取最高容量和力量。
- 不同 key 的供物可以相加。
- ItemBlock 可以放进祭品盘，但如果没有 plateOffering 规则，不会提供数值。

删除供物：

```zenscript
// 按供物规则 id 删除。
Altar.removeOfferingById("eidolon:light");

// 按物品删除所有相关供物规则。
Altar.removeOfferingByItem(<minecraft:diamond>);

// 删除全部供物规则。
// Altar.removeAllOfferings();
```

## Athame Harvest

Athame Harvest 是仪式匕首采集规则。

### 导入

```zenscript
// 导入 EidolonLegacy 的 Athame CT 接口。
import mods.eidolon.Athame;
```

### 添加采集规则

方法：

```zenscript
Athame.addRecipe(id, source, output, sourceLabel);
```

参数：

- `id`：字符串，采集规则 id。
- `source`：`IIngredient`，可被仪式匕首采集的来源。
- `output`：`IItemStack`，成功采集后的产物。
- `sourceLabel`：字符串，Codex 中显示的来源名称，可省略。

逐行注释示例：

```zenscript
// 添加一条仪式匕首采集规则。
Athame.addRecipe(
    // 第 1 个参数：采集规则 id。
    "pack:test_athame_harvest",

    // 第 2 个参数：采集来源。
    // 这里表示红花 metadata 0。
    <minecraft:red_flower:0>,

    // 第 3 个参数：采集产物。
    // 成功时掉落灵魂碎片。
    <eidolon:soul_shard>,

    // 第 4 个参数：来源显示名。
    // 会显示在 Codex 的来源说明中。
    "玫瑰"
);
```

删除：

```zenscript
// 按采集规则 id 删除。
Athame.removeById("eidolon:avennian_sprig_from_fern");

// 按产物删除。
Athame.removeByOutput(<eidolon:oanna_bloom>);

// 按来源删除。
Athame.removeBySource(<minecraft:waterlily>);

// 删除全部仪式匕首采集规则。
// Athame.removeAll();
```

## Research

Research 是研究定义和研究笔记触发来源。

### 导入

```zenscript
// 导入 EidolonLegacy 的 Research CT 接口。
import mods.eidolon.Research;
```

### 添加研究

方法：

```zenscript
Research.addResearch(id, name, stars, prerequisites, stepItems, source, description);
```

参数：

- `id`：字符串，研究 id。
- `name`：字符串，研究显示名称，可直接写中文。
- `stars`：整数，研究星级，必须为 1-5。
- `prerequisites`：`String[]`，前置研究 id 列表。
- `stepItems`：`IItemStack[][]`，每一级研究可提交的物品。
- `source`：字符串，Codex 来源文字，可省略。
- `description`：字符串，Codex 描述文字，可省略。

逐行注释示例：

```zenscript
// 添加或替换一条研究。
Research.addResearch(
    // 第 1 个参数：研究 id。
    // 研究笔记、完成研究、前置判断都使用这个 id。
    "pack:test_research",

    // 第 2 个参数：研究显示名称。
    // 会显示在 Codex、研究笔记和完成研究 tooltip 中。
    "测试研究",

    // 第 3 个参数：研究星级。
    // 必须为 1 到 5，这里表示两级研究。
    2,

    // 第 4 个参数：前置研究列表。
    // 玩家必须先掌握这些研究，该研究才算解锁。
    [
        // 前置研究 1：秘典入门。
        "eidolon:codex"
    ],

    // 第 5 个参数：每一级可提交物品。
    // 外层数组数量必须等于星级。
    [
        // 第 1 级研究任务组。
        [
            // 可选提交项 1：提交 2 张纸。
            <minecraft:paper> * 2,
            // 可选提交项 2：提交魔法墨水。
            <eidolon:magic_ink>
        ],

        // 第 2 级研究任务组。
        [
            // 可选提交项 1：提交羊皮纸。
            <eidolon:parchment>,
            // 可选提交项 2：提交金锭。
            <minecraft:gold_ingot>
        ]
    ],

    // 第 6 个参数：来源文字。
    // 会显示在 Codex 的来源区域。
    "用笔记工具右键测试方块。",

    // 第 7 个参数：描述文字。
    // 会显示在 Codex 的研究详情页。
    "这是由 CraftTweaker 定义的研究。"
);
```

### 使用 lang 键添加研究

方法：

```zenscript
Research.addResearchLang(id, stars, prerequisites, stepItems, sourceKey);
```

说明：

- 显示名会走 lang key。
- 适合整合包自行维护 lang 文件时使用。

逐行注释示例：

```zenscript
// 添加一条使用 lang 键的研究。
Research.addResearchLang(
    // 第 1 个参数：研究 id。
    "pack:lang_research",

    // 第 2 个参数：研究星级。
    1,

    // 第 3 个参数：前置研究列表。
    // 空数组表示没有前置。
    [],

    // 第 4 个参数：每一级可提交物品。
    [
        // 第 1 级研究任务组。
        [
            // 提交一本书即可完成。
            <minecraft:book>
        ]
    ],

    // 第 5 个参数：来源文本 lang key。
    "research_source.pack.lang_research"
);
```

### 添加研究触发

```zenscript
// 方块触发。
// 玩家用笔记工具右键黑曜石时，生成 pack:test_research 研究笔记。
Research.addBlockTrigger("minecraft:obsidian", "pack:test_research");

// 实体触发。
// 玩家用笔记工具右键猪时，生成 pack:test_research 研究笔记。
Research.addEntityTrigger("minecraft:pig", "pack:test_research");

// 维度触发。
// 玩家在主世界右键空气时，生成 pack:test_research 研究笔记。
Research.addDimensionTrigger(0, "pack:test_research");

// 流体触发。
// 玩家用笔记工具右键静态水时，生成 pack:test_research 研究笔记。
Research.addFluidTrigger("minecraft:water", "pack:test_research");

// 流体触发。
// 玩家用笔记工具右键流动水时，生成 pack:test_research 研究笔记。
Research.addFluidTrigger("minecraft:flowing_water", "pack:test_research");
```

触发规则：

- 同一个来源可以绑定多个研究。
- 如果同一个来源绑定多个研究，玩家每次触发会轮流生成不同研究笔记。
- 轮换进度按玩家保存。
- 方块、实体、维度、流体的轮换互相独立。
- 当前实现不检查玩家是否已经掌握该研究；触发成功就会生成笔记。

### 删除研究和触发

```zenscript
// 按研究 id 删除。
Research.removeById("eidolon:gluttony");

// 删除某个方块的全部研究触发。
Research.removeBlockTriggers("minecraft:obsidian");

// 删除某个实体的全部研究触发。
Research.removeEntityTriggers("minecraft:pig");

// 删除某个维度的全部研究触发。
Research.removeDimensionTriggers(0);

// 删除某个流体方块的全部研究触发。
Research.removeFluidTriggers("minecraft:water");

// 删除全部研究。
// Research.removeAll();

// 删除全部研究触发。
// Research.removeAllTriggers();
```

## 调试建议

- 脚本加载失败时，先查看 `crafttweaker.log`。
- 坩埚配方报错时，优先检查 `stirs` 数量是否等于 `steps` 数量。
- 坩埚每一步投入物不能超过 6 个。
- HEI/Codex 不显示新增配方时，确认脚本是否成功加载。
- 替换内置配方时，先 `removeById` 再添加新配方。
- 不建议在正式整合包中保留临时测试 `.zs` 文件。
