package com.github.theredbrain.betteradventuremode.azurelib;

import com.github.theredbrain.betteradventuremode.client.render.renderer.ModeledTrinketRenderer;
//import mod.azure.azurelib.common.api.client.renderer.layer.GeoRenderLayer;
//import mod.azure.azurelib.common.internal.client.renderer.GeoRenderer;
//import mod.azure.azurelib.common.internal.common.cache.object.BakedGeoModel;
//import mod.azure.azurelib.common.internal.common.event.GeoRenderEvent;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.event.GeoRenderEvent;
import mod.azure.azurelib.renderer.GeoRenderer;
import mod.azure.azurelib.renderer.layer.GeoRenderLayer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class Trinket implements GeoRenderEvent {
    private final ModeledTrinketRenderer<?> renderer;

    public Trinket(ModeledTrinketRenderer<?> renderer) {
        this.renderer = renderer;
    }

    /**
     * Returns the renderer for this event
     */
    @Override
    public ModeledTrinketRenderer<?> getRenderer() {
        return this.renderer;
    }

    /**
     * Shortcut method for retrieving the entity being rendered
     */
    @Nullable
    public net.minecraft.entity.Entity getEntity() {
        return getRenderer().getCurrentEntity();
    }

    /**
     * Shortcut method for retrieving the ItemStack relevant to the trinket being rendered
     */
    @Nullable
    public ItemStack getItemStack() {
        return getRenderer().getCurrentStack();
    }

    /**
     * Shortcut method for retrieving the slot group of the trinket being rendered
     */
    @Nullable
    public String getSlotGroup() {
        return getRenderer().getCurrentSlotGroup();
    }

    /**
     * Shortcut method for retrieving the slot name of the trinket being rendered
     */
    @Nullable
    public String getSlotName() {
        return getRenderer().getCurrentSlotName();
    }

    /**
     * Pre-render event for trinkets being rendered by {@link ModeledTrinketRenderer}.<br>
     * This event is called before rendering, but after {@link GeoRenderer#preRender}<br>
     * <br>
     * This event is <u>cancellable</u>.<br>
     * If the event is cancelled by returning false in the {@link Pre.Listener}, the Trinket will not be rendered and the corresponding {@link Post} event will not be fired.
     */
    public static class Pre extends Trinket {
        public static final Event<Pre.Listener> EVENT = EventFactory.createArrayBacked(Pre.Listener.class, event -> true, listeners -> event -> {
            for (Pre.Listener listener : listeners) {
                if (!listener.handle(event))
                    return false;
            }

            return true;
        });

        private final MatrixStack poseStack;
        private final BakedGeoModel model;
        private final VertexConsumerProvider bufferSource;
        private final float partialTick;
        private final int packedLight;

        public Pre(ModeledTrinketRenderer<?> renderer, MatrixStack poseStack, BakedGeoModel model, VertexConsumerProvider bufferSource, float partialTick, int packedLight) {
            super(renderer);

            this.poseStack = poseStack;
            this.model = model;
            this.bufferSource = bufferSource;
            this.partialTick = partialTick;
            this.packedLight = packedLight;
        }

        public MatrixStack getPoseStack() {
            return this.poseStack;
        }

        public BakedGeoModel getModel() {
            return this.model;
        }

        public VertexConsumerProvider getBufferSource() {
            return this.bufferSource;
        }

        public float getPartialTick() {
            return this.partialTick;
        }

        public int getPackedLight() {
            return this.packedLight;
        }

        /**
         * Event listener interface for the Trinket.Pre GeoRenderEvent.<br>
         * Return false to cancel the render pass
         */
        @FunctionalInterface
        public interface Listener {
            boolean handle(Pre event);
        }
    }

    /**
     * Post-render event for trinkets being rendered by {@link ModeledTrinketRenderer}.<br>
     * This event is called after {@link GeoRenderer#postRender}
     */
    public static class Post extends Trinket {
        public static final Event<Post.Listener> EVENT = EventFactory.createArrayBacked(Post.Listener.class, post -> {
        }, listeners -> event -> {
            for (Post.Listener listener : listeners) {
                listener.handle(event);
            }
        });

        private final MatrixStack poseStack;
        private final BakedGeoModel model;
        private final VertexConsumerProvider bufferSource;
        private final float partialTick;
        private final int packedLight;

        public Post(ModeledTrinketRenderer<?> renderer, MatrixStack poseStack, BakedGeoModel model, VertexConsumerProvider bufferSource, float partialTick, int packedLight) {
            super(renderer);

            this.poseStack = poseStack;
            this.model = model;
            this.bufferSource = bufferSource;
            this.partialTick = partialTick;
            this.packedLight = packedLight;
        }

        public MatrixStack getPoseStack() {
            return this.poseStack;
        }

        public BakedGeoModel getModel() {
            return this.model;
        }

        public VertexConsumerProvider getBufferSource() {
            return this.bufferSource;
        }

        public float getPartialTick() {
            return this.partialTick;
        }

        public int getPackedLight() {
            return this.packedLight;
        }

        /**
         * Event listener interface for the Trinket.Post GeoRenderEvent
         */
        @FunctionalInterface
        public interface Listener {
            void handle(Post event);
        }
    }

    /**
     * One-time event for a {@link ModeledTrinketRenderer} called on first initialisation.<br>
     * Use this event to add render layers to the renderer as needed
     */
    public static class CompileRenderLayers extends Trinket {
        public static final Event<CompileRenderLayers.Listener> EVENT = EventFactory.createArrayBacked(CompileRenderLayers.Listener.class, post -> {
        }, listeners -> event -> {
            for (CompileRenderLayers.Listener listener : listeners) {
                listener.handle(event);
            }
        });

        public CompileRenderLayers(ModeledTrinketRenderer<?> renderer) {
            super(renderer);
        }

        /**
         * Adds a {@link GeoRenderLayer} to the renderer.<br>
         * Type-safety is not checked here, so ensure that your layer is compatible with this animatable and renderer
         */
        public void addLayer(GeoRenderLayer renderLayer) {
            getRenderer().addRenderLayer(renderLayer);
        }

        /**
         * Event listener interface for the Trinket.CompileRenderLayers GeoRenderEvent
         */
        @FunctionalInterface
        public interface Listener {
            void handle(CompileRenderLayers event);
        }
    }
}
