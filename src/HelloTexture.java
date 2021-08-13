import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class HelloTexture {

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

		Shader shaderProgram = new Shader("4.1.shader.vs", "4.1.shader.fs");

		float[] vertices = {
				// positions          // colors           // texture coords
		         0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f,   1.0f, 1.0f, // top right
		         0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f,   1.0f, 0.0f, // bottom right
		        -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f, // bottom left
		        -0.5f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f,   0.0f, 1.0f  // top left 
		};

		int[] indices = { 0, 1, 3, // first triangle
				1, 2, 3 // second triangle
		};

		int VAO = glGenVertexArrays(); // VAO handle
		int VBO = glGenBuffers(); // VBO handle
		int EBO = glGenBuffers(); // EBO handle

		glBindVertexArray(VAO);

		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

		// position attribute
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
		glEnableVertexAttribArray(0);

		// color attribute
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
		glEnableVertexAttribArray(1);

		// texture coord attribute
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
		glEnableVertexAttribArray(2);

		// Desvincula qualquer objeto anteriormente vinculado
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		int textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		try {
			PNGDecoder decoder = new PNGDecoder(new FileInputStream(new File("wall.png")));
			ByteBuffer buffer = ByteBuffer.allocateDirect(3 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buffer, decoder.getWidth() * 3, Format.RGB);
			buffer.flip();
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, 512, 512, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer);
			glGenerateMipmap(GL_TEXTURE_2D);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Wireframe mode
//		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		while (!glfwWindowShouldClose(window)) {
			processInput(window);

			glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT);

			glBindTexture(GL_TEXTURE_2D, textureID);

			shaderProgram.use();
			glBindVertexArray(VAO);
			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

			glfwSwapBuffers(window);
			glfwPollEvents();
		}

		glDeleteVertexArrays(VAO);
		glDeleteBuffers(VBO);
		glDeleteBuffers(EBO);

		glfwTerminate();
	}

	static void processInput(long window) {
		// A janela será fechada quando a tecla 'ESC' for pressionada
		if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
			glfwSetWindowShouldClose(window, true);
	}

}