package me.axiumyu

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class StaffGet : CommandExecutor {
    override fun onCommand(
        p0: CommandSender, p1: Command, p2: String,
        p3: Array<out String>?
    ): Boolean {
        if (p0 !is Player) return false
        if (p0.inventory.firstEmpty() == -1) {
            p0.sendMessage("背包已满！")
            return false
        }
        p0.inventory.setItem(p0.inventory.firstEmpty(), Staff(p0).staff)
        return true
    }
}