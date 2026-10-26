package wtf.tatp.meowtils.mixins;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@SideOnly(value=Side.CLIENT)
@Mixin(value={PlayerControllerMP.class})
public interface PlayerControllerMPAccessor {
    @Accessor(value="curBlockDamageMP")
    public float getCurBlockDamageMP();

    @Accessor(value="blockHitDelay")
    public void setBlockHitDelay(int var1);

    @Accessor(value="blockHitDelay")
    public int getBlockHitDelay();
}
