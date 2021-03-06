/*
 * This file is part of LiquidBounce (https://github.com/CCBlueX/LiquidBounce)
 *
 * Copyright (c) 2016 - 2021 CCBlueX
 *
 * LiquidBounce is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LiquidBounce is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LiquidBounce. If not, see <https://www.gnu.org/licenses/>.
 */
package net.ccbluex.liquidbounce.features.command.commands.creative

import net.ccbluex.liquidbounce.features.command.Command
import net.ccbluex.liquidbounce.features.command.CommandException
import net.ccbluex.liquidbounce.features.command.builder.CommandBuilder
import net.ccbluex.liquidbounce.features.command.builder.ParameterBuilder
import net.ccbluex.liquidbounce.utils.*
import net.ccbluex.liquidbounce.utils.extensions.asText
import net.ccbluex.liquidbounce.utils.extensions.isNothing
import net.ccbluex.liquidbounce.utils.extensions.translateColorCodes
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket
import net.minecraft.util.Hand

object CommandItemRename {

    fun createCommand(): Command {
        return CommandBuilder
            .begin("rename")
            .description("Allows you to rename items")
            .parameter(
                ParameterBuilder
                    .begin<String>("name")
                    .description("Custom name for the item")
                    .verifiedBy(ParameterBuilder.STRING_VALIDATOR)
                    .required()
                    .build()
            )
            .handler { args ->
                val name = args[0] as String

                if (mc.interactionManager?.hasCreativeInventory() == false) {
                    throw CommandException("You need to be in creative mode.")
                }

                val itemStack = mc.player?.getStackInHand(Hand.MAIN_HAND)
                if (itemStack.isNothing()) {
                    throw CommandException("You need to hold a item.")
                }

                itemStack!!.setCustomName(name.translateColorCodes().asText())
                mc.networkHandler!!.sendPacket(CreativeInventoryActionC2SPacket(36 + mc.player!!.inventory.selectedSlot, itemStack))
                chat(regular("Renamed "), itemStack.item.name, regular("to "), variable(name), dot())
            }
            .build()
    }

}
