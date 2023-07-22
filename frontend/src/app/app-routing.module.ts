import { NgModule } from '@angular/core';
import { RouterModule, Routes } from "@angular/router";
import { HomeComponent } from "./components/home/home.component";
import { LoginComponent } from "./components/login/login.component";
import { AuthorizedComponent } from "./components/authorized/authorized.component";
import { SongsComponent } from "./components/songs/songs.component";
import { authGuard } from "./guards/auth.guard";

const routes: Routes = [
    {path: 'home', component: HomeComponent},
    {path: 'login', component: LoginComponent},
    {path: 'authorized', component: AuthorizedComponent},
    {path: 'songs', component: SongsComponent, canActivate: [authGuard]},
    {path: '**', redirectTo: 'home'}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
