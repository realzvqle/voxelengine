import org.lwjgl.opengl.GL30
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import java.nio.IntBuffer

class Object {
    private val vertices = floatArrayOf(
        // Positions        // Colors          // Texture Coords
        0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f,   1.0f, 1.0f,   // top right
        0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f,   1.0f, 0.0f,   // bottom right
        -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f,   // bottom left
        -0.5f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f,   0.0f, 1.0f    // top left
    )

    private val indices = intArrayOf(
        0, 1, 3,
        1, 2, 3
    )

    private var shader: Shader? = null
    private var texture: Textures? = null
    private var isRan: Boolean = false
    private val vertexBuffer: FloatBuffer = MemoryUtil.memAllocFloat(vertices.size).put(vertices).flip()
    private val indexBuffer: IntBuffer = MemoryUtil.memAllocInt(indices.size).put(indices).flip()
    private val VBO = IntArray(1)
    private val EBO = IntArray(1)
    private val VAO = IntArray(1)

    fun run() {
        if (!isRan) {
            create()
        }
        shader!!.use()
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture!!.texture[0])
        GL30.glBindVertexArray(VAO[0])
        GL30.glDrawElements(GL30.GL_TRIANGLES, indices.size, GL30.GL_UNSIGNED_INT, 0)
    }

    private fun create() {
        setupVAO()
        setupVBO()
        setupEBO()
        shader = Shader("glsl/vertex.glsl", "glsl/frag.glsl")
        texture = Textures("textures/stone.png")
        setupVertexAttribs()
        isRan = true
    }

    private fun setupVBO() {
        GL30.glGenBuffers(VBO)
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, VBO[0])
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertexBuffer, GL30.GL_STATIC_DRAW)
    }

    private fun setupVAO() {
        GL30.glGenVertexArrays(VAO)
        GL30.glBindVertexArray(VAO[0])
    }

    private fun setupEBO() {
        GL30.glGenBuffers(EBO)
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, EBO[0])
        GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL30.GL_STATIC_DRAW)
    }

    private fun setupVertexAttribs() {
        GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 8 * Float.SIZE_BYTES, 0L)
        GL30.glEnableVertexAttribArray(0)
        GL30.glVertexAttribPointer(1, 3, GL30.GL_FLOAT, false, 8 * Float.SIZE_BYTES, (3 * Float.SIZE_BYTES).toLong())
        GL30.glEnableVertexAttribArray(1)
        GL30.glVertexAttribPointer(2, 2, GL30.GL_FLOAT, false, 8 * Float.SIZE_BYTES, (6 * Float.SIZE_BYTES).toLong())
        GL30.glEnableVertexAttribArray(2)
    }
}