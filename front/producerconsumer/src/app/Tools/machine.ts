import { Renderer2 } from "@angular/core";
import { Tool } from "./Tool";

export class Machine implements Tool{
    constructor(private renderer: Renderer2, private svg: any) { };
    private circle: any;
    type = "Machine";
    id = '';
    x = 0;
    y = 0;
    offsetX = 0;
    offsetY = 0;
    status = '';
    fill = '#ffffff';
    draw(event: MouseEvent) {
        this.x = event.offsetX;
        this.y = event.offsetY;
        this.circle = this.renderer.createElement('circle', 'svg');
        this.circle.setAttribute("cx", this.x.toString());
        this.circle.setAttribute("cy", this.y.toString());
        this.circle.setAttribute("r", "20");
        this.circle.setAttribute("id", this.id);
        this.circle.setAttribute('fill', this.fill);
        this.circle.setAttribute('stroke', '#000000');
        this.renderer.appendChild(this.svg.nativeElement, this.circle);
    };
    move(event: MouseEvent, id: number) {
        if (this.status !== 'joined') {
            var shape = document.getElementById(this.id);
            this.x = this.x - this.offsetX +  event.offsetX;
            this.y = this.y - this.offsetY +  event.offsetY;
            shape.setAttribute("cx", this.x.toString());
            shape.setAttribute("cy", this.y.toString());
            this.offsetX = event.offsetX;
            this.offsetY= event.offsetY;
            return true;
        }
        else {
            window.alert("Only unjoined Machines can be moved.");
            return false;
        }
    }
    select( x: number, y: number): boolean { 
        var equation = Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2);
        if (equation < Math.pow(20, 2)) {
            this.offsetX = x;
            this.offsetY = y;
            return true;
        }
        return false
    }
    drawHelper(event: MouseEvent) { }
    remove(): void {
        if (this.status !== 'joined') {
                this.circle = document.getElementById(this.id);
            this.renderer.removeChild(this.svg.nativeElement, this.circle);
        }
        else {
            window.alert("Only unjoined Machines can be removed.");
        }
    }
    getDim(): number[]{
        return [this.x, this.y];
    }
    edit(e: string) {
        console.log("e", e)
        this.fill = e;
        this.circle.setAttribute('fill', this.fill);
    };
}