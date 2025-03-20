package com.github.ringoame196_s_mcPlugin

import org.bukkit.plugin.java.JavaPlugin
import com.github.ringoame196_s_mcPlugin.commands.Command
import com.github.ringoame196_s_mcPlugin.events.Events

class Main : JavaPlugin() {
    private val plugin = this
    override fun onEnable() {
        super.onEnable()
        server.pluginManager.registerEvents(Events(), plugin)
        val command = getCommand("originalenchant")
        command!!.setExecutor(Command())
    }
}
