package com.github.ringoame196_s_mcPlugin

import com.github.ringoame196_s_mcPlugin.commands.Command
import com.github.ringoame196_s_mcPlugin.events.Events
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    private val plugin = this
    override fun onEnable() {
        super.onEnable()
        server.pluginManager.registerEvents(Events(), plugin)

        val customEnchantMent = CustomEnchantment(NamespacedKey(plugin, "custom_enchant111"), "Custom Enchant111")
        Data.ENCHANT = customEnchantMent

        val key = customEnchantMent.key

        if (Enchantment.getByKey(key) == null) {
            try {
                val field = Enchantment::class.java.getDeclaredField("acceptingNew")
                field.isAccessible = true
                field.set(null, true) // Bukkit に新しいエンチャントを受け入れさせる

                Enchantment.registerEnchantment(customEnchantMent)
                logger.info("カスタムエンチャント ${key.key} を登録しました！")
            } catch (e: Exception) {
                logger.severe("カスタムエンチャントの登録に失敗しました！")
                e.printStackTrace()
            }
        } else {
            logger.warning("カスタムエンチャント ${key.key} は既に登録されています。")
        }

        val command = getCommand("originalenchant")
        command!!.setExecutor(Command(plugin))
    }
}
