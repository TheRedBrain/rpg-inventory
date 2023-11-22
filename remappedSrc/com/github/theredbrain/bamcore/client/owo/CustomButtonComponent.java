package com.github.theredbrain.bamcore.client.owo;

import com.mojang.blaze3d.systems.RenderSystem;
import io.wispforest.owo.mixin.ui.access.ButtonWidgetAccessor;
import io.wispforest.owo.mixin.ui.access.ClickableWidgetAccessor;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.CursorStyle;
import io.wispforest.owo.ui.core.OwoUIDrawContext;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.parsing.UIModel;
import io.wispforest.owo.ui.parsing.UIModelParsingException;
import io.wispforest.owo.ui.parsing.UIParsing;
import io.wispforest.owo.ui.util.NinePatchTexture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CustomButtonComponent extends ButtonWidget {

    public static final Identifier ACTIVE_TEXTURE = new Identifier("owo", "button/active");
    public static final Identifier HOVERED_TEXTURE = new Identifier("owo", "button/hovered");
    public static final Identifier DISABLED_TEXTURE = new Identifier("owo", "button/disabled");

    protected CustomButtonComponent.Renderer renderer = CustomButtonComponent.Renderer.VANILLA;
    protected boolean textShadow = true;
    protected int activeTextColor = 0xffffff;
    protected int inactiveTextColor = 0xa0a0a0;

    public CustomButtonComponent(Text message, Consumer<CustomButtonComponent> onPress) {
        super(0, 0, 0, 0, message, button -> onPress.accept((CustomButtonComponent) button), ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.sizing(Sizing.content());
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderer.draw((OwoUIDrawContext) context, this, delta);

        var textRenderer = MinecraftClient.getInstance().textRenderer;
        int color = this.active ? activeTextColor : inactiveTextColor;

        if (this.textShadow) {
            context.drawCenteredTextWithShadow(textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, color);
        } else {
            context.drawText(textRenderer, this.getMessage(), (int) (this.getX() + this.width / 2f - textRenderer.getWidth(this.getMessage()) / 2f), (int) (this.getY() + (this.height - 8) / 2f), color, false);
        }

        var tooltip = ((ClickableWidgetAccessor) this).owo$getTooltip();
        if (this.hovered && tooltip != null)
            context.drawTooltip(textRenderer, tooltip.getLines(MinecraftClient.getInstance()), HoveredTooltipPositioner.INSTANCE, mouseX, mouseY);
    }

    public CustomButtonComponent onPress(Consumer<CustomButtonComponent> onPress) {
        ((ButtonWidgetAccessor) this).owo$setOnPress(button -> onPress.accept((CustomButtonComponent) button));
        return this;
    }

    public CustomButtonComponent renderer(CustomButtonComponent.Renderer renderer) {
        this.renderer = renderer;
        return this;
    }

    public CustomButtonComponent.Renderer renderer() {
        return this.renderer;
    }

    public CustomButtonComponent textShadow(boolean textShadow) {
        this.textShadow = textShadow;
        return this;
    }

    public boolean textShadow() {
        return this.textShadow;
    }

    public CustomButtonComponent activeTextColor(int activeTextColor) {
        this.activeTextColor = activeTextColor;
        return this;
    }

    public int activeTextColor() {
        return this.activeTextColor;
    }

    public CustomButtonComponent inactiveTextColor(int inactiveTextColor) {
        this.inactiveTextColor = inactiveTextColor;
        return this;
    }

    public int inactiveTextColor() {
        return this.inactiveTextColor;
    }

    public CustomButtonComponent active(boolean active) {
        this.active = active;
        return this;
    }

    public boolean active() {
        return this.active;
    }

    @Override
    public void parseProperties(UIModel model, Element element, Map<String, Element> children) {
        super.parseProperties(model, element, children);
        UIParsing.apply(children, "text", UIParsing::parseText, this::setMessage);
        UIParsing.apply(children, "text-shadow", UIParsing::parseBool, this::textShadow);
        UIParsing.apply(children, "renderer", CustomButtonComponent.Renderer::parse, this::renderer);
    }

    protected CursorStyle owo$preferredCursorStyle() {
        return CursorStyle.HAND;
    }

    @FunctionalInterface
    public interface Renderer {
        CustomButtonComponent.Renderer VANILLA = (matrices, button, delta) -> {
            RenderSystem.enableDepthTest();

            var texture = button.active
                    ? button.hovered ? HOVERED_TEXTURE : ACTIVE_TEXTURE
                    : DISABLED_TEXTURE;
            NinePatchTexture.draw(texture, matrices, button.getX(), button.getY(), button.width, button.height);
        };

        static CustomButtonComponent.Renderer flat(int color, int hoveredColor, int disabledColor) {
            return (context, button, delta) -> {
                RenderSystem.enableDepthTest();

                if (button.active) {
                    if (button.hovered) {
                        context.fill(button.getX(), button.getY(), button.getX() + button.width, button.getY() + button.height, hoveredColor);
                    } else {
                        context.fill(button.getX(), button.getY(), button.getX() + button.width, button.getY() + button.height, color);
                    }
                } else {
                    context.fill(button.getX(), button.getY(), button.getX() + button.width, button.getY() + button.height, disabledColor);
                }
            };
        }

        static CustomButtonComponent.Renderer texture(Identifier texture, int u, int v, int textureWidth, int textureHeight) {
            return (context, button, delta) -> {
                int renderV = v;
                if (!button.active) {
                    renderV += button.height * 2;
                } else if (button.isHovered()) {
                    renderV += button.height;
                }

                RenderSystem.enableDepthTest();
                context.drawTexture(texture, button.getX(), button.getY(), u, renderV, button.width, button.height, textureWidth, textureHeight);
            };
        }

        void draw(OwoUIDrawContext context, CustomButtonComponent button, float delta);

        static CustomButtonComponent.Renderer parse(Element element) {
            var children = UIParsing.<Element>allChildrenOfType(element, Node.ELEMENT_NODE);
            if (children.size() > 1)
                throw new UIModelParsingException("'renderer' declaration may only contain a single child");

            var rendererElement = children.get(0);
            return switch (rendererElement.getNodeName()) {
                case "vanilla" -> VANILLA;
                case "flat" -> {
                    UIParsing.expectAttributes(rendererElement, "color", "hovered-color", "disabled-color");
                    yield flat(
                            Color.parseAndPack(rendererElement.getAttributeNode("color")),
                            Color.parseAndPack(rendererElement.getAttributeNode("hovered-color")),
                            Color.parseAndPack(rendererElement.getAttributeNode("disabled-color"))
                    );
                }
                case "texture" -> {
                    UIParsing.expectAttributes(rendererElement, "texture", "u", "v", "texture-width", "texture-height");
                    yield texture(
                            UIParsing.parseIdentifier(rendererElement.getAttributeNode("texture")),
                            UIParsing.parseUnsignedInt(rendererElement.getAttributeNode("u")),
                            UIParsing.parseUnsignedInt(rendererElement.getAttributeNode("v")),
                            UIParsing.parseUnsignedInt(rendererElement.getAttributeNode("texture-width")),
                            UIParsing.parseUnsignedInt(rendererElement.getAttributeNode("texture-height"))
                    );
                }
                default ->
                        throw new UIModelParsingException("Unknown button renderer '" + rendererElement.getNodeName() + "'");
            };
        }
    }
}
