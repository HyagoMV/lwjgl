import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.opengl.GL;

public class HelloTriangle4 {

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
		
		Shader shaderProgram = new Shader("3.3.shader.vs", "3.3.shader.fs");

		float[] vertices = {
				// positions         // colors
				0.5f, -0.5f, 0.0f,  1.0f, 0.0f, 0.0f,  // bottom right
			   -0.5f, -0.5f, 0.0f,  0.0f, 1.0f, 0.0f,  // bottom left
				0.0f,  0.5f, 0.0f,  0.0f, 0.0f, 1.0f   // top 
		};
	

		
		int VBO = glGenVertexArrays(); 	// VBO handle
		int VAO = glGenBuffers(); 		// VAO handle
		
		glBindVertexArray(VAO);

		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

		
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
	    glEnableVertexAttribArray(1);

		// Desvincula qualquer objeto anteriormente vinculado
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		// Wireframe mode
//		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		while (!glfwWindowShouldClose(window)) {
			processInput(window);

			glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT);

			shaderProgram.use();
			glBindVertexArray(VAO);
			glDrawArrays(GL_TRIANGLES, 0, 3);

			glfwSwapBuffers(window);
			glfwPollEvents();
		}

		glDeleteVertexArrays(VAO);
		glDeleteBuffers(VBO);

		glfwTerminate();
	}

	static void processInput(long window) {
		// A janela será fechada quando a tecla 'ESC' for pressionada
		if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
			glfwSetWindowShouldClose(window, true);
	}

}