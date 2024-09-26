package me.axiumyu

import me.axiumyu.Staff.Companion.FOOD
import me.axiumyu.Staff.Companion.OWNER
import me.axiumyu.Staff.Companion.TAG
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
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

abstract class WindStaffRemake : JavaPlugin(), Listener{

    override fun onEnable() {
        getCommand("windstaff")?.setExecutor(StaffGet())
        getCommand("staffup")?.setExecutor(StaffUpdate())
        server.pluginManager.registerEvents(this, this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    //TODO:将消耗的饱食度，飞起的速度系数与等级挂钩
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerInteract(event: PlayerInteractEvent) {

        if (event.item == null) return
        if (event.item!!.type != Material.STICK) return
        if (event.item!!.persistentDataContainer.get(
                TAG, PersistentDataType.STRING
            ) != Staff.Companion.KEY || (event.action != Action.RIGHT_CLICK_BLOCK && event.action != Action.RIGHT_CLICK_AIR)) return
        val food = event.item!!.persistentDataContainer.get(FOOD, PersistentDataType.INTEGER)!!
        if (server.getPlayer(
                event.item!!.persistentDataContainer.get(OWNER, PersistentDataType.STRING) as String
            ) != event.player || event.player.foodLevel < food) return


        event.player.foodLevel -= food
        val lev = event.item!!.persistentDataContainer.get(Staff.Companion.LEVEL, PersistentDataType.INTEGER)!!
        val pl = event.player
        val pitch: Double = pl.pitch.toDouble()
        val yaw: Double = pl.yaw.toDouble()
        val vec: Vector = pl.velocity
        val halfPi = PI / 2
        val exact: Double = 2 * abs((toRadians(abs(pitch)) - halfPi) / halfPi)
        vec.x = (lev - exact) * sin(toRadians(yaw)) * 0.1 + 0.05 * vec.getX()
        vec.y = -lev * sin(toRadians(pitch)) * 0.1 + 0.25 * vec.getY()
        vec.z = (exact - lev) * cos(toRadians(yaw)) * 0.1 + 0.05 * vec.getZ()
        pl.playSound(pl.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0F, 1.0F)
        pl.velocity = vec
        pl.fallDistance = -20F
    }
}
