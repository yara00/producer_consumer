import { Renderer2 } from '@angular/core';
import { Join } from './join';
import { Machine } from './machine';
import { Queue } from './queue';

export class Factory{
    constructor(private renderer: Renderer2, private svg: any) { };
    getTool(toolType: string) {
        if (toolType == 'machine') {
            return new Machine(this.renderer, this.svg);
        }
        else if (toolType == 'queue') {
            return new Queue(this.renderer, this.svg)
        }
        else if (toolType == 'join') {
            return new Join(this.renderer, this.svg);
        }
        return null;
    }
}