import org.lwjgl.opengl.GL30
import org.lwjgl.system.MemoryUtil
import java.io.File


class triangle {
    private val vertices = floatArrayOf (
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        -0.0f, 0.5f, 0.0f
    )
    val vertexBuffer = MemoryUtil.memAllocInt(vertices.size)
    private var VBO = IntArray(1)

    private fun readFile(path: String): String{
        val fileContent = File(path).readText();
        return fileContent;
    }
    private fun VertexShader(): Int {
        var VertexShader = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
        GL30.glShaderSource(VertexShader, readFile("glsl/vertex.glsl"))
        GL30.glCompileShader(VertexShader);
        return VertexShader;
    }
    private fun FragmentShader() {
        // TODO: Add Fragment Shader
        readFile("glsl/frag.glsl")
    }
    private fun setupVBO(){
        GL30.glGenBuffers(VBO);
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, VBO[0])
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertexBuffer, GL30.GL_STATIC_DRAW)
    }

}