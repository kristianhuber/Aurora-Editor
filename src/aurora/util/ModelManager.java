package aurora.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import aurora.editor.Editor;
import aurora.world.entities.RawModel;

/**
 * @Author: Thin Matrix
 * @Description: Registers Textures and Models
 * 
 */

public class ModelManager {

	private static HashMap<String, RawModel> models = new HashMap<String, RawModel>();
	private static List<Integer> vaos = new ArrayList<Integer>();
	private static List<Integer> vbos = new ArrayList<Integer>();

	/* Registers the 2D Object into Memory */
	public static void loadToVao(String ID, float[] positions) {

		int vaoID = createVAO();

		ModelManager.storeDataInAttributeList(0, 2, positions);

		GL30.glBindVertexArray(0);

		ModelManager.models.put(ID, new RawModel(vaoID, positions.length / 2));
	}

	/* Registers the 3D Object into memory */
	public static void loadToVao(String ID, float[] positions,
			float[] textureCoords, float[] normals, int[] indices) {

		int vaoID = createVAO();

		ModelManager.bindIndicesBuffer(indices);

		ModelManager.storeDataInAttributeList(0, 3, positions);
		ModelManager.storeDataInAttributeList(1, 2, textureCoords);
		ModelManager.storeDataInAttributeList(2, 3, normals);

		GL30.glBindVertexArray(0);

		ModelManager.models.put(ID, new RawModel(vaoID, indices.length));
	}

	public static RawModel getModel(String ID) {

		return ModelManager.models.get(ID);
	}

	/* Creates a Unique VAO ID */
	private static int createVAO() {

		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);

		GL30.glBindVertexArray(vaoID);

		return vaoID;
	}

	/* Stores Data in VBOs */
	private static void storeDataInAttributeList(int attributeNumber,
			int coordinateSize, float data[]) {

		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);

		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

		GL20.glVertexAttribPointer(attributeNumber, coordinateSize,
				GL11.GL_FLOAT, false, 0, 0);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	/* Takes Care of Storing Data */
	private static void bindIndicesBuffer(int[] indicies) {

		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indicies);

		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer,
				GL15.GL_STATIC_DRAW);
	}

	/* Stores Data in an Integer Buffer */
	private static IntBuffer storeDataInIntBuffer(int[] data) {

		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);

		buffer.flip();

		return buffer;
	}

	/* Stores Data in a Float Buffer */
	private static FloatBuffer storeDataInFloatBuffer(float[] data) {

		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);

		buffer.put(data);
		buffer.flip();

		return buffer;
	}

	/* Loads the RawModel, Author: Thin Matrix */
	public static void loadModel(String fileName) {

		// Variables
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();

		float[] verticesArray = null;
		float[] normalArray = null;
		float[] textureArray = null;
		int[] indicesArray = null;

		// Loads the File
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				ModelManager.class
						.getResourceAsStream("/aurora/resources/models/"
								+ fileName + ".obj")));

		String line;

		// Tries to Read the File
		try {

			// Reads until it gets to the Faces
			while (true) {

				// Read a Line
				line = reader.readLine();
				String[] currentLine = line.split(" ");

				// Reads Vertices
				if (line.startsWith("v ")) {

					// Creates a new Vertex and Adds it to the Array
					Vector3f vertex = new Vector3f(
							Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3]));

					vertices.add(vertex);

					// Reads the UV Coordinates
				} else if (line.startsWith("vt ")) {

					// Creates a new Vertex and Adds it to the Array
					Vector2f texture = new Vector2f(
							Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]));

					textures.add(texture);

					// Reads the Normals and Adds it to the Array
				} else if (line.startsWith("vn ")) {

					// Creates a new Vertex and Adds it to the Array
					Vector3f normal = new Vector3f(
							Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3]));

					normals.add(normal);

					// Ends the Readings
				} else if (line.startsWith("f ")) {

					// Initializes the Arrays and Continues
					textureArray = new float[vertices.size() * 2];
					normalArray = new float[vertices.size() * 3];

					break;
				}
			}

			// Reads the Faces and Processes
			while (line != null) {

				// Checks to Make Sure its Reading the Faces
				if (!line.startsWith("f ")) {

					line = reader.readLine();
					continue;
				}

				// Variables
				String[] currentLine = line.split(" ");

				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");

				// Processes Vertices
				ModelManager.processVertex(vertex1, indices, textures, normals,
						textureArray, normalArray);
				ModelManager.processVertex(vertex2, indices, textures, normals,
						textureArray, normalArray);
				ModelManager.processVertex(vertex3, indices, textures, normals,
						textureArray, normalArray);

				// Reads the Line
				line = reader.readLine();
			}

			reader.close();
		} catch (Exception e) {

			Editor.errorFree = false;
			Editor.writeError("Could Not Read File For " + fileName);
		}

		// Initializes Arrays
		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()];

		// Converts from Vectors to Floats
		int vertexPointer = 0;

		for (Vector3f vertex : vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}

		for (int i = 0; i < indices.size(); i++) {

			indicesArray[i] = indices.get(i);
		}

		// Stores the Data
		ModelManager.loadToVao(fileName, verticesArray, textureArray,
				normalArray, indicesArray);
	}

	/* Processes the Vertex, Author: Thin Matrix */
	private static void processVertex(String[] vertexData,
			List<Integer> indices, List<Vector2f> textures,
			List<Vector3f> normals, float[] textureArray, float[] normalsArray) {

		// Keeps the Files Organized
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;

		indices.add(currentVertexPointer);

		// Texture Decoding
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);

		textureArray[currentVertexPointer * 2] = currentTex.x;
		textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;

		// Normals Decoding
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);

		normalsArray[currentVertexPointer * 3] = currentNorm.x;
		normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
		normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
	}

	/* Deletes All the Memory from the Game */
	public static void cleanUp() {

		for (int vao : vaos) {

			GL30.glDeleteVertexArrays(vao);
		}

		for (int vbo : vbos) {

			GL15.glDeleteBuffers(vbo);
		}
	}
}
