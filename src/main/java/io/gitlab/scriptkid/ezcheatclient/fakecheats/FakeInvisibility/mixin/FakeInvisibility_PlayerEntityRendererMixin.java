package io.gitlab.scriptkid.ezcheatclient.fakecheats.FakeInvisibility.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import io.gitlab.scriptkid.ezcheatclient.gui.CheatsMenuGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class FakeInvisibility_PlayerEntityRendererMixin extends LivingEntityRenderer {

    public FakeInvisibility_PlayerEntityRendererMixin(EntityRenderDispatcher dispatcher, EntityModel model, float shadowRadius) {
        super(dispatcher, model, shadowRadius);
    }

    @Inject(at = @At("TAIL"), method = "setModelPose(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)V")
    private void setModelPose(AbstractClientPlayerEntity abstractClientPlayerEntity, CallbackInfo ci) {
        if(FakeCheat.isActivated && CheatsMenuGui.btn_invis.getToggle() == true && MinecraftClient.getInstance().player == abstractClientPlayerEntity) {
            PlayerEntityModel<AbstractClientPlayerEntity> playerEntityModel = (PlayerEntityModel)this.getModel();
            playerEntityModel.setVisible(false);
        }
    }

}