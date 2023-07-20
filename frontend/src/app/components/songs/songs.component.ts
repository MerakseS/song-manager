import { Component } from '@angular/core';
import { AuthenticationService } from "../../services/authentication.service";
import { Router } from "@angular/router";
import { SongService } from "../../services/song.service";

@Component({
    selector: 'app-songs',
    templateUrl: './songs.component.html',
    styleUrls: ['./songs.component.css'],
    providers: [SongService]
})
export class SongsComponent {
    private selectedFile: File;

    constructor(private authenticationService: AuthenticationService,
        private router: Router,
        private songService: SongService) {
    }

    onFileSelected(event: Event) {
        this.selectedFile = event.target['files'][0];
    }

    onSubmit() {
        this.songService.uploadFile(this.selectedFile)
            .subscribe(response => console.log(response))
    }

    logout() {
        this.authenticationService.logout();
        return this.router.navigate(['/home']);
    }
}
