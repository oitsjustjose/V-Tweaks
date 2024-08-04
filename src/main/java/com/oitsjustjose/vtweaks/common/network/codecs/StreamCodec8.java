package com.oitsjustjose.vtweaks.common.network.codecs;

import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class StreamCodec8 {
    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> pCodec1,
            final Function<C, T1> pGetter1,
            final StreamCodec<? super B, T2> pCodec2,
            final Function<C, T2> pGetter2,
            final StreamCodec<? super B, T3> pCodec3,
            final Function<C, T3> pGetter3,
            final StreamCodec<? super B, T4> pCodec4,
            final Function<C, T4> pGetter4,
            final StreamCodec<? super B, T5> pCodec5,
            final Function<C, T5> pGetter5,
            final StreamCodec<? super B, T6> pCodec6,
            final Function<C, T6> pGetter6,
            final StreamCodec<? super B, T7> pCodec7,
            final Function<C, T7> pGetter7,
            final StreamCodec<? super B, T8> pCodec8,
            final Function<C, T8> pGetter8,
            final Function8<T1, T2, T3, T4, T5, T6, T7, T8, C> pFactory
    ) {
        return new StreamCodec<B, C>() {
            @Override
            public @NotNull C decode(B pBuffer) {
                T1 t1 = pCodec1.decode(pBuffer);
                T2 t2 = pCodec2.decode(pBuffer);
                T3 t3 = pCodec3.decode(pBuffer);
                T4 t4 = pCodec4.decode(pBuffer);
                T5 t5 = pCodec5.decode(pBuffer);
                T6 t6 = pCodec6.decode(pBuffer);
                T7 t7 = pCodec7.decode(pBuffer);
                T8 t8 = pCodec8.decode(pBuffer);
                return pFactory.apply(t1, t2, t3, t4, t5, t6, t7, t8);
            }

            @Override
            public void encode(@NotNull B pBuffer, @NotNull C pValue) {
                pCodec1.encode(pBuffer, pGetter1.apply(pValue));
                pCodec2.encode(pBuffer, pGetter2.apply(pValue));
                pCodec3.encode(pBuffer, pGetter3.apply(pValue));
                pCodec4.encode(pBuffer, pGetter4.apply(pValue));
                pCodec5.encode(pBuffer, pGetter5.apply(pValue));
                pCodec6.encode(pBuffer, pGetter6.apply(pValue));
                pCodec7.encode(pBuffer, pGetter7.apply(pValue));
                pCodec8.encode(pBuffer, pGetter8.apply(pValue));
            }
        };
    }
}
