import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutes } from './app-routing.module';

import { AppComponent } from './app.component';
import { MenuComponent } from './components/menu/menu.component';
import { HomeComponent } from './components/home/home.component';
import { MatchCreateDialogComponent } from './components/match/match-create-dialog/match-create-dialog.component'

import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatMenuModule} from '@angular/material/menu';
import {MatButtonModule} from '@angular/material/button';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatIconModule} from '@angular/material/icon';
import {MatDialogModule} from '@angular/material/dialog';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { MatchDTO } from './models/match.dto';
import { MatchSearchComponent } from './components/match/match-search/match-search.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthHttpInterceptor } from '@auth0/auth0-angular';
import { AuthModule } from '@auth0/auth0-angular';


@NgModule({
  declarations: [
    AppComponent,
    MenuComponent,
    HomeComponent,
    MatchCreateDialogComponent,
    MatchSearchComponent,
   
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatMenuModule,
    MatButtonModule,
    MatToolbarModule,
    MatIconModule,
    MatDialogModule,
    MatFormFieldModule,
    MatSelectModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    AppRoutes,
    HttpClientModule,
    AuthModule.forRoot({
      domain: 'L5stWjySWxTZxrtLJ221E37WE2lP2fl7',
      clientId: 'tacticas.us.auth0.com',
      redirectUri: window.location.origin,
      httpInterceptor: {
        allowedList: [
          {
            uri: 'https://tacticas.auth0.com/api/v2/users',
            tokenOptions: {
              audience: 'https://tacticas.com/api/v2/',
              scope: 'read:users',
            },
          },
          {
            uri: 'https://your-domain.auth0.com/api/v2/users',
            tokenOptions: {
              audience: 'https://tacs.game/api',
            },
          },
        ],
      },
    }),
  ],
  entryComponents: [
    MatchCreateDialogComponent
  ],
  providers: [MatchDTO, { provide: HTTP_INTERCEPTORS, useClass: AuthHttpInterceptor, multi: true }], 
  bootstrap: [AppComponent]
})
export class AppModule { }
