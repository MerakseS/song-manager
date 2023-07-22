import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from "../../services/authentication.service";
import { Router } from "@angular/router";
import { SongService } from "../../services/song.service";
import { Song } from "../../models/song";
import { HttpResponse } from "@angular/common/http";

const FILENAME_REGEX = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;

@Component({
    selector: 'app-songs',
    templateUrl: './songs.component.html',
    styleUrls: ['./songs.component.css'],
    providers: [SongService]
})
export class SongsComponent implements OnInit {
    selectedFile: File;
    songList: Song[];

    constructor(private authenticationService: AuthenticationService,
        private router: Router,
        private songService: SongService) {
    }

    ngOnInit(): void {
        this.updateSongList();
    }

    onFileSelected(event: Event) {
        this.selectedFile = event.target['files'][0];
    }

    uploadFile() {
        if (this.selectedFile) {
            this.songService.uploadFile(this.selectedFile)
                .subscribe(response => {
                    console.log(response);
                    this.updateSongList();
                })
        }
    }

    downloadFile(id: string) {
        this.songService.downloadFile(id)
            .subscribe(response =>
                this.downloadBlob(response));
    }

    deleteSong(id: string) {
        this.songService.delete(id)
            .subscribe(() => this.updateSongList())
    }

    private updateSongList() {
        this.songService.getSongList()
            .subscribe(songs => this.songList = songs);
    }

    private downloadBlob(response: HttpResponse<Blob>) {
        const objectUrl = URL.createObjectURL(response.body);
        const a = document.createElement('a');
        a.href = objectUrl;
        a.download = this.parseFilename(response);
        a.click();

        URL.revokeObjectURL(objectUrl);
    }

    private parseFilename(response: HttpResponse<Blob>) {
        const contentDisposition = response.headers.get('Content-Disposition')
        const matches = FILENAME_REGEX.exec(contentDisposition);
        return matches != null && matches[1]
            ? matches[1].replace(/['"]/g, '')
            : 'audio';
    }

    protected readonly Array = Array;
}
