package me.axiumyu

import me.axiumyu.Staff.Companion.KEY
import me.axiumyu.Staff.Companion.OWNER
import me.axiumyu.Staff.Companion.TAG
import me.axiumyu.WindStaffRemake.Companion.xc
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.TextColor.color
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.Enchantment.FEATHER_FALLING
import org.bukkit.enchantments.Enchantment.PUNCH
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import java.math.BigDecimal

object Rebind : CommandExecutor{
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        if(p3.isEmpty()) {
            p0.sendMessage(text().content("请将原物品放在副手,将新物品放在主手,重新绑定将花费200纯净结晶").color(color(0xa3fffc)))

            p0.sendMessage(text().content("重新绑定将会清除新物品的所有附魔,Lore,稀有度,最大堆叠数,并将物品数量重置为1,请确认主手物品不是重要物品!!!").decorate(TextDecoration.BOLD).color(color(0xF05179))
                .appendNewline()
                .append(text().content("是否继续?").color(color(0xF05179))))
            p0.sendMessage(text().content("如果确认请输入/rebind confirm以继续").color(color(0xa3fffc)))
            return true
        }
        if(p3[0] == "confirm") {
            if (p0 !is Player) return false
            if (xc.getPlayerData(p0.name).balance< BigDecimal(200)) {
                p0.sendMessage(text().content("你没有足够的纯净结晶").color(color(0xa3fffc)))
                return false
            }

            val oldItem = p0.inventory.itemInOffHand
            val newItem = p0.inventory.itemInMainHand
            if (oldItem.isEmpty || newItem.isEmpty) {
                p0.sendMessage(text().content("请将原物品放在副手,将新物品放在主手").color(color(0xa3fffc)))
                return false
            }
            val oldPDC = oldItem.itemMeta.persistentDataContainer
            val newPDC = newItem.itemMeta.persistentDataContainer
            if (!oldPDC.has(TAG, PersistentDataType.STRING) || oldPDC.get(TAG, PersistentDataType.STRING) != KEY) {
                p0.sendMessage(text().content("副手物品不是一个合法的风之法杖").color(color(0xa3fffc)))
                return false
            }
            if (newPDC.has(TAG)){
                p0.sendMessage(text().content("主手物品具有其他功能,不可绑定到该物品").color(color(0xa3fffc)))
                return false
            }
            if (!oldPDC.has(OWNER, PersistentDataType.STRING) || oldPDC.get(OWNER, PersistentDataType.STRING) != p0.name) {
                p0.sendMessage(text().content("副手物品不属于你").color(color(0xa3fffc)))
                return false
            }
            val enchs = oldItem.enchantments.filter { it.key== PUNCH || it.key == FEATHER_FALLING }
            if (enchs.size!= 2) {
                p0.sendMessage(text().content("副手物品附魔不正确").color(color(0xa3fffc)))
                return false
            }

            oldItem.itemMeta.persistentDataContainer.remove(TAG)
            oldItem.itemMeta.persistentDataContainer.remove(OWNER)
            oldItem.lore(listOf())
            oldItem.removeEnchantment(PUNCH)
            oldItem.removeEnchantment(FEATHER_FALLING)
            p0.inventory.setItemInMainHand(Staff(p0, newItem, enchs).staff)
            xc.changePlayerBalance(p0.uniqueId,p0.name, BigDecimal(200), false)

            p0.sendMessage(text().content("绑定成功").color(color(0xa3fffc)))
            return true
        }
        return false
    }
}