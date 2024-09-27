package me.axiumyu

import me.axiumyu.Staff.Companion.FOOD
import me.axiumyu.Staff.Companion.LEVEL
import me.axiumyu.Staff.Companion.TAG
import me.axiumyu.WindStaffRemake.Companion.xc
import me.yic.xconomy.api.XConomyAPI
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextColor.color
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import kotlin.math.log
import kotlin.math.log10

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
        val level = item.itemMeta.getEnchantLevel(Enchantment.FEATHER_FALLING)
        if (xc.getPlayerData(p0.name).balance <= getUpCost(level).toBigDecimal()) {
            p0.sendMessage(
                text("升级需要${xc.getdisplay(getUpCost(level).toBigDecimal())} ！").color(
                    color(0xffea3a)
                )
            )
            return false
        }
        xc.changePlayerBalance(p0.uniqueId,p0.name, getUpCost(level).toBigDecimal(), false)
        item.editMeta {
            val food = it.persistentDataContainer.get(FOOD, PersistentDataType.INTEGER)!!
            if (level.mod(5) == 0 && food > 1) {
                it.persistentDataContainer.set(FOOD, PersistentDataType.INTEGER, food - 1)
            }
            it.addEnchant(Enchantment.FEATHER_FALLING, level + 1, true)
            it.persistentDataContainer.set(LEVEL, PersistentDataType.INTEGER, level)
        }
        p0.sendMessage(text("升级成功！").color(color(0xa3fffc)))
        return true
    }

    fun getUpCost(lvl: Int): Double {
        return log10(lvl.toDouble()) + lvl.toDouble() + 5
    }
}