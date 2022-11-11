import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CoreModule } from './core/core.module';
import { CategoryModule } from './category/category.module';
import { AuthorModule } from './author/author.module';
import { GameModule } from './game/game.module';

import { ClientModule } from './client/client.module';
import { LoanModule } from './loan/loan.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CoreModule,
    CategoryModule,
    BrowserAnimationsModule,
    AuthorModule,
    GameModule,
    ClientModule,
    LoanModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }


/*
En angular todo son componentes, este (app.module.ts) es el base

tambien tiene la informacion de librerias de 3os que queramos usar

Cualquier componente, directivas y pipes deben ser declaradas
Cualquier libreria ha de ser importada

NgModule es un decorador que pasa los datos al compilador sobre que imports tenemos
bootstrap indica que componente se carga el 1o
  
COMPONENTES:
4 archivos:
  - .ts
  - .html
  - .css
  - .spec.ts

  Propiedades de un componente:
    -Selector: Indica como llamar a ese componente 
        ->en angular.json podemos cambiar el prefijo elegido, 
          debemos usar este en los componentes
        
    -TemplateUrl: Indica que archivo contiene su template 
    (Puede hacerse inline con template:'<h1>hola mundo!<h1>')
    Aquí definimos como se va a renderizar ese componente,
    también pododemos usar propiedades de la clase:
          <div [hidden]="disponible">

    -styleUrl: [Indica que archivo contiene su estilo]

    export class 
      -> Aqui podemos definir atributos cómo:
          nombreProducto = "Katan"
          numeroJugadores = 6
          edadMin = 6
          disponible = true

        y funciones cómo:
          reservar(){
            this.disponible = false;
          }

  Podemos crear interfaces dentro de la carpeta del componente
  Componente juegos
  creamos juegos.ts y dentro podemos hacer:
  export interface Juego {
      nombreProducto : string;...
  }
  export interface ListaJuegos{
    ....
  }


  Interpolation es pasar de una variable a texto para ser mostrado
  Se usa con {{nombreVariable}} y así se puede mostrar

  Si tenemos un array de objetos Juegos 
  y hacemos {{Juegos}}
  se recore el array y se muestra cada objeto, res -> Object, Object, Object
  Para recorrerlo usando info podemos usar 
  <tr *ngFor="let juego of ListaJuegos">
    <td>{{juego.nombreProducto}}</td> ....



  EventBinding
  Se usa poniendo el evento entre () -> (click)
  
  Directivas 
  Strctural directives -> cambian el comportamiento del dom -> * -> *ngFor
  Attribute directives -> puedem modificar el dom pero no añadir ni eliminar elementos
                          suelen añadir atributos o logica a algo

  built-in directives:
        *nfIf -> 
        El problema es que con hidden los elementos siguen en el DOM,
        con apps grandes esto puede causar problemas de rendimiento, 
        con esto podemos solucionar este problema.
        <div *ngIf="juego.edadMin > 5"> Nombre... <div>
  
        ngClass -> <div [ngClass]= "disponible ? 'disponible':'noDisponible'">
        y se aplicará esa clase de css para ese elemento


  Pipes: Se usan para transformar los datos de un objeto
  x ejemplo hacer un cambio de moneda para mostrarlo
  
  existen built-in pipes
      {{juego.tiempoDeAlquiler | date : 'short' }}
      para que lo transforme al formato


  Life-cycle hooks:
    Cada componente tiene un ciclo de vida que termina cuando es destruido.
    Estos hooks están en distintos puntos de este ciclo de vida.

    Por ejemplo, si queremos introducir la informacion de una api en
    nuestro componente, lo debemos hacer en ngOnInit, ya que el componente
    debe ser creado antes de introducir la información, pero esta debe ser
    camiada antes de mostrarse.

  Component comunication:
    Cuando dos componentes necesitan interactuar entre ellos.
    Varias formas -> @Input o @Output
                  
                  -> Servicios

      Por ejemplo, si queremos que un componente Juegos le pase la informacion
      al componente RenderizaListaJuegos podemos hacer:

      En el componente RenderizaListaJuegos usamos el decorador @Input
      @Input() juegos: ListaJuegos[] = [];

      y en Juegos
      <app-RenderizalistaJuegos [juegos]="listaJuegos">

      y así el componente que se encarga de renderizar recibe los datos.
      Si el componente hijo ha de modificar algo y devolverlo, entonces:

      @Output() juegoSelecionado = new EventEmitter<ListaJuegos>();

      y en el padre
      <app-RenderizalistaJuegos [juegos]="listaJuegos"
      (juegoSeleccionado)="selecciona($event)">
      donde juegoSeleccionado es el evento,
      y selecciona será una funcion de la clase padre encargada de tratar 
      el evento









  Optional chaining:
          {{juegos?.juegosDisponibles ?? "No nos quedan juegos"}}
          Si no quedan juegos disponibles o no existe el objeto juegos
          entones podemos mostrar que no quedan
        
*/