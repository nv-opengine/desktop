package com.gracefulcode.opengine.platforms.desktop;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWVulkan.*;

import com.gracefulcode.opengine.core.ExtensionConfiguration;
import com.gracefulcode.opengine.core.Platform;
import com.gracefulcode.opengine.core.Ternary;
import com.gracefulcode.opengine.renderers.vulkan.Vulkan;

import java.util.ArrayList;

import org.lwjgl.PointerBuffer;

public class Desktop implements Platform<Vulkan> {
	protected Window defaultWindow;
	protected Window.Configuration defaultWindowConfiguration;
	protected ArrayList<Window> otherWindows = new ArrayList<Window>();

	public Desktop(Window.Configuration defaultWindowConfiguration) {
		if (!glfwInit()) {
			throw new RuntimeException("Failed to initialize GLFW");
		}

		if (!glfwVulkanSupported()) {
			throw new AssertionError("GLFW failed to find the Vulkan loader");
		}

		this.defaultWindowConfiguration = defaultWindowConfiguration;
	}

	public String name() {
		return "Desktop (LWJGL3)";
	}

	public Window createWindow(Window window) {
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

		long windowId = glfwCreateWindow(this.defaultWindowConfiguration.width, this.defaultWindowConfiguration.height, this.defaultWindowConfiguration.title, 0, 0);
		window.setId(windowId);

		if (this.defaultWindow == null)
			this.defaultWindow = window;

		glfwShowWindow(windowId);

		return window;
	}

	public void run() {
		while(!this.defaultWindow.shouldClose()) {
			this.defaultWindow.render();
			for (Window w: this.otherWindows) {
				w.render();
			}
			glfwPollEvents();
		}
	}

	public void configureRendererLayers(Vulkan vulkan) {
	}

	public void configureRendererExtensions(ExtensionConfiguration configuration) {
		PointerBuffer requiredExtensions = glfwGetRequiredInstanceExtensions();
		if (requiredExtensions == null) {
			throw new AssertionError("Failed to find list of required Vulkan extensions");
		}

		for (int i = 0; i < requiredExtensions.limit(); i++) {
			configuration.setExtension(requiredExtensions.getStringUTF8(i), Ternary.YES);
		}
		// configuration.setExtension("VK_KHR_swapchain", Ternary.YES);

		System.out.println(configuration);
	}
}
