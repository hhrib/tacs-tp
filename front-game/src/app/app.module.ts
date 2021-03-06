import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import {MatNativeDateModule, MatRippleModule} from '@angular/material/core';

import { AppRoutes } from './app-routing.module';

import { AppComponent } from './app.component';
import { MenuComponent } from './components/menu/menu.component';
import { HomeComponent } from './components/home/home.component';
import { MatchCreateDialogComponent } from './components/match/match-create-dialog/match-create-dialog.component'
import { MatchSearchComponent } from '../app/components/match/match-search/match-search.component'
import { SocketComponent } from './components/socket/socket.component';

import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatMenuModule} from '@angular/material/menu';
import {MatButtonModule} from '@angular/material/button';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatIconModule} from '@angular/material/icon';
import {MatDialogModule} from '@angular/material/dialog';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatchDTO} from './models/match.dto';

import {MatTableModule} from '@angular/material/table';
import {MatInputModule} from '@angular/material/input';
import {MatPaginatorModule} from '@angular/material/paginator'
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner'
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatSortModule} from '@angular/material/sort'
import { FindMatchDTO } from './models/findMatch.dto';
import { MatchService } from './services/matches.service';

import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { InterceptorService } from './services/interceptor.service';
import { StatisticsPanelComponent } from './components/match/statistics-panel/statistics-panel.component';
import {WebSocketService} from "./services/web-socket.service";

import { MatchMapComponent } from './components/match/match-map/match-map.component';
import { MapService } from './services/map.service';
import { MarkerService } from './services/marker.service';
import { PopUpService } from './services/pop-up.service';

import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { MatchResponse } from './models/match.response';
import { MatchAtackDialogComponent } from './components/match/match-atack-dialog/match-atack-dialog.component';
import { MatchMoveDialogComponent } from './components/match/match-move-dialog/match-move-dialog.component';
import { MatchStateDialogComponent } from './components/match/match-state-dialog/match-state-dialog.component';
import { MatchEndshiftDialogComponent } from './components/match/match-endshift-dialog/match-endshift-dialog.component';
import { User } from './models/user';
import { AdminComponent } from './components/admin/admin.component';
import {MatGridListModule} from '@angular/material/grid-list';
import { AdminMatchesComponent } from './components/admin/admin-matches/admin-matches.component';
import { AdminUserStatsComponent } from './components/admin/admin-user-stats/admin-user-stats.component';
import { AdminScoreboardComponent } from './components/admin/admin-scoreboard/admin-scoreboard.component';

import {MatDatepickerModule} from '@angular/material/datepicker';
import { MatTooltipModule } from '@angular/material/tooltip';

import { MatchErrorDialogComponent } from './components/match/match-error-dialog/match-error-dialog.component';
import { MatchSuccessDialogComponent } from './components/match/match-success-dialog/match-success-dialog.component';


@NgModule({
  declarations: [
    AppComponent,
    MenuComponent,
    HomeComponent,
    MatchCreateDialogComponent,
    MatchSearchComponent,
    SocketComponent,
    StatisticsPanelComponent,
    MatchMapComponent,
    MatchAtackDialogComponent,
    MatchMoveDialogComponent,
    MatchStateDialogComponent,
    MatchEndshiftDialogComponent,
    AdminComponent,
    AdminMatchesComponent,
    AdminUserStatsComponent,
    AdminScoreboardComponent,
    MatchErrorDialogComponent,
    MatchSuccessDialogComponent
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
    MatTableModule,
    MatInputModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatSortModule,
    MatProgressBarModule,
    MatGridListModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatTooltipModule,
    AppRoutes,
    LeafletModule
  ],
  entryComponents: [
    MatchCreateDialogComponent,
    MatchSearchComponent,
    StatisticsPanelComponent,
    MatchMapComponent,
    MatchAtackDialogComponent,
    MatchMoveDialogComponent,
    MatchStateDialogComponent,
    MatchEndshiftDialogComponent,
    MatchErrorDialogComponent,
    MatchSuccessDialogComponent
  ],
  providers: [MatchDTO, MatchResponse, FindMatchDTO, MatchService, MarkerService, PopUpService, WebSocketService, User, MapService,
    { provide: HTTP_INTERCEPTORS, useClass: InterceptorService, multi: true }],
    
  bootstrap: [AppComponent]
})
export class AppModule { }
