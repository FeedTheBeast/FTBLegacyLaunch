package net.ftb.legacylaunch.data;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by progwml6 on 5/22/2014.
 */
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
    public String packName;

    @Parameter(names = {"--animationName"}, description = "name of animation to be used")
    public String animationName;

    @Parameter(names = {"--forgeName"}, description = "name of forge zip/jar")
    public String forgeName;

    @Parameter(names = {"--packImage"}, description = "pack image")
    public String packImage;

}

