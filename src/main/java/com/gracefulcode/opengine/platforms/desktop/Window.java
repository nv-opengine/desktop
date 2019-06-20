package com.gracefulcode.opengine.platforms.desktop;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

public class Window implements com.gracefulcode.opengine.core.Window {
	protected long id;

	public Window(long id) {
		this.id = id;

		GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (action != GLFW_RELEASE) return;

				if (key == GLFW_KEY_ESCAPE) glfwSetWindowShouldClose(window, true);
			}
		};
		glfwSetKeyCallback(this.id, keyCallback);

		GLFWWindowSizeCallback windowSizeCallback = new GLFWWindowSizeCallback() {
			public void invoke(long window, int width, int height) {
				if (width <= 0 || height <= 0) return;

				System.out.println("Window: " + window + ", Width: " + width + ", Height: " + height);

				// ClearScreenDemo.width = width;
				// ClearScreenDemo.height = height;
				// swapchainRecreator.mustRecreate = true;
			}
		};
		glfwSetWindowSizeCallback(this.id, windowSizeCallback);
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(this.id);
	}

	public void render() {

	}
}