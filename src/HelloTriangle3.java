import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.opengl.GL;

public class HelloTriangle3 {

	private static String vertexShaderSource = """
			#version 330 core
			layout (location = 0) in vec3 aPos;
			void main() {
				gl_Position = vec4(aPos, 1.0);
			}
			""";

	private static String fragmentShaderSource = """
			#version 330 core
			   out vec4 FragColor;
			   uniform vec4 ourColor;
			   void main() {
			   	FragColor = ourColor;
			   };
			""";

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

		// vertex handle
		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShader, vertexShaderSource);
		glCompileShader(vertexShader);

		int[] success = new int[1];
		String infoLog = "";

		glGetShaderiv(vertexShader, GL_COMPILE_STATUS, success);
		if (success[0] == 0) {
			infoLog = glGetShaderInfoLog(vertexShader, 512);
			System.err.println("ERROR::SHADER::VERTEX::COMPILATION_FAILED " + infoLog);
		}

		// fragment handle
		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShader, fragmentShaderSource);
		glCompileShader(fragmentShader);
		glGetShaderiv(fragmentShader, GL_COMPILE_STATUS, success);
		if (success[0] == 0) {
			infoLog = glGetShaderInfoLog(fragmentShader, 512);
			System.err.println("ERROR::SHADER::FRAGMENT::COMPILATION_FAILED " + infoLog);
		}

		
		int shaderProgram = glCreateProgram();	// Shader handle
		glAttachShader(shaderProgram, vertexShader);
		glAttachShader(shaderProgram, fragmentShader);
		glLinkProgram(shaderProgram);
		glGetProgramiv(shaderProgram, GL_LINK_STATUS, success);
		if (success[0] == 0) {
			infoLog = glGetProgramInfoLog(shaderProgram, 512);
			System.err.println("ERROR::SHADER::PROGRAM::LINKING_FAILED\n" + infoLog);
		}

		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);

		float[] vertices = { 
				 0.5f, -0.5f, 0.0f,   // bottom right
		        -0.5f, -0.5f, 0.0f,  // bottom left
		         0.0f,  0.5f, 0.0f   // top 
		};
		
		int VBO = glGenVertexArrays(); 	// VBO handle
		int VAO = glGenBuffers(); 		// VAO handle
		
		glBindVertexArray(VAO);

		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

		glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
		glEnableVertexAttribArray(0);

		// Desvincula qualquer objeto anteriormente vinculado
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		// Wireframe mode
//		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		while (!glfwWindowShouldClose(window)) {
			processInput(window);

			glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT);

			glUseProgram(shaderProgram);
			
			double timeValue = glfwGetTime();
			double greenValue = Math.sin(timeValue) / 2.0 + 0.5;
	        int vertexColorLocation = glGetUniformLocation(shaderProgram, "ourColor");
	        glUniform4f(vertexColorLocation, 0.0f,  (float)  greenValue, 0.0f, 1.0f);
		
			glDrawArrays(GL_TRIANGLES, 0, 3);

			glfwSwapBuffers(window);
			glfwPollEvents();
		}

		glDeleteVertexArrays(VAO);
		glDeleteBuffers(VBO);
		glDeleteProgram(shaderProgram);

		glfwTerminate();
	}

	static void processInput(long window) {
		// A janela será fechada quando a tecla 'ESC' for pressionada
		if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
			glfwSetWindowShouldClose(window, true);
	}

}