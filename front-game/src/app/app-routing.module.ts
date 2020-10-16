import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { MatchSearchComponent } from './components/match/match-search/match-search.component';
import { SocketComponent } from "./components/socket/socket.component";

const appRoutes: Routes = [
    //Ejemplo: { path: 'home', component: HomeComponent}
    { path: 'home', component: HomeComponent },
    { path:'searchMatches', component: MatchSearchComponent},
    { path: 'socket', component: SocketComponent},
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
