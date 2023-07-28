import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from "../../services/authentication.service";
import { SongService } from "../../services/song.service";
import { Song } from "../../models/song";
import { HttpResponse } from "@angular/common/http";
import { retry } from "rxjs";

const FILENAME_REGEX = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;

@Component({
    selector: 'app-songs',
    templateUrl: './songs.component.html',
    providers: [SongService]
})
export class SongsComponent implements OnInit {
    selectedFile: File;
    songList: Song[];
    isSongLoading: boolean = false;
    protected readonly Array = Array;

    constructor(public authService: AuthenticationService,
        private songService: SongService) {
    }

    ngOnInit(): void {
        this.updateSongList();
    }

    uploadFile() {
        if (!this.authService.hasAuthority('ROLE_ADMIN')) {
            return;
        }

        if (!this.selectedFile) {
            return;
        }

        this.isSongLoading = true;
        this.songService.uploadFile(this.selectedFile)
            .subscribe(response =>
                this.songService.get(response['id'])
                    .pipe(retry({delay: 500, count: 20}))
                    .subscribe(song => {
                        this.songList.push(song);
                        this.isSongLoading = false;
                    })
            );
    }

    downloadFile(id: string) {
        this.songService.downloadFile(id)
            .subscribe(response =>
                this.downloadBlob(response));
    }

    deleteSong(id: string) {
        if (!this.authService.hasAuthority('ROLE_ADMIN')) {
            return;
        }

        this.songService.delete(id)
            .subscribe(() => {
                this.songList = this.songList.filter(song => song.id !== id)
            })
    }

    onFileSelected(event: Event) {
        this.selectedFile = event.target['files'][0];
    }

    private updateSongList() {
        this.songService.getAll()
            .subscribe(songs => this.songList = songs);
    }

    private downloadBlob(response: HttpResponse<Blob>) {
        const objectUrl = URL.createObjectURL(response.body);
        const a = document.createElement('a');
        a.download = this.parseFilename(response);
        a.href = objectUrl;
        a.click();

        URL.revokeObjectURL(objectUrl);
    }

    private parseFilename(response: HttpResponse<Blob>) {
        const contentDisposition = response.headers.get('Content-Disposition')
        const matches = FILENAME_REGEX.exec(contentDisposition);
        return matches != null && matches[1]
            ? matches[1].replace(/['"]/g, '')
            : 'audio.mp3';
    }
}
