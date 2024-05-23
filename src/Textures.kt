import org.lwjgl.opengl.GL30
import org.lwjgl.stb.STBImage
import java.nio.ByteBuffer
import org.joml.Matrix4f;
import org.joml.Vector4f

class Textures(path: String) {
    private var width = IntArray(1)
    private var height = IntArray(1)
    private var nrChannels = IntArray(1)
    private var texture = IntArray(1)

    init {
        STBImage.stbi_set_flip_vertically_on_load(true)
        val data: ByteBuffer? = STBImage.stbi_load(path, width, height, nrChannels, 0)

        if (data != null) {
            val format = if (nrChannels[0] == 4) GL30.GL_RGBA else GL30.GL_RGB
            GL30.glGenTextures(texture)
            GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture[0])
            GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, format, width[0], height[0], 0, format, GL30.GL_UNSIGNED_BYTE, data)
            GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D)
            GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_REPEAT)
            GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, GL30.GL_REPEAT)
            GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR_MIPMAP_LINEAR)
            GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR)
            STBImage.stbi_image_free(data)
        } else {
            throw RuntimeException("Failed to load texture: " + STBImage.stbi_failure_reason())
        }
    }

    fun run() {
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture[0])
    }
}
