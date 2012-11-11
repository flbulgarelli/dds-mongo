package dds.mongo;

import proyecto.Impuesto
import proyecto.Proyecto
import tareas.TareaCompuesta
import tareas.TareaSimple

import complejidad.Complejidad

class HomeProyectosMongo extends MongoHome {
  
  def mapear() {
    Complejidad.metaClass {
      toBson = { -> delegate.name() }
      static.fromBson = { Complejidad.valueOf(it) }
    }
    
    def tareaFromBson = {
       if(it.subTareas)
         TareaCompuesta.fromBson(it)
       else
         TareaSimple.fromBson(it)
    }

    Proyecto.metaClass {
      toBson = {
        ->
        [tareas: delegate.tareas*.toBson() ]
      }
      static.fromBson = { 
        new Proyecto(it.tareas.collect(tareaFromBson))
      }
    }

    TareaCompuesta.metaClass {
      toBson = {
        ->
        [subTareas: delegate.tareas*.toBson() ]
      }
      static.fromBson = { 
        new TareaCompuesta(it.subTareas.collect(tareaFromBson))
      }
    }

    TareaSimple.metaClass {
      toBson = {
        ->
        [complejidad: delegate.complejidad.toBson(),
              cantidadDias: delegate.cantidadDias,
              impuestos: delegate.impuestos*.porcentajeImpuesto*.toDouble() ]
      }
      static.fromBson = {
        new TareaSimple(
          Complejidad.fromBson(it.complejidad), 
          it.cantidadDias, 
          it.impuestos.collect { new Impuesto(it)})
      }
    }
  }
  
  def setUp() {
    mapear()
    super.setUp()
  }
  
  def getCollection() {
    mongo.getDB("proyectos").proyectos
  }
  
  def getEntityClass() {
    Proyecto
	}
}
