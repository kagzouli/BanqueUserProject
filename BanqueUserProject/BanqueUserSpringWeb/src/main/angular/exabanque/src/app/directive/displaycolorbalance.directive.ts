import { Directive, ElementRef, HostListener } from '@angular/core'


@Directive({
  selector: '[appDisplaycolorbalance]'
})
export class DisplaycolorbalanceDirective {

  value_max_red = 100;

  value_max_orange = 300;


  constructor(private el: ElementRef){
    el.nativeElement.style.color = 'gray';
  }

  
  @HostListener('mouseenter') onMouseenter() {
    let nativeElement : HTMLElement = this.el.nativeElement;
    let value : number;
 
    if (nativeElement != null && nativeElement  .childNodes !=null && nativeElement.childNodes.length >= 1){
      value = parseInt(nativeElement.childNodes[0].textContent);

      // Color of the text
      if (value < this.value_max_red){
        nativeElement.style.color = 'red';        
      }else if (value <= this.value_max_orange){
        nativeElement.style.color = 'orange';        
      }else{
        nativeElement.style.color = 'green';
      }
    }
  }
  
  @HostListener('mouseleave') onMouseleave() {
    this.el.nativeElement.style.color = 'gray';
  }

}
