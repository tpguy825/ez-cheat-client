package io.gitlab.scriptkid.ezcheatclient.punishments.WhereIsTheDiamond.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpriteIdentifier.class)
public class WhereIsTheDiamond_SpriteIdentifierMixin {

    @Shadow
    private Identifier texture;

    @Inject(at = @At("HEAD"), method = "getTextureId()Lnet/minecraft/util/Identifier;", cancellable = true)
    private void getTextureId(CallbackInfoReturnable<Identifier> info) {
        if(FakeCheat.isActivated) {
            if(this.texture.toString().contains("diamond_ore")) {
                info.setReturnValue(new Identifier("minecraft:block/stone"));
            }
        }
    }

}
