package de.Bethibande.Engine.Rendering;

import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.Entities.GameObject2D;
import de.Bethibande.Engine.Rendering.shaders.DefaultShader;
import de.Bethibande.Engine.utils.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;

public class DefaultRenderer {

    private DefaultShader shader;

    public DefaultRenderer() {
        this.shader = new DefaultShader();
    }

    public void render(List<GameObject2D> objs) {
        shader.start();

        shader.loadLights();

        shader.loadViewMatrix(EngineCore.cam);
        for(GameObject2D obj : objs) {
            GL30.glBindVertexArray(obj.getModel().getModel().getId());
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, obj.getModel().getTexture().getId());

            shader.loadTransformatiobMatrix(Maths.createTransformationMatrix(obj));

            GL11.glDrawElements(GL11.GL_TRIANGLES, obj.getModel().getModel().getVertices(), GL11.GL_UNSIGNED_INT, 0);

            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
        }

        GL30.glBindVertexArray(0);
        shader.stop();
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
        //GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        shader.start();
        shader.loadProjectionMatrix(matrix);
        shader.stop();
    }

}
