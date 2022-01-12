import { Tool } from './Tool';
import { Renderer2 } from "@angular/core";
import { windowWhen } from 'rxjs';

export class Queue implements Tool{
    constructor(private renderer: Renderer2, private svg: any) { };
    private rectangle: any;
    private text: any;
    id = '';
    x = 0;
    y = 0;
    offsetX : number = 0;
    offsetY: number = 0;
    fill = '#91d3fb'
    type = "Queue";
    status = '';
    noProducts = 0;
    draw(event: MouseEvent) {
        this.x = event.offsetX;
        this.y = event.offsetY;
        this.rectangle = this.renderer.createElement('rect', 'svg');
        this.rectangle.setAttribute("id", this.id);
        this.rectangle.setAttribute("x", this.x.toString());
        this.rectangle.setAttribute('y', this.y.toString());
        this.rectangle.setAttribute('width', '40');
        this.rectangle.setAttribute('height', '40');
        this.rectangle.setAttribute('fill', this.fill);
        this.rectangle.setAttribute('stroke', '#000000');
        this.renderer.appendChild(this.svg.nativeElement, this.rectangle);
        this.addingText();
    }
    move(event: MouseEvent, id: number) {
        if (this.status !== 'joined') {
            var rectangle = document.getElementById(this.id);
            let text = document.getElementById(`t${this.id[1]}`);
            this.x = this.x - this.offsetX + event.offsetX;
            this.y = this.y - this.offsetY + event.offsetY;
            rectangle.setAttribute("x", this.x.toString());
            rectangle.setAttribute("y", this.y.toString());
            text.setAttribute('x', (this.x + 15).toString());
            text.setAttribute('y', (this.y + 25).toString());
            this.offsetX = event.offsetX;
            this.offsetY = event.offsetY;
        }
        else {
            window.alert('Only unjoined Queues can be moved.');
        }
    }
    select(x: number, y: number) {
        if (x >= this.x && x <= 40 + this.x
            && y >= this.y && y <= 40 + this.y) {
            this.offsetX = x;
            this.offsetY = y;
            return true; 
        }
        return false
    }
    drawHelper(event: MouseEvent) { }
    remove(): void {
        if (this.id === 'q0') {
            window.alert("Start Queue can not be removed.");
        }
        else if (this.status !== 'joined') {
            this.rectangle = document.getElementById(this.id)
            this.text = document.getElementById(`t${this.id[1]}`);
            this.renderer.removeChild(this.svg.nativeElement, this.rectangle);
            this.renderer.removeChild(this.svg.nativeElement, this.text);
        }
        else {
            window.alert('Only unjoined Queues can be removed.');
        }
    }
    getDim(): number[] {
        return [this.x, this.y]
    }
    addingText() {
        this.text = document.createElementNS('http://www.w3.org/2000/svg', 'text');
        this.text.setAttribute('x', (this.x + 15).toString());
        this.text.setAttribute('y', (this.y + 25).toString());
        this.text.setAttribute('width', '500');
        this.text.style.fill = 'black';
        this.text.style.fontSize = '15';
        this.text.innerHTML = this.noProducts;
        this.text.setAttribute("id", `t${this.id[1]}`);
        this.renderer.appendChild(this.svg.nativeElement, this.text);
    }
    edit(e: string) { 
        this.noProducts = +e;
        this.text.innerHTML = this.noProducts;
    };
}