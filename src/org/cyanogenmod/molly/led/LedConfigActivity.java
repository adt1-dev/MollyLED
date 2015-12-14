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
	String LED_PATH = "/sys/class/leds/white/";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
		
		final LedValue RED = new LedValue(0, "brightness"), GREEN = new LedValue(0, "green"), BLUE = new LedValue(0, "blue");
		
		final SeekBar redSeek = (SeekBar)findViewById(R.id.redSlider);
		Button redPlus = (Button)findViewById(R.id.redPlusButton);
		Button redMinus = (Button)findViewById(R.id.redMinusButton);
		
		redSeek.setMax(MAX);
		
		redSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
				@Override public void onProgressChanged(SeekBar p1, int p2, boolean p3){}
				@Override public void onStartTrackingTouch(SeekBar p1){}
				@Override
				public void onStopTrackingTouch(SeekBar p1)
				{
					RED.setValue(p1.getProgress());
				}
			}
		);
		redPlus.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					RED.setValue(RED.getValue() + 1);
				}
		});
		redMinus.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					RED.setValue(RED.getValue() - 1);
				}
			});
	}

	public class LedValue
	{
		private int variable;
		private String color;
		public LedValue(int value, String proposedColor)
		{
			variable = value;
			color = proposedColor;
		}
		public void setValue(int value)
		{
			if (value <= MAX && value >= 0)
			{
				variable = value;
				new WriteValueInBG().writeToDevice(Integer.toString(variable), color);
				updatePrintedValue(value, color);
			}
		}
		public int getValue()
		{
		    return variable;
		}
		public void setColor(String proposedColor)
		{
			color = proposedColor;
		}
		public String getColor()
		{
		    return color;
		}
	}
	
	private class WriteValueInBG extends AsyncTask<String, Void, String>
	{
		@Override
		protected String writeToDevice(String value, String color)
		{
			// I honestly wish there was a better way than this.
			Shell.SU.run("echo " + value + " > " + LED_PATH + color);
            return null;
		}
		@Override protected String doInBackground(String[] params){return null;}
        @Override protected void onPostExecute(String result){}
        @Override protected void onPreExecute(){}
        @Override protected void onProgressUpdate(Void[] values){}
    }
	
	public void updatePrintedValue(int value, String color)
	{
		TextView redTextView = (TextView)findViewById(R.id.txtRedValue);
		TextView greenTextView = (TextView)findViewById(R.id.txtGreenValue);
		TextView blueTextView = (TextView)findViewById(R.id.txtBlueValue);
		SeekBar redSeek = (SeekBar)findViewById(R.id.redSlider);
		SeekBar greenSeek = (SeekBar)findViewById(R.id.greenSlider);
		SeekBar blueSeek = (SeekBar)findViewById(R.id.blueSlider);
		if (color.contains("brightness"))
		{
			redSeek.setProgress(value);
			redTextView.setText(Integer.toString(value));
		} else
		if (color.contains("green"))
		{
			greenSeek.setProgress(value);
			greenTextView.setText(Integer.toString(value));
		} else
		if (color.contains("blue"))
		{
			blueSeek.setProgress(value);
			blueTextView.setText(Integer.toString(value));
		}
	}
}
