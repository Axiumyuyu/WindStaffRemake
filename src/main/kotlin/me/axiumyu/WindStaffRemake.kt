package me.axiumyu

import me.axiumyu.Staff.Companion.OWNER
import me.axiumyu.Staff.Companion.TAG
import me.yic.xconomy.api.XConomyAPI
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.TextColor.color
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.Statistic
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import java.lang.Math.toRadians
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class WindStaffRemake : JavaPlugin(), Listener {

    companion object {
        lateinit var xc : XConomyAPI
    }

    override fun onEnable() {
        xc = XConomyAPI()
        if(!server.pluginManager.isPluginEnabled("XConomy") ){
            logger.warning("XConomy插件未启用，插件已禁用！")
            server.pluginManager.disablePlugin(this)
        }
        getCommand("windstaff")?.setExecutor(StaffGet)
        getCommand("staffup")?.setExecutor(StaffUpdate)
        getCommand("rebind")?.setExecutor(Rebind)
        server.pluginManager.registerEvents(this, this)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val item = event.item ?: return
        if (event.player.gameMode == GameMode.SPECTATOR) return
        if (item.persistentDataContainer.get(TAG, PersistentDataType.STRING) != Staff.KEY) return
        if (!event.action.isRightClick) return
        val food = item.getEnchantmentLevel(Enchantment.PUNCH)
        if (event.player.foodLevel < food) {
            event.player.sendActionBar(text("你没有足够的饱食度！").color(color(0xffea3a)))
            return
        }
        if (item.persistentDataContainer.get(OWNER, PersistentDataType.STRING) != event.player.name) {
            event.player.sendActionBar(text("这不是你的物品！").color(color(0xffea3a)))
            return
        }

        event.player.foodLevel -= food
        val lev = item.getEnchantmentLevel(Enchantment.FEATHER_FALLING).toDouble()
        val pl = event.player
        val pitch = pl.pitch.toDouble()
        val yaw = pl.yaw.toDouble()
        val vec = pl.velocity
        val exact: Double = 4 * abs((toRadians(abs(pitch)) - PI) / PI)
        vec.x = (2 - exact) * sin(toRadians(yaw)) + 0.15 * vec.x
        vec.y = -2 * sin(toRadians(pitch)) + 0.05 * vec.y
        vec.z = (exact - 2) * cos(toRadians(yaw)) + 0.15 * vec.z
        pl.velocity = vec.multiply(lev / 35)
        pl.playSound(pl.location, Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0F, 1.0F)
        pl.fallDistance = -20F
    }
}
