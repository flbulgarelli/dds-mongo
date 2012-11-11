package dds.mongo;

import static complejidad.Complejidad.*
import static org.junit.Assert.*

import org.junit.After;
import org.junit.Before;
import org.junit.Test

import proyecto.Impuesto
import proyecto.Proyecto
import tareas.TareaCompuesta
import tareas.TareaSimple

class ProyectosTest {

  static {
    new HomeProyectosMongo().setUp()
  }
  
  @After
  void tearDown() {
    Proyecto.home.clear()
  }
  
  def proyecto = new Proyecto([
    new TareaCompuesta([
      new TareaSimple(MINIMA, 10, [
        new Impuesto(0.5),
        new Impuesto(0.1)
      ]),
      new TareaSimple(MEDIA, 15, [new Impuesto(0.1)])
    ]),
    new TareaSimple(MAXIMA, 5, [])
  ])

  @Test
  void toBson() {
    assert proyecto.toBson() ==
    [tareas:[
        [subTareas:[
            [complejidad:'MINIMA', cantidadDias:10, impuestos:[0.5, 0.1]],
            [complejidad:'MEDIA', cantidadDias:15, impuestos:[0.1]]
          ]],
        [complejidad:'MAXIMA', cantidadDias:5, impuestos:[]]
      ]]
  }
  
  @Test
  void 'fromBson y toBson son operaciones inversas'() {
    def proyectoBson = [tareas:[
        [subTareas:[
            [complejidad:'MINIMA', cantidadDias:10, impuestos:[0.5, 0.1]],
            [complejidad:'MEDIA', cantidadDias:15, impuestos:[0.1]]
          ]],
        [complejidad:'MAXIMA', cantidadDias:5, impuestos:[]]
      ]]
    
    assert Proyecto.fromBson(proyectoBson).toBson() == proyectoBson 
  }
  
  
  @Test
  public void elProyectoEsPersistente()  {
    proyecto.persist()
    assert Proyecto.home.count() == 1
  }
  
  @Test
  public void __()  {
    proyecto.persist()
    print Proyecto.home.findAll()
  }
}
