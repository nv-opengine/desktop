package com.gracefulcode.opengine.platforms.desktop;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWVulkan.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.vulkan.VK10.*;

import com.gracefulcode.opengine.renderers.vulkan.Vulkan;

import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.vulkan.VkInstance;

public class Window implements com.gracefulcode.opengine.core.Window {
	protected long id;
	protected VkInstance vkInstance;
	protected WindowSurface surface;

	public Window() {
	}

	public void key(int key, int scancode, int action, int mods) {
		if (action != GLFW_RELEASE) return;

		// System.out.println("In key");

		if (key == GLFW_KEY_ESCAPE) glfwSetWindowShouldClose(this.id, true);
	}

	public void mousePosition(double xpos, double ypos) {
		// System.out.println("Mouse position: " + xpos + ", " + ypos);
	}

	public void resized(int width, int height) {
		if (width <= 0 || height <= 0) return;

		// System.out.println("Width: " + width + ", Height: " + height);

		// ClearScreenDemo.width = width;
		// ClearScreenDemo.height = height;
		// swapchainRecreator.mustRecreate = true;
	}

	public void cursorEnter(boolean entered) {

	}

	public void mouseButton(int button, int action, int mods) {
		System.out.println("Mouse Button: " + button + ", " + action);
	}

	public void scroll(double xoffset, double yoffset) {
		System.out.println("Scroll: " + xoffset + ", " + yoffset);
	}

	public void position(int xpos, int ypos) {
		System.out.println("Position: " + xpos + ", " + ypos);
	}

	public void close() {
		System.out.println("Close!");
	}

	public void setId(VkInstance vkInstance, long id) {
		this.id = id;
		this.vkInstance = vkInstance;

		final Window w = this;
		GLFWKeyCallback kc = new GLFWKeyCallback() {
			public void invoke(long window, int key, int scancode, int action, int mods) {
				w.key(key, scancode, action, mods);
			}
		};
		glfwSetKeyCallback(this.id, kc);

		GLFWCharCallback cc = new GLFWCharCallback() {
			public void invoke(long window, int codepoint) {
				System.out.println("Invoke!");
			}
		};
		glfwSetCharCallback(this.id, cc);

		// 0, 0 is top left
		GLFWCursorPosCallback cpc = new GLFWCursorPosCallback() {
			public void invoke(long window, double xpos, double ypos) {
				w.mousePosition(xpos, ypos);
			}
		};
		glfwSetCursorPosCallback(this.id, cpc);

		GLFWWindowSizeCallback wsc = new GLFWWindowSizeCallback() {
			public void invoke(long window, int width, int height) {
				w.resized(width, height);
			}
		};
		glfwSetWindowSizeCallback(this.id, wsc);

		GLFWCursorEnterCallback cec = new GLFWCursorEnterCallback() {
			public void invoke(long window, boolean entered) {
				w.cursorEnter(entered);
			}
		};
		glfwSetCursorEnterCallback(this.id, cec);

		GLFWMouseButtonCallback mbc = new GLFWMouseButtonCallback() {
			public void invoke(long window, int button, int action, int mods) {
				w.mouseButton(button, action, mods);
			}
		};
		glfwSetMouseButtonCallback(this.id, mbc);

		GLFWScrollCallback sc = new GLFWScrollCallback() {
			public void invoke(long window, double xoffset, double yoffset) {
				w.scroll(xoffset, yoffset);
			}
		};
		glfwSetScrollCallback(this.id, sc);

		GLFWWindowPosCallback wpc = new GLFWWindowPosCallback() {
			public void invoke(long window, int xpos, int ypos) {
				w.position(xpos, ypos);
			}
		};
		glfwSetWindowPosCallback(this.id, wpc);

		GLFWWindowCloseCallback wcc = new GLFWWindowCloseCallback() {
			public void invoke(long window) {
				w.close();
			}
		};
		glfwSetWindowCloseCallback(this.id, wcc);

		this.surface = new WindowSurface(this.vkInstance, this.id);
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(this.id);
	}

	public void render() {

	}
}