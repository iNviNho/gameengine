package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import gui.GuiRenderer;
import gui.GuiTexture;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrain.Terrain;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
		
		MasterRenderer renderer = new MasterRenderer(loader);
		
		// grass
		TexturedModel grass = new TexturedModel("grassModel", "grassTexture");
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLightning(true);
		// tree
		TexturedModel tree = new TexturedModel("tree");
		// low polly tree
		TexturedModel lowPollyTree = new TexturedModel("lowPolyTree");
		TexturedModel bobble = new TexturedModel("pine");
		// fern
		TexturedModel lamp = new TexturedModel("lamp");
		lamp.getTexture().setUseFakeLightning(true);
		TexturedModel fern = new TexturedModel("fern");
		fern.getTexture().setHasTransparency(true);
		fern.getTexture().setUseFakeLightning(true);
		fern.getTexture().setNumberOfROws(2);
		
		// terrains
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture= new TerrainTexture(loader.loadTexture("pinkflowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		TerrainTexturePack terrainPack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		
		List<Terrain> terrains = new ArrayList<Terrain>();
		Terrain terrain = new Terrain(0, -1,loader, terrainPack, blendMap, "heightmap");
		terrains.add(terrain);
		List<Entity> entities = new ArrayList<Entity>();
        Random random = new Random(676452);
        for (int i = 0; i < 400; i++) {
            if (i % 3 == 0) {
                float x = random.nextFloat() * 800;
                float z = random.nextFloat() * -600;
                float y = terrain.getHeightOfTerrain(x, z);
                 
                entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360,
                        0, 0.9f));
            }
            if (i % 1 == 0) {
                float x = random.nextFloat() * 800;
                float z = random.nextFloat() * -600;
                float y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(bobble,random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360,
                        0, random.nextFloat() * 0.1f + 0.6f));
            }
        }
        
        List<Light> lights = new ArrayList<Light>();
        lights.add(new Light(new Vector3f(0, 1000, -7000), new Vector3f(0.8f,0.8f,0.8f)));
		lights.add(new Light(new Vector3f(185, 15, -293), new Vector3f(2,0,0), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(370, 17, -300), new Vector3f(0,2,2), new Vector3f(1, 0.01f, 0.002f)));
		Light svetielko  =new Light(new Vector3f(293, 7, -300), new Vector3f(2,2,0), new Vector3f(1, 0.01f, 0.002f));
		
		lights.add(svetielko);
		
		
        entities.add(new Entity(lamp, new Vector3f(185, -5f, -293), 0, 0, 0, 1));
		entities.add(new Entity(lamp, new Vector3f(370, 4.2f, -300), 0, 0, 0, 1));
		entities.add(new Entity(lamp, new Vector3f(293, -6.8f, -305), 0, 0, 0, 1));
        
        // height initialization
        for (Entity ent : entities) {
        	float height = terrain.getHeightOfTerrain(ent.getPosition().x, ent.getPosition().z);
        	ent.getPosition().y = height;
        }
        
        //GUIS
        List<GuiTexture> guis = new ArrayList<GuiTexture>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("socuwan"), new Vector2f(0.60f, -0.55f), new Vector2f(0.25f, 0.25f));
        guis.add(gui);
        
        GuiRenderer guiRenderer = new GuiRenderer(loader);
        
        TexturedModel playerTexturedModel = new TexturedModel("person", "playerTexture");
        Player player = new Player(playerTexturedModel, new Vector3f(75,5,-75), 0,  100, 0, 0.6f);
        Camera camera = new Camera(player);
        entities.add(player);
        MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
        
        Entity lampa = new Entity(lamp, new Vector3f(250, 20, -300), 0, 0, 0, 1);
        entities.add(lampa);
        
        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix());
        List<WaterTile> waters = new ArrayList<WaterTile>();
        waters.add(new WaterTile(75, -75, 0));
        
		while (!Display.isCloseRequested()) {
			
			player.move(terrain);
			camera.move();
			
			picker.update();
			
//			Vector3f terrainPoint = picker.getCurrentTerrainPoint();
//			if (terrainPoint != null) {
//				lampa.setPosition(terrainPoint);
//				svetielko.setPosition(new Vector3f(terrainPoint.x, terrainPoint.y + 15, terrainPoint.z));
//			}
			
			renderer.renderScene(entities, terrains, lights, camera);
			waterRenderer.render(waters, camera);
			guiRenderer.render(guis);
			
			DisplayManager.updateDisplay();
		}
		
		waterShader.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
		
	}

}
