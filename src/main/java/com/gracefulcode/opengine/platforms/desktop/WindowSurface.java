package com.gracefulcode.opengine.platforms.desktop;

import static org.lwjgl.glfw.GLFWVulkan.*;
import static org.lwjgl.vulkan.KHRSurface.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.vulkan.VK10.*;

import com.gracefulcode.opengine.renderers.vulkan.PhysicalDevice;
import com.gracefulcode.opengine.renderers.vulkan.Vulkan;

import java.nio.LongBuffer;

import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkSurfaceCapabilitiesKHR;

public class WindowSurface {
	protected VkInstance vkInstance;
	protected long surface;

	public WindowSurface(VkInstance vkInstance, long windowId) {
		this.vkInstance = vkInstance;

		LongBuffer pSurface = memAllocLong(1);
		int err = glfwCreateWindowSurface(this.vkInstance, windowId, null, pSurface);
		this.surface = pSurface.get(0);

		if (err != VK_SUCCESS) {
			throw new AssertionError("Failed to create surface: " + Vulkan.translateVulkanResult(err));
		}
	}

	void getCapabilities(PhysicalDevice physicalDevice, VkSurfaceCapabilitiesKHR capabilities) {
		int err = vkGetPhysicalDeviceSurfaceCapabilitiesKHR(physicalDevice.getPhysicalDevice(), this.surface, capabilities);
		if (err != VK_SUCCESS) {
			throw new AssertionError("Failed to get surface capabilities: " + Vulkan.translateVulkanResult(err));
		}
	}
}