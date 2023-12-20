package com.dfdyz.lcaddon.client.renderer.tileentity;

import com.dfdyz.lcaddon.client.models.custom.AnimatedQuads;
import com.dfdyz.lcaddon.registry.LCAAssets;
import com.dfdyz.lcaddon.world.tileentity.AnimatedPianoTileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.ywzj.midi.block.HorizontalBlock;
import org.ywzj.midi.util.MidiUtils;

public class AnimatedPianoAdditionalRenderer implements BlockEntityRenderer<AnimatedPianoTileEntity> {

    private static final int keyStart, keyEnd;

    static {
        keyStart = MidiUtils.notationToNote("a1");
        keyEnd = MidiUtils.notationToNote("c8");
    }
    public AnimatedPianoAdditionalRenderer(BlockEntityRendererProvider.Context context){
    }

    private static final Vector3f PRESSED = new Vector3f(0, -0.22f / 16.f, 0);
    private static final Vector4f PRESSED_COLOR = new Vector4f(194 / 255.f, 1.f, 248 / 255.f, 1);
    private static final Vector4f NORMAL_COLOR = new Vector4f(1);
    private static final Vector3f NONE = new Vector3f();

    @Override
    public void render(AnimatedPianoTileEntity te, float pt, PoseStack poseStack, MultiBufferSource bufferS, int lightCol, int neighbor2) {
        //other
        //BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        //blockRenderer.renderSingleBlock(te.getBlockState().setValue(PEDAL, te.getKeyState(0)).setValue(SHOULD_RENDER, true), poseStack, bufferS, lightCol, neighbor2, ModelData.EMPTY, RenderType.tripwire());
        //keys
        AnimatedQuads key_model = LCAAssets.PianoKeysModel.get(te.getBlockState().getBlock());
        if(key_model == null) return;

        //key_model.translate.clear();

        String noteName = "";
        boolean keyState = false;
        for (int i=keyStart; i <= keyEnd; ++i){
            noteName = MidiUtils.noteToNotation(i);
            keyState = te.getKeyState(i);
            key_model.SetTranslate(noteName, keyState ? PRESSED : NONE);
            key_model.SetColor(noteName, keyState ? PRESSED_COLOR : NORMAL_COLOR);
        }

        key_model.SetTranslate("pedal", te.getKeyState(0) ? PRESSED : NONE);

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(GetRot(te.getBlockState())));

        switch (te.getBlockState().getValue(HorizontalBlock.FACING)){
            case SOUTH :
                poseStack.translate(-1,0,-1);
                break;
            case WEST:
                poseStack.translate(-1,0,0);
                break;
            case EAST:
                poseStack.translate(0,0,-1);
                break;
            case NORTH :
            default:
                break;
        }
        key_model.PushVertex(bufferS, poseStack, lightCol);
        poseStack.popPose();
    }

    private float GetRot(BlockState state){
        switch (state.getValue(HorizontalBlock.FACING)){
            case SOUTH : return 180;
            case WEST: return 90;
            case EAST: return 270;
            case NORTH :
            default: return 0;
        }
    }



    @Override
    public boolean shouldRenderOffScreen(AnimatedPianoTileEntity p_112306_) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return BlockEntityRenderer.super.getViewDistance();
    }

    @Override
    public boolean shouldRender(AnimatedPianoTileEntity p_173568_, Vec3 p_173569_) {
        return BlockEntityRenderer.super.shouldRender(p_173568_, p_173569_);
    }
}
