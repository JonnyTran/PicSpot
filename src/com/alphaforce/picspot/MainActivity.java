package com.alphaforce.picspot;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;


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
		
		// Render the view only when there is a change in the drawing data
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

	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private float mPreviousX;
	private float mPreviousY;

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		// MotionEvent reports input details from the touch screen
		// and other input controls. In this case, you are only
		// interested in events where the touch position changed.

		float x = e.getX();
		float y = e.getY();

		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:

			float dx = x - mPreviousX;
			float dy = y - mPreviousY;

			// reverse direction of rotation above the mid-line
			if (y > mGLView.getHeight() / 2) {
				dx = dx * -1 ;
			}

			// reverse direction of rotation to left of the mid-line
			if (x < mGLView.getWidth() / 2) {
				dy = dy * -1 ;
			}

			mRenderer.setAngle(
					mRenderer.getAngle() +
					((dx + dy) * TOUCH_SCALE_FACTOR));  // = 180.0f / 320
			mGLView.requestRender();
		}

		mPreviousX = x;
		mPreviousY = y;
		return true;
	}

}
