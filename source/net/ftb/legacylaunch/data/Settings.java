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
package net.ftb.legacylaunch.data;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import net.ftb.legacylaunch.util.OSUtils;
import net.ftb.legacylaunch.util.OSUtils.OS;

@SuppressWarnings("serial")
public class Settings {



    public static int getLastExtendedState () {
        return Integer.valueOf(getProperty("lastExtendedState", String.valueOf(Frame.MAXIMIZED_BOTH)));
    }

    public static Point getLastPosition () {
        Point lastPosition = (Point) getObjectProperty("lastPosition");
        if (lastPosition == null) {
            lastPosition = new Point(300, 300);
        }
        return lastPosition;
    }


    public static Dimension getLastDimension () {
        Dimension lastDimension = (Dimension) getObjectProperty("lastDimension");
        if (lastDimension == null) {
            lastDimension = new Dimension(854, 480);
        }
        return lastDimension;
    }


    public static Object getObjectProperty (String propertyName) {
        return objectFromString(getProperty(propertyName, ""));
    }

    public static Object objectFromString (String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        byte[] data = javax.xml.bind.DatatypeConverter.parseBase64Binary(s);
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            try {
                return ois.readObject();
            } finally {
                ois.close();
            }
        } catch (Exception e) {
            //Logger.logError("Failed to read object from string: " + s, e);
            return null;
        }
    }
    //TODO use jcommander for this!!!
    public static String getProperty(String s, String defVal){
        return defVal;
    }
}
