import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment";
import { Song } from "../models/song";

const FILES_URL = `${environment.baseUrl}/file-api/files`
const SONGS_URL = `${environment.baseUrl}/song-api/songs`

@Injectable({
    providedIn: 'root'
})
export class SongService {

    constructor(private httpClient: HttpClient) {
    }

    public uploadFile(selectedFile: File) {
        const formData = new FormData();
        formData.append('file', selectedFile, selectedFile.name);

        return this.httpClient.post(FILES_URL, formData);
    }

    getSongList() {
        return this.httpClient.get<Song[]>(SONGS_URL);
    }

    delete(id: string) {
        return this.httpClient.delete(`${SONGS_URL}/${id}`);
    }

    downloadFile(id: string) {
        return this.httpClient.get(`${FILES_URL}/${id}`, {
            responseType: 'blob',
            observe: 'response'
        });
    }
}
