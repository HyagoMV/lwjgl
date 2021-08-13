import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.lwjgl.opengl.GL33C.*;

public class Shader {

	private int id;

	public Shader(String vertexPath, String fragmentPath) {
		File vertexFile = new File(vertexPath);
		File fragmentFile = new File(fragmentPath);

		if (!vertexFile.exists() || !fragmentFile.exists()) {
			System.err.println("Arquivo não encontrado");
			return;
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(vertexFile));
			String vertexCode = "";
			while (true) {
				String line = reader.readLine();
				if (line == null)
					break;
				vertexCode += line + "\n";
			}

			reader = new BufferedReader(new FileReader(fragmentFile));
			String fragmentCode = "";
			while (true) {
				String line = reader.readLine();
				if (line == null)
					break;
				fragmentCode += line + "\n";
			}

			int vertexId = glCreateShader(GL_VERTEX_SHADER);
			glShaderSource(vertexId, vertexCode);
			glCompileShader(vertexId);
			checkCompileErrors(vertexId, "VERTEX");

			int fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
			glShaderSource(fragmentId, fragmentCode);
			glCompileShader(fragmentId);
			checkCompileErrors(fragmentId, "FRAGMENT");

			id = glCreateProgram();
			
			glAttachShader(id, vertexId);
			glAttachShader(id, fragmentId);
			glLinkProgram(id);
			checkCompileErrors(id, "PROGRAM");
			
			glDeleteShader(vertexId);
			glDeleteShader(fragmentId);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void checkCompileErrors(int id, String type) {
		int[] success = new int[1];
		String infoLog = "";

		if (type != "PROGRAM") {
			glGetShaderiv(id, GL_COMPILE_STATUS, success);
			if (success[0] == 0) {
				infoLog = glGetShaderInfoLog(id, 1024);
				System.out.println("ERROR::SHADER_COMPILATION_ERROR of type: " + type + "\n" + infoLog
						+ "\n ------------------------------------------------------- ");
			}
		} else {
			glGetProgramiv(id, GL_LINK_STATUS, success);
			if (success[0] == 0) {
				infoLog = glGetShaderInfoLog(id, 1024);
				System.out.println("ERROR::PROGRAM_LINKING_ERROR of type: " + type + "\n" + infoLog
						+ "\n ------------------------------------------------------- ");
			}
		}
	}

	public void use() {
		glUseProgram(id);
	}
}
