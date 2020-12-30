package io.beansnapper.builder

import kotlinx.metadata.jvm.KotlinClassHeader
import kotlinx.metadata.jvm.KotlinClassMetadata
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.Element

val Element.kotlinMetadata get() = ReadKotlinMetaData.readFrom(this)

object ReadKotlinMetaData {
    private const val kotlinMetaData = "kotlin.Metadata"

    fun readFrom(element: Element): KotlinClassMetadata? {
        val kotlinAnnotation = element.annotationMirrors
            .find { it.annotationType.toString() == kotlinMetaData }
            ?: throw Exception("Could not find Kotlin Meta Data")


        var kind: Int? = null
        var metadataVersion: IntArray? = null
        var bytecodeVersion: IntArray? = null
        var data1: Array<String>? = null
        var data2: Array<String>? = null
        var extraString: String? = null
        var packageName: String? = null
        var extraInt: Int? = null

        for ((e, wrappedValue) in kotlinAnnotation.elementValues) {
            val value = unwrapAnnotationValue(wrappedValue)
            @Suppress("UNCHECKED_CAST")
            when (e.simpleName.toString()) {
                "k" -> kind = value as? Int
                "mv" -> metadataVersion = (value as? List<Int>)?.toIntArray()
                "bv" -> bytecodeVersion = (value as? List<Int>)?.toIntArray()
                "d1" -> data1 = (value as? List<String>)?.toTypedArray()
                "d2" -> data2 = (value as? List<String>)?.toTypedArray()
                "xs" -> extraString = value as? String
                "pn" -> packageName = value as? String
                "xi" -> extraInt = value as? Int
            }
        }

        return KotlinClassMetadata.read(
            KotlinClassHeader(
                kind,
                metadataVersion,
                bytecodeVersion,
                data1,
                data2,
                extraString,
                packageName,
                extraInt
            )
        )
    }

    private tailrec fun unwrapAnnotationValue(value: Any?): Any? =
        when (value) {
            is AnnotationValue -> unwrapAnnotationValue(value.value)
            is List<*> -> value.map(::unwrapAnnotationValue)
            else -> value
        }

}