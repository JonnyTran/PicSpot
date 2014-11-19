package com.alphaforce.picspot;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private final float[] mProjectionMatrix = new float[16];
	private Triangle mTriangle;
	
	/* 
	 * Called once to set up the view's OpenGL ES environment
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		// Set background color
		GLES30.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
		
	    mTriangle = new Triangle();

	}

	/* 
	 * Called if the geometry of the view changes, for example 
	 * when the device's screen orientation changes.
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES30.glViewport(0, 0, width, height);

		// make adjustments for screen ratio
		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);        // set matrix to projection mode
		gl.glLoadIdentity();                        // reset the matrix to its default state
		gl.glFrustumf(-ratio, ratio, -1, 1, 3, 7);  // apply the projection matrix
	}

	/* 
	 * Called for each redraw of the view
	 */
	@Override
	public void onDrawFrame(GL10 gl) {
		// Redraw background color
		GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

		mTriangle.draw();
	}
	
}
