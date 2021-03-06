package de.Bethibande.Engine;

import com.sun.istack.internal.Nullable;
import de.Bethibande.Engine.Boot.ArgumentParser;
import de.Bethibande.Engine.Boot.Bootscreen;
import de.Bethibande.Engine.CodeInjection.ClassLoading;
import de.Bethibande.Engine.Entities.FBO;
import de.Bethibande.Engine.Entities.PrefabManager;
import de.Bethibande.Engine.Error.EngineError;
import de.Bethibande.Engine.FileUtils.FileUpdater;
import de.Bethibande.Engine.Fonts.fontMeshCreator.FontType;
import de.Bethibande.Engine.Fonts.fontMeshCreator.MasterFontRenderer;
import de.Bethibande.Engine.Rendering.MasterRenderer;
import de.Bethibande.Engine.Rendering.SpriteLoader;
import de.Bethibande.Engine.utils.*;
import de.Bethibande.Engine.FileUtils.FileUtils;
import lombok.Getter;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector2f;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SuppressWarnings("unused")
public class EngineCore {

    public static final int FULLSCREEN = -0x1;
    public static final int NO_FPS_CAP = -0x2;

    public static final File engine_root = new File("C:/GameEngine/");
    public static final File native_root = new File(engine_root + "/natives/");

    public static File project_root = new File(engine_root + "/projects/%%project%%/");
    public static File project_config;

    public static EngineConfig cfg;
    public static Loader loader = new Loader();

    // default engine font
    public static FontType font;

    public static List<File> handleUpdate = new ArrayList<>();
    public static FileUpdater updater;

    public static Camera cam = new Camera(new Vector2f(0, 0), 0, 1);
    public static Scene2D currentScene = new Scene2D();

    public static DisplayMode fullscreenMode;

    @Getter
    private static boolean initializedDiscord = false;

    public static boolean physicsPaused = false;
    public static boolean devMode = false;

    public static boolean releaseMode = false;

    @Getter
    private static final TextureQuality textureQuality = TextureQuality.GOOD;

    @Getter
    private static final List<Controller> listenToControllers = new ArrayList<>();

    public static float deltaTime = 0;

    public static void initFromConfig(EngineConfig cfg) throws Exception {
        EngineCore.cfg = cfg;
        if(!cfg.fulllscreen) init(cfg.title, cfg.resizable, cfg.vsync, cfg.width, cfg.height);
        if(cfg.fulllscreen) init(cfg.title, cfg.resizable, cfg.vsync, FULLSCREEN, FULLSCREEN);
    }

    private static void init(String title, boolean resizable, boolean vsync, int width, int height) throws Exception {
        if(!GLContext.getCapabilities().GL_EXT_framebuffer_object) {
            Log.logError("FBOs are not supported on this device!");
            stopWithoutExit();
            throw new Exception("Your graphics card has no fbo support!");
        }
        if(width == FULLSCREEN) {
            DisplayManager.createDisplay(title, resizable, vsync);
        } else {
            DisplayManager.createDisplay(title, resizable, vsync, width, height);
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            e.printStackTrace();
            EngineError.openError(e);
        }
        Bootscreen.progress = 200;
        Bootscreen.action = "Load objects..";
        font = new FontType(loader.loadTexture2(engine_root + "/font/sans-serif.png"), new File(engine_root + "/font/sans-serif.fnt"));
        updater = new FileUpdater();
        updater.start();
        handleFileUpdates();
        PrefabManager.loadPrefabs(project_root);

        DisplayManager.loadIcon(cfg.icon);
        Bootscreen.progress = 300;
        Bootscreen.action = "Load default scene..";
        currentScene = FileUtils.loadScene(new File(cfg.defaultScene));
        //FontType font = new FontType(loader.loadTexture2("C:/Bethibande/font.png"), new File("C:/Bethibande/font.fnt"));
        //t = new GUIText("Test", 1.0f, font, new Vector2f(0, 0), 1.0f, true);
        //SpriteSheet sheet = new SpriteSheet(new File("C:/GameEngine/projects/test-project/sprites/Skeleton Walk.png"), 22, 33, 0);
        //sheet.setLoopAnimation(true);
        //sheet.play(20, currentScene.getObjects().get(0));

        //UIElement e = new UIElement(new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f), 0, Colors.black_light);
        //UIMaster.elements.add(e);

        try {
            System.setProperty("net.java.games.input.librarypath", new File(native_root + "/" + OperatingSystem.getOSforLWJGLNatives() + "/").getAbsolutePath());
            Controllers.create();
            if(Controllers.getControllerCount() >= 1) {
                Controller con = Controllers.getController(0);
                listenToControllers.add(con);
                System.out.println("Controller: " + con.getName());
            }
        } catch(LWJGLException e) {
            Log.log("No controllers found");
            e.printStackTrace();
        }

        fullscreenMode = Display.getDesktopDisplayMode();

        Bootscreen.progress = 400;
        Bootscreen.action = "Inject project Classes..";
        Log.log("Inject project classes..");
        ClassLoading.start();
        Log.log("Finished injecting classes! Run main class..");
        Bootscreen.progress = 450;
        Bootscreen.action = "Running main class...";
        for(Class c : ClassLoading.injected) {
            if(c.getName().equals(cfg.mainClass)) {
                try {
                    c.getMethod("main", null).invoke(null, null);
                } catch(Exception ex) {
                    Log.logError("An error occurred while executing the class '" + cfg.mainClass + "'!");
                    EngineError.openError(ex);
                }
            }
        }
        Bootscreen.action = "Started!";
        Bootscreen.progress = 500;
        if(!(ArgumentParser.args.containsKey("--devMode") && ArgumentParser.args.get("--devMode").equals("true"))) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } else devMode = true;
        Log.log("Start rendering! Engine has started!");
        Bootscreen.close();
        MasterRenderer.startRendering();
    }

    public static void setupDiscordRPC(String applicationID) {
        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
            System.out.println("Welcome " + user.username + "#" + user.discriminator + "!");
        }).build();
        DiscordRPC.discordInitialize(applicationID, handlers, true);
        initializedDiscord = true;
    }

    public static void setDiscordRichPresence(String state, String details, @Nullable String bigImage, @Nullable String smallImage, @Nullable String bigImageText, @Nullable String smallImageText) {
        DiscordRichPresence.Builder builder = new DiscordRichPresence.Builder(state);
        builder.setDetails(details);
        builder.setStartTimestamps(Calendar.getInstance().getTimeInMillis()/1000);
        if(bigImage != null && bigImageText != null) {
            builder.setBigImage(bigImage, bigImageText);
        }
        if(smallImage != null && smallImageText != null) {
            builder.setSmallImage(smallImage, smallImageText);
        }
        DiscordRichPresence rp = builder.build();
        DiscordRPC.discordUpdatePresence(rp);
    }

    public static void startExport(File path) throws Exception {
        project_root = path;
        linkNatives();
        try {
            Display.create();
        } catch(LWJGLException e) {
            e.printStackTrace();
        }
        project_config = new File(project_root + "/engine_config.cfg");
        initFromConfig(FileUtils.loadConfig(project_config));
    }

    public static void loadProject(String name) throws Exception {
        project_root = new File(project_root.toString().replace("%%project%%", name));
        if(!engine_root.exists()) {
            engine_root.mkdirs();
            //engine_root.mkdir();
        }
        if(!native_root.exists()) {
            native_root.mkdirs();
            //native_root.mkdir();
        }
        linkNatives();
        try {
            Display.create();
        } catch(LWJGLException e) {
            e.printStackTrace();
        }
        if(!project_root.exists()) {
            project_root.mkdirs();
            //project_root.mkdir();
            FileUtils.createProjectFiles();
        }
        if(ArgumentParser.args.containsKey("--configOverride")) {
            project_config = new File(ArgumentParser.args.get("--configOverride"));
        } else project_config = new File(project_root + "/engine_config.cfg");
        initFromConfig(FileUtils.loadConfig(project_config));
    }

    private static void linkNatives() {
        try {
            String system = OperatingSystem.getOSforLWJGLNatives();
            System.setProperty("org.lwjgl.librarypath", native_root + "\\" + system);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopWithoutExit() {
        Log.log("stopping...!");

        try {
            updater.interrupt();

            DiscordRPC.discordShutdown();
            SpriteLoader.cleanUP();
            FileUtils.saveConfig(cfg, project_config);
            PrefabManager.savePrefabs();
            loader.cleanUp();
            MasterFontRenderer.cleanUp();
            MasterRenderer.getFbos().keySet().forEach(EngineCore::destroyFBO);
            destroyFBO(MasterRenderer.getFinalFBO());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        Log.log("stopping...!");

        try {
            updater.interrupt();

            DiscordRPC.discordShutdown();
            SpriteLoader.cleanUP();
            FileUtils.saveConfig(cfg, project_config);
            PrefabManager.savePrefabs();
            loader.cleanUp();
            MasterFontRenderer.cleanUp();
            MasterRenderer.getFbos().keySet().forEach(EngineCore::destroyFBO);
            destroyFBO(MasterRenderer.getFinalFBO());
        } catch(Exception e) {
            e.printStackTrace();
        }

        Log.log("Good bye!");
        System.exit(0);
    }

    public static void handleFileUpdates() {
        MasterRenderer.gameLogic.add(() -> {
            //System.out.println(getMouseCoords());
            //currentScene.getObjects().get(0).setPosition(getMouseCoords());
            //currentScene.getObjects().get(0).setPosition(new Vector2f(Mouse.getX()/(fullscreenMode.getWidth()/10), Mouse.getY()/(fullscreenMode.getHeight()/10)));
            for(int i = 0; i < handleUpdate.size(); i++) {
                File updated = handleUpdate.get(i);
                if(updated != null) {
                    if (updated.getName().equalsIgnoreCase("engine_config.cfg")) {
                        try {
                            EngineConfig cfg = FileUtils.loadConfig(updated);
                            Display.setTitle(cfg.title);
                            if (cfg.width == FULLSCREEN) {
                                Display.setDisplayMode(fullscreenMode);
                                Display.setFullscreen(true);
                            } else {
                                Display.setDisplayMode(new DisplayMode(cfg.width, cfg.height));
                            }
                            Display.setVSyncEnabled(cfg.vsync);
                            Display.setResizable(cfg.resizable);
                            EngineCore.cfg = cfg;

                            MasterRenderer.renderer.loadProjectionMatrix(Maths.createProjectionMatrix());

                        } catch (LWJGLException e) {
                            Log.logError("Error while reloading engine config!");
                        }
                    }

                    if(updated.getName().endsWith(".prefab")) {
                        PrefabManager.reloadPrefab(updated);
                    }

                    handleUpdate.remove(updated);
                    Log.log("Reloaded '" + updated.getPath() + "'!");
                }
            }
        });
    }

    public static Vector2f getMouseCoords() {
        double normalizedX = (2f*(float)Mouse.getX())/Display.getWidth()-1;
        double normalizedY = (2f*(float)Mouse.getY())/Display.getHeight()-1;
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(Maths.FOV / 2))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = Maths.FAR_PLANE - Maths.NEAR_PLANE;
        normalizedX *= x_scale*4.9f;
        normalizedY *= y_scale*1.57f;
        //System.out.println(x_scale + " " + y_scale + " " + frustum_length + " " + aspectRatio);
        return new Vector2f((float)normalizedX, (float)normalizedY);
    }

    public static void destroyFBO(FBO fbo) {
        GL30.glDeleteFramebuffers(fbo.getFboID());
        GL11.glDeleteTextures(fbo.getColorTextureID());
        GL30.glDeleteRenderbuffers(fbo.getDepthRenderBufferID());
    }

    public enum TextureQuality {
        // GOOD = the original texture size/resolution
        // MIDDLE = 0.75 of the original texture size/resolution
        // LOW = 0.5 of the original texture size/resolution
        GOOD, MIDDLE, LOW
    }

}
