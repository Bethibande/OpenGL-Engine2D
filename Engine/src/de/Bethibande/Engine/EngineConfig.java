package de.Bethibande.Engine;

import de.Bethibande.Engine.utils.Colors;

import java.awt.*;

public class EngineConfig {

    public String title = "TITLE";

    public boolean vsync = false;

    public boolean resizable = false;

    public int width = 500;
    public int height = 500;

    public boolean fulllscreen = false;

    public int FPS = 120;

    public boolean logFPS = true;

    public String icon = "C:/GameEngine/B.png";

    public Color clearColor = Colors.black;

    public String defaultScene = "C:/GameEngine/";

    public String mainClass = "test.Core";

    public String javacPath = "C:\\Program Files (x86)\\Java\\jdk1.8.0_191\\bin\\javac.exe";

}
