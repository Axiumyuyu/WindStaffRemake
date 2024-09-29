package me.axiumyu

import me.axiumyu.Staff.Companion.TAG
import me.axiumyu.WindStaffRemake.Companion.xc
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.TextColor.color
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemRarity
import org.bukkit.persistence.PersistentDataType
import kotlin.math.round
import kotlin.random.Random

class StaffUpdate : CommandExecutor {
    override fun onCommand(
        p0: CommandSender, p1: Command, p2: String,
        p3: Array<out String>?
    ): Boolean {

        if (p0 !is Player) return false
        val item = p0.inventory.itemInMainHand
        if (item.type != Material.STICK) return false
        if (item.persistentDataContainer.get(
                TAG, PersistentDataType.STRING
            ) != Staff.Companion.KEY) return false
        val upTo = p3?.get(0)?.toInt() ?: 1
        if (upTo > 61) {
            p0.sendActionBar(text("最高等级为 61 级！").color(color(0xffea3a)))
            return false
        }
        if (item.itemMeta.getEnchantLevel(Enchantment.FEATHER_FALLING) == 60) {
            p0.sendActionBar(text("已经达到最高等级！").color(color(0xffea3a)))
            return false
        }
        var allCost = 0.0
        val oldLevel = item.itemMeta.getEnchantLevel(Enchantment.FEATHER_FALLING)
        var newLevel = oldLevel
        val oldFood = item.itemMeta.getEnchantLevel(Enchantment.PUNCH)
        var newFood = oldFood
        for (i in 1..upTo) {
            if (newLevel < 61 && xc.getPlayerData(p0.uniqueId).balance >= getUpCost(newLevel).toBigDecimal()) {
                allCost += getUpCost(newLevel)
                if (newLevel.mod(3) == 0 && newFood > 1) {
                    newFood -= 1
                }
                newLevel += 1
            } else if (newLevel >= 61) {
                p0.sendActionBar(text("已经达到最高等级！").color(color(0xffea3a)))
                break
            } else {
                p0.sendActionBar(
                    text(
                        "你的账户余额只够升级到 $newLevel 级！"
                    ).color(color(0xffea3a))
                )
                break
            }
        }
        if (oldLevel != newLevel) {
            xc.changePlayerBalance(p0.uniqueId, p0.name, allCost.toBigDecimal(), false)
            item.editMeta { it.addEnchant(Enchantment.FEATHER_FALLING, newLevel, true) }
        }
        if (oldFood != newFood) {
            item.editMeta { it.addEnchant(Enchantment.PUNCH, newFood, true) }
        }
        when (item.itemMeta.getEnchantLevel(Enchantment.FEATHER_FALLING)) {
            15 -> item.editMeta { it.setRarity(ItemRarity.UNCOMMON) }
            30 -> item.editMeta { it.setRarity(ItemRarity.RARE) }
            45 -> item.editMeta { it.setRarity(ItemRarity.EPIC) }
        }
        p0.sendMessage(text("升级共消耗了 ${xc.getdisplay(allCost.toBigDecimal())} ！").color(color(0x00ff00)))
        return true
    }

    fun getUpCost(lvl: Int): Double {
        return lvl.toDouble() + round(Random.nextDouble() * 2000.0) / 100 - 5.0
    }
}