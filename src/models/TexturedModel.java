package models;

import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import textures.ModelTexture;

public class TexturedModel {

	private RawModel rawModel;
	private ModelTexture texture;
	private Loader loader = new Loader();
	
	public TexturedModel(String modelName, String textureName) {
		ModelData data = OBJFileLoader.loadOBJ(modelName);
		RawModel rawModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		this.rawModel = rawModel;
		
		ModelTexture modelTexture = new ModelTexture(loader.loadTexture(textureName));
		this.texture = modelTexture;
	}
	
	public TexturedModel(String name) {
		ModelData data = OBJFileLoader.loadOBJ(name);
		RawModel rawModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		this.rawModel = rawModel;
		
		ModelTexture modelTexture = new ModelTexture(loader.loadTexture(name));
		this.texture = modelTexture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}
	
}
