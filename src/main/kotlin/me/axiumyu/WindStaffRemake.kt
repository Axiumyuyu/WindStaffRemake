package me.axiumyu

import me.axiumyu.Staff.Companion.FOOD
import me.axiumyu.Staff.Companion.OWNER
import me.axiumyu.Staff.Companion.TAG
import me.yic.xconomy.api.XConomyAPI
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.TextColor.color
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.Vector
import java.lang.Math.toRadians
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class WindStaffRemake : JavaPlugin(), Listener {

    companion object {
        @JvmField
        val xc = XConomyAPI()
    }

    override fun onEnable() {
        getCommand("windstaff")?.setExecutor(StaffGet())
        getCommand("staffup")?.setExecutor(StaffUpdate())
        server.pluginManager.registerEvents(this, this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.item == null) return
        if (event.item!!.type != Material.STICK) return
        if (event.action != Action.RIGHT_CLICK_AIR || event.item!!.persistentDataContainer.get(TAG, PersistentDataType.STRING
            ) != Staff.Companion.KEY) return
        val food = event.item!!.getEnchantmentLevel(Enchantment.PUNCH)
        if (event.player.foodLevel < food) {
            event.player.sendActionBar(text("你没有足够的饱食度！").color(color(0xffea3a)))
            return
        }
        if (server.getPlayer(
                event.item!!.persistentDataContainer.get(OWNER, PersistentDataType.STRING) as String
            ) != event.player) {
            event.player.sendActionBar(text("这不是你的物品！").color(color(0xffea3a)))
            return
        }

        event.player.foodLevel -= food
        val lev = event.item!!.getEnchantmentLevel(Enchantment.FEATHER_FALLING).toDouble()
        val pl = event.player
        val pitch = pl.pitch.toDouble()
        val yaw = pl.yaw.toDouble()
        val vec = pl.velocity
        val exact: Double = 4 * abs((toRadians(abs(pitch)) - PI) / PI)
        vec.x = (2 - exact) * sin(toRadians(yaw)) + 0.15 * vec.getX()
        vec.y = -2 * sin(toRadians(pitch)) + 0.05 * vec.getY()
        vec.z = (exact - 2) * cos(toRadians(yaw)) + 0.15 * vec.getZ()
        pl.velocity = vec.multiply(lev / 35)
        pl.playSound(pl.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0F, 1.0F)
        pl.fallDistance = -20F
    }
}
