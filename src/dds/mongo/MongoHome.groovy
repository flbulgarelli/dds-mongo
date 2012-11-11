package dds.mongo;

import proyecto.Proyecto
import tareas.TareaCompuesta
import tareas.TareaSimple

import com.gmongo.GMongo
import complejidad.Complejidad

abstract class MongoHome extends Home {
  
  final mongo = new GMongo()  
  
  abstract def getCollection()
  
  def findAll() {
    collection.find().collect { entityClass.fromBson(it) }
  }
  
  def count() {
    collection.count()
  }

  def persist(entity) {
    collection << entity.toBson()
  }
  
  def clear() {
    collection.drop()
  }

}
