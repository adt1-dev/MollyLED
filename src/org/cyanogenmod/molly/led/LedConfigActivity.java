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

public class LedConfigActivity extends Activity
{
	int MAX = 255, MIN = 0;
	int ENABLED = 1, DISABLED = 0;
	int SEEK_MIN = 20;
	//String LED_PATH = "/sys/class/devices/molly-led/";
	String LED_PATH = "/sys/class/leds/white/brightness";
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
		
		final TextView redValue = (TextView)findViewById(R.id.txtRedValue);
		TextView greenValue = (TextView)findViewById(R.id.txtGreenValue);
		TextView blueValue = (TextView)findViewById(R.id.txtBlueValue);
		SeekBar redSeek = (SeekBar)findViewById(R.id.redSlider);
		
		final MutableString RED = new MutableString(null), GREEN = null, BLUE = null;
		
		try
		{
			// This weird one-liner came from http://stackoverflow.com/a/7449797, plus .trim()
			// to get rid of the newline at the end.
			RED.setValue(new Scanner(new File(LED_PATH)).useDelimiter("\\A").next().trim());
			
		}
		catch (FileNotFoundException e)
		{
			Toast.makeText(getApplicationContext(), "Molly LED driver not properly initialized!", Toast.LENGTH_LONG);
		}
		
		redValue.setText(RED.getValue());
		redSeek.setMax(MAX + SEEK_MIN);
		
		if (!Shell.SU.available())
		{
			Toast.makeText(getApplicationContext(), "This application needs root access to run.", Toast.LENGTH_LONG);
			return;
		}
		
		redSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				@Override public void onStartTrackingTouch(SeekBar p1){}
				@Override public void onStopTrackingTouch(SeekBar p1){}
				@Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
				{
					new BackgroundWriteDevice().execute(Integer.toString(progress));
					RED.setValue(String.valueOf(progress));
				}
			});
    }

	private class BackgroundWriteDevice extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String[] params)
		{
			Shell.SU.run("echo " + params[0] + " > " + LED_PATH);
            return null;
		}

        @Override protected void onPostExecute(String result) {}
        @Override protected void onPreExecute() {}
        @Override protected void onProgressUpdate(Void[] values) {}
    }
}
