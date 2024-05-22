import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL30
import org.lwjgl.system.MemoryUtil
import java.io.File
import java.nio.FloatBuffer
import kotlin.math.sin

class Object {
    private val vertices = floatArrayOf(
        0.5f,  0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        -0.5f,  0.5f, 0.0f
    )
    private var shader: Shader? = null
    private val indices = intArrayOf(
        0, 1, 3,
        1, 2, 3
    )
    private var isRan: Boolean = false
    private val vertexBuffer: FloatBuffer = MemoryUtil.memAllocFloat(vertices.size)
    private var VBO = IntArray(1)
    private var EBO = IntArray(1)
    private var VAO = IntArray(1)
    init {
        println("Here?")
        vertexBuffer.put(vertices).flip()
    }
    fun run() {
        if (!isRan) {
            create()
        }
        shader!!.use()
        GL30.glBindVertexArray(VAO[0])
        GL30.glDrawElements(GL30.GL_TRIANGLES, 6, GL30.GL_UNSIGNED_INT, 0)
    }

    private fun create() {
        setupVAO()
        setupVBO()
        setupEBO()
        shader = Shader("glsl\\vertex.glsl", "glsl\\frag.glsl")
        GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 3 * Float.SIZE_BYTES, 0L)
        GL30.glEnableVertexAttribArray(0)
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

    private fun setupEBO(){
        GL30.glGenBuffers(EBO);
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, EBO[0]);
        GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indices, GL30.GL_STATIC_DRAW);
        //GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, EBO[0])
    }
}
