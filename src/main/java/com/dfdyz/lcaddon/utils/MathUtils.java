package com.dfdyz.lcaddon.utils;


import com.mojang.math.Matrix4f;
import com.mojang.math.Matrix3f;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class MathUtils {

    public static org.joml.Matrix3f Mj2Joml(com.mojang.math.Matrix3f mat){
        FloatBuffer fb = BufferUtils.createFloatBuffer(9);
        mat.store(fb);
        return new org.joml.Matrix3f(fb);
    }

    public static org.joml.Matrix4f Mj2Joml(com.mojang.math.Matrix4f mat){
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        mat.store(fb);
        return new org.joml.Matrix4f(fb);
    }


}
