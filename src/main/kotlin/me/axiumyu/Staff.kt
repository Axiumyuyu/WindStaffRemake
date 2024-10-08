package me.axiumyu

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.TextColor.color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemRarity
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class Staff(pl: Player) {
    val staff: ItemStack = ItemStack(Material.STICK)
    val level: Int = 1
    val food: Int = 11
    val owner: Player = pl

    companion object {
        @JvmField
        val TAG = NamespacedKey("axiumyu", "tag")

        @JvmField
        val OWNER = NamespacedKey("axiumyu", "owner")

        const val KEY = "wind-staff"
    }

    init {
        staff.editMeta {
            it.displayName(text().content("风之法杖").color(color(0xa3fffc)).build())
            it.lore(
                listOf<Component>(
                    text().content("物品绑定于： ${owner.name} ").color(color(0xa3fffc))
                        .build(),
                    text().content("右键点击吹向空中").color(color(0xa3fffc)).build(),
                    text().content("摔落缓冲附魔等级即为飞行速度").color(color(0xa3fffc))
                        .build(),
                    text().content("冲击等级即为消耗的饥饿值").color(color(0xa3fffc))
                        .build(),
                    text().content("拿在主手输入/staffup [数字] 可以快速升级输入的等级数").color(color(0xa3fffc))
                        .build(),
                    text().content("输入/windstaff可以再次购买").color(color(0xa3fffc))
                        .build()
                )
            )
            it.persistentDataContainer.set(TAG, PersistentDataType.STRING, KEY)
            it.persistentDataContainer.set(OWNER, PersistentDataType.STRING, owner.name)
            it.addEnchant(Enchantment.FEATHER_FALLING, level, true)
            it.addEnchant(Enchantment.PUNCH, food, true)
            it.setMaxStackSize(1)
            it.setRarity(ItemRarity.COMMON)
        }
    }
}