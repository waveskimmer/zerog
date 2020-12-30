package beansnapper.zerog.domain

import kotlin.reflect.KClass

/**
 * strategies to fix this...
 *
 * need a map from id to types, so they can determine where and how to retrieve an object from an ID.
 *
 */
class ObjectId<T : DataObject<T>> private constructor() {
    lateinit var rawId: RawId
    lateinit var clazz: KClass<T>
    private lateinit var obj: T

    constructor(value: T) : this() {
        this.obj = value
        value.id?.let { rawId = it }
    }

    constructor(rawId: RawId, clazz : KClass<T>) : this() {
        this.rawId = rawId
        this.clazz = clazz
    }

    val value: T
       get() {
           if (!this::obj.isInitialized) {
//               obj = Cache.fetch(this)
           }
            return obj
        }

    override fun toString(): String {
        return if (this::obj.isInitialized)
            obj.toString()
        else
            rawId
    }

}
