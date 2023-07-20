import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { environment } from "../environments/environment";

const FILES_URL = `${environment.baseUrl}/file-api/files`

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
}
