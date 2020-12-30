package diaspora.persistence

import com.mongodb.client.model.FindOneAndReplaceOptions
import com.mongodb.client.model.ReturnDocument
import diaspora.domain.RefId
import org.bson.BsonObjectId
import org.bson.Document
import org.bson.types.ObjectId

object MongoCommon {

    /**
     * this is an option that returns the updated object
     */
    val returnDocAfterReplace = FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER)

    /**
     * this generates a query based on the ID
     *
     * @hexString - 20 byte hex string as used by org.bson.ObjectId
     * @return document ready to put into a find or findAndUpdate
     */
    fun queryById(hexString: String) = Document("_id", BsonObjectId(ObjectId(hexString)))

    fun wrapId(hexString: String?) = BsonObjectId(if (hexString == null) ObjectId() else ObjectId(hexString))

    inline fun wrapId(refId: RefId<*>?) = refId?.id
    inline fun <T> unwrapId(id: String) = RefId<T>(id)

    fun <T> unwrapIds(ids: List<String>): List<RefId<T>> {
        return ids.map { unwrapId<T>(it) }.toList()
    }

    fun wrapIds(ids: List<RefId<*>>?): List<String> {
        return ids?.map { it.id }?.toList() ?: emptyList()
    }

}