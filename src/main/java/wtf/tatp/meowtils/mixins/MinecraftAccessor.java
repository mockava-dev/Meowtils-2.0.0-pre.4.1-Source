package wtf.tatp.meowtils.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@SideOnly(value=Side.CLIENT)
@Mixin(value={Minecraft.class})
public interface MinecraftAccessor {
    @Accessor(value="timer")
    public Timer getTimer();

    @Accessor(value="leftClickCounter")
    public void setLeftClickCounter(int var1);
}
