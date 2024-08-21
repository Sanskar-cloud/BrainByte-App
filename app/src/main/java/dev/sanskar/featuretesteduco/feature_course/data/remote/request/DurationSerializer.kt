package dev.sanskar.featuretesteduco.feature_course.data.remote.request

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.time.Duration


object DurationSerializer : KSerializer<Duration> {

    private val serializer = Duration.serializer()

    override val descriptor: SerialDescriptor = serializer.descriptor

    override fun deserialize(decoder: Decoder): Duration =
        decoder.decodeSerializableValue(serializer)

    override fun serialize(encoder: Encoder, value: Duration) {
        encoder.encodeSerializableValue(serializer, value)
    }
}