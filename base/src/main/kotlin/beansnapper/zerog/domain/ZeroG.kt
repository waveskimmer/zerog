package beansnapper.zerog.domain

import java.util.*

typealias RawId = String
typealias Timestamp = Date

interface DataObject<T : Any> {
    val id: RawId?
    val timestamp: Timestamp?
}

