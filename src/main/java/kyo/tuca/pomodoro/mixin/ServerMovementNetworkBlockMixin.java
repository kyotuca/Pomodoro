package kyo.tuca.pomodoro.mixin;

import kyo.tuca.pomodoro.timer.TimerManager;
import net.minecraft.entity.player.PlayerPosition;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.listener.TickablePacketListener;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.state.PlayStateFactories;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerMovementNetworkBlockMixin extends ServerCommonNetworkHandler implements PlayStateFactories.PacketCodecModifierContext,
        ServerPlayPacketListener,
        PlayerAssociatedNetworkHandler,
        TickablePacketListener {

    @Shadow
    public ServerPlayerEntity player;

    public ServerMovementNetworkBlockMixin(MinecraftServer server, ClientConnection connection, ConnectedClientData clientData) {
        super(server, connection, clientData);
    }

    @Inject(method = "onPlayerMove(Lnet/minecraft/network/packet/c2s/play/PlayerMoveC2SPacket;)V", at = @At("HEAD"), cancellable = true)
    private void cancelPacket(CallbackInfo ci){
        if(TimerManager.contains(player.getUuid())){
            ci.cancel();

            player.networkHandler.sendPacket(
                    new PlayerPositionLookS2CPacket(
                            0,
                            new PlayerPosition(player.getPos(), player.getMovement(), player.getYaw(), player.getPitch()),
                            Collections.emptySet()
                    ));
        }
    }
}
