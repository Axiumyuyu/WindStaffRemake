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
    val staff : ItemStack = ItemStack(Material.STICK)
    var level : Int = 1
    var food : Int = 10
    val owner : Player = pl
    companion object{
        @JvmField
        val TAG = NamespacedKey("axiumyu","tag")

        @JvmField
        val LEVEL = NamespacedKey("axiumyu","level")

        @JvmField
        val OWNER = NamespacedKey("axiumyu","owner")

        @JvmField
        val FOOD = NamespacedKey("axiumyu","food")

        const val KEY = "wind-staff"
    }
    init {
        staff.editMeta {
            it.displayName(text().content("风之法杖").color(color(0xa3fffc)).build())
            val lores = mutableListOf<Component>(
                text().content("右键点击吹向空中").color(color(0xa3fffc)).build(),
                text().content("附魔等级即为能力等级").color(color(0xa3fffc)).build()
            )
            it.lore(lores)
            it.persistentDataContainer.set(TAG, PersistentDataType.STRING, KEY)
            it.persistentDataContainer.set(LEVEL, PersistentDataType.INTEGER, level)
            it.persistentDataContainer.set(OWNER, PersistentDataType.STRING, owner.name)
            it.persistentDataContainer.set(FOOD, PersistentDataType.INTEGER, food)
            it.addEnchant(Enchantment.FEATHER_FALLING,level,true)
            it.setMaxStackSize(1)
            it.setRarity(ItemRarity.EPIC)
        }
    }
}