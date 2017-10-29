import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserbanquelistComponent } from './userbanquelist.component';

@NgModule({
  declarations: [ UserbanquelistComponent ],
  exports: [ UserbanquelistComponent ],
  imports: [ CommonModule ],
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
})
export class CustomModule {}
