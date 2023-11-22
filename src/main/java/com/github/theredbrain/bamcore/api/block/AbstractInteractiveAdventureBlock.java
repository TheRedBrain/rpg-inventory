package com.github.theredbrain.bamcore.api.block;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.util.BetterAdventureModCoreItemUtils;
//import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
//import de.dafuqs.revelationary.api.revelations.RevelationAware;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public abstract class AbstractInteractiveAdventureBlock extends Block/* implements RevelationAware*/ {
    public static final BooleanProperty INTACT;
    private final TagKey<Item> requiredTools;
    private final boolean requiresTools;
    private final int respawnModifier;
    private final Block cloakedBlock;
    private final Identifier cloakAdvancementIdentifier;

    public AbstractInteractiveAdventureBlock(@Nullable Block cloakedBlock, @Nullable Identifier cloakAdvancementIdentifier, @Nullable TagKey<Item> requiredTools, boolean requiresTools, int respawnModifier, Settings settings) {
        super(settings);
        this.requiredTools = requiredTools;
        if (this.requiredTools == null && requiresTools) {
            this.requiresTools = false;
        } else {
            this.requiresTools = requiresTools;
        }
        this.respawnModifier = Math.max(respawnModifier, 1);
        this.setDefaultState(this.getStateManager().getDefaultState().with(INTACT, true));
        this.cloakedBlock = cloakedBlock;
        this.cloakAdvancementIdentifier = cloakAdvancementIdentifier;
//        if (this.cloakAdvancementIdentifier != null) {
//            RevelationAware.register(this);
//        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{INTACT});
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack tool = player.getStackInHand(Hand.MAIN_HAND);
        if (!world.isClient() && (this.cloakAdvancementIdentifier == null/* || AdvancementHelper.hasAdvancement(player, this.getCloakAdvancementIdentifier())*/)) {
            if (state.isOf(this)
                    && (!this.requiresTools
                        || (tool.isIn(this.requiredTools) && tool.getItem() instanceof ToolItem && (BetterAdventureModCoreItemUtils.isUsable(tool) && hand == Hand.MAIN_HAND) || (tool.isIn(this.requiredTools) && !(tool.getItem() instanceof ToolItem) && hand == Hand.MAIN_HAND)))) {

                if (state.get(AbstractInteractiveAdventureBlock.INTACT)) {
                    BetterAdventureModeCore.LOGGER.info("should drop stuff");
                    // TODO drop lootTable on interaction
//                    this.getLootTableId();
                    List<ItemStack> droppedStacks = Block.getDroppedStacks(state, (ServerWorld) world, pos, null, player, tool);

                    for (ItemStack itemStack : droppedStacks) {
                        player.getInventory().offerOrDrop(itemStack);//Block.dropStacks(state, world, pos, null, player, itemStack);
                    }
//                this.dropStacks(state, (ServerWorld) world, hit.getBlockPos(), player, player.getStackInHand(hand));
//                List<ItemStack> droppedStacks = LootTable.EMPTY.generateLoot(new LootContext());//builder().build().generateLoot(LootContext);//this.getLootTableId().
//                    if (this.drop != null) {
//                        if (!player.getInventory().insertStack(new ItemStack(drop))) {
//                            player.dropItem(new ItemStack(drop), false);
//                        }
////                    }
//                    }
                    // TODO sounds
                    world.setBlockState(pos, state.with(INTACT, false));
                    if (/*player != null && */tool.getItem() instanceof ToolItem ) {
                        tool.damage(1, player, p -> p.sendToolBreakStatus(Hand.MAIN_HAND));
                    }
                    return ActionResult.SUCCESS;
                } else {
                    // TODO send message to player that the resource is empty
                    player.sendMessage(Text.translatable("interactive_adventure_block.ressourceNodeDepleted"), true);
//                    RPGMod.LOGGER.info("Resource node depleted");
                }
            } else {
                // TODO send message to player that they need a better tool
                player.sendMessage(Text.translatable("interactive_adventure_block.betterToolRequired"), true);
//                RPGMod.LOGGER.info("Better tool required");
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        player.incrementStat(Stats.MINED.getOrCreateStat(this));
        dropStack(world, pos, this.getPickStack(world, pos, state));
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.isOf(this) && !state.get(INTACT) && random.nextInt(this.respawnModifier) == 0) {

            // TODO push entities out of block bounds
            world.setBlockState(pos, state.with(INTACT, true));
        }
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.isOf(this) && !state.get(INTACT);
    }

    static {
        INTACT = BooleanProperty.of("intact");
    }

//    // Revelationary stuff
//
//    public Identifier getCloakAdvancementIdentifier() {
//        return this.cloakAdvancementIdentifier;
//    }
//
//    public Map<BlockState, BlockState> getBlockStateCloaks() {
//        Hashtable<BlockState, BlockState> hashtable = new Hashtable();
//        if (this.cloakedBlock != null) {
//            hashtable.put(this.getDefaultState(), this.cloakedBlock.getDefaultState());
//            hashtable.put(this.getDefaultState().with(INTACT, false), this.cloakedBlock.getDefaultState());
//        } else {
//            hashtable.put(this.getDefaultState(), this.getDefaultState());
//            hashtable.put(this.getDefaultState().with(INTACT, false), this.getDefaultState().with(INTACT, false));
//        }
//        return hashtable;
//    }

//    public Pair<Item, Item> getItemCloak() {
//        if (this.cloakedBlock != null) {
//            return new Pair<>(this.asItem(), this.cloakedBlock.asItem());
//        } else {
//            return new Pair<>(this.asItem(), this.asItem());
//        }
//    }

//    public boolean isVisibleTo(@Nullable PlayerEntity player) {
//        boolean bl = true;
//        if (this.cloakAdvancementIdentifier != null) {
//            bl = AdvancementHelper.hasAdvancement(player, this.getCloakAdvancementIdentifier());
//        }
//        return bl;
//    }
}
