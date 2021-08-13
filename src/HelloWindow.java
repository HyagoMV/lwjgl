import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33C.*;

import org.lwjgl.opengl.GL;
import static org.lwjgl.system.MemoryUtil.*;

public class HelloWindow {

	public static void main(String[] args) {
		
		glfwInit();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

		if (System.getProperty("os.name").contains("Mac")) {
			 glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		}
		
		// window handle
		long window = glfwCreateWindow(800, 600, "LearnOpenGL", NULL, NULL);
		if (window == NULL) {
			System.err.println("Failed to create GLFW window");
			glfwTerminate();
			System.exit(-1);
		}

		glfwMakeContextCurrent(window);
		GL.createCapabilities(); // LWJGL
		glfwSetFramebufferSizeCallback(window, (_window, width, height) -> {
			glViewport(0, 0, width, height);
		});
		
		while (!glfwWindowShouldClose(window)) {
			processInput(window);
			
			glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT);

			glfwSwapBuffers(window);
			glfwPollEvents();
		}
		
		glfwTerminate();
	}

	static void processInput(long window) {
		// A janela será fechada quando a tecla 'ESC' for pressionada
		if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
			glfwSetWindowShouldClose(window, true);
	}

}