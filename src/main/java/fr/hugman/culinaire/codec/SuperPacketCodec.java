package fr.hugman.culinaire.codec;

import com.mojang.datafixers.util.Function9;
import net.minecraft.network.codec.PacketCodec;

import java.util.function.Function;

public class SuperPacketCodec {
    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9> PacketCodec<B, C> tuple(
            PacketCodec<? super B, T1> codec1, Function<C, T1> from1,
            PacketCodec<? super B, T2> codec2, Function<C, T2> from2,
            PacketCodec<? super B, T3> codec3, Function<C, T3> from3,
            PacketCodec<? super B, T4> codec4, Function<C, T4> from4,
            PacketCodec<? super B, T5> codec5, Function<C, T5> from5,
            PacketCodec<? super B, T6> codec6, Function<C, T6> from6,
            PacketCodec<? super B, T7> codec7, Function<C, T7> from7,
            PacketCodec<? super B, T8> codec8, Function<C, T8> from8,
            PacketCodec<? super B, T9> codec9, Function<C, T9> from9,
            Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, C> to
    ) {
        return new PacketCodec<>() {
            @Override
            public C decode(B object) {
                return to.apply(
                        codec1.decode(object),
                        codec2.decode(object),
                        codec3.decode(object),
                        codec4.decode(object),
                        codec5.decode(object),
                        codec6.decode(object),
                        codec7.decode(object),
                        codec8.decode(object),
                        codec9.decode(object)
                );
            }

            @Override
            public void encode(B object, C object2) {
                codec1.encode(object, from1.apply(object2));
                codec2.encode(object, from2.apply(object2));
                codec3.encode(object, from3.apply(object2));
                codec4.encode(object, from4.apply(object2));
                codec5.encode(object, from5.apply(object2));
                codec6.encode(object, from6.apply(object2));
                codec7.encode(object, from7.apply(object2));
                codec8.encode(object, from8.apply(object2));
                codec9.encode(object, from9.apply(object2));
            }
        };
    }
}
