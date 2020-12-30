package inertia.persistence

import beansnapper.zerog.Collection
import beansnapper.zerog.Datastore
import beansnapper.zerog.Mapping
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import inertia.domain.Task
import org.bson.Document

@Mapping(Task::class)
@Datastore("Inertia")
@Collection("Tasks")
interface TaskDataService {

    fun find(id : String) : Task


}

