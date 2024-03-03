/*
 * Skytils - Hypixel Skyblock Quality of Life Mod
 * Copyright (C) 2020-2024 Skytils
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package gg.skytils.skytilsws.shared.packet

import gg.skytils.skytilsws.shared.packet.c2s.C2SPacketConnect
import gg.skytils.skytilsws.shared.packet.c2s.C2SPacketLogin
import gg.skytils.skytilsws.shared.packet.s2c.S2CPacketAcknowledge
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer

@Serializable(with = PacketID.Companion::class)
enum class PacketID(val serializer: KSerializer<out Packet>) {
    C2S_CONNECT(serializer<C2SPacketConnect>()),
    C2S_LOGIN(serializer<C2SPacketLogin>()),
    S2C_ACKNOWLEDGE(serializer<S2CPacketAcknowledge>()),

    ;

    val id = ordinal.toByte()

    companion object : KSerializer<PacketID> {
        val map = entries.associateBy { it.id }

        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(PacketID::class.qualifiedName!!, PrimitiveKind.BYTE)

        override fun deserialize(decoder: Decoder): PacketID = map[decoder.decodeByte()] ?: error("Unknown Packet ID")

        override fun serialize(encoder: Encoder, value: PacketID) = encoder.encodeByte(value.id)
    }
}