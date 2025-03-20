package com.github.ringoame196_s_mcPlugin

import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.inventory.ItemStack

class CustomEnchantment(
    key: NamespacedKey,
    private val name: String
) : Enchantment(key) {

    override fun getName(): String = name
    override fun getMaxLevel(): Int = 3 // 最大レベル
    override fun getStartLevel(): Int = 1
    override fun getItemTarget(): EnchantmentTarget = EnchantmentTarget.WEAPON // 適用対象
    override fun isTreasure(): Boolean = false
    override fun isCursed(): Boolean = false
    override fun conflictsWith(other: Enchantment): Boolean = false
    override fun canEnchantItem(item: ItemStack): Boolean = true

    override fun toString(): String {
        return "CustomEnchantment(name='$name', key='$key')"
    }
}
