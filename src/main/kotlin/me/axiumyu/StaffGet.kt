package me.axiumyu

import me.axiumyu.WindStaffRemake.Companion.xc
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.TextColor.color
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.math.BigDecimal

class StaffGet : CommandExecutor {
    override fun onCommand(
        p0: CommandSender, p1: Command, p2: String,
        p3: Array<out String>?
    ): Boolean {
        if (p0 !is Player) return false
        if (p0.inventory.firstEmpty() == -1) {
            p0.sendMessage(text("背包没有空位").color(color(0xffea3a)))
            return false
        }
        if (xc.getPlayerData(p0.name).balance < BigDecimal(200)) {
            p0.sendMessage(text("余额不足，需要${xc.getdisplay(BigDecimal(200))}").color(color(0xffea3a)))
            return false
        }
        xc.changePlayerBalance(p0.uniqueId, p0.name, BigDecimal(200), false)
        p0.inventory.setItem(p0.inventory.firstEmpty(), Staff(p0).staff)
        return true
    }
}