import { Injectable } from '@angular/core';

import {$WebSocket, WebSocketSendMode } from 'angular2-websocket/angular2-websocket';

@Injectable()
export class DisplaydateService {   

  contextBanqueServiceUrl = 'ws://localhost:14090';
  
  socket : $WebSocket =  new $WebSocket(this.contextBanqueServiceUrl + '/sendDateToDisplay');
  
  

  constructor() { }

 /**
  *   Connect
  */
  connect(){
     
    // set received message stream
    this.socket.getDataStream().subscribe(
      (msg)=> {
       console.log("next", msg.data);
       this.socket.close(false);
    },
    (msg)=> {
        console.log("error", msg);
    },
    ()=> {
        console.log("complete");
    }
    );
  }


  /**
   *  Disconnect.
   */
  disconnect(){
    this.socket.close(true);    // close immediately
  }
}
