import { RouterModule, Routes } from "@angular/router";
import { LoginComponent } from "./components/login/login.component";
import { AuthorizedComponent } from "./components/authorized/authorized.component";
import { authGuard } from "./guards/auth.guard";
import { SongsComponent } from "./components/songs/songs.component";

const routes: Routes = [
    {path: 'login', component: LoginComponent},
    {path: 'authorized', component: AuthorizedComponent},
    {path: 'songs', component: SongsComponent, canActivate: [authGuard]},

    {path: '**', redirectTo: 'songs'}
];

export const appRoutingModule = RouterModule.forRoot(routes);
