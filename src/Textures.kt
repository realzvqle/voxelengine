import org.lwjgl.opengl.GL30
import org.lwjgl.stb.STBImage
import java.nio.ByteBuffer

class Textures(path: String) {
    private var width = IntArray(1)
    private var height = IntArray(1)
    private var nrChannels = IntArray(1)
    var texture = IntArray(1)
    init{
        val data = STBImage.stbi_load("textures\\stone.png", width, height, nrChannels, 0)
        GL30.glGenTextures(texture)
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture[0])
        GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGB, width[0], height[0], 0, GL30.GL_RGB, GL30.GL_UNSIGNED_BYTE, data)
        GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D)
        if (data != null) {
            STBImage.stbi_image_free(data)
        }
    }
}