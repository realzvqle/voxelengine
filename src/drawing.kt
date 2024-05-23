import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL30
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer

class Object {
    var vertices: FloatArray = floatArrayOf(
        -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
        0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
        0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
        0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
        -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
        -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,

        -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
        0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
        0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
        0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
        -0.5f, 0.5f, 0.5f, 0.0f, 1.0f,
        -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,

        -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
        -0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
        -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
        -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
        -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
        -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

        0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
        0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
        0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
        0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
        0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
        0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

        -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
        0.5f, -0.5f, -0.5f, 1.0f, 1.0f,
        0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
        0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
        -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
        -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,

        -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
        0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
        0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
        0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
        -0.5f, 0.5f, 0.5f, 0.0f, 0.0f,
        -0.5f, 0.5f, -0.5f, 0.0f, 1.0f
    )

    private var shader: Shader? = null
    private var texture: Textures? = null
    private var isRan: Boolean = false
    private val vertexBuffer: FloatBuffer = MemoryUtil.memAllocFloat(vertices.size).put(vertices).flip()
    private val VBO = IntArray(1)
    private val VAO = IntArray(1)

    fun run() {
        if (!isRan) {
            create()
        }
        texture!!.run()
        shader!!.use()

        val trans = Matrix4f().identity()
        trans.rotate(GLFW.glfwGetTime().toFloat() * Math.toRadians(-55.0).toFloat(), Vector3f(0.5f, 1.0f, 0.0f))
        val view = Matrix4f().identity()
        view.translate(Vector3f(0.0f, 0.0f, -3.0f))

        val projection = Matrix4f().identity()
        projection.perspective(Math.toRadians(45.0).toFloat(), 800.0f / 600.0f, 0.1f, 100.0f)



        val modelLoc: Int = GL30.glGetUniformLocation(shader!!.id, "model")
        MemoryStack.stackPush().use { stack ->
            val buffer = trans.get(stack.mallocFloat(16))
            GL30.glUniformMatrix4fv(modelLoc, false, buffer)
        }

        val viewLoc: Int = GL30.glGetUniformLocation(shader!!.id, "view")
        MemoryStack.stackPush().use { stack ->
            val buffer = view.get(stack.mallocFloat(16))
            GL30.glUniformMatrix4fv(viewLoc, false, buffer)
        }

        val projectionLoc: Int = GL30.glGetUniformLocation(shader!!.id, "projection")
        MemoryStack.stackPush().use { stack ->
            val buffer = projection.get(stack.mallocFloat(16))
            GL30.glUniformMatrix4fv(projectionLoc, false, buffer)
        }

        GL30.glBindVertexArray(VAO[0])
        GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, 36)
    }

    private fun create() {
        texture = Textures("textures\\stone.png")
        shader = Shader("glsl\\vertex.glsl", "glsl\\frag.glsl")
        setupVAO()
        setupVBO()


        setupVertexAttribs()
        isRan = true
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT or GL30.GL_DEPTH_BUFFER_BIT)

        GL30.glEnable(GL30.GL_DEPTH_TEST) // Enable depth testing
        GL30.glDepthFunc(GL30.GL_LESS)    // Specify the depth test function
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

    private fun setupVertexAttribs() {
        GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 5 * Float.SIZE_BYTES, 0L)
        GL30.glEnableVertexAttribArray(0)
        GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT, false, 5 * Float.SIZE_BYTES, (3 * Float.SIZE_BYTES).toLong())
        GL30.glEnableVertexAttribArray(1)
    }
}
