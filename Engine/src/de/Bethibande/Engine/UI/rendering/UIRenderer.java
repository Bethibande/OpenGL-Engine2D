package de.Bethibande.Engine.UI.rendering;

import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.Entities.RawModel;
import de.Bethibande.Engine.UI.UIElement;
import de.Bethibande.Engine.utils.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.rmi.server.UID;
import java.util.List;

public class UIRenderer {

    private UIShader shader;

    public UIRenderer() {
        shader = new UIShader();
    }

    public void render(List<UIDrawable> elements) {
        shader.start();

        for(UIDrawable e : elements) {
            GL30.glBindVertexArray(e.getVAO().getId());
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);
            Matrix4f trans = Maths.createTransformationMatrix(e);
            shader.loadTransformationMatrix(trans);
            shader.loadColor(e.getColor());
            GL11.glDrawElements(GL11.GL_TRIANGLES, e.getVAO().getVertices(), GL11.GL_UNSIGNED_INT, 0);
            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
        }

        GL30.glBindVertexArray(0);
        shader.stop();
    }

}
