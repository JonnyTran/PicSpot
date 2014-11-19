package com.alphaforce.picspot;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class MyGLSurfaceView extends GLSurfaceView {

	private final MyGLRenderer mRenderer;

	public MyGLSurfaceView(Context context) {
		super(context);

		// Create an OpenGL ES 3.0 context.
		setEGLContextClientVersion(3);

		// Render the view only when there is a change in the drawing data
//		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        // Set the Renderer for drawing on the GLSurfaceView
		mRenderer = new MyGLRenderer();
		setRenderer(mRenderer);
	}

}
