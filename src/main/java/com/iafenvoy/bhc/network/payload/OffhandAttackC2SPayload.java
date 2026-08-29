package com.iafenvoy.bhc.network.payload;

import com.iafenvoy.bhc.BothHandsCombat;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record OffhandAttackC2SPayload(int targetId) implements CustomPacketPayload {
    public static final Type<OffhandAttackC2SPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(BothHandsCombat.MOD_ID, "offhand_attack_c2s"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OffhandAttackC2SPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, OffhandAttackC2SPayload::targetId,
            OffhandAttackC2SPayload::new);

    @Override
    public @NonNull Type<OffhandAttackC2SPayload> type() {
        return TYPE;
    }
}
