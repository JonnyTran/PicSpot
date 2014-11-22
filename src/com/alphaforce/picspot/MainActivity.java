package com.alphaforce.picspot;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;


public class MainActivity extends Activity {

	private SensorManager mSensorManager;
	private GLSurfaceView mGLView;
	private MyGLRenderer mRenderer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get sensor service
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		// Create a GLSurfaceView instance and set it
		// as the ContentView for this Activity
		mGLView = new GLSurfaceView(this);
		
		// Create an OpenGL ES 3.0 context.
		mGLView.setEGLContextClientVersion(3);

		// Set the Renderer for drawing on the GLSurfaceView
		mRenderer = new MyGLRenderer();
		mGLView.setRenderer(mRenderer);
		
		// Render the view continuously
		mGLView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		
		setContentView(mGLView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// The following call pauses the rendering thread.
		// If your OpenGL application is memory intensive,
		// you should consider de-allocating objects that
		// consume significant memory here.
		mGLView.onPause();

		// Unregister sensor
		mSensorManager.unregisterListener(mRenderer);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// The following call resumes a paused rendering thread.
		// If you de-allocated graphic objects for onPause()
		// this is a good place to re-allocate them.
		mGLView.onResume();

		// Register sensors
		mSensorManager.registerListener(
				mRenderer, 
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME );
		mSensorManager.registerListener(
				mRenderer, 
				mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), 
				SensorManager.SENSOR_DELAY_GAME );
	}

}
