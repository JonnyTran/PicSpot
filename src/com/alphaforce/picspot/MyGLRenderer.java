package com.alphaforce.picspot;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;

public class MyGLRenderer implements GLSurfaceView.Renderer, SensorEventListener {

	private float[] gravity = new float[3];
	private float[] geomag = new float[3];
	
	// mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private float[] mMVPMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];
    
	private Triangle mTriangle;
	
	/* 
	 * Called once to set up the view's OpenGL ES environment
	 */
	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {

		// Set the background frame color
		GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
	    mTriangle = new Triangle();
	}

	/* 
	 * Called if the geometry of the view changes, for example 
	 * when the device's screen orientation changes.
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES30.glViewport(0, 0, width, height);

		// Make adjustments for screen ratio
		float ratio = (float) width / height;
		
		// Edit frustum cone
        Matrix.frustumM(mProjectionMatrix, 0, 
        		-ratio, 	// left
        		ratio, 		// right
        		-1, 		// bottom
        		1, 			// top
        		3f, 		// near
        		10f);		// far
	}

	/* 
	 * Called for each redraw of the view
	 */
	@Override
	public void onDrawFrame(GL10 gl) {
		float[] scratch = new float[16];
		
		//apply rotation matrix
		gl.glLoadMatrixf(mRotationMatrix, 0);

		/*set default camera, 2 units of the ground, 
		with the up vector pointing positively on the y axis.*/
		GLU.gluLookAt(gl, 0, 0, 2, 0, 0, 0, 0, 1, 0);
		
		// Redraw background color
		GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

		// Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 
        		0, 		0, 		0, 	// eye
        		0f, 	0f, 	1.0f, 	// center of view
        		0.0f, 	1.0f, 	0.0f);	// up vector

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
		
        // Create rotation matrix
//        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 
//        		1.0f, 	0, 		0);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        mTriangle.draw(scratch);
	}
	
	/**
     * Utility method for compiling a OpenGL shader.
     *
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
	public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES30.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);
        
        return shader;
    }

	@Override
	public void onSensorChanged(SensorEvent event) {
		int type= event.sensor.getType();

		//Smoothing the sensor data a bit
		if (type == Sensor.TYPE_MAGNETIC_FIELD) {
			geomag[0]=(geomag[0]*1+event.values[0])*0.5f;
			geomag[1]=(geomag[1]*1+event.values[1])*0.5f;
			geomag[2]=(geomag[2]*1+event.values[2])*0.5f;
		} else if (type == Sensor.TYPE_ACCELEROMETER) {
			gravity[0]=(gravity[0]*2+event.values[0])*0.33334f;
			gravity[1]=(gravity[1]*2+event.values[1])*0.33334f;
			gravity[2]=(gravity[2]*2+event.values[2])*0.33334f;
		}

		if ((type==Sensor.TYPE_MAGNETIC_FIELD) || (type==Sensor.TYPE_ACCELEROMETER)) {
			mRotationMatrix = new float[16];
			SensorManager.getRotationMatrix(mRotationMatrix, null, gravity, geomag);
			SensorManager.remapCoordinateSystem( 
					mRotationMatrix, 
					SensorManager.AXIS_MINUS_X,
					SensorManager.AXIS_Y,
					mRotationMatrix );
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}
