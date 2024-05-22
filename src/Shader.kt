import org.lwjgl.opengl.GL30
import java.io.File


class Shader(vertexPath: String, fragmentPath: String) {

    public var id: Int = 0
    private var vertexCode = ""
    private var fragmentCode = ""
    private var vertex = 0
    private var fragment = 0
    private var success = IntArray(1)
    init {
        vertexCode = readFile(vertexPath)
        fragmentCode = readFile(fragmentPath)
        vertex = GL30.glCreateShader(GL30.GL_VERTEX_SHADER)
        GL30.glShaderSource(vertex, vertexCode)
        GL30.glCompileShader(vertex)

        GL30.glGetShaderiv(vertex, GL30.GL_COMPILE_STATUS, success)
        if(success[0] == GL30.GL_FALSE){
            val infoLog = GL30.glGetShaderInfoLog(vertex)
            throw RuntimeException(infoLog)
        }

        fragment = GL30.glCreateShader(GL30.GL_FRAGMENT_SHADER)
        GL30.glShaderSource(fragment, fragmentCode)
        GL30.glCompileShader(fragment)
        GL30.glGetShaderiv(fragment, GL30.GL_COMPILE_STATUS, success)
        if(success[0] == GL30.GL_FALSE){
            val infoLog = GL30.glGetShaderInfoLog(fragment)
            throw RuntimeException(infoLog)
        }
        id = GL30.glCreateProgram()
        GL30.glAttachShader(id, vertex)
        GL30.glAttachShader(id, fragment)
        GL30.glLinkProgram(id)
        GL30.glGetProgramiv(id, GL30.GL_LINK_STATUS, success)
        if(success[0] == GL30.GL_FALSE){
            val infoLog = GL30.glGetShaderInfoLog(id)
            throw RuntimeException(infoLog)
        }
        GL30.glDeleteShader(vertex)
        GL30.glDeleteShader(fragment)
    }
    private fun readFile(path: String): String {
        try{
            val result: String = File(path).readText()
            return result;
        }
        catch (e: Exception){
            throw RuntimeException(e)
        }

    }
    fun use(){
        GL30.glUseProgram(id)
    }
    fun setBool(name: String, value: Int){
        GL30.glUniform1i(GL30.glGetUniformLocation(id, name), value)
    }
    fun setInt(name: String, value: Int){
        GL30.glUniform1i(GL30.glGetUniformLocation(id, name), value)
    }
    fun setFloat(name: String, value: Float){
        GL30.glUniform1f(GL30.glGetUniformLocation(id, name), value)
    }
}