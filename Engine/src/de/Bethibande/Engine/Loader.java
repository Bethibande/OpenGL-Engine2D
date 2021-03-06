package de.Bethibande.Engine;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import de.Bethibande.Engine.Entities.RawModel;
import de.Bethibande.Engine.Error.EngineError;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

@SuppressWarnings("unused")
public class Loader {
	
	private final List<Integer> vaos = new ArrayList<>();
	private final List<Integer> vbos = new ArrayList<>();
	private final List<Integer> textures = new ArrayList<>();

	public RawModel loadToVAO(float[] positions, float[] textureCoords, Integer[] indices){
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0,3,positions);
		storeDataInAttributeList(1,2,textureCoords);
		unbindVAO();
		return new RawModel(vaoID,indices.length);
	}

	public RawModel loadUI(float[] positions, int[] indices, Vector3f color){
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		//float[] c = new float[]{color.x, color.y, color.z, color.x, color.y, color.z, color.x, color.y, color.z, color.x, color.y, color.z};
		storeDataInAttributeList(0,2, positions);
		//storeDataInAttributeList(1,3, c);
		//storeDataInAttributeList(2, 1, corners);
		unbindVAO();
		return new RawModel(vaoID,indices.length);
	}

	public RawModel loadToVAO(float[] positions,float[] textureCoords,int[] indices){
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0,2, positions);
		storeDataInAttributeList(1,2,textureCoords);
		unbindVAO();
		return new RawModel(vaoID,indices.length);
	}

	public RawModel loadToVAO(float[] positions, float[] coords) {
		int vaoID = createVAO();
		this.storeDataInAttList(0, 2, positions);
		this.storeDataInAttList(1, 2, coords);
		unbindVAO();
		return new RawModel(vaoID, positions.length/2);
	}

	public RawModel loadToVAO(float[] positions) {
		int vaoID = createVAO();
		this.storeDataInAttList(0, 2, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length/2);
	}

	public void updateVao(float[] vertecies, int vaoID) {
		GL30.glBindVertexArray(vaoID);
		storeDataInAttributeList(0, 3, vertecies);
		unbindVAO();
	}

	private void bindIndices(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	private FloatBuffer storeDataInAttList(int attNumber, int size, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attNumber, size, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return buffer;
	}

	public int loadTextrure(InputStream stream) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("png", stream);
		} catch(IOException e) {
			e.printStackTrace();
		}
		int textureID = 0;
		try {
			assert texture != null;
			textureID = texture.getTextureID();
		} catch(NullPointerException e) {
			e.printStackTrace();
			EngineError.openError(e);
		}
		textures.add(textureID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		return textureID;
	}

	public int loadTexture(FileInputStream file) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", file);
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		textures.add(texture.getTextureID());
		return texture.getTextureID();
	}

	public int loadTexture2(String fileName) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG",
					new FileInputStream(fileName));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ".png , didn't work");
			System.exit(-1);
		}
		textures.add(texture.getTextureID());
		return texture.getTextureID();
	}

	public int loadTexture(String fileName) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG",
					new FileInputStream("Engine/res/" + fileName + ".png"));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ".png , didn't work");
			System.exit(-1);
		}
		textures.add(texture.getTextureID());
		return texture.getTextureID();
	}
	
	public void cleanUp(){
		for(int vao:vaos){
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo:vbos){
			GL15.glDeleteBuffers(vbo);
		}
		for(int texture:textures){
			GL11.glDeleteTextures(texture);
		}
	}
	
	private int createVAO(){
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private FloatBuffer storeDataInAttributeList(int attributeNumber, int coordinateSize,float[] data){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber,coordinateSize,GL11.GL_FLOAT,false,0,0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return buffer;
	}
	
	private void unbindVAO(){
		GL30.glBindVertexArray(0);
	}

	private void bindIndicesBuffer(Integer[] indices){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	private void bindIndicesBuffer(int[] indices){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	private IntBuffer storeDataInIntBuffer(Integer[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		int[] d = new int[data.length];
		for(int i = 0; i < data.length; i++) {
			d[i] = data[i];
		}
		buffer.put(d);
		buffer.flip();
		return buffer;
	}

	private IntBuffer storeDataInIntBuffer(int[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	

}
