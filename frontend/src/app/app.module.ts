import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { appRoutingModule } from "./appRoutingModule";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { HTTP_INTERCEPTORS, HttpClientModule } from "@angular/common/http";
import { OAuthModule } from "angular-oauth2-oidc";
import { AuthorizedComponent } from './components/authorized/authorized.component';
import { SongsComponent } from './components/songs/songs.component';
import { JwtInterceptor } from "./interceptors/jwt.interceptor";
import { HomeComponent } from './components/home/home.component';

@NgModule({
    declarations: [
        AppComponent,
        LoginComponent,
        AuthorizedComponent,
        SongsComponent,
        HomeComponent
    ],
    imports: [
        BrowserModule,
        appRoutingModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,
        OAuthModule.forRoot()
    ],
    providers: [
        {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true}
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
