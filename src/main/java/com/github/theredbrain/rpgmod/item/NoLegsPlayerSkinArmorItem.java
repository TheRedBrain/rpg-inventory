package com.github.theredbrain.rpgmod.item;

import com.github.theredbrain.rpgmod.RPGMod;
import com.github.theredbrain.rpgmod.client.render.renderer.NoLegsPlayerSkinArmorRenderer;
import com.github.theredbrain.rpgmod.client.render.renderer.PlayerSkinArmorRenderer;
import com.github.theredbrain.rpgmod.entity.ExtendedEquipmentSlot;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class NoLegsPlayerSkinArmorItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public NoLegsPlayerSkinArmorItem(Settings settings) {
        super(CustomArmorMaterials.PLAYER_SKIN, ExtendedEquipmentSlot.PLAYER_SKIN_ARMOR, settings);
    }

//    @Override
//    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//        ItemStack itemStack = user.getStackInHand(hand);
//        EquipmentSlot equipmentSlot = ExtendedEquipmentSlot.PLAYER_SKIN_ARMOR;
//        ItemStack itemStack2 = user.getEquippedStack(equipmentSlot);
////        if (itemStack2.isEmpty()) {
//            user.equipStack(equipmentSlot, itemStack.copy());
//            if (!world.isClient()) {
//                user.incrementStat(Stats.USED.getOrCreateStat(this));
//            }
//            itemStack.setCount(0);
//            return TypedActionResult.success(itemStack, world.isClient());
////        }
////        return TypedActionResult.fail(itemStack);
//    }

    // Create our armor model/renderer for Fabric and return it
    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<LivingEntity> original) {

                Identifier textureIdentifier = null;
                if (livingEntity instanceof PlayerEntity) {
                    textureIdentifier = getPlayerTexture((PlayerEntity)livingEntity);
                }
                if (textureIdentifier == null) {
                    textureIdentifier = new Identifier(RPGMod.MOD_ID, "textures/item/armor/player_skin_model.png");
                }
//                else {
//                    RPGMod.LOGGER.info("textureIdentifier: " + textureIdentifier);
//                }

                if(this.renderer == null) {
                    this.renderer = new NoLegsPlayerSkinArmorRenderer(new Identifier(RPGMod.MOD_ID, "armor/player_skin_model"), textureIdentifier);
                }

//                World world = livingEntity.getWorld();
//                if (world.isClient()) {
//                    if (livingEntity instanceof PlayerEntity) {
//                        if (world.isClient()) {
//                            ((ClientWorld) world).
//                        }
//                        ((ClientPlayerEntity) livingEntity).getUuid();
//                        ((ClientPlayerEntity) livingEntity).networkHandler.
//                    }
//                }
                // This prepares our GeoArmorRenderer for the current render frame.
                // These parameters may be null however, so we don't do anything further with them

                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

                return this.renderer;
            }


        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }

    // Let's add our animation controller
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
//        controllers.add(new AnimationController<>(this, 20, state -> {
//            // Apply our generic idle animation.
//            // Whether it plays or not is decided down below.
//            state.getController().setAnimation(DefaultAnimations.IDLE);
////
////            // Let's gather some data from the state to use below
////            // This is the entity that is currently wearing/holding the item
////            Entity entity = state.getData(DataTickets.ENTITY);
////
////            // We'll just have ArmorStands always animate, so we can return here
////            if (entity instanceof ArmorStandEntity)
////                return PlayState.CONTINUE;
////
////            // For this example, we only want the animation to play if the entity is wearing all pieces of the armor
////            // Let's collect the armor pieces the entity is currently wearing
////            Set<Item> wornArmor = new ObjectOpenHashSet<>();
////            boolean isFullSet = true;
////            for (ItemStack stack : entity.getArmorItems()) {
////                // We can stop immediately if any of the slots are empty
////                if (stack.isEmpty())
////                    return PlayState.STOP;
////
////                isFullSet = stack.isIn(armorSet);
////                wornArmor.add(stack.getItem());
////            }
//
//            // Check each of the pieces match our set
//            // boolean isFullSet = wornArmor.containsAll(ObjectArrayList.of(
////                    ItemRegistry.CUSTOM_IRON_BOOTS,
////                    ItemRegistry.CUSTOM_IRON_LEGGINGS,
////                    ItemRegistry.CUSTOM_IRON_CHESTPLATE,
////                    ItemRegistry.CUSTOM_IRON_HELMET,
////                    ItemRegistry.CUSTOM_IRON_GLOVES,
////                    ItemRegistry.CUSTOM_IRON_SHOULDERS));
//
//            // Play the animation if the full set is being worn, otherwise stop
//            return PlayState.STOP;
//        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Nullable
    private Identifier getPlayerTexture(PlayerEntity entity) {
        PlayerSkinProvider skinProvider = MinecraftClient.getInstance().getSkinProvider();
        if (entity.getUuid() != null) {
            ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
            if (networkHandler != null) {
                PlayerListEntry entry = networkHandler.getPlayerListEntry(entity.getUuid());
                return entry != null ? entry.getSkinTexture() : null;
            } else {
                return skinProvider.loadSkin(new GameProfile(entity.getUuid(), null));
            }
        }
        return null;
    }
}
