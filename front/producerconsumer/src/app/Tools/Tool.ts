export class Tool{
    type = "";
    id = '';
    status = '';
    draw(event: MouseEvent) { }
    move(event: MouseEvent, id: number) { };
    select(x: number, y: number): boolean { return false};
    drawHelper(event: MouseEvent) { };
    remove() { };
    getDim() { };
    edit(e: string ) { };
}