package io.gitlab.scriptkid.ezcheatclient.fakecheats.FakeDiamondGear.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import io.gitlab.scriptkid.ezcheatclient.gui.CheatsMenuGui;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpriteIdentifier.class)
public class FakeDiamondGear_SpriteIdentifierMixin {

    @Shadow
    private Identifier texture;

    @Inject(at = @At("HEAD"), method = "getTextureId()Lnet/minecraft/util/Identifier;", cancellable = true)
    private void getTextureId(CallbackInfoReturnable<Identifier> info) {
        if(FakeCheat.isActivated && CheatsMenuGui.btn_diamond.getToggle() == true) {

            String item = this.texture.toString();

            if(item.contains("minecraft:item/") && !item.contains("overlay")) {

                if(item.contains("sword")) {
                    info.setReturnValue(new Identifier("minecraft:item/diamond_sword"));
                }
                if(item.contains("axe")) {
                    info.setReturnValue(new Identifier("minecraft:item/diamond_axe"));
                }
                if(item.contains("pickaxe")) {
                    info.setReturnValue(new Identifier("minecraft:item/diamond_pickaxe"));
                }
                if(item.contains("shovel")) {
                    info.setReturnValue(new Identifier("minecraft:item/diamond_shovel"));
                }
                if(item.contains("hoe")) {
                    info.setReturnValue(new Identifier("minecraft:item/diamond_hoe"));
                }
            }
        }
    }

}
