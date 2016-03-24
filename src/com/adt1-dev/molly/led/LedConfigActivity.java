/*
 * Copyright (C) 2015 The ADT-1 Development Project
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

package com.adt1-dev.molly.led;

import android.app.*;
import android.os.*;
import android.util.Log;
import android.widget.*;
import eu.chainfire.libsuperuser.*;
import java.io.*;
import java.util.*;
import android.view.View.*;
import android.view.*;

public class LedConfigActivity extends Activity
{
	int MAX = 255;
	int ENABLED = 1, DISABLED = 0;
	String LED_PATH = "/sys/devices/platform/molly-led/";
	int RED_VALUE = Integer.parseInt(readOneLine(LED_PATH + "red"));
	int GREEN_VALUE = Integer.parseInt(readOneLine(LED_PATH + "green"));
	int BLUE_VALUE = Integer.parseInt(readOneLine(LED_PATH + "blue"));
	int PULSING_VALUE = Integer.parseInt(readOneLine(LED_PATH + "pulsing"));
    private static final String TAG = "MollyLED";

    /**
     * Reads the first line of text from the given file
     */
    public static String readOneLine(String fileName) {
        String line = null;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(fileName), 512);
            line = reader.readLine();
        } catch (IOException e) {
            Log.e(TAG, "Could not read from file " + fileName, e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                // ignored, not much we can do anyway
            }
        }

        return line;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
		Log.i(TAG, "RED: " + RED_VALUE + " GREEN: " + GREEN_VALUE + " BLUE: " + BLUE_VALUE);
		if (RED_VALUE == 255) {
			RED_VALUE = 254;
		}
		if (GREEN_VALUE == 255) {
			GREEN_VALUE = 254;
		}
		if (BLUE_VALUE == 255) {
			BLUE_VALUE = 254;
		}
		final LedValue RED = new LedValue(RED_VALUE, "red"),
				GREEN = new LedValue(GREEN_VALUE, "green"), BLUE = new LedValue(BLUE_VALUE, "blue");

		updatePrintedValue(RED_VALUE, "red");
		updatePrintedValue(GREEN_VALUE, "green");
		updatePrintedValue(BLUE_VALUE, "blue");
		final SeekBar redSeek = (SeekBar)findViewById(R.id.redSlider);
		final SeekBar greenSeek = (SeekBar)findViewById(R.id.greenSlider);
		final SeekBar blueSeek = (SeekBar)findViewById(R.id.blueSlider);
		Button redPlus = (Button)findViewById(R.id.redPlusButton);
		Button redMinus = (Button)findViewById(R.id.redMinusButton);
		Button greenPlus = (Button)findViewById(R.id.greenPlusButton);
		Button greenMinus = (Button)findViewById(R.id.greenMinusButton);
		Button bluePlus = (Button)findViewById(R.id.bluePlusButton);
		Button blueMinus = (Button)findViewById(R.id.blueMinusButton);

		redSeek.setMax(MAX);
		redSeek.setProgress(RED_VALUE);
		greenSeek.setMax(MAX);
		greenSeek.setProgress(GREEN_VALUE);
		blueSeek.setMax(MAX);
		blueSeek.setProgress(BLUE_VALUE);

		redSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
				@Override public void onProgressChanged(SeekBar p1, int p2, boolean p3){
					RED.setValue(p1.getProgress());
				}
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
		greenSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
				@Override public void onProgressChanged(SeekBar p1, int p2, boolean p3){
					GREEN.setValue(p1.getProgress());
				}
				@Override public void onStartTrackingTouch(SeekBar p1){}
				@Override
				public void onStopTrackingTouch(SeekBar p1)
				{
					GREEN.setValue(p1.getProgress());
				}
			}
		);
		greenPlus.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					GREEN.setValue(GREEN.getValue() + 1);
				}
		});
		greenMinus.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					GREEN.setValue(GREEN.getValue() - 1);
				}
			});
		blueSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
				@Override public void onProgressChanged(SeekBar p1, int p2, boolean p3){
					BLUE.setValue(p1.getProgress());
				}
				@Override public void onStartTrackingTouch(SeekBar p1){}
				@Override
				public void onStopTrackingTouch(SeekBar p1)
				{
					BLUE.setValue(p1.getProgress());
				}
			}
		);
		bluePlus.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					BLUE.setValue(BLUE.getValue() + 1);
				}
		});
		blueMinus.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					BLUE.setValue(BLUE.getValue() - 1);
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
		protected String writeToDevice(String value, String color)
		{
			// I honestly wish there was a better way than this.
			Shell.SU.run("echo " + value + " > " + LED_PATH + color);
			// Assume that if we are changing value here we want to see the changes
			if (PULSING_VALUE == 1) {
				Shell.SU.run("echo " + "0" + " > " + LED_PATH + "pulsing");
			}
			Log.i(TAG, "echo " + value + " > " + LED_PATH + color);
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
		if (color.contains("red"))
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
