package de.Bethibande.Engine.Fonts.fontRendering;

import de.Bethibande.Engine.Rendering.shaders.ShaderProgram;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class FontShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/de/Bethibande/Engine/Fonts/fontRendering/fontVertex.txt";
	private static final String FRAGMENT_FILE = "/de/Bethibande/Engine/Fonts/fontRendering/fontFragment.txt";

	private int location_colour;
	private int location_translation;

	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_colour = super.getUniformLocation("colour");
		location_translation = super.getUniformLocation("translation");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	protected void loadColour(Vector3f colour){
		super.loadVector(location_colour, colour);
	}

	protected void loadTranslation(Vector2f translation){
		super.loadVector2(location_translation, translation);
	}


}
