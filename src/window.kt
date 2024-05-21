import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil.NULL


class window {
    private var window: Long = NULL
    fun run() {
        System.out.println(("Hello LWJGL " + Version.getVersion()).toString() + "!")
        init(1600, 900, "idk")
        loop()
        glfwFreeCallbacks(window)
        glfwDestroyWindow(window)
        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }
    private fun init(x: Int, y: Int, title: String){
        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = glfwCreateWindow(x, y, title, NULL, NULL)
        if(window == NULL) throw RuntimeException("Failed To Create GLFW Window");

        glfwSetKeyCallback(window) { window: Long, key: Int, scancode: Int, action: Int, mods: Int ->
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, true)
        }
        stackPush().use { stack ->
            val pWidth = stack.mallocInt(1) // int*
            val pHeight = stack.mallocInt(1) // int*
            glfwGetWindowSize(window, pWidth, pHeight)
            val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())
            glfwSetWindowPos(
                window,
                (vidmode!!.width() - pWidth[0]) / 2,
                (vidmode!!.height() - pHeight[0]) / 2
            )
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
    }
    private fun loop(){
        GL.createCapabilities();
        glClearColor(1.0f, 1.0f, 0.0f, 0.0f);
        while(!glfwWindowShouldClose(window)){
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

}