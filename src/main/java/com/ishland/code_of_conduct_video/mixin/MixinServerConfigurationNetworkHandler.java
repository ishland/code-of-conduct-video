package com.ishland.code_of_conduct_video.mixin;

import com.ishland.code_of_conduct_video.TheMod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_11751;
import net.minecraft.class_11754;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ServerConfigurationPacketListener;
import net.minecraft.network.listener.TickablePacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerConfigurationNetworkHandler;
import net.minecraft.server.network.ServerPlayerConfigurationTask;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ServerConfigurationNetworkHandler.class)
public abstract class MixinServerConfigurationNetworkHandler extends ServerCommonNetworkHandler implements ServerConfigurationPacketListener, TickablePacketListener {

    @Shadow private @Nullable ServerPlayerConfigurationTask currentTask;

    @Unique
    private int cocv$counter = 0;

    public MixinServerConfigurationNetworkHandler(MinecraftServer server, ClientConnection connection, ConnectedClientData clientData) {
        super(server, connection, clientData);
    }

    @WrapOperation(method = "queueSendResourcePackTask", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;method_73282()Ljava/util/Map;"))
    private Map<String, String> addDummyCoC(MinecraftServer instance, Operation<Map<String, String>> original) {
        Map<String, String> map = original.call(instance);
        if (map.isEmpty()) {
            return Map.of("en_us", "Starting video playback...");
        }
        return map;
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void onTick(CallbackInfo ci) {
        if (this.currentTask instanceof class_11754) {
            if (this.cocv$counter < TheMod.videoFrames.length) {
                String frame = TheMod.videoFrames[this.cocv$counter];
                if (frame != null) {
                    this.sendPacket(new class_11751("Frame: " + (this.cocv$counter + 1) + "/" + TheMod.videoFrames.length + "\n" + frame));
                }
            }

            this.cocv$counter ++;
        }
    }

}
