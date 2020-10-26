import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { MatchSearchComponent } from './components/match/match-search/match-search.component';
import { SocketComponent } from "./components/socket/socket.component";
import { StatisticsPanelComponent } from './components/match/statistics-panel/statistics-panel.component';
import { AuthGuard } from './services/auth.guard';
import { MatchMapComponent } from './components/match/match-map/match-map.component';
import { AdminComponent } from './components/admin/admin.component';


const appRoutes: Routes = [
    //Ejemplo: { path: 'home', component: HomeComponent}
    { path: 'home', component: HomeComponent },
    { path: 'socket', component: SocketComponent},
    { path:'searchMatches', component: MatchSearchComponent, canActivate: [AuthGuard]},
    { path:'admin', component: AdminComponent, canActivate: [AuthGuard]},
    { path:'mapMatch/:id', component: MatchMapComponent, canActivate: [AuthGuard]},
    { path:'getStatistics', component: StatisticsPanelComponent, canActivate: [AuthGuard]},
    { path: '**', redirectTo: 'home' },
];

export const AppRoutes = RouterModule.forRoot(appRoutes);

/*import { AuthGuard } from './authGuard';
// </imports>

const appRoutes: Routes = [
    { path: 'home', component: HomeComponent },
// <routes>
    { path: 'suscriptions', component: SuscriptionsComponent, canActivate: [AuthGuard]  },
    { path: 'publications', component: PublicationsComponent, canActivate: [AuthGuard] },
    { path: 'dispatchers', component: DispatchersComponent, canActivate: [AuthGuard] },
    { path: 'devices', component: DevicesComponent, canActivate: [AuthGuard] },
    { path: 'measuresall', component: MeasuresComponent, canActivate: [AuthGuard] },
    { path: 'measures', component: MeasurepacketsComponent, canActivate: [AuthGuard]},
// </routes>
    { path: '**', redirectTo: 'home' },
];

export const AppRoutes = RouterModule.forRoot(appRoutes);
*/
