package beansnapper.zerog.mongo

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import mu.KotlinLogging
import org.bson.Document

private val log = KotlinLogging.logger {}

class NotFoundException(message: String = "", cause: Exception? = null) : PersistenceException(message, cause)

open class PersistenceException(message: String = "", cause: Exception? = null) : Exception(message, cause)

abstract class MongoDataServiceImpl<T>(
        val mongoClient: MongoClient,
        val clazz: Class<T>
) {
    abstract fun read(document: Document): T
    abstract fun write(obj: T): Document


}
