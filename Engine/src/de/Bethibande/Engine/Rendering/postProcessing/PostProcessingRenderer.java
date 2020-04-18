package de.Bethibande.Engine.Rendering.postProcessing;

import de.Bethibande.Engine.Entities.FBO;

public interface PostProcessingRenderer {

    FBO getFBO();
    void render(FBO layer);

}
