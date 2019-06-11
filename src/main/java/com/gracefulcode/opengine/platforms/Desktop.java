package com.gracefulcode.opengine.platforms;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWVulkan.*;

import com.gracefulcode.opengine.core.ExtensionConfiguration;
import com.gracefulcode.opengine.core.Platform;
import com.gracefulcode.opengine.renderers.vulkan.Vulkan;

import org.lwjgl.PointerBuffer;

public class Desktop implements Platform<Vulkan> {
	public Desktop() {
		if (!glfwInit()) {
			throw new RuntimeException("Failed to initialize GLFW");
		}		
	}

	public String name() {
		return "Desktop (LWJGL3)";
	}

	public void configureRendererLayers(Vulkan vulkan) {
	}

	public void configureRendererExtensions(Vulkan vulkan) {
		ExtensionConfiguration configuration = vulkan.getExtensionConfiguration();

		PointerBuffer requiredExtensions = glfwGetRequiredInstanceExtensions();
		if (requiredExtensions == null) {
			throw new AssertionError("Failed to find list of required Vulkan extensions");
		}

		for (int i = 0; i < requiredExtensions.limit(); i++) {
			configuration.setExtension(requiredExtensions.getStringUTF8(i), ExtensionConfiguration.RequireType.REQUIRED);
		}
		configuration.setExtension("VK_KHR_swapchain", ExtensionConfiguration.RequireType.REQUIRED);

		System.out.println(configuration);
	}
}
