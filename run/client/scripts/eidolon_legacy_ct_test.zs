// EidolonLegacy CraftTweaker 测试脚本。
// 放置位置：run/client/scripts/eidolon_legacy_ct_test.zs
// 用途：只新增测试配方/规则，不删除内置内容，方便确认 CT 接口是否生效。
// 关闭方式：把本文件移出 scripts 目录，或改名为 .zs.disabled 后重启游戏。

import mods.eidolon.Worktable;
import mods.eidolon.Crucible;
import mods.eidolon.Altar;
import mods.eidolon.Athame;
import mods.eidolon.Research;

// ==============================
// 1. Worktable 测试
// ==============================
// 验证方式：
// 1. 在 HEI 中查看 <eidolon:test_sigil> 是否出现 Worktable 配方。
// 2. 在 Worktable 中按 3x3 摆放木棍，中间放钻石，试剂槽 1 放白镴镶嵌。
// 3. 输出应为 1 个 <eidolon:test_sigil>。
Worktable.addRecipe(
    // 配方 id。
    "cttest:worktable_test_sigil",
    // 配方输出。
    <eidolon:test_sigil>,
    // 3x3 主输入，从左到右、从上到下排列。
    [
        // 第 1 行。
        <minecraft:stick>, <minecraft:stick>, <minecraft:stick>,
        // 第 2 行。
        <minecraft:stick>, <minecraft:diamond>, <minecraft:stick>,
        // 第 3 行。
        <minecraft:stick>, <minecraft:stick>, <minecraft:stick>
    ],
    // 4 个试剂槽。
    [
        // 试剂槽 1。
        <eidolon:pewter_inlay>,
        // 试剂槽 2。
        null,
        // 试剂槽 3。
        null,
        // 试剂槽 4。
        null
    ]
);

// ==============================
// 2. Crucible 测试
// ==============================
// 验证方式：
// 1. 在 HEI 中查看 <eidolon:test_sigil> 是否出现 Crucible 配方。
// 2. 先用水桶给坩埚倒入水。
// 3. 第 1 步投入腐肉和附魔灰烬，空手确认。
// 4. 第 2 步投入灵魂碎片，用木棍搅拌 1 次，再空手确认。
// 5. 第 3 步投入木炭，空手确认。
// 6. 输出应为 2 个 <eidolon:test_sigil>。
Crucible.addRecipe(
    // 配方 id。
    "cttest:crucible_test_sigil",
    // 配方输出。
    <eidolon:test_sigil> * 2,
    // 每一步需要搅拌的次数。
    [
        // 第 1 步不需要搅拌。
        0,
        // 第 2 步需要搅拌 1 次。
        1,
        // 第 3 步不需要搅拌。
        0
    ],
    // 每一步需要投入的物品。
    [
        // 第 1 步投入物。
        [
            <minecraft:rotten_flesh>,
            <eidolon:enchanted_ash>
        ],
        // 第 2 步投入物。
        [
            <eidolon:soul_shard>
        ],
        // 第 3 步投入物。
        [
            <minecraft:coal:1>
        ]
    ],
    // 搅拌物。
    <minecraft:stick>,
    // 起始流体。
    <liquid:water> * 1000
);

// ==============================
// 3. Altar Offering 测试
// ==============================
// 验证方式：
// 1. 查看钻石、红石火把、骷髅头 tooltip 或 Codex 供物栏目是否出现对应数值。
// 2. 钻石放入祭品盘后应提供容量 6、力量 6。
// 3. 红石火把放在祭坛上方时应提供容量 0、力量 3。
// 4. 骷髅头放祭坛上方或放入祭品盘都应提供容量 2、力量 2。
Altar.addPlateOffering(
    // 供物规则 id，也作为数值分组 key。
    "cttest:plate_diamond",
    // 供物物品。
    <minecraft:diamond>,
    // 容量。
    6.0,
    // 力量。
    6.0
);

Altar.addBlockOffering(
    // 供物规则 id，也作为数值分组 key。
    "cttest:block_redstone_torch",
    // 供物物品。
    <minecraft:redstone_torch>,
    // 容量。
    0.0,
    // 力量。
    3.0
);

Altar.addOfferingModes(
    // 供物规则 id，也作为数值分组 key。
    "cttest:skull_both_modes",
    // 供物物品。
    <minecraft:skull:0>,
    // 容量。
    2.0,
    // 力量。
    2.0,
    // 是否作为祭坛上方方块供物生效。
    true,
    // 是否作为祭品盘物品供物生效。
    true
);

// ==============================
// 4. Altar Ritual 普通产物测试
// ==============================
// 验证方式：
// 1. 在 HEI 或 Codex 中查看 <eidolon:test_sigil> 的祭坛仪式配方。
// 2. 火盆上放骨头并点燃。
// 3. 不需要 Stone Hand 物品，不需要 Necrotic Focus 物品。
// 4. 仪式完成后中间应生成 3 个 <eidolon:test_sigil>。
Altar.addItemResult(
    // 仪式 id。
    "cttest:altar_item_result_test_sigil",
    // 仪式输出。
    <eidolon:test_sigil> * 3,
    // 容量需求。
    0.0,
    // 力量需求。
    0.0,
    // 仪式物品列表；未单独指定 sacrifice 时，第 1 个物品作为火盆祭品。
    [
        <minecraft:bone>
    ]
);

// ==============================
// 5. Altar Ritual 转化测试
// ==============================
// 验证方式：
// 1. 在 HEI 或 Codex 中查看 <eidolon:test_sigil> 的转化仪式配方。
// 2. Necrotic Focus 中放铁锭。
// 3. 火盆上放红石并点燃。
// 4. 仪式完成后 Necrotic Focus 中的铁锭应转化为 <eidolon:test_sigil>。
Altar.addTransform(
    // 仪式 id。
    "cttest:altar_transform_test_sigil",
    // 转化结果。
    <eidolon:test_sigil>,
    // Necrotic Focus 中的被转化物。
    <minecraft:iron_ingot>,
    // 容量需求。
    0.0,
    // 力量需求。
    0.0,
    // 仪式物品列表；未单独指定 sacrifice 时，第 1 个物品作为火盆祭品。
    [
        <minecraft:redstone>
    ],
    // 生命消耗。
    0.0
);

// ==============================
// 6. Altar Ritual 充能测试
// ==============================
// 验证方式：
// 1. 准备一个未满充能的魂火魔杖。
// 2. Necrotic Focus 中放魂火魔杖。
// 3. 火盆上放萤石粉并点燃。
// 4. 仪式完成后魂火魔杖应恢复满充能。
Altar.addCharge(
    // 仪式 id。
    "cttest:altar_charge_soulfire_wand",
    // HEI / Codex 显示输出。
    <eidolon:soulfire_wand>,
    // Necrotic Focus 中的待充能物。
    <eidolon:soulfire_wand>,
    // 容量需求。
    0.0,
    // 力量需求。
    0.0,
    // 仪式物品列表；未单独指定 sacrifice 时，第 1 个物品作为火盆祭品。
    [
        <minecraft:glowstone_dust>
    ]
);

// ==============================
// 7. Altar Ritual 召唤测试
// ==============================
// 验证方式：
// 1. Necrotic Focus 中放木炭。
// 2. 火盆上放腐肉并点燃。
// 3. 仪式完成后应召唤 1 个僵尸。
Altar.addSummon(
    // 仪式 id。
    "cttest:altar_summon_zombie",
    // HEI / Codex 显示输出。
    <minecraft:spawn_egg>.withTag({EntityTag: {id: "minecraft:zombie"}}),
    // 实际召唤的实体 id。
    "minecraft:zombie",
    // Necrotic Focus 中的焦点物品。
    <minecraft:coal:1>,
    // 容量需求。
    0.0,
    // 力量需求。
    0.0,
    // 仪式物品列表；未单独指定 sacrifice 时，第 1 个物品作为火盆祭品。
    [
        <minecraft:rotten_flesh>
    ]
);

// ==============================
// 8. Altar Ritual 吸收测试
// ==============================
// 验证方式：
// 1. Necrotic Focus 中放召唤法杖。
// 2. 火盆上放死亡精华并点燃。
// 3. 祭坛附近放一个低血量亡灵生物，例如低血量僵尸。
// 4. 仪式完成后召唤法杖应吸收该亡灵类型。
Altar.addAbsorption(
    // 仪式 id。
    "cttest:altar_absorption_summoning_staff",
    // HEI / Codex 显示输出。
    <eidolon:summoning_staff>,
    // Necrotic Focus 中的焦点物品。
    <eidolon:summoning_staff>,
    // 容量需求。
    0.0,
    // 力量需求。
    0.0,
    // 仪式物品列表；未单独指定 sacrifice 时，第 1 个物品作为火盆祭品。
    [
        <eidolon:death_essence>
    ]
);

// ==============================
// 9. Athame Harvest 测试
// ==============================
// 验证方式：
// 1. 在 HEI 或 Codex 中查看仪式匕首采集分类是否出现蒲公英 -> 测试符印。
// 2. 用仪式匕首右键蒲公英。
// 3. 破坏触发后有 1/3 概率掉落 <eidolon:test_sigil>。
Athame.addRecipe(
    // 采集规则 id。
    "cttest:athame_dandelion_test_sigil",
    // 可采集来源。
    <minecraft:yellow_flower>,
    // 采集产物。
    <eidolon:test_sigil>,
    // Codex 中显示的来源文字。
    "蒲公英"
);

// ==============================
// 10. Research 测试
// ==============================
// 验证方式：
// 1. 用笔记工具右键黑曜石或鸡，应该生成“CT测试研究”的研究笔记。
// 2. 把研究笔记放入研究桌。
// 3. 第 1 级会显示两个可选任务：纸、魔法墨水；提交任意一个即可推进。
// 4. 第 2 级会显示两个可选任务：羊皮纸、金锭；提交任意一个即可推进。
// 5. 完成后盖章，得到完成研究，右键学习。
Research.addResearch(
    // 研究 id。
    "cttest:test_research",
    // 研究显示名。
    "CT测试研究",
    // 研究星级。
    2,
    // 前置研究列表；空数组表示没有前置。
    [],
    // 每一级可选择提交的物品；内层每个物品都会生成一条独立可选任务。
    [
        // 第 1 级可选提交物。
        [
            <minecraft:paper>,
            <eidolon:magic_ink>
        ],
        // 第 2 级可选提交物。
        [
            <eidolon:parchment>,
            <minecraft:gold_ingot>
        ]
    ],
    // Codex 来源文字。
    "用笔记工具右键黑曜石或鸡。",
    // Codex 描述文字。
    "这是用于确认 CraftTweaker 研究系统是否正常工作的测试研究。"
);

// 方块触发：右键黑曜石生成 CT 测试研究笔记。
Research.addBlockTrigger("minecraft:obsidian", "cttest:test_research");
// 实体触发：右键鸡生成 CT 测试研究笔记。
Research.addEntityTrigger("minecraft:chicken", "cttest:test_research");
// 维度触发：主世界右键空气也追加 CT 测试研究；如果内置主世界/秘典研究未完成，可能会先生成内置研究。
Research.addDimensionTrigger(0, "cttest:test_research");
// 流体触发：右键水也追加 CT 测试研究；如果内置水研究未完成，可能会先生成内置水研究。
Research.addFluidTrigger("minecraft:water", "cttest:test_research");
Research.addFluidTrigger("minecraft:flowing_water", "cttest:test_research");
