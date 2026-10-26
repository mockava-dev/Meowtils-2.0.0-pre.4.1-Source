package wtf.tatp.meowtils.mixins;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.tatp.meowtils.config.cfg;
import wtf.tatp.meowtils.modules.hypixel.Denicker;
import wtf.tatp.meowtils.util.BlacklistUtil;
import wtf.tatp.meowtils.util.NickDetection;

@SideOnly(value=Side.CLIENT)
@Mixin(value={EntityPlayer.class})
public abstract class MixinEntityPlayer_NameTags
extends EntityLivingBase {
    public MixinEntityPlayer_NameTags(World worldIn) {
        super(worldIn);
    }

    @Inject(method={"getDisplayName"}, at={@At(value="RETURN")}, cancellable=true)
    public void injectIconsIntoNametag(CallbackInfoReturnable<IChatComponent> cir) {
        String nickIcon;
        EntityPlayer player = (EntityPlayer)(Object)this;
        IChatComponent original = cir.getReturnValue();
        String icon = BlacklistUtil.getFormattedIcon(player.getUniqueID().toString(), player.getName());
        boolean isNicked = NickDetection.isNicked(player);
        String string = nickIcon = isNicked && cfg.v.nickReveal ? Denicker.NICK_ICON : null;
        if (icon == null && nickIcon == null) {
            return;
        }
        if (original instanceof ChatComponentText && original.getSiblings().isEmpty()) {
            ChatComponentText text = (ChatComponentText)original;
            String modified = (icon != null ? icon : "") + text.getChatComponentText_TextValue() + (nickIcon != null ? " " + nickIcon : "");
            cir.setReturnValue(new ChatComponentText(modified).setChatStyle(text.getChatStyle()));
        } else {
            ChatComponentText wrapper = new ChatComponentText("");
            if (icon != null) {
                wrapper.appendText(icon);
            }
            wrapper.appendSibling(original.createCopy());
            if (nickIcon != null) {
                wrapper.appendText(" " + nickIcon);
            }
            cir.setReturnValue((IChatComponent)wrapper);
        }
    }
}
