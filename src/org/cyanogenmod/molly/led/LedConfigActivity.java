/*
 * Copyright (C) 2015 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cyanogenmod.molly.led;

import android.app.*;
import android.os.*;
import android.widget.*;
import eu.chainfire.libsuperuser.*;
import java.io.*;
import java.util.*;
import android.view.View.*;
import android.view.*;

public class LedConfigActivity extends Activity
{
	int MAX = 255, MIN = 0;
	int ENABLED = 1, DISABLED = 0;
	//String LED_PATH = "/sys/class/devices/molly-led/";
	// Just testing this on my Nexus 7, will change when done.
	String LED_PATH = "/sys/class/leds/white/brightness";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
	}
}
