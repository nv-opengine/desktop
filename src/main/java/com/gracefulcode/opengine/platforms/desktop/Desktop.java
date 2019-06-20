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

	public Window getDefaultWindow() {
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		
		long window = glfwCreateWindow(this.defaultWindowConfiguration.width, this.defaultWindowConfiguration.height, this.defaultWindowConfiguration.title, 0, 0);

		glfwShowWindow(window);

		this.defaultWindow = new Window(window);

		while(!this.defaultWindow.shouldClose()) {
			this.defaultWindow.render();
			for (Window w: this.otherWindows) {
				w.render();
			}
			glfwPollEvents();
		}

		return null;
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
		configuration.setExtension("VK_KHR_swapchain", Ternary.YES);

		System.out.println(configuration);
	}
}
