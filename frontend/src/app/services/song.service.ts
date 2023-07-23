import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment";
import { Song } from "../models/song";

@Injectable({
    providedIn: 'root'
})
export class SongService {

    constructor(private httpClient: HttpClient) {
    }

    getAll() {
        return this.httpClient.get<Song[]>(environment.baseUrl);
    }

    get(id: string) {
        return this.httpClient.get<Song>(`${environment.baseUrl}/${id}`)
    }

    delete(id: string) {
        return this.httpClient.delete(`${environment.baseUrl}/${id}`);
    }

    public uploadFile(selectedFile: File) {
        const formData = new FormData();
        formData.append('file', selectedFile, selectedFile.name);

        return this.httpClient.post(`${environment.baseUrl}/file`, formData);
    }

    downloadFile(id: string) {
        return this.httpClient.get(`${environment.baseUrl}/file/${id}`, {
            responseType: 'blob',
            observe: 'response'
        });
    }
}
