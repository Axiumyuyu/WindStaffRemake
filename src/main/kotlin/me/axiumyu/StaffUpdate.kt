package me.axiumyu

import me.axiumyu.Staff.Companion.FOOD
import me.axiumyu.Staff.Companion.LEVEL
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
import kotlin.math.E
import kotlin.math.exp
import kotlin.math.log
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
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
        for (i in 1..upTo) {
            val level = item.itemMeta.getEnchantLevel(Enchantment.FEATHER_FALLING)
            val food = item.itemMeta.getEnchantLevel(Enchantment.PUNCH)
            if (level < 61 && xc.getPlayerData(p0.uniqueId).balance >= getUpCost(level).toBigDecimal()) {
                xc.changePlayerBalance(p0.uniqueId, p0.name, getUpCost(level).toBigDecimal(), false)
                allCost += getUpCost(level)
                item.editMeta {
                    if (level.mod(3) == 0 && food > 1) {
                        it.addEnchant(Enchantment.PUNCH, food - 1, true)
                    }
                    it.addEnchant(Enchantment.FEATHER_FALLING, level + 1, true)
                }
            } else if (level>= 61) {
                p0.sendActionBar(text("已经达到最高等级！").color(color(0xffea3a)))
            }else{
                p0.sendActionBar(text("你的账户余额只够升级到 $level 级！").color(color(0xffea3a)))
            }
        }
        when (item.itemMeta.getEnchantLevel(Enchantment.FEATHER_FALLING)) {
            15 -> item.editMeta { it.setRarity(ItemRarity.UNCOMMON) }
            30 -> item.editMeta { it.setRarity(ItemRarity.RARE) }
            45 -> item.editMeta { it.setRarity(ItemRarity.EPIC) }
        }
        p0.sendActionBar(text("升级成功！共消耗了 ${xc.getdisplay(allCost.toBigDecimal())} ！").color(color(0x00ff00)))
        return true
    }

    fun getUpCost(lvl: Int): Double {
        return lvl.toDouble() + round(Random.nextDouble() * 2000.0) / 100
    }
}