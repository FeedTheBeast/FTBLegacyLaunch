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

import net.ftb.legacylaunch.Launch;

import java.awt.Dimension;
import java.awt.Point;

@SuppressWarnings("serial")
public class Settings {

    public static int getLastExtendedState () {
        return Integer.valueOf(Launch.ld.lastExtendedState);
    }

    public static Point getLastPosition () {
        return new Point(300, 300);
    }


    public static Dimension getLastDimension () {
        Dimension lastDimension = new Dimension();
        lastDimension.setSize(Double.parseDouble(Launch.ld.width), Double.parseDouble(Launch.ld.height));
        return lastDimension;
    }

}
