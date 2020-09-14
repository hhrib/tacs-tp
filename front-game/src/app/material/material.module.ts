//Es un módulo para emprolijar imports.

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatFormFieldModule} from '@angular/material/form-field';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MatToolbarModule,
    MatFormFieldModule
  ],
  exports:[
    MatToolbarModule,
    MatFormFieldModule
  ]
})
export class MaterialModule { }
