import { Component, Renderer2, ViewChild, ElementRef, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Tool } from './Tools/Tool';
import { Factory } from './Tools/Factory';
import { ThisReceiver } from '@angular/compiler';
import { WebSocketAPI } from './WebSocketAPI';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

@Injectable()
export class AppComponent {
  constructor(private renderer: Renderer2, private http: HttpClient) { };
  @ViewChild('svg', { static: true }) svg: ElementRef;
  numProducts = 1;
  
  isRemove = false;
  isDrawing = false;
  isMoving = false;
  isDown = false;
  selectedTool = null;
  indx = 0;
  Tool = new Tool();
  Machines = [];
  Queues = [];
  Joins = [];
  start = false;
  idMachine = 0;
  idQueue = 0;
  idJoin = 0;

  fillMachine = '';
  order = [];


  webSocketAPI: WebSocketAPI;
  greeting: any;
  name: string;
  ngOnInit() {
    this.webSocketAPI = new WebSocketAPI(new AppComponent(this.renderer, null));
    this.webSocketAPI._connect();
  }

  disconnect(){
    this.webSocketAPI._disconnect();
  }


  handleMessage(message){
    this.greeting = message;
  }

  draw(tool: string) {
    const factory = new Factory(this.renderer, this.svg);
    this.Tool = factory.getTool(tool);
    this.isDrawing = true;
    this.isMoving = false;
  }

  remove() {
    this.isRemove = true;
  }

  move() {
    if(this.isMoving) return;
    this.isMoving = true;
    this.isDrawing = false;
  }

  onMouseDown(event: MouseEvent) {
    this.isDown = true;
    if (this.isDrawing) {
      if (this.Tool.type === 'Machine') {
        this.Tool.id = `m${this.idMachine}`;   
        this.idMachine++;
      }
      else if (this.Tool.type === 'Queue') {
        this.Tool.id = `q${this.idQueue}`;   
        this.idQueue++;
      }
      else if (this.Tool.type == 'Join') {
        this.Tool.id = `l${this.idJoin}`;
        this.idJoin++;
      }
      this.Tool.draw(event);
    }
    else if (this.isMoving) {
      this.select(event)
    }
    else if (this.isRemove) {
      this.select(event);
      if (this.selectedTool === null) return;
      this.selectedTool.remove();
      this.isRemove = false;
      if (this.selectedTool.type == 'Queue') {
        this.Queues.splice(this.indx, 1);
      }
      else if (this.selectedTool.type == 'Machine') {
        this.order.splice(this.indx, 1);
        this.Machines.splice(this.indx, 1);
      }
    }
    this.isDown = true;
  }

  select(event: MouseEvent) {
    var x = event.offsetX;
    var y = event.offsetY;
    for (var i = 0; i < this.Machines.length; i++) {
      if (this.Machines[i].select(x, y) == true) {
        this.selectedTool = this.Machines[i];
        this.indx = i;
      }
    }
    for (var i = 0; i < this.Queues.length; i++) {
      if (this.Queues[i].select(x, y) == true) {
        this.selectedTool = this.Queues[i];
        this.indx = i;
      }
    }
    for (var i = 0; i < this.Joins.length; i++) {
      if (this.Joins[i].select(x, y) == true) {
        this.selectedTool = this.Joins[i];
        this.indx = i;
      }
    }

  }

  onMouseMove(event: MouseEvent) {
    if (this.isDrawing && this.isDown) {
      this.Tool.drawHelper(event);
    }
    if (this.selectedTool === null) return;
    if (this.isMoving && this.isDown) {
      if (this.selectedTool === null) {
        this.isMoving = false;
        return;
      }
      if(!this.selectedTool.move(event, this.selectedTool.id)) {
        this.isMoving = false;
      }
    }
  }

  onMouseUp(event: MouseEvent) {
    if (this.isDrawing) {
      this.isDrawing = false;
      if (this.Tool.type == 'Join') {
        this.Joins.push(this.Tool);
        this.checkJoin();
      }
      else if (this.Tool.type == 'Machine') {
        this.Machines.push(this.Tool);
        this.order.push({
          before: [],
          after: '',
          id: this.Tool.id,
        });
      }
      else if (this.Tool.type == 'Queue') {
        if (this.start == false) {
          this.start = true;
          document.getElementById(this.Tool.id)
            .setAttribute("fill", "#eee");
        }
        this.Queues.push(this.Tool);
      }
      console.log("order", this.order)
    }
    if (this.isMoving) {
      console.log("ana d5lt")
      this.isMoving = false;
      
    }
    this.isMoving = false;
    this.isDrawing = false;
    this.isRemove = false;  
    this.isDown = false;
    this.selectedTool = null;
  }

  reset() {
    this.Queues = [];
    this.Machines = [];
    this.Joins = [];
    this.start = false;
    this.isDrawing = false;
    this.isRemove = false;
    this.isDown = false;
    this.order = [];
    this.idJoin = 0;
    this.idMachine = 0;
    this.idQueue = 0;
    document.querySelector('svg').innerHTML = "";
    this.createArrow();
    //this.renderer.createElement('svg', marker)
  }

  checkJoin() {
    let array = this.Tool.getDim();
    let point1 = this.checkPoint(array[0], array[1]).split(',');
    let point2 = this.checkPoint(array[2], array[3]).split(',');
    if (point1[0] !== point2[0] && point1[0] !== "" && point2[0] !== "") {
      if (point1[0] === 'machine' && point2[0] === 'queue') {
        this.Machines[point1[1]].status = 'joined';
        this.Queues[point2[1]].status = 'joined';
        let index = this.order.findIndex(e => e.id == point1[2]);
        if (this.order[index].after.length === 0) {
          this.order[index].after = point2[2];
        }
        else {
          //na2es yt3ml delete lel line
          this.Tool.remove();
          window.alert("Only one Queue follows the Machine");
        }
        
      }
      else if (point1[0] === 'queue' && point2[0] === 'machine') {
        this.Machines[point2[1]].status = 'joined';
        this.Queues[point1[1]].status = 'joined';
        let index = this.order.findIndex((e) => e.id == point2[2]);
        this.order[index].before.push(point1[2]);
      }
    }
    else {
      this.Tool.remove();
      window.alert('The line should connect between a Machine and a Queue');
    }
  }

  checkPoint(x: number, y: number): string {
    for (let i = 0; i < this.Machines.length; i++){
      if (this.Machines[i].select(x, y)) {
        return `machine,${i},${this.Machines[i].id}`;
      }
    }
    for (let i = 0; i < this.Queues.length; i++){
      if (this.Queues[i].select(x, y)) {
        return `queue,${i},${this.Queues[i].id}`;
      }
    }
    return '';
  }

  // check that all machines are connected 
  checkGraph(): boolean {
    if (this.Machines.length == 0 || this.Queues.length == 0) {
      window.alert("No machines or queues found");
      return false;
    }
    else {
      for (let i = 0; i < this.Machines.length; i++){
        console.log("machine", this.Machines)
        console.log("machine", this.Machines.length)
        if (this.Machines[i].status !== "joined") {
          window.alert('All Machines should be connected to Queues.');
          return false;
        }
        
        for (let i = 0; i < this.Queues.length; i++){
          console.log("q", this.Queues[i])
          if (this.Queues[i].status !== "joined") {
            window.alert("All Queues should be connected to the Graph.");
            return false;
          }
        }
        
      }
      
    }
    return true;
  }
  createArrow(){
    var defs = this.renderer.createElement('defs','svg');
    var marker = this.renderer.createElement('marker', 'svg');
    this.renderer.appendChild(defs, marker);
    this.renderer.setAttribute(marker, 'id', "triangle");
    this.renderer.setAttribute(marker, 'viewBox', "0 0 10 10");
    this.renderer.setAttribute(marker, 'refX', "1");
    this.renderer.setAttribute(marker, 'refY', "5");
    this.renderer.setAttribute(marker, 'markerUnits', "strokeWidth");
    this.renderer.setAttribute(marker, 'markerWidth', "10");
    this.renderer.setAttribute(marker, 'markerHeight', "10");
    this.renderer.setAttribute(marker, 'orient', "auto");
    var path = this.renderer.createElement('path', "svg");
    this.renderer.setAttribute(path,"d" ,"M 0 0 L 10 5 L 0 10 z");
    this.renderer.setAttribute(path, "fill", "#000000");
    this.renderer.appendChild(marker, path);
    this.renderer.appendChild(this.svg.nativeElement, defs);  
   }

  simulate() {
    if (this.checkGraph()) {
      
      let x = this.listID();
      console.log("ana true", this.numProducts)
      this.http.post("http://localhost:8080/simulation/start",{graph:this.order}, {
        params:
      {
        productsNumber: this.numProducts,
        queuesList: this.listID()
      }
      }).subscribe(()=>{console.log("lolo")})
  
      // call listID to get a string of ids of queues
      // numProducts is the variable that carries the number of products
    };
  }

  replay() {
    for(let i = 0; i < this.Queues.length; i++) {
      this.Queues[i].edit(0)
    }
    this.http.post("http://localhost:8080/simulation/replay",{}, {
        
      }).subscribe(()=>{console.log("lolo")})

  }
  showResponse(response: string){
    console.log("koko", response)
    let array = response.split(",");
    console.log(array[1])
    let i = document.getElementById(array[2])
    i.setAttribute("fill", array[3])
    let text = document.getElementById('t'+array[0][1])
    text.innerHTML = array[1]
   // let index = this.Queues.findIndex(e => e.id === array[0]);
   // this.Queues[index].edit(array[1]);
   // index = this.Machines.findIndex(e => e.id === array[2]);
   // this.Machines[index].edit(array[3]);
  }

  listID(): string {
    let str = [];
    for (let i = 0; i < this.Queues.length; i++){
      str.push(this.Queues[i].id);
    }
    return str.join(',');
  }
}
