package dds.mongo

abstract class Home {
  
  abstract def persist(entity)
  abstract def clear()
  abstract def findAll()
  def count() {
    findAll().size()
  }
  abstract def getEntityClass()
  
  def setUp() {
    entityClass.metaClass {
      static.getHome = { -> this }
    }
    
    entityClass.metaClass {
      persist = { ->
        this.persist(delegate)
      }
    }
  }

}
