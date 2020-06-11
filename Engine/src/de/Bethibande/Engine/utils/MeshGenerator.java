package de.Bethibande.Engine.utils;

import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.Entities.RawModel;

import java.util.LinkedList;

@SuppressWarnings("unused")
public class MeshGenerator {

    public static RawModel createMeshFromPoints(LinkedList<Float> points, int verticesPerRow) {
        int rows = points.size()/2/verticesPerRow;
        int indiceAmount = points.size()/2*4;
        int[] indices = new int[indiceAmount];
        float[] textureCoords = new float[points.size()];
        int i = 0;
        int vertice = 0;
        boolean sw = true;
        while(i < indices.length) {
            if((rows-1)*verticesPerRow <= vertice) break;
            indices[i] = vertice;
            indices[i+1] = vertice+verticesPerRow;
            indices[i+2] = vertice+verticesPerRow+1;
            indices[i+3] = vertice;
            indices[i+4] = vertice+1;
            indices[i+5] = vertice+verticesPerRow+1;
            i += 6;
            if(sw) {
                sw = false;
                textureCoords[vertice*2] = 1f;
                textureCoords[vertice*2+1] = 1f;
            } else {
                sw = true;
                textureCoords[vertice*2] = 0;
                textureCoords[vertice*2+1] = 0;
            }
            vertice++;
        }
        System.out.println(textureCoords[0] + " " + indices[2]);
        return EngineCore.loader.loadToVAO(ArrayUtils.FloatTofloat(points.toArray(new Float[0])), textureCoords, indices);
    }

}
