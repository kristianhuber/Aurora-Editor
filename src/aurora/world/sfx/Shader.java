package aurora.world.sfx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import aurora.editor.Editor;

/**
 * @Author: Thin Matrix
 * @Description: Base Shader Class
 * 
 */

public abstract class Shader {

	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	private int fragmentShaderID;
	private int vertexShaderID;
	private int programID;

	/* Constructor Method: Creates and Compiles the Shaders */
	public Shader(String vertexFile, String fragmentFile) {

		this.fragmentShaderID = Shader.loadShader(fragmentFile,
				GL20.GL_FRAGMENT_SHADER);
		this.vertexShaderID = Shader.loadShader(vertexFile,
				GL20.GL_VERTEX_SHADER);

		this.programID = GL20.glCreateProgram();

		GL20.glAttachShader(programID, fragmentShaderID);
		GL20.glAttachShader(programID, vertexShaderID);

		this.bindAttributes();

		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);

		this.getAllUniformLocations();
	}

	/* Binds a Single VAO to the Shader */
	protected void bindAttribute(int attribute, String varName) {

		GL20.glBindAttribLocation(programID, attribute, varName);
	}

	/* Binds All the VAOs to the Shader */
	protected abstract void bindAttributes();

	/* Gets a Single Uniform Variable from the Shader */
	protected int getUniformLocation(String uniformName) {

		return GL20.glGetUniformLocation(programID, uniformName);
	}

	/* Gets All the Uniform Variables from the Shader */
	protected abstract void getAllUniformLocations();

	/* Writes a Float to the Shader */
	protected void loadFloat(int location, float value) {

		GL20.glUniform1f(location, value);
	}

	/* Writes an Int to the Shader */
	protected void loadInt(int location, int value) {

		GL20.glUniform1i(location, value);
	}

	/* Writes a Vector3f to the Shader */
	protected void loadVector(int location, Vector3f vector) {

		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}

	/* Writes a Boolean to the Shader */
	protected void loadBoolean(int location, boolean value) {

		float toLoad = 0;

		if (value) {
			toLoad = 1;
		}

		GL20.glUniform1f(location, toLoad);
	}

	/* Writes a Matrix to the Shader */
	protected void loadMatrix(int location, Matrix4f matrix) {

		matrix.store(matrixBuffer);
		matrixBuffer.flip();

		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}

	/* Loads the Shader Program to LWJGL */
	public void start() {

		GL20.glUseProgram(programID);
	}

	/* Removes the Shader Program to LWJGL */
	public void stop() {

		GL20.glUseProgram(0);
	}

	/* Cleans Up, Deletes the Shaders and Program */
	public void cleanUp() {

		this.stop();

		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);

		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);

		GL20.glDeleteProgram(programID);
	}

	/* Loads and Compiles the Shader */
	private static int loadShader(String file, int type) {

		StringBuilder shaderSource = new StringBuilder();

		// Trys to Load the Shader File
		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					Shader.class.getResourceAsStream("/aurora/" + file)));
			String line;

			// Reads each line
			while ((line = reader.readLine()) != null) {

				shaderSource.append(line).append("\n");
			}

			reader.close();

		} catch (Exception e) {

			Editor.errorFree = false;
			Editor.writeError("Could not read file!");

			e.printStackTrace();

			System.exit(0);
		}

		// Sees if the Shader was Compiled Correctly
		int shaderID = GL20.glCreateShader(type);

		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);

		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {

			Editor.errorFree = false;
			Editor.writeError("Could not compile shader.");

			Editor.writeError(GL20.glGetShaderInfoLog(shaderID, 500));

			System.exit(0);
		}

		return shaderID;
	}
}
