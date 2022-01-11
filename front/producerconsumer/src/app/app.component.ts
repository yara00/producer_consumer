import { Component } from '@angular/core';
import { WebSocketAPI } from './WebSocketAPI';
import { HttpClient } from '@angular/common/http';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = "";
  constructor(private http: HttpClient){}

  webSocketAPI: WebSocketAPI;
  greeting: any;
  name: string;
  ngOnInit() {
    this.webSocketAPI = new WebSocketAPI(new AppComponent(null));
    this.webSocketAPI._connect();
  }

  start(){
    this.http.post('http://localhost:8080/start', {},{}).subscribe(()=> {console.log("hello")})
    //this.webSocketAPI._connect();
  }

  disconnect(){
    this.webSocketAPI._disconnect();
  }


  handleMessage(message){
    this.greeting = message;
  }
}