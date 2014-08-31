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
package net.ftb.legacylaunch.util;


public class OSUtils {

    public static enum OS {
        WINDOWS, UNIX, MACOSX, OTHER,
    }

    /**
     * Used to get the java delimiter for current OS
     * @return string containing java delimiter for current OS
     */
    public static String getJavaDelimiter () {
        switch (getCurrentOS()) {
        case WINDOWS:
            return ";";
        case UNIX:
            return ":";
        case MACOSX:
            return ":";
        default:
            return ";";
        }
    }

    /**
     * Used to get the current operating system
     * @return OS enum representing current operating system
     */
    public static OS getCurrentOS () {
        String osString = System.getProperty("os.name").toLowerCase();
        if (osString.contains("win")) {
            return OS.WINDOWS;
        } else if (osString.contains("nix") || osString.contains("nux")) {
            return OS.UNIX;
        } else if (osString.contains("mac")) {
            return OS.MACOSX;
        } else {
            return OS.OTHER;
        }
    }

    /**
     * Used to get check if JVM is 64-bit
     * @return true if 64-bit JVM
     */
    public static Boolean is64BitVM() {
        Boolean bits64;
           bits64 = System.getProperty("sun.arch.data.model").equals("64");
        return bits64;
    }

}
