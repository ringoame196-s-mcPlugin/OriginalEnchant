package com.github.ringoame196_s_mcPlugin.commands

import com.github.ringoame196_s_mcPlugin.Data
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.plugin.Plugin

class Command(private val plugin: Plugin) : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        val player: Player = sender

        val item = player.inventory.itemInMainHand
        if (item.type.isAir) {
            player.sendMessage("アイテムを持ってください！")
            return true
        }

        when (item.type) {
            Material.ENCHANTED_BOOK -> player.inventory.setItemInMainHand(enchantBook())
            else -> itemEnchant(item)
        }

        player.sendMessage("カスタムエンチャントを付与しました！")
        return true

        return true
    }

    private fun enchantBook(): ItemStack {
        val book = ItemStack(Material.ENCHANTED_BOOK)
        val meta = book.itemMeta as? EnchantmentStorageMeta ?: return book

        val enchantRegister = Data.ENCHANT
        meta.addStoredEnchant(enchantRegister, 1, true) // エンチャントを追加
        book.itemMeta = meta

        return book
    }

    private fun itemEnchant(item: ItemStack) {
        val meta = item.itemMeta ?: return
        val enchantRegister = Data.ENCHANT
        meta.addEnchant(enchantRegister, 1, true)
        item.itemMeta = meta
    }

    override fun onTabComplete(commandSender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
        return null
    }
}
