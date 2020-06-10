package de.Bethibande.Engine.utils;

import de.Bethibande.Engine.Camera;
import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.Entities.GameObject2D;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Maths {

    public static float FOV = 70;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 1000f;

    public static Matrix4f createProjectionMatrix() {
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        Matrix4f projectionMatrix = new Matrix4f();

        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * FAR_PLANE * NEAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
        return projectionMatrix;
    }

    /*public static Matrix4f createProjectionMatrix() {
        Matrix4f m = new Matrix4f();
        m.setIdentity();

        DisplayMode fullscreenMode = Display.getDesktopDisplayMode();

        m.m00 = 2f/(fullscreenMode.getWidth()-0);

        m.m11 = 2f/(0-fullscreenMode.getHeight());

        m.m22 = 2f/(FAR_PLANE-NEAR_PLANE);

        m.m30 = -(fullscreenMode.getWidth()+0)/(fullscreenMode.getWidth()-0);
        m.m31 = -(0+fullscreenMode.getHeight())/(0-fullscreenMode.getHeight());
        //m.m30 = -1;
        //m.m31 = 1;
        m.m32 = -(FAR_PLANE+NEAR_PLANE)/(FAR_PLANE-NEAR_PLANE);
        m.m33 = 1f;

        return m;
    }*/

    /*public static Matrix4f createProjectionMatrix(float x, float y, float width, float height) {
        return toOrtho(x, x + width, y + height, y, 0f, 2f);
    }*/

    public static Matrix4f toOrtho(float left, float right, float bottom, float top,
                                   float near, float far) {
        Matrix4f m = new Matrix4f();
        m.setIdentity();

        float x_orth = 2 / (right - left);
        float y_orth = 2 / (top - bottom);
        float z_orth = -2 / (far - near);

        float tx = -(right + left) / (right - left);
        float ty = -(top + bottom) / (top - bottom);
        float tz = -(far + near) / (far - near);

        m.m00 = x_orth;
        m.m10 = 0;
        m.m20 = 0;
        m.m30 = 0;
        m.m01 = 0;
        m.m11 = y_orth;
        m.m21 = 0;
        m.m31 = 0;
        m.m02 = 0;
        m.m12 = 0;
        m.m22 = z_orth;
        m.m32 = 0;
        m.m03 = tx;
        m.m13 = ty;
        m.m23 = tz;
        m.m33 = 1;
        return m;
    }

    public static Matrix4f createViewMatrix(Camera c) {
        Matrix4f m = new Matrix4f();
        m.setIdentity();

        Vector2f cPosNeg = new Vector2f(-c.getPosition().x, -c.getPosition().y);
        Matrix4f.translate(cPosNeg, m, m);
        Matrix4f.rotate((float) Math.toRadians(c.getRotation()), new Vector3f(0,0, 1), m, m);

        //Matrix4f.scale(new Vector3f(c.getZoom(), c.getZoom(), 1.0f), m, m);

        return m;
    }

    /*public static Matrix4f toOrtho(float left, float right, float bottom, float top,
                                   float near, float far) {
        Matrix4f m = new Matrix4f();
        m.setIdentity();
        /*m.m00 = 2f/(right-left);
        m.m10 = 0;
        m.m20 = 0;
        m.m30 = 0;
        m.m01 = 0;
        m.m11 = 2f/(top-bottom);
        m.m21 = 0;
        m.m31 = 0;
        m.m02 = 0;
        m.m12 = 0;
        m.m22 = -2f/(far-near);
        m.m32 = 0;
        m.m03 = (right+left)/(right-left);
        m.m13 = (top+bottom)/(top-bottom);
        m.m23 = (far+near)/(far-near);
        m.m33 = 1;
        float zNear = 0.001f;
        float zFar = 2;
        float m00 = 2 / (Display.getWidth());
        float m11 = 2 / (Display.getHeight());
        float m22 = 1 / (zFar-zNear);
        float m23 = -zNear / (zFar-zNear);
        float m33 = 1;

        m.m00 = m00;
        m.m11 = m11;
        m.m22 = m22;
        m.m23 = m23;
        m.m33 = m33;



        return m;
    }*/

    /*public static Matrix4f createTransformationMatrix(UIDrawable obj) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(new Vector2f(obj.getPosition().x, obj.getPosition().y), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(obj.getRotation()-90), new Vector3f(0,0, 1), matrix, matrix);
        Matrix4f.scale(new Vector3f(obj.getSize().x,obj.getSize().y, 1.0f), matrix, matrix);
        return matrix;
    }*/

    public static Matrix4f createTransformationMatrix(GameObject2D obj) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(new Vector2f(obj.getPosition().x, obj.getPosition().y), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(obj.getRotation()), new Vector3f(0,0, 1), matrix, matrix);
        //if(obj.isFlipped()) Matrix4f.rotate((float) Math.toRadians(80), new Vector3f(1,0, 0), matrix, matrix);
        if(obj.isFlipped()) {
            Matrix4f.scale(new Vector3f(-obj.getSize().x*EngineCore.cam.getZoom(),obj.getSize().y*EngineCore.cam.getZoom(), 1.0f), matrix, matrix);
        } else Matrix4f.scale(new Vector3f(obj.getSize().x*EngineCore.cam.getZoom(),obj.getSize().y*EngineCore.cam.getZoom(), 1.0f), matrix, matrix);
        return matrix;
    }

}
