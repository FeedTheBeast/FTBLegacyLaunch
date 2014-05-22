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
package net.ftb.legacylaunch.mclauncher;

import java.applet.Applet;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.ftb.legacylaunch.util.OSUtils;

public class MinecraftLauncher {
public Logger log = new Logger("FTBLegacy");
    public static void main (String[] args) {
        String basepath = args[0], animationname = args[1], forgename = args[2], username = args[3], password = args[4], modPackName = args[5], modPackImageName = args[6];
        Settings.getSettings().save(); //Call so that the settings file is loaded from the correct location.  Would be wrong on OS X and *nix if called after user.home is reset

        basepath = new File(basepath).getAbsoluteFile().toString().replaceAll("[/\\\\]$", "");

        try {
            System.out.println("Loading jars...");
            String[] jarFiles = new String[] { "minecraft.jar", "lwjgl.jar", "lwjgl_util.jar", "jinput.jar" };
            ArrayList<File> classPathFiles = new ArrayList<File>();
            File tempDir = new File(new File(basepath).getParentFile(), "instMods/");
            if (tempDir.isDirectory()) {
                for (String name : tempDir.list()) {
                    if (!name.equalsIgnoreCase(forgename)) {
                        if (name.toLowerCase().endsWith(".zip") || name.toLowerCase().endsWith(".jar")) {
                            classPathFiles.add(new File(tempDir, name));
                        }
                    }
                }
            }

            classPathFiles.add(new File(tempDir, forgename));
            for (String jarFile : jarFiles) {
                classPathFiles.add(new File(new File(basepath, "bin"), jarFile));
            }

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
            String nativesDir = new File(new File(basepath, "bin"), "natives").toString();
            System.out.println("Natives loaded...");

            System.setProperty("org.lwjgl.librarypath", nativesDir);
            System.setProperty("net.java.games.input.librarypath", nativesDir);

            System.setProperty("user.home", new File(basepath).getParent());

            URLClassLoader cl = new URLClassLoader(urls, MinecraftLauncher.class.getClassLoader());
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
                f.set(null, new File(basepath));
                System.out.println("Fixed Minecraft Path: Field was " + f.toString());
                break;
            }

            String mcDir = mc.getMethod("a", String.class).invoke(null, (Object) "minecraft").toString();

            System.out.println("MCDIR: " + mcDir);

            System.out.println("Launching with applet wrapper...");
            try {
                Class<?> MCAppletClass = cl.loadClass("net.minecraft.client.MinecraftApplet");
                Applet mcappl = (Applet) MCAppletClass.newInstance();
                MinecraftFrame mcWindow = new MinecraftFrame(modPackName, modPackImageName, animationname);
                mcWindow.start(mcappl, username, password);
            } catch (InstantiationException e) {
                log.warning("Applet wrapper failed! Falling back to compatibility mode.", e);
                mc.getMethod("main", String[].class).invoke(null, (Object) new String[] { username, password });
            }
        } catch (Throwable t) {
            log.error("Unhandled error launching minecraft", t);
        }
    }
}
