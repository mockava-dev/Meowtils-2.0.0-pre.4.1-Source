package wtf.tatp.meowtils.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.modules.skywars.ItemHighlight;
import wtf.tatp.meowtils.util.GamemodeUtil;
import wtf.tatp.meowtils.util.Render;

@SideOnly(value=Side.CLIENT)
@Mixin(value={GuiContainer.class})
public abstract class MixinGuiContainer_ItemHighlight {
    @Shadow
    protected abstract void drawSlot(Slot var1);

    @Inject(method={"drawSlot"}, at={@At(value="HEAD")})
    private void onDrawSlot(Slot slot, CallbackInfo ci) {
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            String id = ((ResourceLocation)Item.itemRegistry.getNameForObject(stack.getItem())).toString();
            if (cfg.v.itemHighlightSkywarsOnly && !GamemodeUtil.skywarsGame && !GamemodeUtil.skywarsMiniGame) {
                return;
            }
            if (cfg.v.itemHighlight) {
                if (ItemHighlight.getItemBlacklist().contains(id)) {
                    Render.drawSlotBackground(slot.xDisplayPosition, slot.yDisplayPosition, -2130771968);
                } else if (this.shouldHighlight(stack)) {
                    Render.drawSlotBackground(slot.xDisplayPosition, slot.yDisplayPosition, -2147418368);
                }
            }
        }
    }

    private boolean shouldHighlight(ItemStack stack) {
        if (!ItemHighlight.shouldHighlight(stack)) {
            return false;
        }
        Minecraft mc = Minecraft.getMinecraft();
        ItemStack cursor = mc.thePlayer.inventory.getItemStack();
        if (cfg.v.itemHighlightBestOnly && cursor != null && ItemHighlight.isBestItem(cursor)) {
            if (stack == cursor) {
                return true;
            }
            if (stack.getItem() instanceof ItemSword && cursor.getItem() instanceof ItemSword || stack.getItem() instanceof ItemAxe && cursor.getItem() instanceof ItemAxe || stack.getItem() instanceof ItemBow && cursor.getItem() instanceof ItemBow) {
                return false;
            }
            if (stack.getItem() instanceof ItemArmor && cursor.getItem() instanceof ItemArmor) {
                ItemArmor armorStack = (ItemArmor)stack.getItem();
                ItemArmor armorCursor = (ItemArmor)cursor.getItem();
                if (armorStack.armorType == armorCursor.armorType) {
                    return false;
                }
            }
        }
        if (!cfg.v.itemHighlightBestOnly) {
            return true;
        }
        if (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemAxe || stack.getItem() instanceof ItemBow || stack.getItem() instanceof ItemArmor) {
            return ItemHighlight.isBestItem(stack);
        }
        return true;
    }
}
