package de.twometer.modelutil

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import org.lwjgl.assimp.*
import java.io.File

class Animation : CliktCommand(name = "gen-ani", help = "Generate animation files") {

    private val modelFile by argument()
    private val aniFile by argument()

    override fun run() {
        val scene = Assimp.aiImportFile(modelFile, 0)
        val animation = AIAnimation.create(scene!!.mAnimations()!!.get())
        val data = serializeAnimation(animation)
        File(aniFile).writeText(data.toString())
        echo("Animation file was generated successfully")
    }

    private fun serializeAnimation(aiAnimation: AIAnimation): StringBuilder {
        val sb = StringBuilder()
        sb.appendLine(
            "a ${
                aiAnimation.mName().dataString()
            } ${aiAnimation.mTicksPerSecond()} ${aiAnimation.mDuration()}"
        )
        aiAnimation.mChannels()?.also { aiChannels ->
            val aiNumChannels = aiAnimation.mNumChannels()
            for (i in 0 until aiNumChannels) {
                val aiChannel = AINodeAnim.create(aiChannels[i])
                sb.appendLine("c ${aiChannel.mNodeName().dataString()}")

                aiChannel.mPositionKeys()?.also {
                    while (it.hasRemaining()) {
                        val key = it.get()
                        sb.appendLine("p ${key.mTime()} ${key.mValue().serialize()}")
                    }
                }

                aiChannel.mRotationKeys()?.also {
                    while (it.hasRemaining()) {
                        val key = it.get()
                        sb.appendLine("r ${key.mTime()} ${key.mValue().serialize()}")
                    }
                }

                aiChannel.mScalingKeys()?.also {
                    while (it.hasRemaining()) {
                        val key = it.get()
                        sb.appendLine("s ${key.mTime()} ${key.mValue().serialize()}")
                    }
                }
            }
        }
        return sb
    }

    private fun AIVector3D.serialize(): String = "${x()} ${y()} ${z()}"

    private fun AIQuaternion.serialize(): String = "${x()} ${y()} ${z()} ${w()}"

}