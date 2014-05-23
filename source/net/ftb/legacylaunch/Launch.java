/*
 * This file is part of FTB Launcher.
 *
 * Copyright © 2012-2013, FTB Launcher Contributors <https://github.com/Slowpoke101/FTBLaunch/>
 * FTB Launcher is licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ftb.legacylaunch;

import com.beust.jcommander.JCommander;
import net.ftb.legacylaunch.data.LegacyData;

import java.applet.Applet;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Launch {
    public static LegacyData ld;
public static Logger log = Logger.getLogger("FTBLegacy");

    public static void main (String[] args) {
        ld =new LegacyData(args);
        JCommander jc = new JCommander();
        jc.setAcceptUnknownOptions(true);
        jc.addObject(ld);
        jc.parse(args);
        ld.gameDir = new File(ld.gameDir).getAbsoluteFile().toString().replaceAll("[/\\\\]$", "");
        try {
            System.out.println("Loading jars...");
            ArrayList<File> classPathFiles = new ArrayList<File>();
            File tempDir = new File(new File(ld.gameDir).getParentFile(), "instMods/");
            if (tempDir.isDirectory()) {
                for (String name : tempDir.list()) {
                    if (!name.equalsIgnoreCase(ld.forgeName)) {
                        if (name.toLowerCase().endsWith(".zip") || name.toLowerCase().endsWith(".jar")) {
                            classPathFiles.add(new File(tempDir, name));
                        }
                    }
                }
            }
            classPathFiles.add(new File(tempDir, ld.forgeName));
            classPathFiles.add(new File(ld.mcJar));

            URL[] urls = new URL[classPathFiles.size()];
            for (int i = 0; i < classPathFiles.size(); i++) {
                try {
                    urls[i] = classPathFiles.get(i).toURI().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                System.out.println("Added URL to classpath: " + urls[i].toString());
            }

            System.out.println("Loading natives...");
            String nativesDir = new File(new File(ld.gameDir).getParentFile(), "natives").toString();
            System.out.println("Natives loaded...");

            System.setProperty("org.lwjgl.librarypath", nativesDir);
            System.setProperty("net.java.games.input.librarypath", nativesDir);

            System.setProperty("user.home", new File(ld.gameDir).getParent());

            URLClassLoader cl = new URLClassLoader(urls, Launch.class.getClassLoader());
            System.out.println("Loading minecraft class");
            Class<?> mc = cl.loadClass("net.minecraft.client.Minecraft");
            System.out.println("mc = " + mc);
            Field[] fields = mc.getDeclaredFields();
            System.out.println("field amount: " + fields.length);

            for (Field f : fields) {
                if (f.getType() != File.class) {
                    continue;
                }
                if (0 == (f.getModifiers() & (Modifier.PRIVATE | Modifier.STATIC))) {
                    continue;
                }
                f.setAccessible(true);
                f.set(null, new File(ld.gameDir));
                System.out.println("Fixed Minecraft Path: Field was " + f.toString());
                break;
            }

            String mcDir = mc.getMethod("a", String.class).invoke(null, (Object) "minecraft").toString();

            System.out.println("MCDIR: " + mcDir);

            System.out.println("Launching with applet wrapper...");
            try {
                Class<?> MCAppletClass = cl.loadClass("net.minecraft.client.MinecraftApplet");
                Applet mcappl = (Applet) MCAppletClass.newInstance();
                MinecraftFrame mcWindow = new MinecraftFrame(ld.packName, ld.packImage, ld.animationName);
                mcWindow.start(mcappl, ld.auth_player_name, ld.auth_session);
            } catch (InstantiationException e) {
                log.warning("Applet wrapper failed! Falling back to compatibility mode.");
                mc.getMethod("main", String[].class).invoke(null, (Object) new String[] { ld.auth_player_name, ld.auth_session });
            }
        } catch (Throwable t) {
            log.severe("Unhandled error launching minecraft");
            t.printStackTrace();
        }
    }
}
