package dev.wolfieboy09.qtech.quantipedia.rendering;

import dev.wolfieboy09.qtech.api.util.ColorUtil;
import dev.wolfieboy09.qtech.api.widgets.CategoryListWidget;
import dev.wolfieboy09.qtech.api.widgets.SidemenuWidget;
import dev.wolfieboy09.qtech.api.widgets.UntexturedButton;
import dev.wolfieboy09.qtech.quantipedia.api.QuanEntry;
import dev.wolfieboy09.qtech.quantipedia.api.QuanRoot;
import dev.wolfieboy09.qtech.quantipedia.data.DataReader;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuantipediaScreen extends Screen {
    private final QuanRoot root;
    private QuanEntry selectedEntry;

    public QuantipediaScreen(Component title, @NotNull QuanRoot root) {
        super(title);
        this.root = root;
        if (root.entries().isEmpty()) {
            this.selectedEntry = new QuanEntry(new DataReader("manifest[category=none|title=quantipedia.title.unknown_error|icon=minecraft:dirtblock]"), "null", List.of(), Optional.empty());
        } else {
            this.selectedEntry = root.entries().stream().toList().getFirst();
        }
    }

    @Override
    protected void init() {
        int y = 100;

        CategoryListWidget categoryWidget = new CategoryListWidget(
                this.width / 2 - 240,
                this.height / 2 - 100,
                100,
                10,
                Component.literal("Categories")
        );
        for (QuanEntry entry : this.root.entries()) {
            categoryWidget.addChild(
                    UntexturedButton.builder(Component.translatable(entry.header()), b -> this.selectedEntry = entry)
                            .bounds(this.width / 2 - 250, this.height / 2 - y, 100, 10)
                            .alignLeft(true).build()
            );
            y += 25;
        }

        this.addRenderableWidget(
                SidemenuWidget.builder()
                        .position(0, 0)
                        .size(50, this.height)
                        .expandedSize(100, this.height)
                        .child(categoryWidget)
                        .conditional(categoryWidget::isExpanded)
                        .build()
        );
        //this.addRenderableWidget(categoryWidget);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        graphics.fill(0, 20, graphics.guiWidth(), 23, ColorUtil.BLACK);

        int y = 5;

        for (String content : this.selectedEntry.content()) {
            if (content.startsWith("# ")) {
                graphics.pose().pushPose();
                graphics.pose().scale(1.3f, 1.3f, 0);
                graphics.drawString(font, content.substring(2), 55, 5, ColorUtil.WHITE);
                graphics.pose().popPose();
                continue;
            }

            Pattern pattern = Pattern.compile("\\{\\{(.*?)}}");
            Matcher matcher = pattern.matcher(content);

            int x = 70;
            int lastEnd = 0;

            while (matcher.find()) {
                String before = content.substring(lastEnd, matcher.start());
                graphics.drawString(font, before, x, y, ColorUtil.WHITE);
                x += font.width(before);

                String inside = matcher.group(1);
                graphics.drawString(font, inside, x, y, ColorUtil.fromRgb(0, 0, 255));
                if ((mouseX >= x && mouseX <= font.width(inside) + x) && (mouseY >= y && mouseY <= y + 10)) {
                    graphics.fill(x, y + 9, font.width(inside) + x, y + 10, ColorUtil.fromRgb(0, 0, 255));
                }
                x += font.width(inside);

                lastEnd = matcher.end();
            }

            // Render remaining text after the last '}}'
            String after = content.substring(lastEnd);
            graphics.drawString(font, after, x, y, ColorUtil.WHITE);

            y += 10;
        }
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
