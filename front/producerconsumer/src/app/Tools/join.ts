import { Renderer2 } from "@angular/core";
import { Tool } from "./Tool";

export class Join implements Tool {
    constructor(private renderer: Renderer2, private svg: any) { };
    private line: any;
    x1 = 0;
    y1 = 0;
    x2 = 0;
    y2 = 0;
    id = '';
    offsetX: number = 0;
    offsetY: number = 0;
    type = "Join";
    status = '';
    draw(event: MouseEvent): void {
        this.x1 = event.offsetX;
        this.y1 = event.offsetY;
        this.line = this.renderer.createElement('line', 'svg');
        this.line.setAttribute("stroke", "#000000");
        this.line.setAttribute("x1", this.x1.toString());
        this.line.setAttribute("y1", this.y1.toString());
        this.line.setAttribute("x2", this.x1.toString());
        this.line.setAttribute("y2", this.y1.toString());
        this.line.setAttribute("id", this.id);
        this.line.setAttribute("marker-end","url(#triangle)");
        this.renderer.appendChild(this.svg.nativeElement, this.line);
    }
    drawHelper(event: MouseEvent) {
        this.x2 = event.offsetX;
        this.y2 = event.offsetY;
        this.line.setAttribute("x2", this.x2.toString());
        this.line.setAttribute("y2", this.y2.toString());
    }
    move(event: MouseEvent, id: number) { }
    select(x: number, y: number): boolean {
        return false;
    }
    remove(): void {
        this.line = document.getElementById(this.id);
        this.renderer.removeChild(this.svg.nativeElement, this.line);
    }
    getDim(): number[] {
        return [this.x1, this.y1, this.x2, this.y2];
    }
    edit(e: string) { };
}