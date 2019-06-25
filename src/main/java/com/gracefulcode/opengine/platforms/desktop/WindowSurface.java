package com.gracefulcode.opengine.platforms.desktop;

import static org.lwjgl.glfw.GLFWVulkan.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.vulkan.VK10.*;

import com.gracefulcode.opengine.renderers.vulkan.Vulkan;

import java.nio.LongBuffer;

import org.lwjgl.vulkan.VkInstance;

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
}