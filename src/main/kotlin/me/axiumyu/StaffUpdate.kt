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
        if (item.itemMeta.getEnchantLevel(Enchantment.FEATHER_FALLING) == 60) {
            p0.sendActionBar(text("已经达到最高等级！").color(color(0xffea3a)))
            return false
        }
        var allCost = 0.0
        for (i in 1..upTo) {
            val level = item.itemMeta.getEnchantLevel(Enchantment.FEATHER_FALLING)
            val food = item.itemMeta.getEnchantLevel(Enchantment.PUNCH)
            if (level < 60) {
                xc.changePlayerBalance(p0.uniqueId, p0.name, getUpCost(level).toBigDecimal(), false)
                allCost += getUpCost(level)
                item.editMeta {
                    if (level.mod(3) == 0 && food > 1) {
                        it.addEnchant(Enchantment.PUNCH, food - 1, true)
                    }
                    it.addEnchant(Enchantment.FEATHER_FALLING, level + 1, true)
                }
            } else {
                p0.sendActionBar(text("已经达到最高等级！").color(color(0xffea3a)))
            }
        }
        when (item.itemMeta.getEnchantLevel(Enchantment.FEATHER_FALLING)) {
            15 -> item.editMeta { it.setRarity(ItemRarity.UNCOMMON) }
            30 -> item.editMeta { it.setRarity(ItemRarity.RARE) }
            45 -> item.editMeta { it.setRarity(ItemRarity.EPIC) }
        }
        return true
    }

    fun getUpCost(lvl: Int): Double {
        return min(lvl.toDouble().pow(1.06), 1.25 * lvl.toDouble())
    }
}