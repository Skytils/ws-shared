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

import io.ktor.websocket.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

@Serializable
abstract class Packet(open val id: PacketID) {
    companion object {
        @OptIn(ExperimentalSerializationApi::class)
        fun decodePacket(frame: Frame): Packet {
            frame as? Frame.Binary ?: error("Cannot decode non-binary frame")
            val data = frame.data
            val packetID = PacketID.map[data[0]] ?: error("Invalid packet ID")
            val packetData = data.copyOfRange(1, data.size)
            return ProtoBuf.decodeFromByteArray(packetID.serializer, packetData)
        }

        @OptIn(ExperimentalSerializationApi::class)
        fun encodePacket(packet: Packet): Frame {
            val packetData = ProtoBuf.encodeToByteArray(packet)
            val packetID = packet.id.id
            val out = ByteArray(packetData.size + 1)
            out[0] = packetID

            return Frame.Binary(true, packetData.copyInto(out, 1))
        }
    }
}
