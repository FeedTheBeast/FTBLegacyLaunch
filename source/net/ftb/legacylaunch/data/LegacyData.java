package net.ftb.legacylaunch.data;

import com.beust.jcommander.Parameter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class LegacyData {
   String[] args;
   public String auth_player_name;
   public String auth_session;

   public LegacyData (String[] args){
       this.args = args;
       auth_player_name = args[0];
       auth_session = args[1];
   }

    // modPackImageName = args[6];
    @Parameter
    private List<String> params = new ArrayList<String>();

    @Parameter(names = {"--gameDir"}, description = "Game directory")
    public String gameDir;

    @Parameter(names = {"--assetsDir"}, description = "Assets directory(virtual folder)")
    public String assetsDir;

    @Parameter(names = {"--packName"}, description = "name of modpack")
    public String packName = "Minecraft";

    @Parameter(names = {"--animationName"}, description = "name of animation to be used")
    public String animationName;//TODO make sure that this being missing isn't fatal!!!

    @Parameter(names = {"--forgeName"}, description = "name of forge zip/jar")
    public String forgeName;//TODO make sure that this being missing isn't fatal!!!

    @Parameter(names = {"--packImage"}, description = "pack image(icon)")
    public String packImage;//TODO make sure that this being missing isn't fatal!!!

    @Parameter(names = {"--lastExtendedState"}, description = "Extended State from settings")
    public String lastExtendedState = String.valueOf(Frame.MAXIMIZED_BOTH);

    @Parameter(names = {"--width"}, description = "Width of applet(MUST BE PARSEABLE TO A Double)")
    public String width = String.valueOf(854);

    @Parameter(names = {"--height"}, description = "Height of applet(MUST BE PARSEABLE TO A Double)")
    public String height = String.valueOf(480);

    @Parameter(names = {"--mcJar"}, description = "Location of MC Jar (if you have jar mods remove meta-inf!!)")
    public String mcJar;

}

