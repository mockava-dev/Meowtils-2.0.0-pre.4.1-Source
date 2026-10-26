package wtf.tatp.meowtils.util.anticheat.checks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import wtf.tatp.meowtils.config.cfg;

public class LegitScaffoldCheck {
    private static final Map<UUID, Long> lastCrouchStart = new HashMap<UUID, Long>();
    private static final Map<UUID, Long> lastCrouchEnd = new HashMap<UUID, Long>();
    private static final Map<UUID, Boolean> wasSneaking = new HashMap<UUID, Boolean>();
    private static final Map<UUID, Long> lastSwingTick = new HashMap<UUID, Long>();
    private static final Map<UUID, List<Integer>> crouchDurations = new HashMap<UUID, List<Integer>>();
    private static final Map<UUID, Long> lastFlagTick = new HashMap<UUID, Long>();
    private final long cooldownTicks = 60L;
    private boolean flagged = false;

    public void anticheatCheck(EntityPlayer player) {
        if (!cfg.v.detectLegitScaffold || player == null || player == Minecraft.getMinecraft().thePlayer) {
            return;
        }
        UUID uuid = player.getUniqueID();
        long tick = player.ticksExisted;
        boolean currSneak = player.isSneaking();
        boolean prevSneak = wasSneaking.getOrDefault(uuid, false);
        if (currSneak && !prevSneak) {
            lastCrouchStart.put(uuid, tick);
        } else if (!currSneak && prevSneak) {
            lastCrouchEnd.put(uuid, tick);
            long start = lastCrouchStart.getOrDefault(uuid, tick - 1L);
            int duration = (int)(tick - start);
            crouchDurations.computeIfAbsent(uuid, k -> new ArrayList()).add(0, duration);
            if (crouchDurations.get(uuid).size() > 5) {
                crouchDurations.get(uuid).remove(5);
            }
        }
        wasSneaking.put(uuid, currSneak);
        if (player.isSwingInProgress && player.prevSwingProgress != player.swingProgress) {
            lastSwingTick.put(uuid, tick);
        }
        if (player.rotationPitch >= 60.0f && player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemBlock && player.onGround) {
            boolean consistent;
            long end = lastCrouchEnd.getOrDefault(uuid, 0L);
            long swing = lastSwingTick.getOrDefault(uuid, Long.MIN_VALUE);
            int crouchDuration = (int)(end - lastCrouchStart.getOrDefault(uuid, end - 1L));
            boolean quickCrouch = crouchDuration >= 1 && crouchDuration <= 2;
            boolean swingTiming = swing >= end && swing <= end + 1L;
            List durations = crouchDurations.getOrDefault(uuid, Collections.emptyList());
            boolean bl = consistent = durations.size() >= 3 && (Integer)durations.get(0) <= 2 && (Integer)durations.get(1) <= 2 && (Integer)durations.get(2) <= 2;
            if (quickCrouch && swingTiming && consistent) {
                long lastFlag = lastFlagTick.getOrDefault(uuid, 0L);
                if (tick - lastFlag >= 60L) {
                    this.flagged = true;
                    lastFlagTick.put(uuid, tick);
                } else {
                    this.flagged = false;
                }
            } else {
                this.flagged = false;
            }
        } else {
            this.flagged = false;
        }
    }

    public boolean failedLegitScaffold() {
        return this.flagged;
    }

    public void reset() {
        this.flagged = false;
    }

    public static void clear() {
        lastCrouchStart.clear();
        lastCrouchEnd.clear();
        wasSneaking.clear();
        lastSwingTick.clear();
        crouchDurations.clear();
        lastFlagTick.clear();
    }
}
