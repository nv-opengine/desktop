package com.gracefulcode.opengine.platforms.desktop;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWVulkan.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.vulkan.VK10.*;

import com.gracefulcode.opengine.renderers.vulkan.PhysicalDevice;
import com.gracefulcode.opengine.renderers.vulkan.VkInstance;
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
import org.lwjgl.vulkan.VkSurfaceCapabilitiesKHR;

import java.util.Comparator;

/**
 * Represents a single window.
 * <p>
 * A single window in the app can be independently opened and closed, it
 * handles its own rendering, and it chooses its own Physical Device.
 *
 * @author Daniel Grace<dgrace@gracefulcode.com>
 * @version 0.1
 */
public class Window implements com.gracefulcode.opengine.core.Window,  Comparator<PhysicalDevice> {
	/**
	 * The underlying window id.
	 */
	protected long id;

	/**
	 * Hanging onto the VkInstance here lets us do things like (re-)create our
	 * own Surface. VkInstance should be safe, as it should never change.
	 */
	protected VkInstance vkInstance;

	protected PhysicalDevice physicalDevice;

	/**
	 * The surface of this window.
	 */
	protected WindowSurface surface;

	public Window() {
	}

	/**
	 * This comparator is used to determine what physical device is used for
	 * this window if multiple are viable.
	 */
	public int compare(PhysicalDevice a, PhysicalDevice b) {
		VkSurfaceCapabilitiesKHR capabilitiesA = VkSurfaceCapabilitiesKHR.calloc();
		VkSurfaceCapabilitiesKHR capabilitiesB = VkSurfaceCapabilitiesKHR.calloc();

		this.surface.getCapabilities(a, capabilitiesA);
		this.surface.getCapabilities(b, capabilitiesB);

		if (a.isDiscreteGpu() && !b.isDiscreteGpu()) return 1;
		if (!a.isDiscreteGpu() && b.isDiscreteGpu()) return -1;

		capabilitiesA.free();
		capabilitiesB.free();

		return 0;
	}

	public void key(int key, int scancode, int action, int mods) {
		if (action != GLFW_RELEASE) return;

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

	/**
	 * Sets up the connection between this VulkanWindow and a more generic
	 * Window.
	 * <p>
	 * This should only be called once and only from the core system.
	 *
	 * @param vkInstance The Vulkan instance. Used to call vulkan functions to
	 *        do things like set up a surface.
	 * @param id The window id from the underlying system (glfw by way of lwjgl
	 *        in this instance).
	 */
	void setId(VkInstance vkInstance, long id) {
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

		this.surface = new WindowSurface(this.vkInstance.getInstance(), this.id);

		this.physicalDevice = this.vkInstance.getFirst(this);

		System.out.println("Physical Device: " + this.physicalDevice);
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(this.id);
	}

	public void render() {

	}
}